package com.shbst.kone_home.Entity.FrameEntity;

import android.content.Context;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.SysPrefer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by suzhiming on 2017/11/7.
 */

public class Frame0BaseInfo {

    public static final int TAG = 0x00;

    private String currentFloor;
    private int arrowState;//include functioninfo
    private int newSpecialState;

    private int[] specialState;//old version
    private boolean arrive_enable;
    private boolean voice_reporter_enable;
    private boolean savemode_enable;
    private boolean arriving_enable;

    private boolean isLeftView = false; //更改左边控件显示

    private boolean isAdenburg = false;

    public boolean getArrive_enable() {
        return arrive_enable;
    }

    public void setArrive_enable(boolean arrive_enable) {
        this.arrive_enable = arrive_enable;
    }

    public boolean getVoice_reporter_enable() {
        return voice_reporter_enable;
    }

    public void setVoice_reporter_enable(boolean voice_reporter_enable) {
        this.voice_reporter_enable = voice_reporter_enable;
    }

    public boolean getSavemode_enable() {
        return savemode_enable;
    }

    public void setSavemode_enable(boolean savemode_enable) {
        this.savemode_enable = savemode_enable;
    }

    public boolean getArriving_enable() {
        return arriving_enable;
    }

    public void setArriving_enable(boolean arriving_enable) {
        this.arriving_enable = arriving_enable;
    }

    public int[] getSpecialState() {
        return specialState;
    }

    public void setSpecialState(int[] specialState) {
        this.specialState = specialState;
    }

    public String getCurrentFloor() {
        return currentFloor;
    }

    private static HashMap<String,String> mFloorMarkingMap=null;
    public String getCurrentFloorIndex(Context context){
        String  current = getCurrentFloor().toLowerCase();

        if (mFloorMarkingMap == null){
            mFloorMarkingMap = new LinkedHashMap<>();

            int count =  SysPrefer.getInstence().getFloorCount();

            for (int i=count;i>=1;i--){
                FloorItemBean bean = new FloorItemBean();
                SysPrefer.getInstence().getFloorItemBean(i,bean);
                String display = bean.getDisplayFloor().toLowerCase();
                mFloorMarkingMap.put(display,String.valueOf(i));
                Log.d("www","create floor marking map :"+display + " >>> "+i);
            }
        }
        String index =  mFloorMarkingMap.get(current);
        if (index == null){
            Log.d("www"," not use floor marking map  "+current);
            return current;
        }else{
            Log.d("www"," use floor marking map  "+current);
            return index;
        }
    }

    public void setCurrentFloor(String currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getArrowState() {
        return arrowState;
    }

    public void setArrowState(int arrowState) {
        this.arrowState = arrowState;
    }

    public boolean isLeftView() {
        return isLeftView;
    }

    public void setLeftView(boolean leftView) {
        isLeftView = leftView;
    }

    public boolean isAdenburg() {
        return isAdenburg;
    }

    public void setAdenburg(boolean adenburg) {
        isAdenburg = adenburg;
    }

    @Override
    public String toString() {
        return "EvltBaseInfo{" +
                "currentFloor='" + currentFloor + '\'' +
                ", arrowState=" + arrowState +
                ", newSpecialState=" + newSpecialState +
                ", specialState=" + Arrays.toString(specialState) +
                ", arrive_enable=" + arrive_enable +
                ", voice_reporter_enable=" + voice_reporter_enable +
                ", savemode_enable=" + savemode_enable +
                ", arriving_enable=" + arriving_enable +
                ", isLeftView=" + isLeftView +
                ", isAdenburg=" + isAdenburg +
                '}';
    }

    public int getNewSpecialState() {
        return newSpecialState;
    }

    public void setNewSpecialState(int newSpecialState) {
        this.newSpecialState = newSpecialState;
    }
}
