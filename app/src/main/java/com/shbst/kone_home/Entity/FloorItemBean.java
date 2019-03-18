package com.shbst.kone_home.Entity;

import com.shbst.kone_home.Utils.Log;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

public class FloorItemBean {
    //物理楼层
    private int physicalFloor;
    //在界面上显示的数据
    private String displayFloor;
    //楼层描述，如：书房，卧室
    private String floorDescription;
    /*画板 文本，时钟是前台显示的*/
    //楼层对应的显示文本
    private boolean displayTextEnable;
    private String displayText;
    //楼层显示时钟
    private boolean displayClockEnable;
    //显示画板
    private boolean displaySketchpadEnable;
    private String sketchpadImgPath;
    /*图片 背景颜色是作为背景的*/
    //显示图片
    private boolean displayImageEnable;
    private String displayImagePath;
    private int displayBgImgMode;
    //显示背景颜色
    private boolean displayColorEnable;
    private int displayColorMode = -1;

    /*视屏独立*/
    //显示视屏
    private boolean displayVideoEnable;
    private String displayVideoPath;

    //电梯超载
    private boolean oldLoad = false;
    //紧急呼叫模式,-1 无，0：正在呼叫，1：已经接通，2：挂断
    private int alarmMode = -1;

    public int getPhysicalFloor() {
        return physicalFloor;
    }

    public void setPhysicalFloor(int physicalFloor) {
        this.physicalFloor = physicalFloor;
    }

    public String getDisplayFloor() {
        return displayFloor;
    }

    public void setDisplayFloor(String displayFloor) {
        this.displayFloor = displayFloor;
    }

    public String getFloorDescription() {
        return floorDescription;
    }

    public void setFloorDescription(String floorDescription) {
        this.floorDescription = floorDescription;
    }

    public boolean isDisplayTextEnable() {
        return displayTextEnable;
    }

    public void setDisplayTextEnable(boolean displayTextEnable) {
        this.displayTextEnable = displayTextEnable;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        Log.d("texttext","set display text ;"+displayText);

        this.displayText = displayText;
    }

    public boolean isDisplayClockEnable() {
        return displayClockEnable;
    }

    public void setDisplayClockEnable(boolean displayClockEnable) {
        this.displayClockEnable = displayClockEnable;
    }

    public boolean isDisplaySketchpadEnable() {
        return displaySketchpadEnable;
    }

    public void setDisplaySketchpadEnable(boolean displaySketchpadEnable) {
        this.displaySketchpadEnable = displaySketchpadEnable;
    }

    public boolean isDisplayImageEnable() {
        return displayImageEnable;
    }

    public void setDisplayImageEnable(boolean displayImageEnable) {
        this.displayImageEnable = displayImageEnable;
    }

    public String getDisplayImagePath() {
        return displayImagePath;
    }

    public void setDisplayImagePath(String displayImagePath) {
        this.displayImagePath = displayImagePath;
    }

    public boolean isDisplayVideoEnable() {
        return displayVideoEnable;
    }

    public void setDisplayVideoEnable(boolean displayVideoEnable) {
        this.displayVideoEnable = displayVideoEnable;
    }

    public String getDisplayVideoPath() {
        return displayVideoPath;
    }

    public void setDisplayVideoPath(String displayVideoPath) {
        this.displayVideoPath = displayVideoPath;
    }

    public boolean isDisplayColorEnable() {
        return displayColorEnable;
    }

    public void setDisplayColorEnable(boolean displayColorEnable) {
        this.displayColorEnable = displayColorEnable;
    }


    public String getSketchpadImgPath() {
        return sketchpadImgPath;
    }

    public void setSketchpadImgPath(String sketchpadImgPath) {
        this.sketchpadImgPath = sketchpadImgPath;
    }

    public boolean isOldLoad() {
        return oldLoad;
    }

    public void setOldLoad(boolean oldLoad) {
        this.oldLoad = oldLoad;
    }

    public int getAlarmMode() {
        return alarmMode;
    }

    public void setAlarmMode(int alarmMode) {
        this.alarmMode = alarmMode;
    }

    public int getDisplayBgImgMode() {
        return displayBgImgMode;
    }

    public void setDisplayBgImgMode(int displayBgImgMode) {
        this.displayBgImgMode = displayBgImgMode;
    }

    public int getDisplayColorMode() {
        return displayColorMode;
    }

    public void setDisplayColorMode(int displayColorMode) {
        this.displayColorMode = displayColorMode;
    }

}
