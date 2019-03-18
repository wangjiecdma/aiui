package com.shbst.kone_home.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextClock;

import java.io.File;
import java.util.Arrays;

/**
 * Created by zhouwenchao on 2017-05-23.
 */
public class UpdataManager {
    private static final String TAG = "UpdataManager: ";
    private static Context context;

    //升级资源文件

    //升级软件版本
    private static final String APKNAME = "firmware/KONE_HOME.apk";

    public static synchronized void startUpSource(String sourcePathTmp, Context contextTmp) {
        context = contextTmp;


        String sourcePath = clearStr(sourcePathTmp, "file://") + File.separator;
        if (new File(sourcePath  + APKNAME).exists()) {
            Log.d(TAG, "Update file---> " + sourcePath + APKNAME + " <---");
            String apkPath = sourcePath +APKNAME;
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            if (info != null)
            {
                Log.i(TAG, "startUpSource:apk  new app info: " + info.toString());

                Log.i(TAG,"reset gui enable");

                PreferManager.getInstence().clearGuiEnable(contextTmp);


                String packageName = info.packageName;

                String version = String.valueOf(info.versionName);

                Log.i(TAG, "startUpSource:apk  update app packageName: " + packageName);

                float versionCode = Float.parseFloat(String.valueOf(info.versionCode));

                Log.i(TAG, "startUpSource:apk  new app VersionCode  " + versionCode + "   Version   " + version);
                String oldVersion = String.valueOf(getVersionName());

                float oldVersionCode = Float.parseFloat(String.valueOf(getVersionCode()));
                Log.i(TAG, "startUpSource:apk  local app oldVersionCode  " + oldVersionCode + "   oldVersion   " + oldVersion);
                if (versionCode > oldVersionCode) {
                    Log.i(TAG, "检测到APK 升级");
                    upAPK(apkPath);
                } else {
                    Log.i(TAG, "未检测到APK");
                }
            }
        } else {
            //没有升级文件存在
            return;
        }
    }
    //版本名
    public static String getVersionName() {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode() {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取apk 版本信息
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {

            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
    /**
     * 通知apk 升级
     *
     * @param filePath apk路径
     */

    private static void upAPK(String filePath) {
        Intent intent =  new Intent();
        Log.i(TAG, "upAPK: Send broadcast upgrade apk");
        intent.setAction("zhouwc.example.com.apkupdatedemo");
        intent.putExtra("apk", filePath);
        context.sendBroadcast(intent);
    }
    public static synchronized void  stopCopy(){
        // TODO: 2017-09-03 取消复制文件操作
    }
    /**
     * 去除 路径前 包含的 file:// 字符串
     *
     * @param str
     * @param clearStr
     * @return
     */
    private static String clearStr(String str, String clearStr) {
        byte[] bytes = str.getBytes();
        byte[] clearStrBytes = clearStr.getBytes();
        byte[] StrTitleBytes = new byte[clearStrBytes.length];
        System.arraycopy(bytes, 0, StrTitleBytes, 0, StrTitleBytes.length);
        byte[] newByteTmp = null;
        if (Arrays.equals(clearStrBytes, StrTitleBytes)) {
            newByteTmp = new byte[bytes.length - clearStrBytes.length];
            System.arraycopy(bytes, clearStrBytes.length, newByteTmp, 0, newByteTmp.length);
        }
        if (newByteTmp != null) {
            return new String(newByteTmp);
        } else {
            return str;
        }
    }
}
