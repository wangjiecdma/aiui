package com.shbst.kone_home.Views.GuidePage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.MyTime;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.FontTextView;
import com.shbst.kone_home.Views.GifView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouwenchao on 2018-02-27.
 */

public class GuidePageTime extends RelativeLayout implements View.OnClickListener {
    TimeSelectView timeSelectViewH1, timeSelectViewM2, timeSelectViewH2, timeSelectViewM1, time_selectY, time_selectD, time_selectM;
    ;
    ImageView guide_one_set_12h_view, guide_one_set_24h_view;
    RelativeLayout guide_one_set_time, guide_one_set_date;
    FontTextView guide_click_to_set_date, formatData0, formatData1, formatData2;
    LinearLayout guide_one_switch_time, guide_one_switch_date;
    private String TAG = "GuidePageOne";

    private String NowYear = "2000";        //当前的年份
    private String NowMonth = "1";       //当前的月份
    private String NowDay = "1";         //当前的日期
    private String NowHour = "00";        //当前的小时
    private String NowMinute = "00";      //当前的分钟

    public GuidePageTime(Context context) {
        super(context);
        InitLayout(context);
    }

    public GuidePageTime(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public GuidePageTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        this.invalidate();
        View view = LayoutInflater.from(context).inflate(R.layout.guide_one_page
                , null);

        this.addView(view);

        //add view and init view
        InitView();
        setTimeList();
        setTimeSelect();
    }

    private String Select_h1 = "0";  //时间控制判断参数  十位数值
    private String Select_h2 = "0";  //时间控制判断参数  个位数值
    private String Select_M1 = "0";  //时间控制判断参数  十位数值
    private String Select_M2 = "0";  //时间控制判断参数  个位数值


    private int LEAP_COMMON_YEAR = 0;  // 判断年份是闰年还是平年   0  表示闰年，1 表示平年
    private int MAX_DAY = 0;  //最大天数
    private int MOUTH_SELECT = 0;   //月份选择参数


    private void InitView() {
        GifView gifView = this.findViewById(R.id.pageOneGif);
        gifView.setGifResource(R.drawable.cake_gif);

        timeSelectViewH1 = this.findViewById(R.id.time_selectH1);
        timeSelectViewM2 = this.findViewById(R.id.time_selectM2);
        timeSelectViewH2 = this.findViewById(R.id.time_selectH2);
        timeSelectViewM1 = this.findViewById(R.id.time_selectM1);
        formatData0 = this.findViewById(R.id.format_data0);
        formatData1 = this.findViewById(R.id.format_data1);
        formatData2 = this.findViewById(R.id.format_data2);

        time_selectY = this.findViewById(R.id.time_selectY);
        time_selectM = this.findViewById(R.id.time_selectM);
        time_selectD = this.findViewById(R.id.time_selectD);

        guide_one_set_12h_view = this.findViewById(R.id.guide_one_set_12h_view);
        guide_one_set_24h_view = this.findViewById(R.id.guide_one_set_24h_view);
        guide_one_set_time = this.findViewById(R.id.guide_one_set_time);
        guide_one_set_date = this.findViewById(R.id.guide_one_set_date);
        guide_one_switch_time = this.findViewById(R.id.guide_one_switch_time);
        guide_one_switch_date = this.findViewById(R.id.guide_one_switch_date);
        guide_click_to_set_date = this.findViewById(R.id.guide_click_to_set_date);


        //获取系统时间制  0. 表示24小时制    1. 表示12小时制白天AM   2.表示12小时制晚上PM
        int timeFormat = DeviceUtils.getTimeFormat(getContext());
        if (timeFormat == 0) {
            guide_one_set_24h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_select));
            guide_one_set_12h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_un));
        } else {
            guide_one_set_12h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_select));
            guide_one_set_24h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_un));
        }
        guide_one_set_12h_view.setOnClickListener(this);
        guide_one_set_24h_view.setOnClickListener(this);

        guide_one_set_time.setOnClickListener(this);


        guide_one_set_date.setOnClickListener(this);
//


        formatData0.setOnClickListener(this);
        formatData1.setOnClickListener(this);
        formatData2.setOnClickListener(this);

        //init data

//        int H1 = Integer.valueOf();
//        int m1 = Integer.valueOf();


        timeSelectViewH1.setmItemLabelTvId(0);
        timeSelectViewM2.setmItemLabelTvId(0);
        timeSelectViewH2.setmItemLabelTvId(0);
        timeSelectViewM1.setmItemLabelTvId(0);

        time_selectY.setmItemLabelTvId(1);
        time_selectM.setmItemLabelTvId(1);
        time_selectD.setmItemLabelTvId(1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_one_set_time:
                guide_one_switch_time.setVisibility(VISIBLE);
                guide_one_switch_date.setVisibility(GONE);
                guide_click_to_set_date.setText("Click to set date");
                guide_one_set_time.setBackgroundColor(getResources().getColor(R.color.black));
                guide_one_set_date.setBackgroundColor(getResources().getColor(R.color.setDate));
                break;
            case R.id.guide_one_set_date:
                guide_one_set_time.setBackgroundColor(getResources().getColor(R.color.setDate));
                guide_one_set_date.setBackgroundColor(getResources().getColor(R.color.black));
                guide_one_switch_date.setVisibility(VISIBLE);
                guide_click_to_set_date.setText("");
                guide_one_switch_time.setVisibility(GONE);
                break;
            case R.id.guide_one_set_12h_view:
                guide_one_set_12h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_select));
                guide_one_set_24h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_un));
//                Log.i(TAG, "设置时间格式为 12 小时制");
                PreferManager.getInstence().setTimeFormat12(this.getContext());
                break;
            case R.id.guide_one_set_24h_view:
                guide_one_set_24h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_select));
                guide_one_set_12h_view.setImageDrawable(getResources().getDrawable(R.drawable.button_un));
//                Log.i(TAG, "设置时间格式为 24 小时制");
                PreferManager.getInstence().setTimeFormat24(this.getContext());
                break;
            case R.id.format_data0:
                formatData0.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.darkGray));
                formatData1.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.setDate));
                formatData2.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.setDate));
                PreferManager.getInstence().setDateFormat(view.getContext(), "0");
                break;
            case R.id.format_data1:
                formatData0.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.setDate));
                formatData1.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.darkGray));
                formatData2.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.setDate));
                PreferManager.getInstence().setDateFormat(view.getContext(), "1");
                break;
            case R.id.format_data2:
                formatData0.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.setDate));
                formatData1.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.setDate));
                formatData2.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.darkGray));
                PreferManager.getInstence().setDateFormat(view.getContext(), "2");
                break;
            default:
                throw new Resources.NotFoundException("未知 view 点击事件");
        }
    }


    private void setTimeList() {
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
        timeSelectViewH1.setLabels(labels1);
        timeSelectViewH1.setCycleEnable(false);

        timeSelectViewM2.setLabels(labels);
        timeSelectViewM2.setCycleEnable(true);

        timeSelectViewH2.setLabels(labels);
        timeSelectViewH2.setCycleEnable(true);
        timeSelectViewM1.setLabels(labels2);
        timeSelectViewM1.setCycleEnable(true);

        time_selectY.setLabels(labelsY);
        time_selectY.setCycleEnable(true);
        time_selectM.setLabels(labelsM);
        time_selectM.setCycleEnable(true);
        time_selectD.setLabels(labelsD);
        time_selectD.setCycleEnable(true);

        timeSelectViewH1.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                Select_h1 = label;
                if (Select_h2 != "") {
                    if (Integer.parseInt(Select_h2) >= 5) {
                        if (label.equals("2")) {
                            timeSelectViewH1.setSelectionView(1);
                        }
                    }
                }
//                Log.i(TAG, "onItemSelected:timeSelectViewH1 " + position + "   label :" + timeSelectViewH1.getSelection());
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);
            }
        });

        timeSelectViewH2.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                Select_h2 = label;
//                Log.i(TAG, "onItemSelected:timeSelectViewH2 " + label);
                if (position >= 5) {
                    if (Integer.parseInt(Select_h1) == 2) {
                        timeSelectViewH1.setSelectionView(1);
                    }
                }
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);
            }
        });
        timeSelectViewM1.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                Select_M1 = label;
//                Log.i(TAG, "onItemSelected:timeSelectViewM1 " + label);
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);
            }
        });

        timeSelectViewM2.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                Select_M2 = label;
//                Log.i(TAG, "onItemSelected:timeSelectViewM2 " + label);
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);
            }
        });


        time_selectY.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                NowYear = label;
                if (Float.parseFloat(label) % 4 == 0 && Float.parseFloat(label) % 100 != 0 || Float.parseFloat(label) % 400 == 0) {
                    Log.i(TAG, "年份选择: " + label + "年是闰年");
                    LEAP_COMMON_YEAR = 0;  // 表示闰年
                } else {
                    Log.i(TAG, "年份选择: " + label + "年是平年");
                    LEAP_COMMON_YEAR = 1;  // 表示平年
                }
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);

            }
        });
        time_selectM.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                MOUTH_SELECT = position;
                NowMonth = label;
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);
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
            }
        });

        time_selectD.setOnWheelItemSelectedListener(new TimeSelectView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(View view,int position, String label) {
                Log.i(TAG, "日期选择: " + label);
                NowDay = label;
                if (Integer.parseInt(label) > MAX_DAY) {

                    List<String> labes = time_selectD.getLabels();
                    String maxDayStr = String.valueOf(MAX_DAY);
                    for (int i = 0; i < labes.size(); i++) {
                        if (maxDayStr.equals(labes.get(i))) {
                            time_selectD.setSelectionView(i);
                            Log.i("日期选择: 选择的日期大于当月最大天数");
                        }
                    }
                }
                guideOneHandler.removeMessages(MainHandlerEnum.SET_TIME_DATE.ordinal());
                guideOneHandler.sendEmptyMessageDelayed(MainHandlerEnum.SET_TIME_DATE.ordinal(), 500);
            }
        });


    }

    private void setTimeSelect() {
        Log.v("set time select");
        // TODO 初始选择
        int Y = Integer.valueOf(MyTime.date_Y());
        int M = Integer.valueOf(MyTime.date_m());
        int D = Integer.valueOf(MyTime.date_D());

        String strH = MyTime.date_H();
        String strm = MyTime.date_M();
        int H1 = 0;
        int m1 = 0;
        int H2 = 0;
        int m2 = 0;
        if (strH.length() >= 2) {
            H1 = Integer.valueOf(strH.substring(0, 1));
            H2 = Integer.valueOf(strH.substring(1, strH.length()));
        } else {
            H2 = Integer.valueOf(strH);
        }
        if (strm.length() >= 2) {
            m1 = Integer.valueOf(strm.substring(0, 1));
            m2 = Integer.valueOf(strm.substring(1, strm.length()));
        } else {
            m2 = Integer.valueOf(strm);
        }
        Log.v("h1:" + H1
                + " h2" + H2
                + " m1:" + m1
                + " m2:" + m2
                + " Y:" + Y
                + " M:" + M
                + " D:" + D);


//        timeSelectViewM1.setSelectionView(1);
//        timeSelectViewH2.setSelectionView(1);
//        timeSelectViewM2.setSelectionView(1);
//        time_selectY.setSelectionView(2);
//        time_selectM.setSelectionView(1);
//        time_selectD.setSelectionView(1);


        timeSelectViewH1.setSelectionView(H1);
        timeSelectViewM1.setSelectionView(m1);
        timeSelectViewH2.setSelectionView(H2);
        timeSelectViewM2.setSelectionView(m2);
        time_selectY.setSelectionView(Y - 2000);
        time_selectM.setSelectionView(M-1);
        time_selectD.setSelectionView(D);

//        time_selectM.setSelectionView(M - 2);
//        time_selectD.setSelectionView(D - 2);
    }

    private enum MainHandlerEnum {
        SET_TIME_DATE  //设置当前时间日期
    }

    @SuppressLint("HandlerLeak")
    Handler guideOneHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainHandlerEnum handlerCase = MainHandlerEnum.values()[msg.what];
            switch (handlerCase) {
                case SET_TIME_DATE:
                    NowHour = Select_h1 + Select_h2;
                    NowMinute = Select_M1 + Select_M2;
                    // TODO 设置当前时间
                    Log.i(TAG, "当前时间: "
                            + NowYear
                            + "年"
                            + NowMonth
                            + "月"
                            + NowDay
                            + "日"
                            + NowHour
                            + "时"
                            + NowMinute
                            + "分");
                    PreferManager.getInstence().setDataTime(
                            NowYear
                            , NowMonth
                            , NowDay
                            , NowHour
                            , NowMinute
                    );
                    break;
            }
        }
    };


}
