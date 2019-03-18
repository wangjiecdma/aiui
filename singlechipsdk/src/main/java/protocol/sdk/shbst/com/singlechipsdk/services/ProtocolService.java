package protocol.sdk.shbst.com.singlechipsdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

import protocol.sdk.shbst.com.singlechipsdk.SerialPort;
import protocol.sdk.shbst.com.singlechipsdk.ThreadManager;

/**
 * Created by suzhiming on 2017/11/1.
 * 线程只用于接收来自单片机的服务（发送信息不在线程内）
 */
public class ProtocolService extends Service {

    private static final String TAG = "ProtocolService";
    public static final int ENTERUPDATE_RESOPNE = 3;//进入升级相应

    private ServiceBinder mBinder;
    private SerialPort mSerialPort;
    private ProtocolCallBack mCallBack;

    //存放主线程发送给单片机的消息队列,升级协议的消息不应该放在这里，因为它们必须同步响应
    private LinkedList<byte[]> messageQueue;
    private boolean threadRun;

    //等待相应
    private byte mainThreadWait = -1;

    private boolean appFirstStart;//App启动的时候必须发一个信息给单片机(因为应用第一次启动会发送进入升级来获取单片机版本信息)
    private int sendLength;//升级的时候已经发送过的数据的长度
    private int mcu_length;//升级文件的总长度
    private boolean tryUpdateAgain;//升级步骤走完以后，是否已经进行了升级失败重试
    private int tyrEnterUpdateTimes;//请求进入升级次数，(用于兼容17年前老版本,进入升级次数超过6次以后将判断为老版本，不进行升级)

    private File binFile;//升级bin文件

    private InputStream input;
    private OutputStream output;

    private byte[] cache0x00;//use for compare to avoid repeat
    private byte[] cache0x01;//use for compare to avoid repeat
    private byte[] cache0x02;//use for compare to avoid repeat
    private byte[] cache0x04;//use for compare to avoid repeat

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new ServiceBinder();
    }

    public void startReceiveService(String pathName, int baudrate, int flags, ProtocolCallBack mCallBack) throws IOException {
        Log.v(TAG, "startReceiveService");
        this.mCallBack = mCallBack;
        threadRun = true;
        mSerialPort = new SerialPort(new File(pathName), baudrate, flags);
        if (null == mSerialPort) {
            Log.e(TAG, "error: SerialPort created failed");
            throw new RuntimeException("SerialPort created failed");
        }
        input = mSerialPort.getInputStream();
        output = mSerialPort.getOutputStream();
        ThreadManager.getInstance().submit(protocolMainThread);
    }

    /**
     * 如果需要用有升级程序功能请需要配置
     */
    public void configUpdateFile(String model, String copyRight, File binFile) {
        Config.MCU_INFO_MODEL = model;
        Config.MCU_INFO_COPYRIGHT = copyRight;
        this.binFile = binFile;
    }

    public void stopProtocolService() {
        Log.v(TAG, "stopProtocolService");
        threadRun = false;
        try {
            if (null != input) {
                input.close();
            }
            if (null != output) {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    /**
     * 用于接收数据的线程
     */
    private Runnable protocolMainThread = new Runnable() {
        @Override
        public void run() {
            try {
                Log.v(TAG, "protocolMainThread start running");
                tryUpdateAgain = false;

                if (null != binFile && binFile.length() > 0) {
                    Log.v(TAG, "app启动，开始请求进入升级模式,请稍等");
                    //第一包直接发进入升级
                    appFirstStart = true;
                    mainThreadWait = ENTERUPDATE_RESOPNE;
                    sendEnterUpdateQuest();

                    ThreadManager.getInstance().submit(new Runnable() {
                        @Override
                        public void run() {
                            while (mainThreadWait == ENTERUPDATE_RESOPNE && appFirstStart) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (mainThreadWait != 3 || !appFirstStart) {
                                    break;
                                }
                                if (tyrEnterUpdateTimes++ > 5) {
                                    Log.e(TAG, "已经重发了6次无响应，单片机程序有问题或者可能是老版本，开始工作不再升级");
                                    singleChipIsReady(false);
                                    break;
                                }
                                Log.v(TAG, "app启动请求进入升级没有响应，重发。第" + tyrEnterUpdateTimes + "次重发");
                                sendEnterUpdateQuest();
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "没有配置升级文件，无升级功能");
                    singleChipIsReady(false);
                }
                receiveMessage();
                Log.e("zhouwenchao", "receiveMessage method over");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("zhouwenchao", "receiveMessage IOException");
            }
            Log.v(TAG, "protocolMainThread runs over");
        }
    };


    private void receiveMessage() throws IOException {
        Log.v(TAG, "receiveMessage........");
        byte lDat;
        int lCnt;
        int SerialLenth;
        int gBvtRecStatus = 0;
        int gBvtRecCnt = 0;
        byte[] SerialData = new byte[Config.FRAME_MAX_LENGTH];
        byte[] gBvtFrameBuf = new byte[Config.FRAME_MAX_LENGTH];
        Log.e("zhouwenchao", "thread start");
        while ((SerialLenth = input.read(SerialData)) != 0) {
            lCnt = 0;
            while (lCnt < SerialLenth) {
                lDat = SerialData[lCnt++];
                if (gBvtRecStatus == 0)/*未收到帧头*/ {
                    if ((lDat & 0xFF) == Config.STX)/*收到帧头*/ {
                        gBvtFrameBuf[gBvtRecCnt++] = lDat;
                        gBvtRecStatus = 1;
                    } else {
                        continue;
                    }
                } else if (gBvtRecStatus == 1) {/*正在接收数据*/
                    if (gBvtRecCnt < Config.FRAME_MAX_LENGTH) {
                        if ((lDat & 0xFF) == Config.ETX)/*收到帧尾*/ {
                            gBvtRecStatus = 2;
                        }
                        gBvtFrameBuf[gBvtRecCnt++] = lDat;
                    } else {//无效包
                        gBvtRecStatus = 0;
                        gBvtRecCnt = 0;
                    }
                }
                if (gBvtRecStatus == 2) {//收到帧尾
                    compareFrameByKind(gBvtFrameBuf, gBvtRecCnt);
                    gBvtRecStatus = 0;
                    gBvtRecCnt = 0;
                }
            }
        }
        Log.e(TAG, "receiveMessage over");
    }

    /**
     * byte数据无法这样传递 姑且这样
     *
     * @param gBvtFrameBuf
     * @param cnt
     */
    private void compareFrameByKind(byte[] gBvtFrameBuf, int cnt) {
        Log.e(TAG, "compareFrameByKind:" + Arrays.toString(gBvtFrameBuf));
        switch (gBvtFrameBuf[1]) {
            case 0x00:
                int sameCount0 = 0;
                if (cache0x00 == null || cache0x00.length != cnt - 2) {
                    cache0x00 = new byte[cnt - 2];
                    System.arraycopy(gBvtFrameBuf, 1, cache0x00, 0, cnt - 2);
                } else {
                    for (int i = 0; i < cnt - 2; i++) {
                        if (gBvtFrameBuf[i + 1] == cache0x00[i]) {
                            sameCount0++;
                        } else {
                            break;
                        }
                    }
                }
                if (sameCount0 != cnt - 2) {
                    System.arraycopy(gBvtFrameBuf, 1, cache0x00, 0, cnt - 2);
                    startParse(cache0x00);
                }
                break;
            case 0x01:
                int sameCount1 = 0;
                if (cache0x01 == null || cache0x01.length != cnt - 2) {
                    cache0x01 = new byte[cnt - 2];
                    System.arraycopy(gBvtFrameBuf, 1, cache0x01, 0, cnt - 2);
                } else {
                    for (int i = 0; i < cnt - 2; i++) {
                        if (gBvtFrameBuf[i + 1] == cache0x01[i]) {
                            sameCount1++;
                        } else {
                            break;
                        }
                    }
                }
                if (sameCount1 != cnt - 2) {
                    System.arraycopy(gBvtFrameBuf, 1, cache0x01, 0, cnt - 2);
                    startParse(cache0x01);
                }
                break;
            case 0x02:
                int sameCount2 = 0;
                if (cache0x02 == null || cache0x02.length != cnt - 2) {
                    cache0x02 = new byte[cnt - 2];
                    System.arraycopy(gBvtFrameBuf, 1, cache0x02, 0, cnt - 2);
                } else {
                    for (int i = 0; i < cnt - 2; i++) {
                        if (gBvtFrameBuf[i + 1] == cache0x02[i]) {
                            sameCount2++;
                        } else {
                            break;
                        }
                    }
                }
                if (sameCount2 != cnt - 2) {
                    System.arraycopy(gBvtFrameBuf, 1, cache0x02, 0, cnt - 2);
                    startParse(cache0x02);
                }
                break;
            case 0x04:
                int sameCount4 = 0;
                if (cache0x04 == null || cache0x04.length != cnt - 2) {
                    cache0x04 = new byte[cnt - 2];
                    System.arraycopy(gBvtFrameBuf, 1, cache0x04, 0, cnt - 2);
                } else {
                    for (int i = 0; i < cnt - 2; i++) {
                        if (gBvtFrameBuf[i + 1] == cache0x04[i]) {
                            sameCount4++;
                        } else {
                            break;
                        }
                    }
                }
                if (sameCount4 != cnt - 2) {
                    System.arraycopy(gBvtFrameBuf, 1, cache0x04, 0, cnt - 2);
                    startParse(cache0x04);
                }
                break;
            case 0x06:
                byte[] frametemp = new byte[cnt - 2];
                System.arraycopy(gBvtFrameBuf, 1, frametemp, 0, cnt - 2);
                startParse(frametemp);
                break;
        }
    }

    private void startParse(byte[] datas) {
        byte[] originalData;
        byte[] NoneCRCData;
        byte CheckValue;
        if (datas != null && threadRun) {
            originalData = ProtocolUtils.AntiEscape(datas, datas.length);   /* 逆转Escape转换 */
            NoneCRCData = new byte[originalData.length - 1];
            System.arraycopy(originalData, 0, NoneCRCData, 0, NoneCRCData.length);
            CheckValue = ProtocolUtils.CRC_Check(NoneCRCData);
            if (originalData[0] == 0x05) {//don't check the four-keys
                parseData(NoneCRCData);
            } else {
                if (CheckValue == originalData[originalData.length - 1]) {
                    parseData(NoneCRCData);
                } else {
                    Log.e(TAG, "CRC check error! The received value is " + originalData[originalData.length - 1]
                            + ", and the calculate value is " + CheckValue);
                }
            }
        }
    }

    /**
     * 处理数据，着重处理升级请求，进行阻塞处理，除了升级请求以后其余数据直接回调丢给应用处理
     *
     * @param frame
     */
    private void parseData(byte[] frame) {
        if (mainThreadWait != -1) {//说明需要进行阻塞等待
            if (frame[0] != 0x06) {
                Log.e(TAG, "等待升级响应中，非响应帧不处理");
                return;
            }

            if (frame[1] == 5 && mainThreadWait == 4) {//在发送升级包的时候，上一包发送完毕，但是单片机相应了数据有误要重发
                Log.v(TAG, "升级数据包有误,重发上一包");
                mainThreadWait = -1;
                sendLength = sendLength - Config.MCU_FRAME_LENGTH;
                sendUpdateFileData();
            }

            if (mainThreadWait != frame[1]) {
                Log.e(TAG, "非目标等待帧，不处理");
                return;
            }

            switch (mainThreadWait) {
                case 0:
                    Log.v(TAG, "无需升级请求响应成功，可以正常运行");
                    mainThreadWait = -1;
                    singleChipIsReady(true);
                    break;
                case 3:
                    if (appFirstStart) {
                        Log.v(TAG, "响应app第一次启动，开始接收版本号信息");
                        appFirstStart = false;
                        mainThreadWait = -1;
                    } else {
                        Log.v(TAG, "响应请求进入升级成功");
                        mainThreadWait = 1;
                        sendStartUpdateQuest();
                        ThreadManager.getInstance().submit(new Runnable() {
                            @Override
                            public void run() {
                                while (mainThreadWait == 1) {
                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (mainThreadWait != 1) {
                                        break;
                                    }
                                    Log.v(TAG, "request for starting update has no respone，send again");
                                    sendStartUpdateQuest();
                                }
                            }
                        });
                    }
                    break;
                case 1:
                    Log.v(TAG, "响应开始升级成功，正式开始升级");
                    mainThreadWait = -1;
                    sendLength = 0;
                    sendUpdateFileData();
                    break;

                case 4:
                    Log.v(TAG, "升级进度:%" + sendLength * 100 / mcu_length + "升级数据无误！");
                    mCallBack.onUpdateProcess(sendLength * 100 / mcu_length);
                    mainThreadWait = -1;
                    sendUpdateFileData();
                    break;

                case 2:
                    Log.v(TAG, "响应升级结束成功，退出升级模式");
                    mainThreadWait = -1;
                    singleChipIsReady(true);
                    break;

            }
            return;
        }

        if (frame[0] == 0x04) {//MCU version
            if (null != binFile && binFile.length() > 0) {
                parseMCUVersionData(frame);
            }
        } else if (frame[0] == 0x06 && frame[1] == 6) {//升级成功了，单片机应用程序有问题
            if (tryUpdateAgain) {
                Log.e(TAG, "重新升级依旧失败，单片机应用程序有问题，请联系厂商");
                return;
            }
            Log.e(TAG, "升级完毕，但是单片机应用程序有问题，正在再一次重试");
            tryUpdateAgain = true;
            //重试，与第一包直接发进入升级一致
            appFirstStart = true;
            mainThreadWait = 3;
            Log.v(TAG, "重新开始升级，开始请求进入升级模式,等待5秒钟");
            sendEnterUpdateQuest();
            ThreadManager.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    while (mainThreadWait == 3 && appFirstStart) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mainThreadWait != 3 || !appFirstStart) {
                            return;
                        }
                        Log.v(TAG, "重新开始升级，请求进入升级没有响应，重发");
                        sendEnterUpdateQuest();
                    }
                }
            });
        } else {//其他的数据直接丢给应用处理
            mCallBack.onReceiveData(frame);
        }
    }

    private void parseMCUVersionData(byte[] frame) {
        StringBuffer version = new StringBuffer();
        StringBuffer time = new StringBuffer();
        version.append("V").append(frame[1])
                .append(frame[2] < 10 ? ".0" : ".")
                .append(frame[2]);
        time.append("20");
        if (frame[3] < 10) {//year
            time.append("0").append(frame[3]);
        } else {
            time.append(frame[3]);
        }
        time.append("-");
        if (frame[4] < 10) {//month
            time.append("0").append(frame[4]);
        } else {
            time.append(frame[4]);
        }
        time.append("-");
        if (frame[5] < 10) {//day
            time.append("0").append(frame[5]);
        } else {
            time.append(frame[5]);
        }
        Log.v(TAG, "MCU old version:" + version.toString() + "  old time:" + time.toString());

        boolean needUpdate = ifNeedToUpdateMCU(version.toString(), time.toString());
        if (needUpdate) {
            sendEnterUpdateQuest();
            mainThreadWait = 3;
            ThreadManager.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    while (mainThreadWait == 3) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mainThreadWait != 3) {
                            return;
                        }
                        Log.v(TAG, "请求进入升级没有响应，再发一次");
                        sendEnterUpdateQuest();
                    }
                }
            });
        } else {
            sendNeedNotUpdateQuest();
            mainThreadWait = 0;
            ThreadManager.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    while (mainThreadWait == 0) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mainThreadWait != 0) {
                            return;
                        }
                        Log.v(TAG, "请求不需要升级没有响应，再发一次");
                        sendNeedNotUpdateQuest();
                    }
                }
            });
        }
    }

    private void sendEnterUpdateQuest() {
        Log.v(TAG, "request for entering update modle");
        byte[] data = new byte[2];
        data[0] = 0x05;
        data[1] = 3;
        sendToSinglechip(data);
    }

    private void sendStartUpdateQuest() {
        Log.v(TAG, "请求开始升级模式");
        byte[] data = new byte[8];
        data[0] = 0x05;
        data[1] = 1;
        //file length
        try {
            FileInputStream updateBinFileStream = new FileInputStream(binFile);
            mcu_length = updateBinFileStream.available();
            Log.v(TAG, "sendStartUpdateQuest mcu_length:" + mcu_length);
            //CRC check
            byte[] dataBytes = new byte[mcu_length];
            updateBinFileStream.read(dataBytes);
            updateBinFileStream.close();
            long crc_check = SerialPort.MonachChkVrifi(dataBytes, dataBytes.length);
            Log.v(TAG, "crc_check values is:" + crc_check);
            data[6] = (byte) (crc_check & 0x00ff);
            data[7] = (byte) ((crc_check >> 8) & 0x00ff);
        } catch (IOException e) {
            Log.e(TAG, "sendStartUpdateQuest IOException:" + e.toString());
            e.printStackTrace();
            return;
        }
        data[2] = (byte) (mcu_length & 0x00ff);
        data[3] = (byte) ((mcu_length >> 8) & 0x00ff);
        data[4] = (byte) ((mcu_length >> 16) & 0x00ff);
        data[5] = (byte) ((mcu_length >> 24) & 0x00ff);

        sendToSinglechip(data);
    }

    private void sendUpdateFileData() {
        final byte[] data = new byte[Config.MCU_FRAME_LENGTH + 1];
        data[0] = 0x07;
        int count = 0;
        try {
            FileInputStream updateBinFileStream = new FileInputStream(binFile);//inputStream 必须每次都初始化
            updateBinFileStream.skip(sendLength);
            count = updateBinFileStream.read(data, 1, Config.MCU_FRAME_LENGTH);
            updateBinFileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (count != -1) {
            sendLength += count;

            if (sendLength < mcu_length) {//相当于等待响应处理，因为都是4，所以不能用常规等待,只能通过判断sendLength是否变化了
                ThreadManager.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        int waitTime = 0;
                        int temp = 0;
                        while (waitTime++ < 10) {
                            temp = sendLength;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (sendLength != temp) {
                                return;
                            }
                        }
                        if (sendLength != temp) {
                            return;
                        }
                        Log.v(TAG, "请求确认升级数据是否正确没有响应，重发这一包");
                        Log.v(TAG, "升级进度:%" + sendLength * 100 / mcu_length);
                        sendToSinglechip(data);
                    }
                });
            }

            if (count < Config.MCU_FRAME_LENGTH) {//最后一包数据，不足64位，填充0XFF
                for (int i = count + 1; i < Config.MCU_FRAME_LENGTH + 1; i++) {
                    data[i] = (byte) 0xFF;
                }
            }
            sendToSinglechip(data);
            mainThreadWait = 4;
        } else {//到文件尾了 升级结束
            mainThreadWait = 2;
            sendUpdateFinish();
            ThreadManager.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    while (mainThreadWait == 2) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mainThreadWait != 2) {
                            return;
                        }
                        Log.v(TAG, "请求升级结束没有响应，再发一次");
                        sendUpdateFinish();
                    }
                }
            });
        }
    }

    private void sendUpdateFinish() {
        Log.v(TAG, "请求升级结束");
        byte[] finishData = new byte[2];
        finishData[0] = 0x05;
        finishData[1] = 2;
        sendToSinglechip(finishData);
    }


    private void sendNeedNotUpdateQuest() {
        byte[] data = new byte[2];
        data[0] = 0x05;
        data[1] = 0;
        sendToSinglechip(data);
    }


    private boolean ifNeedToUpdateMCU(String oldVerison, String oldTime) {
        byte[] binInfobuffer = new byte[32];
        try {
            FileInputStream updateBinFileStream = new FileInputStream(binFile);
            updateBinFileStream.skip(0x800);
            try {
                updateBinFileStream.read(binInfobuffer, 0, 32);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateBinFileStream.close();
            if (!new String(binInfobuffer, 0, 10, "ASCII").contains(Config.MCU_INFO_MODEL)) {
                Log.e(TAG, "Error! MCU file tag should be: " + Config.MCU_INFO_MODEL
                        + ", but actual file tag is: " + new String(binInfobuffer, 0, 10, "ASCII"));
                return false;
            }

            if (!new String(binInfobuffer, 28, 3, "ASCII").contains(Config.MCU_INFO_COPYRIGHT)) {
                Log.e(TAG, "Error! MCU file tag should be: " + Config.MCU_INFO_COPYRIGHT
                        + ", but actual file tag is: " + new String(binInfobuffer, 28, 3, "ASCII"));
                return false;
            }

            String new_Version = new String(binInfobuffer, 11, 5, "ASCII");
            String new_Time = new String(binInfobuffer, 17, 10, "ASCII");
            Log.i(TAG, "checkBinFile: " + "new_fileVersion:" + new_Version + "  new_Time:" + new_Time);

            if (!new_Version.equals(oldVerison)) {
                Log.v(TAG, "version don't match,need to update");
                return true;
            }

            if (!new_Time.equals(oldTime)) {
                Log.v(TAG, "time don't match,need to update");
                return true;
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e(TAG, "checkBinFile StringIndexOutOfBoundsException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "checkBinFile IOException");
        }
        return false;
    }

    /**
     * 单片机已经准备好，可以开始发送接收
     */
    private void singleChipIsReady(boolean isNormalReady) {
        Log.v(TAG, "单片机准备就绪，开始正常工作");
        messageQueue = new LinkedList<>();
        //开启对外的发送数据线程
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                while (threadRun) {
                    if (messageQueue.size() > 0) {
                        sendToSinglechip(messageQueue.getFirst());
                        messageQueue.removeFirst();
                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        mCallBack.onReady(isNormalReady);
    }

    /**
     * 内部调用，不可在UI线程
     *
     * @param data
     */
    private void sendToSinglechip(byte[] data) {
        /* 先添加CRC */
        byte[] tmp = new byte[data.length + 1];
        System.arraycopy(data, 0, tmp, 0, data.length);
        byte CRC_Value = ProtocolUtils.CRC_Check(data);
        tmp[tmp.length - 1] = CRC_Value;

        /* 进行Escape处理 */
        byte[] EscapeData = ProtocolUtils.doEscape(tmp);
        if (EscapeData == null) {
            Log.e("sendData", "SendCommand: The frame is too long to send!");
            return;
        }

        /* 加入STX,ETX */
        if (EscapeData.length + 2 > Config.FRAME_MAX_LENGTH) {
            Log.e("sendData", "SendCommand: The frame is too long to send!");
            return;
        }
        byte[] sendFrame = new byte[EscapeData.length + 2];
        System.arraycopy(EscapeData, 0, sendFrame, 1, EscapeData.length);
        sendFrame[0] = (byte) Config.STX;
        sendFrame[sendFrame.length - 1] = (byte) Config.ETX;

        /* send */
        if (sendFrame != null) {
            try {
//                for(int i=0;i<sendFrame.length;i++) {
//                    Log.v(TAG, "111aa:" + sendFrame[i]);
//                }
                Log.i("sendtag", "send data size:" + sendFrame.length + " send data:" + Arrays.toString(sendFrame));
                output.write(sendFrame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在UI线程（主线程）中调用的方法
     * top:是否要优先，设置为true直接放在第一个
     */
    public synchronized void sendMsgInUIThread(byte[] data) {
        if (null == messageQueue) {
            Log.v(TAG, "单片机没有准备好");
            return;
        }
        messageQueue.add(data);
    }

    public class ServiceBinder extends Binder {

        public ProtocolService getInstance() {
            return ProtocolService.this;
        }

    }

   /* public class ProtocolData {
        public static final int RECEIVE_TAG = -100;

        public byte[] data;
        public int tag;

        public ProtocolData(byte[] data, int tag) {
            this.data = data;
            this.tag = tag;
        }
    }*/
}
