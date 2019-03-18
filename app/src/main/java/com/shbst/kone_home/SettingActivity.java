package com.shbst.kone_home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Utils.PreferUtils;
import com.shbst.kone_home.Utils.SysPrefer;
import com.shbst.kone_home.Views.FontEditView;
import com.shbst.kone_home.Views.SelectDateDialog;
import com.shbst.kone_home.Views.SelectTimeDialog;
import com.shbst.kone_home.Views.SettingItemView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.shbst.kone_home.Utils.Constant.locals;
import static com.shbst.kone_home.Utils.MyTime.date_D;
import static com.shbst.kone_home.Utils.MyTime.date_H;
import static com.shbst.kone_home.Utils.MyTime.date_M;
import static com.shbst.kone_home.Utils.MyTime.date_Y;
import static com.shbst.kone_home.Utils.MyTime.date_m;

public class SettingActivity extends BaseActivity
        implements View.OnClickListener {

    private LinearLayout dataData, floorItemLayout;
    //    languageView,
    private TextView time_H, time_m, time_H_2, time_m_2, data_Y, data_M, data_D, formatTimeSet, formatData;
    private FontEditView passInput;
    private LinearLayout languageSet, dataSetTime;
    private ImageView setBoard, exitBtn;
    private ArrayList<SettingItemView> itemViews;
    private RadioGroup themeRGView, languageRGView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        refreshDisplay();
//        setDataFormat(PreferManager.getInstence().getDataFormat(this)
//                , date_Y(), String.valueOf((Integer.valueOf(date_m()) - 1) <= 0 ? 12 :
//                        (Integer.valueOf(date_m()) - 1)), date_D());
        setDataFormat(PreferManager.getInstence().getDateFormat(this)
                , date_Y(), date_m(), date_D());
        setTimeFormat();
        Log.d("lang onCreate");


    }

    @Override
    protected void InitView() {
        //find view
//        languageView = findViewById(R.id.language_title);
        languageSet = findViewById(R.id.language_set);
        floorItemLayout = findViewById(R.id.floor_Item_Layout);
        setBoard = findViewById(R.id.set_board);
        exitBtn = findViewById(R.id.exit_menu);
        dataSetTime = findViewById(R.id.date_setTime);
        time_H = findViewById(R.id.time_H);
        time_H_2 = findViewById(R.id.time_H_2);
        time_m = findViewById(R.id.time_m);
        time_m_2 = findViewById(R.id.time_m_2);
        formatTimeSet = findViewById(R.id.format_time);
        dataData = findViewById(R.id.data_date);
        data_Y = findViewById(R.id.dataY);
        data_M = findViewById(R.id.dataM);
        data_D = findViewById(R.id.dataD);
        formatData = findViewById(R.id.format_data);
        passInput = findViewById(R.id.setup_pass);
        themeRGView = findViewById(R.id.theme_radio);
        languageRGView = findViewById(R.id.language_radio);
        //set onclick listener
        languageSet.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        dataSetTime.setOnClickListener(this);
        formatTimeSet.setOnClickListener(this);
        dataData.setOnClickListener(this);
        formatData.setOnClickListener(this);
        passInput.addTextChangedListener(new TextWatcher() {
            String value;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.v("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.v("beforeTextChanged :" + passInput.getText());
                value = passInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.v("beforeTextChanged");
                PreferManager.getInstence().setPassword(SettingActivity.this, value);
            }
        });
        themeRGView.setOnCheckedChangeListener(themeSelectListener);
        //Init data
        passInput.setText(PreferManager.getInstence().getPassword(SettingActivity.this));
        int themeMode = PreferManager.getInstence().getThemeMode(this);
        switch (themeMode) {
            case Constant.THEME_BLOCKS:
                themeRGView.check(R.id.guideRadio);
                break;
            case Constant.THEME_SPHERE:
                themeRGView.check(R.id.miniRadio);
                break;
            default:
                throw new Resources.NotFoundException("未找到的主题模式");
        }
        String languegeMode = PreferManager.getInstence().getLanguageMode(this);
        switch (languegeMode) {
            case "zh_CN":
                languageRGView.check(R.id.chineseRadio);
                break;
            case "en":
                languageRGView.check(R.id.enRadio);
                break;
            case "hk":
                languageRGView.check(R.id.hkRadio);
                break;
            default:
                throw new Resources.NotFoundException("未找到的语言类型");
        }

        languageRGView.setOnCheckedChangeListener(languageCheckListener);


        findViewById(R.id.reset_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        resetApp();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setTitle("提示");
                builder.setMessage("您确定要恢复出厂设置吗？");
                builder.create().show();

            }
        });
    }

    private void refreshDisplay() {
//        String launguageMode = PreferManager.getInstence().getLanguageMode(this);
//        for (int i = 0; i < Constant.locals.length; i++) {
//            if (!launguageMode.equals(Constant.locals[i])) continue;
//            switch (i) {
//                case 0:
//                    languageView.setText("简体中文");
//                    break;
//                case 1:
//                    languageView.setText("English");
//                    break;
//                case 2:
//                    languageView.setText("繁體中文");
//                    break;
//            }
//        }
        //楼层对应控件
        int viewCount = SysPrefer.getInstence().getFloorCount();
        if (itemViews == null) itemViews = new ArrayList<>();
        for (int i = viewCount; i >= 1; i--) {  //物理楼层保证从 1 开始
            FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(this, i);
            SettingItemView floorItemView = new SettingItemView(this, itemBean);
            itemViews.add(floorItemView);
            floorItemLayout.addView(floorItemView);
        }
    }

    private void showSelectLanguageDialog() {
        Log.v("display select language dialog.");
        final String[] cities = {getString(R.string.lan_chinese), getString(R.string.lan_en), getString(R.string.lan_hk)};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.select_language);
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferManager.getInstence().putLanguageMode(SettingActivity.this, locals[which]);
                changeAppLanguage();
                recreate();//刷新界面
            }
        });
        builder.show();
    }

    private void showDateSetTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(this);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (dialogInterface instanceof SelectTimeDialog) {
                    boolean isFormat;
                    String format = PreferManager.getInstence().getTimeFormat(SettingActivity.this);
                    isFormat = !format.equals("12H");

                    int hourOfDay = Integer.valueOf(((SelectTimeDialog) dialogInterface).getTimeH1()
                            + ((SelectTimeDialog) dialogInterface).getTimeH2());
                    int minute = Integer.valueOf(((SelectTimeDialog) dialogInterface).getTimeM1()
                            + ((SelectTimeDialog) dialogInterface).getTimeM2());

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    long when = c.getTimeInMillis();
                    if (when / 1000 < Integer.MAX_VALUE) {
                        SystemClock.setCurrentTimeMillis(when);
                    }
                    //在设置时间成功后再更新显示
                    if (!isFormat) {
                        if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                        }
                    }
                    setTime_H(String.valueOf(hourOfDay));
                    setTime_M(String.valueOf(minute));
                } else {
                    Log.w("setting time error,dialog instanceof SelectTimeDialog error");
                }
            }
        });
        dialog.show();
    }

    private void showFormatSetDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.format_time)
                .setItems(new String[]{"12H", "24H"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Settings.System.putString(getContentResolver(), Settings.System.TIME_12_24, "12");
                                formatTimeSet.setText("12H");
                                int timeHInt = Integer.parseInt(date_H());
                                if (timeHInt > 12)
                                    setTime_H(String.valueOf(String.valueOf(timeHInt - 12)));
                                else
                                    setTime_H(String.valueOf(String.valueOf(timeHInt)));
                                PreferManager.getInstence().setTimeFormat12(SettingActivity.this);
                                break;
                            case 1:
                                Settings.System.putString(getContentResolver(), Settings.System.TIME_12_24, "24");
                                formatTimeSet.setText("24H");
                                setTime_H(date_H());
                                PreferManager.getInstence().setTimeFormat24(SettingActivity.this);
                                break;
                            default:
                                break;
                        }
                    }
                }).create();
        dialog.show();

    }

    private void showDataSetDialog() {
        SelectDateDialog selectDateDialog = new SelectDateDialog(this);
        selectDateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (dialogInterface instanceof SelectDateDialog) {
                    int Y = Integer.parseInt(((SelectDateDialog) dialogInterface).getDateY());
                    int m = Integer.parseInt(((SelectDateDialog) dialogInterface).getDateM());
                    Log.v("保存日期：" + m);
                    int d = Integer.parseInt(((SelectDateDialog) dialogInterface).getDateD());

                    Calendar canlendar = Calendar.getInstance();
                    canlendar.set(Calendar.YEAR, Y);
                    canlendar.set(Calendar.MONTH, m - 1);
                    canlendar.set(Calendar.DAY_OF_MONTH, d);
                    long when = canlendar.getTimeInMillis();
                    if (when / 1000 < Integer.MAX_VALUE) {
                        SystemClock.setCurrentTimeMillis(when); // 需要系统权限才能执行
                    }



                    setDataFormat(PreferManager.getInstence().getDateFormat(SettingActivity.this)
                            , String.valueOf(Y), String.valueOf((m)), String.valueOf(d));
                } else {
                    Log.w("save date error :" + dialogInterface);
                }
            }
        });
        selectDateDialog.show();
    }

    private void showDataFormatDialog() {
        new AlertDialog.Builder(SettingActivity.this)
                .setTitle(R.string.format_time)
                .setItems(PreferManager.getInstence().getDateList(this), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String style = String.valueOf(which);
                        PreferManager.getInstence().setDateFormat(SettingActivity.this, style);

                        setDataFormat(style, date_Y(), date_m(), date_D());
                    }
                }).show();
    }

    private void setDataFormat(String style, String y, String m, String d) {
        y = y.trim();
        m = m.trim();
        d = d.trim();
        if (m.length() < 2) {
            m = "0" + m;
        }
        if (d.length() < 2) {
            d = "0" + d;
        }
//        StringBuilder timeStr = new StringBuilder();
        int styles = Integer.valueOf(style);
        //TODO 时间控件格式化 设置页面这里要完善
        switch (styles) {
            case 0:
//                dataData.setText(d + "/" + m + "/" + y);
                data_Y.setText(d);
                data_M.setText(m);
                data_D.setText(y);
                break;
            case 1:
//                dataData.setText(m + "/" + d + "/" + y);
                data_Y.setText(m);
                data_M.setText(d);
                data_D.setText(y);
                break;
            case 2:
//                dataData.setText(y + "/" + m + "/" + d);
                data_Y.setText(y);
                data_M.setText(m);
                data_D.setText(d);
                break;
            default:
                data_Y.setText(d);
                data_M.setText(m);
                data_D.setText(y);
                break;
        }
//        ContextWrapper contextWrapper = new ContextWrapper(this);
        formatData.setText(PreferManager.getInstence().getDate(this));
    }

    void setTimeFormat() {
        String format = PreferManager.getInstence().getTimeFormat(this);
        if (format.equals("12H")) {
            int timeHInt = Integer.parseInt(date_H());
            if (timeHInt > 12)
                setTime_H(String.valueOf(timeHInt - 12));
            else {
                setTime_H(String.valueOf(timeHInt));
            }
        } else {
            setTime_H(date_H());
        }
        setTime_M(date_M());
//        time_M.setText(date_M());
        formatTimeSet.setText(PreferManager.getInstence().getTimeFormat(this));
    }

    private void setTime_H(String timeh) {
        if (timeh.length() >= 2) {
            time_H.setText(timeh.substring(0, 1));
            time_H_2.setText(timeh.substring(1, timeh.length()));
        } else {
            time_H.setText("0");
            time_H_2.setText(timeh);
        }
    }

    private void setTime_M(String timem) {
        if (timem.length() >= 2) {
            time_m.setText(timem.substring(0, 1));
            time_m_2.setText(timem.substring(1, timem.length()));
        } else {
            time_m.setText("0");
            time_m_2.setText(timem);
        }
    }


    private void ExitSetting() {
        freshData();
        finish();
        overridePendingTransition(R.anim.fragment_slide_out_from_bottom
                , R.anim.finish_slide_in_from_bottom);
    }

    private void freshData() {
        for (SettingItemView itemView : itemViews) {
            itemView.freshItemBeam();
        }
    }


    @Override
    public void onClick(View view) {
        Log.v("view onclick ,view id :" + view.getId());
        switch (view.getId()) {
            case R.id.language_set:
                Log.d("取消语言选择弹窗");
//                showSelectLanguageDialog();
                break;
            case R.id.exit_menu:
                ExitSetting();
                break;
            case R.id.date_setTime:
                showDateSetTimeDialog();
                break;
            case R.id.format_time:
                showFormatSetDialog();
                break;
            case R.id.data_date:
                showDataSetDialog();
                break;
            case R.id.format_data:
                showDataFormatDialog();
                break;
        }
    }

    private void resetApp(){
        PreferUtils.clearAppData(this);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitSetting();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("lang 3");
                changeSystemLanguage();
                Log.d("lang 4");
            }
        });
        thread.start();
    }

    private RadioGroup.OnCheckedChangeListener themeSelectListener = new RadioGroup.OnCheckedChangeListener() {


        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int checkIndex = Constant.THEME_BLOCKS;
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.guideRadio:
                    checkIndex = Constant.THEME_BLOCKS;
                    break;
                case R.id.miniRadio:
                    checkIndex = Constant.THEME_SPHERE;
                    break;
            }
            PreferManager.getInstence().putThemeMode(SettingActivity.this, checkIndex);
        }
    };
    private RadioGroup.OnCheckedChangeListener languageCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            String languegeValue = locals[0];
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.chineseRadio:
                    languegeValue = locals[0];
                    break;
                case R.id.enRadio:
                    languegeValue = locals[1];
                    break;
                case R.id.hkRadio:
                    languegeValue = locals[2];
                    break;
            }
            Log.d("lang 0");
            freshData();

            PreferManager.getInstence().putLanguageMode(SettingActivity.this, languegeValue);
            Log.d("lang 1");
            changeAppLanguage();
            Log.d("lang 2");
            recreate();//刷新界面






//            RadioButton chineseRadio = findViewById(R.id.chineseRadio);//            RadioButton enRadio = findViewById(R.id.enRadio);
//            RadioButton hkRadio = findViewById(R.id.hkRadio);
//            chineseRadio.setText(getText(R.string.lan_chinese));
//            enRadio.setText(getText(R.string.lan_en));
//            hkRadio.setText(getText(R.string.lan_hk));

            RadioButton miniRadio = findViewById(R.id.miniRadio);
            RadioButton guideRadio = findViewById(R.id.guideRadio);
            miniRadio.setText(R.string.minimal_spheres);
            guideRadio.setText(R.string.guide_blocks);

        }
    };
}
