package com.shbst.kone_home.Entity.FrameEntity;

/**
 * Created by suzhiming on 2017/11/7.
 */

public class Frame1BaseInfo {

    public static final int TAG = 0x01;

    private int launage;
    private int controlBoxError;
    private int doorMachineError;
    private String speed;
    private String height;
    private String runMiles;
    private String runTime;

    public int getLaunage() {
        return launage;
    }

    public void setLaunage(int launage) {
        this.launage = launage;
    }

    public int getControlBoxError() {
        return controlBoxError;
    }

    public void setControlBoxError(int controlBoxError) {
        this.controlBoxError = controlBoxError;
    }

    public int getDoorMachineError() {
        return doorMachineError;
    }

    public void setDoorMachineError(int doorMachineError) {
        this.doorMachineError = doorMachineError;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getRunMiles() {
        return runMiles;
    }

    public void setRunMiles(String runMiles) {
        this.runMiles = runMiles;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return "EvltExtendInfo{" +
                "launage=" + launage +
                ", controlBoxError='" + controlBoxError + '\'' +
                ", doorMachineError='" + doorMachineError + '\'' +
                ", speed='" + speed + '\'' +
                ", height='" + height + '\'' +
                ", runMiles='" + runMiles + '\'' +
                ", runTime='" + runTime + '\'' +
                '}';
    }

}
