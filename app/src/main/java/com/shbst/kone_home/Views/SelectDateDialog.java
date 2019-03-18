package com.shbst.kone_home.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.MyTime;
import com.shbst.kone_home.Views.GuidePage.TimeSelectView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouwenchao on 2018-03-15.
 */

public class SelectDateDialog extends AlertDialog
        implements TimeSelectView.WheelItemSelectedListener {
    TimeSelectView time_selectY, time_selectD, time_selectM;

    private Context mContext;

    private String dateY;
    private String dateM;
    private String dateD;

    private int LEAP_COMMON_YEAR = 0;  // 判断年份是闰年还是平年   0  表示闰年，1 表示平年
    private int MAX_DAY = 0;  //最大天数
    private int MOUTH_SELECT = 0;   //月份选择参数

    public SelectDateDialog(Context context) {
        super(context);
        InitDialog(context);
    }

    protected SelectDateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        InitDialog(context);
    }

    protected SelectDateDialog(Context context, int themeResId) {
        super(context, themeResId);
        InitDialog(context);
    }

    private void InitDialog(Context context) {
        this.mContext = context;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_date_select_layout, null);

        time_selectY = dialogView.findViewById(R.id.time_selectY);
        time_selectM = dialogView.findViewById(R.id.time_selectM);
        time_selectD = dialogView.findViewById(R.id.time_selectD);


        time_selectY.setmItemLabelTvId(1);
        time_selectM.setmItemLabelTvId(1);
        time_selectD.setmItemLabelTvId(1);

        List<String> labelsY = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            labelsY.add(String.valueOf(2000 + i));
        }
        List<String> labelsM = new LinkedList<>();
        for (int i = 1; i < 13; i++) {
            labelsM.add("" + i);
        }
        List<String> labelsD = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            labelsD.add("" + i);
        }

        time_selectY.setLabels(labelsY);
        time_selectY.setCycleEnable(true);
        time_selectM.setLabels(labelsM);
        time_selectM.setCycleEnable(true);
        time_selectD.setLabels(labelsD);
        time_selectD.setCycleEnable(true);

        time_selectY.setOnWheelItemSelectedListener(this);
        time_selectM.setOnWheelItemSelectedListener(this);
        time_selectD.setOnWheelItemSelectedListener(this);

        dateY = MyTime.date_Y();
        dateM = MyTime.date_m();
        dateD = MyTime.date_D();

        int Y = Integer.valueOf(MyTime.date_Y());
        int M = Integer.valueOf(MyTime.date_m());
        int D = Integer.valueOf(MyTime.date_D());

//        dateY = String.valueOf(Y);
//        dateM = String.valueOf(M);
//        dateD = String.valueOf(D);

        int selectIndexM = M - 1;
//        Log.v("保存日期：" + M + "  selectIndexM:" + selectIndexM + " get"
//                + time_selectM.getLabels().get(selectIndexM));
//        Log.v("switch M：" + M);

        time_selectY.setSelectionView(Y - 2000);
        time_selectM.setSelectionView(selectIndexM);
        time_selectD.setSelectionView(D - 1);

        Log.v("M:" + M + "  D:" + D);

        this.setView(dialogView);
    }

    @Override
    public void onItemSelected(View view, int position, String label) {
        switch (view.getId()) {
            case R.id.time_selectY:
                dateY = label;
                if (Float.parseFloat(label) % 4 == 0 && Float.parseFloat(label) % 100 != 0 || Float.parseFloat(label) % 400 == 0) {
                    Log.i("年份选择: " + label + "年是闰年");
                    LEAP_COMMON_YEAR = 0;  // 表示闰年
                } else {
                    Log.i("年份选择: " + label + "年是平年");
                    LEAP_COMMON_YEAR = 1;  // 表示平年
                }
                setMAX_M(dateM);
                break;
            case R.id.time_selectM:
                MOUTH_SELECT = position;
                dateM = label;
                Log.v("保存日期：" + dateM);
                setMAX_M(dateM);
                break;
            case R.id.time_selectD:
                Log.i("日期选择: " + label);
                dateD = label;
                setMAX_DAY(label);
                break;

        }
    }

    private void setMAX_M(String label) {
        //月份判断
        switch (Integer.parseInt(label)) {
            case 2:
                if (LEAP_COMMON_YEAR == 0) {
                    Log.i("onItemSelected: 2月天数为29天");
                    MAX_DAY = 29;
                } else {
                    Log.i("onItemSelected: 2月天数为28天");
                    MAX_DAY = 28;
                }
                break;
            case 1:
                MAX_DAY = 31;
            case 3:
                MAX_DAY = 31;
            case 5:
                MAX_DAY = 31;
            case 7:
                MAX_DAY = 31;
            case 8:
                MAX_DAY = 31;
            case 10:
                MAX_DAY = 31;
            case 12:
                MAX_DAY = 31;
                break;
            default:
                MAX_DAY = 30;
                break;
        }
        setMAX_DAY(dateD);
    }

    private void setMAX_DAY(String label) {
        if (Integer.parseInt(label) > MAX_DAY) {
//                    time_selectM.setSelectionView(MOUTH_SELECT);
            List<String> labes = time_selectD.getLabels();
            String maxDayStr = String.valueOf(MAX_DAY);
            for (int i = 0; i < labes.size(); i++) {
                if (maxDayStr.equals(labes.get(i))) {
                    time_selectD.setSelectionView(i);
                    Log.i("日期选择: 选择的日期大于当月最大天数");
                }
            }
        }
    }

    public String getDateY() {
        return dateY;
    }

    public String getDateM() {
        return dateM;
    }

    public String getDateD() {
        return dateD;
    }
}
