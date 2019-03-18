package com.shbst.kone_home.Entity.FrameEntity;

import java.util.LinkedList;

/**
 * Created by suzhiming on 2017/11/7.
 */

public class Frame2BaseInfo {

    public static final int TAG = 0x02;
    private LinkedList<Integer> registerFloor;

    private boolean isCarAB = false;
    private boolean isLeftView = false;


    private boolean floorUp = false;
    private boolean floorDn = false;
    private boolean openDoor = false;
    private boolean closeDoor = false;
    //警铃
    private boolean alarmBell = false;
    // 警铃接听
    private boolean alarmBellAnswer = false;
    //警铃挂断
    private boolean alarmBellHangUp = false;
    //紧急呼叫
    private boolean emergencyCall = false;


    public boolean isLeftView() {
        return isLeftView;
    }

    public void setLeftView(boolean leftView) {
        isLeftView = leftView;
    }

    public boolean isCarAB() {
        return isCarAB;
    }

    public void setCarAB(boolean carAB) {
        isCarAB = carAB;
    }

    public boolean isFloorUp() {
        return floorUp;
    }

    public void setFloorUp(boolean floorUp) {
        this.floorUp = floorUp;
    }

    public boolean isFloorDn() {
        return floorDn;
    }

    public void setFloorDn(boolean floorDn) {
        this.floorDn = floorDn;
    }

    public boolean isOpenDoor() {
        return openDoor;
    }

    public void setOpenDoor(boolean openDoor) {
        this.openDoor = openDoor;
    }

    public boolean isCloseDoor() {
        return closeDoor;
    }

    public void setCloseDoor(boolean closeDoor) {
        this.closeDoor = closeDoor;
    }

    public boolean isAlarmBell() {
        return alarmBell;
    }

    public void setAlarmBell(boolean alarmBell) {
        this.alarmBell = alarmBell;
    }

    public boolean isEmergencyCall() {
        return emergencyCall;
    }

    public void setEmergencyCall(boolean emergencyCall) {
        this.emergencyCall = emergencyCall;
    }

    public boolean isAlarmBellAnswer() {
        return alarmBellAnswer;
    }

    public void setAlarmBellAnswer(boolean alarmBellAnswer) {
        this.alarmBellAnswer = alarmBellAnswer;
    }

    public boolean isAlarmBellHangUp() {
        return alarmBellHangUp;
    }

    public void setAlarmBellHangUp(boolean alarmBellHangUp) {
        this.alarmBellHangUp = alarmBellHangUp;
    }

    public LinkedList<Integer> getRegisterFloor() {
        return registerFloor;
    }

    public void setRegisterFloor(LinkedList<Integer> registerFloor) {
        this.registerFloor = registerFloor;
    }



    @Override
    public String toString() {
        return "FloorInfo{" +
                "is_register_floor=" + registerFloor +
                ", isCarAB=" + isCarAB +
                ", isLeftView=" + isLeftView +
                '}';
    }
}
