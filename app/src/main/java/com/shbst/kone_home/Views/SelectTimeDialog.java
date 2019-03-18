package com.shbst.kone_home.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.MyTime;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.GuidePage.TimeSelectView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by zhouwenchao on 2018-03-15.
 */

public class SelectTimeDialog extends AlertDialog
        implements TimeSelectView.WheelItemSelectedListener {
    TimeSelectView timeSelectViewH1, timeSelectViewH2, timeSelectViewM2, timeSelectViewM1;

    private String timeH1;
    private String timeH2;
    private String timeM1;
    private String timeM2;
    private Context mContext;

    public SelectTimeDialog(Context context) {
        super(context);
        InitDialog(context);
    }

    public SelectTimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        InitDialog(context);
    }

    public SelectTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
        InitDialog(context);
    }

    private void InitDialog(Context context) {
        this.mContext = context;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_time_select_layout, null);

        timeSelectViewH1 = dialogView.findViewById(R.id.time_selectH1);
        timeSelectViewH2 = dialogView.findViewById(R.id.time_selectH2);
        timeSelectViewM2 = dialogView.findViewById(R.id.time_selectM2);
        timeSelectViewM1 = dialogView.findViewById(R.id.time_selectM1);

        timeSelectViewH1.setmItemLabelTvId(0);
        timeSelectViewH2.setmItemLabelTvId(0);
        timeSelectViewM2.setmItemLabelTvId(0);
        timeSelectViewM1.setmItemLabelTvId(0);

        List<String> labels1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            labels1.add("" + i);
        }
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            labels.add("" + i);
        }
        List<String> labels2 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            labels2.add("" + i);
        }

        timeSelectViewH1.setLabels(labels1);
        timeSelectViewH1.setCycleEnable(false);

        timeSelectViewM2.setLabels(labels);
        timeSelectViewM2.setCycleEnable(true);

        timeSelectViewH2.setLabels(labels);
        timeSelectViewH2.setCycleEnable(true);
        timeSelectViewM1.setLabels(labels2);
        timeSelectViewM1.setCycleEnable(true);

        timeSelectViewH1.setOnWheelItemSelectedListener(this);
        timeSelectViewH2.setOnWheelItemSelectedListener(this);
        timeSelectViewM1.setOnWheelItemSelectedListener(this);
        timeSelectViewM2.setOnWheelItemSelectedListener(this);

        String strH = MyTime.date_H();
        String strm = MyTime.date_M();
        int H1 = 0;
        int m1 = 0;
        int H2 = 0;
        int m2 = 0;
        if (strH.length() >= 2) {
            timeH1 = strH.substring(0, 1);
            timeH2 = strH.substring(1, strH.length());
            H1 = Integer.valueOf(timeH1);
            H2 = Integer.valueOf(timeH2);
        } else {
            timeH1 = "";
            timeH2 = strH;
            H2 = Integer.valueOf(strH);
        }
        if (strm.length() >= 2) {
            timeM1 = strm.substring(0, 1);
            timeM2 = strm.substring(1, strm.length());
            m1 = Integer.valueOf(timeM1);
            m2 = Integer.valueOf(timeM2);
        } else {
            timeM1 = "";
            timeM2 = strm;
            m2 = Integer.valueOf(strm);
        }

        timeSelectViewH1.setSelectionView(H1);
        timeSelectViewM1.setSelectionView(m1);
        timeSelectViewH2.setSelectionView(H2);
        timeSelectViewM2.setSelectionView(m2);

        this.setView(dialogView);
    }

    public String getTimeH1() {
        return timeH1;
    }

    public String getTimeH2() {
        return timeH2;
    }

    public String getTimeM1() {
        return timeM1;
    }

    public String getTimeM2() {
        return timeM2;
    }

    @Override
    public void onItemSelected(View view, int position, String label) {
        switch (view.getId()) {
            case R.id.time_selectH1:
                timeH1 = label;
                if (!TextUtils.isEmpty(timeH2)) {
                    if (Integer.parseInt(timeH2) >= 4) {
                        if (label.equals("2")) {
                            timeSelectViewH1.setSelectionView(1);
                        }
                    }
                }
                break;
            case R.id.time_selectH2:
                timeH2 = label;
                if (position >= 4) {
                    if (Integer.parseInt(timeH1) == 2) {
                        timeSelectViewH1.setSelectionView(1);
                    }
                }
                break;
            case R.id.time_selectM1:
                timeM1 = label;
                break;
            case R.id.time_selectM2:
                timeM2 = label;
                break;
        }
    }

    private void setTime() {
        boolean isFormat;
        String format = PreferManager.getInstence().getTimeFormat(mContext);
        isFormat = !format.equals("12H");

        int hourOfDay = Integer.valueOf(timeH1 + timeH2);
        int minute = Integer.valueOf(timeM1 + timeM2);
        if (!isFormat) {
            if (hourOfDay > 12) {
                hourOfDay = hourOfDay - 12;
            }
        }
//        setTime_H(String.valueOf(hourOfDay));
//        setTime_M(String.valueOf(minute));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();
        if (when / 1000 < Integer.MAX_VALUE) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }

    interface SelectTimeDialogCancelLisenter {
        void dialogCancel(String h1, String h2, String m1, String m2);
    }
}
