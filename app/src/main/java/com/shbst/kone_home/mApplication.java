package com.shbst.kone_home;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;

import com.iflytek.aiui.jni.AIUI;
import com.shbst.kone_home.Utils.BstSerialPort;
import com.shbst.kone_home.Utils.FileUtil;
import com.shbst.kone_home.Utils.FrameFormatUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.ServiceUtil;
import com.shbst.kone_home.aiui.AIUIManager;
import com.shbst.kone_home.aiui.DoorController;
import com.shbst.kone_home.aiui.FloorControl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import protocol.sdk.shbst.com.singlechipsdk.services.ProtocolCallBack;
import protocol.sdk.shbst.com.singlechipsdk.services.ProtocolService;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

public class mApplication extends Application implements ProtocolCallBack
        , FileUtil.CopyFileCallBack, Application.ActivityLifecycleCallbacks {

    private ProtocolService mService;

    private boolean protocolServiceReady = false;

    private ArrayList<ProtocolCallBack> mCallbacks;

    private static mApplication instant;


    private final String binFilePath = "sdcard/Download/kone.bin";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("app start ,application onCreate");
        instant = this;
        mCallbacks = new ArrayList<>();

        this.registerActivityLifecycleCallbacks(this);
        File binFile = new File(binFilePath);

        //todo 在实际设备中，绑定单片机服务
//        if (!binFile.exists())
        cpFileToSDcard();
//        else
//            bindAndStartService();


        //BstSerialPort.test(this);

        //HermesEventBus.getDefault().init(this);




    }

    public void initAIUI(){
        AIUIManager aiuiManager = AIUIManager.getInstance();
        aiuiManager.init(this, new FloorControl() {
            @Override
            public void registerFloor(int floor) {
                ArrayList<Integer> floorList = new ArrayList<>();
                floorList.add(floor);
                byte data[] = FrameFormatUtil.getInstance().getRegisterFloorData(floorList);
                sendData(data);
            }

            @Override
            public void open() {
                byte data[] = FrameFormatUtil.getInstance().getOpenDoorData();
                sendData(data);

                DoorController.openDoor(mApplication.this);
            }

            @Override
            public void close() {
                byte data[] = FrameFormatUtil.getInstance().getCloseDoorData();
                sendData(data);
                DoorController.closeDoor(mApplication.this);


            }
        });
    }


    private void sendData(byte[] data) {
        if (data == null) {
            Log.e("send data is null");
            return;
        }
        if (!mApplication.getInstant().isProtocolServiceReady()) {
            Log.w("prodocol service not ready," +
                    "send data:" + Log.get(data));
            return;
        }
        if (!mApplication.getInstant().protocolServiceIsNull()) {
            mApplication.getInstant().getmService()
                    .sendMsgInUIThread(data);
            Log.v("send data size:" + data.length + " data:" + Log.get(data));
        } else {
            Log.e("mService is null" +
                    ", send data error，data：" + Log.get(data));
        }
    }

    // TODO: 2018/4/8 给出 kone.bin 文件
    private void cpFileToSDcard() {
        FileUtil.copyAssasFileToPath(this, "kone.bin", binFilePath, this);
    }

    public static mApplication getInstant() {
        return instant;
    }

    public ProtocolService getmService() {
        return mService;
    }

    public boolean isProtocolServiceReady() {
        return protocolServiceReady;
    }

    public void setProtocolServiceReady(boolean protocolServiceReady) {
        this.protocolServiceReady = protocolServiceReady;
    }

    public void registerCallback(ProtocolCallBack mCallback) {
        if (mCallbacks != null)
            mCallbacks.add(mCallback);
    }

    public void unregisterCallback(ProtocolCallBack mCallback) {
        if (mCallbacks != null)
            mCallbacks.remove(mCallback);
    }

    public boolean protocolServiceIsNull() {
        return null == mService;
    }

    private void bindAndStartService() {
        setProtocolServiceReady(false);
        Intent bindIntent = new Intent(this, ProtocolService.class);
        bindService(bindIntent, protocolConnection, BIND_AUTO_CREATE);
    }

    private ProtocolConnect protocolConnection;

    {
        protocolConnection = new ProtocolConnect() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.w("product service disconnect ");
                isBind = false;
                setProtocolServiceReady(false);
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                isBind = true;
                File file = new File(binFilePath);
                Log.v("file exist:" + file.exists());
                mService = ((ProtocolService.ServiceBinder) service).getInstance();
                mService.configUpdateFile("Kone i.Mx6", "BST", file);//若不需要升级功能可跳过这一步
                try {
                    mService.bindCallBack(mApplication.this);
                    Log.v("start receive service");
                    mService.startReceiveService("/dev/ttymxc2"
                            , 9600, 0);
//                    Log.v("start receive service");
//                    mService.startReceiveService("/dev/ttymxc2"
//                            , 9600, 0,mApplication.this);

                } catch (IOException e) {
                    Log.e("start receive service error:" + Log.get(e));
                }
            }
        };
    }

    @Override
    public void onTerminate() {
        if (mService != null) {
            mService.stopProtocolService();
            mService = null;
        }
        if (protocolConnection.isBind)
            unbindService(protocolConnection);
        instant = null;
        super.onTerminate();
    }

    @Override
    public void onReceiveData(byte[] data) {
        Log.v(" application on receive data:" + Log.get(data));
        for (ProtocolCallBack callBack : mCallbacks) {
            callBack.onReceiveData(data);
        }
    }

    @Override
    public void onUpdateProcess(int process) {
        Log.v(" application onUpdateProcess:" + process);
        for (ProtocolCallBack callBack : mCallbacks) {
            callBack.onUpdateProcess(process);
        }

    }

    @Override
    public void onReady(boolean isNormalReady) {
        Log.v(" application onReady:" + isNormalReady);
        setProtocolServiceReady(true);
        for (ProtocolCallBack callBack : mCallbacks) {
            callBack.onReady(isNormalReady);
        }
    }

    @Override
    public void startCopy(String path, String toPath, long filelenght) {
        Log.v("start copy bin file");
    }

    @Override
    public void CopyProgress(String path, String toPath, long progress, long fileLenght) {
        Log.v("copy bin file progress:" + progress);
    }

    @Override
    public void CopyOver(String path, String toPath, boolean over, String error) {
        Log.v("copy bin file over,over:" + over + " error:" + error);

        if (over && TextUtils.isEmpty(error))
            bindAndStartService();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        boolean isForeground = ServiceUtil.isForegroundApp(
                activity, activity.getApplication().getPackageName());
        if (!isForeground) {
            Log.v("应用程序已经退出，结束线程");
            Process.killProcess(Process.myPid()); //kill process ,exit app
        }
    }
}
