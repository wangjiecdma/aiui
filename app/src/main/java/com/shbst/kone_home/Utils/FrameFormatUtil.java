package com.shbst.kone_home.Utils;

import com.shbst.kone_home.Entity.FrameEntity.Frame0BaseInfo;
import com.shbst.kone_home.Entity.FrameEntity.Frame1BaseInfo;
import com.shbst.kone_home.Entity.FrameEntity.Frame2BaseInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by zhouwenchao on 2018-03-01.
 * <p>
 * 对应协议版本：BST Multimedia display communication protocol-Ver1.7.xls
 */

public class FrameFormatUtil {
    //根据tag判断帧类型
    public static final int FRAME_TAG0 = 0;
    public static final int FRAME_TAG1 = 1;
    public static final int FRAME_TAG2 = 2;
    public static final int FRAME_TAG3 = 3;
    public static final int FRAME_TAG4 = 4;
    public static final int FRAME_TAG5 = 5;
    public static final int FRAME_TAG6 = 6;
    public static final int FRAME_TAG7 = 7;

    private static FrameFormatUtil formatUtil;

    private FrameFormatUtil() {
    }

    public static FrameFormatUtil getInstance() {
        if (formatUtil == null) {
            synchronized (FrameFormatUtil.class) {
                if (formatUtil == null) {
                    formatUtil = new FrameFormatUtil();
                }
            }
        }
        return formatUtil;
    }

    /**
     * 返回 开门数据
     *
     * @return 开门数据
     */
    public byte[] getOpenDoorData() {
//        byte[] data = new byte[4];
//        data[0] = 0x02;
//        data[1] = 0x01;
        return getRegisterFloorData(null, false, false,
                true, false, false, false, false, false, false);
    }

    /**
     * 返回关门数据
     *
     * @return 关门数据
     */
    public byte[] getCloseDoorData() {
        return getRegisterFloorData(null, false, false,
                false, true, false, false, false, false, false);
    }

    public byte[] getDelayOpenDoorData(){
        return getRegisterFloorData(null, false, false,
                false, false, false, false, false, false, true);

    }

    public byte[] getRegisterFloorData(ArrayList<Integer> registerFloor) {
        return getRegisterFloorData(registerFloor, false, false
                , false, false
                , false, false,
                false, false, false);
    }

    public byte[] getRegisterFloorData(ArrayList<Integer> registerFloor, boolean floor_up,
                                       boolean floor_dn, boolean opendoor, boolean closedoor,
                                       boolean alarmBell, boolean alarmBellHangup, boolean alarmBellAnswer
            , boolean call, boolean delayOpenDoor) {
        int bitfloor = 8;/*楼层进制 ，一个byte 代表八位即为 8*/
        byte[] bytes = new byte[19];
        bytes[0] = 0x03;
        bytes[bytes.length - 1] = (byte) 0xAA;
        if (registerFloor != null && registerFloor.size() > 0) {
            for (int i = 0; i < registerFloor.size(); i++) {
                int floorInt = registerFloor.get(i);
                if (floorInt > 48 || floorInt < 1) {
                    Log.e("floor max 48,min 1" +
                            ",current rigister floor:" +
                            floorInt);
                    continue;
                }
                /*byte在整个bytes中的下标,因为给电梯发送的楼层数据是从第 1 位开始的，故加上1*/
                int byteIndex = ((floorInt - 1) / bitfloor) + 1;
                int bitIndex = (floorInt - 1) % bitfloor; /*数据在单个byte中的下标*/

                bytes[byteIndex] = (byte) (bytes[byteIndex] | 1 << bitIndex);
            }
        }
        if (floor_up) {
            bytes[7] = (byte) (bytes[7] | 1);
        }
        if (floor_dn) {
            bytes[7] = (byte) (bytes[7] | 1 << 1);
        }
        if (opendoor) {
            bytes[7] = (byte) (bytes[7] | 1 << 2);
        }
        if (closedoor) {
            bytes[7] = (byte) (bytes[7] | 1 << 3);
        }
        if (alarmBell) {
            bytes[7] = (byte) (bytes[7] | 1 << 4);
        }
        if (alarmBellHangup) {
            bytes[7] = (byte) (bytes[7] | 1 << 5);
        }
        if (alarmBellAnswer) {
            bytes[7] = (byte) (bytes[7] | 1 << 6);
        }
        if (call) {
            bytes[7] = (byte) (bytes[7] | 1 << 7);
        }

        if (delayOpenDoor) {
            bytes[8] = (byte) (bytes[8] | 1);
        }
        return bytes;
    }

    public int getTAG(byte[] data) {
        if (data == null) return -1;
        switch (data[0]) {
            case 0x00:
                return FRAME_TAG0;
            case 0x01:
                return FRAME_TAG1;
            case 0x02:
                return FRAME_TAG2;
            case 0x03:
                return FRAME_TAG3;
            case 0x04:
                return FRAME_TAG4;
            case 0x05:
                return FRAME_TAG5;
            case 0x06:
                return FRAME_TAG6;
            case 0x07:
                return FRAME_TAG7;
        }
        return -1;
    }

    public Frame0BaseInfo getTAG0Entity(byte[] data) {
        if (data[0] != 0x00) {
            Log.e("frame error");
            return null;
        }
        if (data.length < 8) {
            Log.e("frame error ,size error:" + data.length);
            return null;
        }
        Log.v("data:" + Arrays.toString(data));
        Frame0BaseInfo info = new Frame0BaseInfo();
        if (data[7] == (byte) 0xAA ||
                data[7] == (byte) 0xA1 ||
                data[7] == (byte) 0xA2) {//new version
            //floor value
            StringBuilder floorBuffer = new StringBuilder();
            for (int i = 4; i >= 1; i--) {
                if (data[i] > 32) {
                    floorBuffer.append(Character.toString((char) data[i]));
                }
            }

            info.setCurrentFloor(floorBuffer.toString());
            //arrow
            info.setArrowState(data[5]);
            //specialState
            int state = data[6];
            int[] states = {state};
            info.setSpecialState(states);

            //表示新版本协议，否则为爱登堡协议
            if (data[7] == (byte) 0xAA)
                info.setAdenburg(false);

            if (data[7] == (byte) 0xA1) {
                info.setLeftView(true);
                info.setAdenburg(true);
            }
            if (data[7] == (byte) 0xA2) {
                info.setLeftView(false);
                info.setAdenburg(true);
            }
        } else {//old version
            info.setAdenburg(false);
            //floor value
            byte[] floor = new byte[3];
            int length = 0;
            for (int i = 1; i < 4; i++) {
                if (data[i] > 32) {
                    floor[length] = data[i];
                    length++;
                }
            }

            try {
                info.setCurrentFloor(new String(floor, 0, length, "ASCII"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //functioninfo
            if ((data[4] & 0x01) == 0x01) {//上行
                if ((data[4] & 0x04) == 0x04) {//滚动
                    info.setArrowState(13);
                } else {//不滚动
                    info.setArrowState(11);
                }
            } else if ((data[4] & 0x02) == 0x02) {//下行
                if ((data[4] & 0x04) == 0x04) {//滚动
                    info.setArrowState(23);
                } else {//不滚动
                    info.setArrowState(22);
                }
            }

            //到站使能
            info.setArrive_enable((data[4] & 0x08) == 0x08);
            //语音报站使能
            info.setVoice_reporter_enable((data[4] & 0x10) == 0x10);
            //节能模式使能
            info.setSavemode_enable((data[4] & 0x20) == 0x20);
            //到站中
            info.setArriving_enable((data[4] & 0x40) == 0x40);

            //specialstate
            int index = 0;
            int[] states = new int[21];
            for (char c = 0; c < 21; c++) {
                if ((data[5 + c / 7] & (1 << (c % 7))) != 0) {
                    states[index++] = c + 1;
                }
            }
            info.setSpecialState(states);
        }
        return info;
    }

    public Frame1BaseInfo getFrame1Entity(byte[] data) {
        if (data[0] != 0x01) {
            Log.e("frame error");
            return null;
        }
        Frame1BaseInfo info = new Frame1BaseInfo();
        info.setLaunage(data[1]);
        info.setControlBoxError(data[2]);
        info.setDoorMachineError(data[3]);
        //speed
        info.setSpeed(String.valueOf(data[4] + "." + String.valueOf(data[5])));
        //height
        info.setHeight(String.valueOf(data[6] + "." + String.valueOf(data[7])));
        //runMiles
        byte[] miles = new byte[4];
        int length = 0;
        for (int i = 8; i < 12; i++) {
            if (data[i] > 32) {
                miles[length] = data[i];
                length++;
            }
        }
        try {
            info.setRunMiles(new String(miles, 0, length, "ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //runTimes
        byte[] times = new byte[4];
        length = 0;
        for (int i = 8; i < 12; i++) {
            if (data[i] > 32) {
                times[length] = data[i];
                length++;
            }
        }
        try {
            info.setRunTime(new String(times, 0, length, "ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return info;
    }

    public Frame2BaseInfo getFrame2Entity(byte[] data) {
        if (data[0] != 0x02) {
            Log.e("frame error");
            return null;
        }
        Frame2BaseInfo floorInfo = new Frame2BaseInfo();

        if (data.length == 20) {//new code
         /*读取已点亮楼层*/

            LinkedList<Integer> floorList = new LinkedList<>();
            for (int i = 1; i < 7; i++) {  //ver1.6:17 ,ver1.7:7
                int apeendFloor = (i - 1) * 8;  //根据下标追加楼层

                for (int i1 = 0; i1 <= 7; i1++) {
                    if ((data[i] & (1 << i1)) != 0) {
                        floorList.addLast(apeendFloor + i1 + 1);
                    }
                }
            }
//            Log.v("FrameOperate", Log.get(floorList));
            floorInfo.setRegisterFloor(floorList);

            floorInfo.setFloorUp((data[7] & (1)) != 0);
            floorInfo.setFloorDn((data[7] & (1 << 1)) != 0);
            floorInfo.setOpenDoor((data[7] & (1 << 2)) != 0);
            floorInfo.setCloseDoor((data[7] & (1 << 3)) != 0);

            // 警铃呼叫、挂断、接听
            floorInfo.setAlarmBell((data[7] & (1 << 4)) != 0);
            floorInfo.setAlarmBellHangUp((data[7] & (1 << 5)) != 0);
            floorInfo.setAlarmBellAnswer((data[7] & (1 << 6)) != 0);

//            Log.v("floorInfo 7:" + data[7]);
//            if (floorInfo.isAlarmBellAnswer())
//                throw new RuntimeException("警铃接通");


            floorInfo.setEmergencyCall((data[7] & (1 << 7)) != 0);

            if (data[18] == (byte) 0xA1) {
                floorInfo.setCarAB(true);
                floorInfo.setLeftView(true);
            }
            if (data[18] == (byte) 0xA2) {
                floorInfo.setCarAB(true);
                floorInfo.setLeftView(false);
            }
        } else {//old code
            Log.v(" old code");
            LinkedList<Integer> floorList = new LinkedList<>();
            for (int i = 1; i < 7; i++) {
                int apeendFloor = (i - 1) * 7;  //根据下标追加楼层

                for (int i1 = 0; i1 <= 6; i1++) {
                    if ((data[i] & (1 << i1)) != 0) {
                        floorList.addLast(apeendFloor + i1 + 1);
                    }
                }
            }
//            Log.v("FrameOperate", floorList.size() + "");
            floorInfo.setRegisterFloor(floorList);

            floorInfo.setFloorUp((data[7] & (1)) != 0);
            floorInfo.setFloorDn((data[7] & (1 << 1)) != 0);
            floorInfo.setOpenDoor((data[7] & (1 << 2)) != 0);
            floorInfo.setCloseDoor((data[7] & (1 << 3)) != 0);
            floorInfo.setAlarmBell((data[7] & (1 << 4)) != 0);
            floorInfo.setEmergencyCall((data[7] & (1 << 5)) != 0);
        }

        return floorInfo;
    }

}
