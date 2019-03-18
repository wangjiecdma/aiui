package com.shbst.kone_home.Utils;

import android.annotation.SuppressLint;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

public class PreferManager {
    private static PreferManager instence;
    private Gson gson;

    private PreferManager() {
        gson = new Gson();
    }

    private static  HashMap<String,String> mDateStrMap = new LinkedHashMap<>();

    public static PreferManager getInstence() {
        if (instence == null) {
            synchronized (PreferManager.class) {
                if (instence == null) {
                    instence = new PreferManager();
                    mDateStrMap.put("Monday","星期壹");
                    mDateStrMap.put("Tuesday","星期二");
                    mDateStrMap.put("Wednesday","星期三");
                    mDateStrMap.put("Thursday","星期四");
                    mDateStrMap.put("Friday","星期五");
                    mDateStrMap.put("Saturday","星期六");
                    mDateStrMap.put("Sunday","星期日");

                    mDateStrMap.put("January","壹月");
                    mDateStrMap.put("Jan","壹月");

                    mDateStrMap.put("February","二月");
                    mDateStrMap.put("Feb","二月");

                    mDateStrMap.put("March","三月");
                    mDateStrMap.put("Mar","三月");

                    mDateStrMap.put("April","四月");
                    mDateStrMap.put("Apr","四月");

                    mDateStrMap.put("May","五月");

                    mDateStrMap.put("June","六月");
                    mDateStrMap.put("Jun","六月");

                    mDateStrMap.put("July","七月");
                    mDateStrMap.put("Jul","七月");

                    mDateStrMap.put("August","八月");
                    mDateStrMap.put("Aug","八月");

                    mDateStrMap.put("September","九月");
                    mDateStrMap.put("Sep","九月");

                    mDateStrMap.put("October","十月");
                    mDateStrMap.put("Oct","十月");

                    mDateStrMap.put("November","十壹月");
                    mDateStrMap.put("Nov","十壹月");

                    mDateStrMap.put("December","十二月");
                    mDateStrMap.put("Dec","十二月");


                }
            }
        }
        return instence;
    }

    public int getFloorCount() {
        return SysPrefer.getInstence().getFloorCount();
    }

    public FloorItemBean getFloorItemBean(Context context, int floor) {

        //code
        String key = Constant.FLOOR_LEFT_APPEND_KEY + floor;
        String jsonData = PreferUtils.getString(context, key, "");
        FloorItemBean beam = null;
        if (!jsonData.isEmpty()) {
            beam = gson.fromJson(jsonData, FloorItemBean.class);
        }
        beam = SysPrefer.getInstence().getFloorItemBean(floor, beam);

        if (beam.getPhysicalFloor() == 0)  //默认楼层
            beam.setPhysicalFloor(floor);


        if (!beam.isDisplaySketchpadEnable()
                && !beam.isDisplayTextEnable()
                && !beam.isDisplayClockEnable()) {  //默认显示 文字功能，且文字为 KONE WELCOME
//            beam.setDisplaySketchpadEnable(true);
            beam.setDisplayTextEnable(true);
            beam.setDisplayText(context.getResources().getString(R.string.kone_welcome));
            //beam.setDisplayText("KONE WELCOME");
        }
        if (!beam.isDisplayColorEnable()
                && !beam.isDisplayImageEnable())  //默认显示背景颜色
            beam.setDisplayColorEnable(true);

        if (beam.getDisplayColorMode() == -1)  //默认显示颜色
            beam.setDisplayColorMode(2);

        if (TextUtils.isEmpty(beam.getFloorDescription())) {
            String str ;
//            if(floor == 1){
//                str = context.getResources().getString(R.string.floor_parking);
//            }else if(floor ==2 ){
//                str = context.getResources().getString(R.string.floor_lobby);
//            }else {
//                str = context.getResources().getString(R.string.floor);
//            }
            // 统一显示为楼
            str = context.getResources().getString(R.string.floor);
            beam.setFloorDescription(str);
        }


        if (TextUtils.isEmpty(beam.getDisplayFloor())) {
            switch (floor) {
                case 1:
                    beam.setDisplayFloor("P");
                    break;
                case 2:
                    beam.setDisplayFloor("G");
                    break;
                case 3:
                    beam.setDisplayFloor("2");
                    break;
                case 4:
                    beam.setDisplayFloor("3");
                    break;
                case 5:
                    beam.setDisplayFloor("5");
                    break;
                default:
                    beam.setDisplayFloor(String.valueOf(floor));
                    break;
            }
        }

        beam.setOldLoad(false);
        beam.setAlarmMode(-1);
        return beam;
    }

    /**
     * 保存 beam数据到配置文件中
     *
     * @param context
     * @param itemBean
     */
    public void putFloorItemBean(Context context, FloorItemBean itemBean) {
        String key = Constant.FLOOR_LEFT_APPEND_KEY + itemBean.getPhysicalFloor();
        String jsonData = gson.toJson(itemBean);
        PreferUtils.setString(context, key, jsonData);
    }

    public String getLanguageMode(Context context) {
        //return PreferUtils.getString(context, Constant.LANGUAGE_MODE_KEY, "zh_CN");
        return PreferUtils.getString(context, Constant.LANGUAGE_MODE_KEY, "en");
    }

    public void putLanguageMode(Context context, String value) {
        PreferUtils.setString(context, Constant.LANGUAGE_MODE_KEY, value);
    }

    public int getThemeMode(Context context) {
        return PreferUtils.getInt(context, Constant.THEME_KEY, Constant.THEME_BLOCKS);
    }

    public void putThemeMode(Context context, int mode) {
        PreferUtils.setInt(context, Constant.THEME_KEY, mode);
    }

    public int getColorResFromMode(int mode) {
        switch (mode) {
            case 0:
                return R.drawable.gree_round;
            case 1:
                return R.drawable.red_round;
            case 2:
                return R.drawable.black_round;
            case 3:
                return R.drawable.white_round;
            case 4:
                return R.drawable.blue_round;
            case 5:
                return R.drawable.yellow_round;
            case 6:
                return R.drawable.red_kone_round;
            case 7:
                return R.drawable.gray_round;
            case 8:
                return R.drawable.gray_dark_round;
        }
        return R.drawable.black_round;
    }

    public int getColorFromMode(int mode) {
        switch (mode) {
            case 0:
                return R.color.gree_color;
            case 1:
                return R.color.red_color;
            case 2:
                return R.color.black_color;
            case 3:
                return R.color.white_color;
            case 4:
                return R.color.blue_color;
            case 5:
                return R.color.yellow_color;
            case 6:
                return R.color.red_kone_color;
            case 7:
                return R.color.gray_color;
            case 8:
                return R.color.gray_dark_color;
        }
        return R.color.black_color;
    }

    public String getColorStrFormMode(Context con,int mode) {
        switch (mode) {
            case 0:
                return con.getString(R.string.color_name_green);
            case 1:
                return con.getString(R.string.color_name_orange);
            case 2:
                return con.getString(R.string.color_name_black);
            case 3:
                return con.getString(R.string.color_name_white);
            case 4:
                return con.getString(R.string.color_name_blue);
            case 5:
                return con.getString(R.string.color_name_yellow);
            case 6:
                return con.getString(R.string.color_name_red);
            case 7:
                return con.getString(R.string.color_name_gray);
            case 8:
                return con.getString(R.string.color_name_dark_gray);
        }
        return con.getString(R.string.color_name_black);
    }

    public int getBgImgSetFromMode(int mode) {
        switch (mode) {
            case 0:
                return R.drawable.image_dragon_btn;
            case 1:
                return R.drawable.image_branch_btn;
            case 2:
                return R.drawable.image_bamboo_btn;
            case 3:
                return R.drawable.image_lights_btn;
            case 4:
                return R.drawable.image_water_btn;
        }
        return R.drawable.image_dragon_btn;
    }

    public int getBgImgFromMode(Context context, int mode) {
        if (getThemeMode(context)==Constant.THEME_BLOCKS){
            switch (mode) {
                case 0:
                    return R.drawable.image_dragon_bg;
                case 1:
                    return R.drawable.image_branch_bg;
                case 2:
                    return R.drawable.image_bamboo_bg;
                case 3:
                    return R.drawable.image_lights_bg;
                case 4:
                    return R.drawable.image_water_bg;
            }
        }else{
            switch (mode) {
                case 0:
                    return R.drawable.image_dragon_bg2;
                case 1:
                    return R.drawable.image_branch_bg2;
                case 2:
                    return R.drawable.image_bamboo_bg2;
                case 3:
                    return R.drawable.image_lights_bg2;
                case 4:
                    return R.drawable.image_water_bg2;
            }
        }

        return R.drawable.image_dragon_bg;
    }

    public String getBgImgStrFromMode(Context con  ,int mode) {
        switch (mode) {
            case 0:
                return con.getString(R.string.image_name_dragon);
            case 1:
                return con.getString(R.string.image_name_branch);
            case 2:
                return con.getString(R.string.image_name_bamboo);
            case 3:
                return con.getString(R.string.image_name_light);
            case 4:
                return con.getString(R.string.image_name_water);
        }
        return con.getString(R.string.image_name_dragon);
    }

    public boolean getGuiEnable(Context context) {
        //return true;
        return PreferUtils.getBoolean(context, Constant.GUIDEENABLE, true);
    }

    public boolean getSetupEnable(Context context) {
        return PreferUtils.getBoolean(context, Constant.SETUPENABLE, true);
    }
    public void clearSetupEnable(Context context) {
        PreferUtils.setBoolean(context, Constant.SETUPENABLE, false);
    }
    public void clearGuiEnable(Context context) {
        PreferUtils.setBoolean(context, Constant.GUIDEENABLE, true);
    }

    public void cancelGuiEnable(Context context) {
        PreferUtils.setBoolean(context, Constant.GUIDEENABLE, false);
    }

    public String getTimeFormat(Context context) {
        return PreferUtils.getString(context, Constant.TIME_FORMAT, "12H");
    }

    public void setTimeFormat24(Context context) {
        PreferUtils.setString(context, Constant.TIME_FORMAT, "24H");
    }

    public void setTimeFormat12(Context context) {
        PreferUtils.setString(context, Constant.TIME_FORMAT, "12H");
    }

    public String getDateFormat(Context context) {
        return PreferUtils.getString(context, Constant.DATA_FORMAT, "0");
    }

    public void setDateFormat(Context context, String format) {
        PreferUtils.setString(context, Constant.DATA_FORMAT, format);
    }

    public String getDateFormatStr(String dataFormat) {
        int format = Integer.valueOf(dataFormat);
        String[] formatList = getDateFormatList();
        if (format >= formatList.length)
            format = 0;
        return formatList[format];
//        switch (dataFormat) {
//            case "0":
//                return "dd/MM/yyyy";
//            case "1":
//                return "MM/dd/yyyy";
//            case "2":
//                return "yyyy/MM/dd";
//            default:
//                throw new Resources.NotFoundException("未知格式");
//        }
    }

    /**
     * 通过一个统一的 数组保存时间格式
     *
     * @return 返回格式列表
     */
    public String[] getDateFormatList() {
        return new String[]{
                "MMMM d### yyyy"
                , "MM.dd.yyyy"
                , "MM/dd/yyyy"
                ,"yyyy-MM-dd"
                , "yyyy-M-d"
                , "yy-M-d"
                , "yy-MM-dd"
                , "EEEE,d### MMMM,yyyy"
                , "EEEE,MMMM d###,yyyy"
                , "EEEE M.d.yyyy"
                , "d### MMMM,yyy"
                , "M/d/yyyy"
                , "yyyy-dd-MM"
                , "yyyy/MM/dd"
                , "d###-MMM-yy"
        };
    }

    public String[] getDateList(Context context) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Locale locale = context.getResources().getConfiguration().locale;
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat)
                SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String[] formatList = getDateFormatList();
        String[] dateList = new String[formatList.length];
        Date date = new Date(System.currentTimeMillis());
        String format;
        for (int i = 0; i < formatList.length; i++) {
            format = formatList[i];
            simpleDateFormat.applyPattern(format);
//            simpleDateFormat.applyLocalizedPattern(format);
            dateList[i] = simpleDateFormat.format(date);
        }


        for (int i=0;i<dateList.length;i++){
            dateList[i] = convertDataStr(context,dateList[i]);
        }

        return dateList;
    }

    private String  convertDataStr(Context context,String str){

        if ("hk".equals(getLanguageMode(context))){

                for (String key :mDateStrMap.keySet()){
                    if (str.contains(key)){
                        str = str.replace(key,mDateStrMap.get(key));
                    }
                }
        }

        if("en".equals(getLanguageMode(context))){
            str = str.replace("###","");
        }else{
            str = str.replace("###","日");
        }

        return str;
    }

    public String getDate(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat)
                SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        simpleDateFormat.applyPattern(getDateFormatStr(
                getDateFormat(context)
        ));
        String str =  simpleDateFormat.format(new Date(System.currentTimeMillis()));

        return convertDataStr(context , str);
    }

    public String getPassword(Context context) {
        return PreferUtils.getString(context, Constant.PASSWORD_KEY, "123456");
    }

    public void setPassword(Context context, String pass) {
        PreferUtils.setString(context, Constant.PASSWORD_KEY, pass);
    }

    /**
     * 设置时间
     *
     * @param y 年
     * @param M 月
     * @param d 日
     * @param h 时
     * @param m 分
     */
    public void setDataTime(String y, String M, String d, String h, String m) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(y);
        stringBuilder.append("/");
        stringBuilder.append(M);
        stringBuilder.append("/");
        stringBuilder.append(d);
        stringBuilder.append("/");
        stringBuilder.append(h);
        stringBuilder.append("/");
        stringBuilder.append(m);
//        String str = "2015-08-31 21:08:06";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        Date date = null;
        try {
            date = (Date) sdf.parse(stringBuilder.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            SystemClock.setCurrentTimeMillis(date.getTime());
            Log.v("setting data and time success,:" + stringBuilder.toString());
        } else {
            Log.e("save date time error,parse error");
        }
    }

    public boolean OpenDoorDelay() {
        //读取本地配置文件，是否具有开关门延时功能
        return SysPrefer.getInstence().OpenDoorDelay();
    }


    public static void setLanguage(Locale locale) {
        try {
            Object objIActMag;

            Class clzIActMag = Class.forName("android.app.IActivityManager");

            Class clzActMagNative = Class
                    .forName("android.app.ActivityManagerNative");

            Method mtdActMagNative$getDefault = clzActMagNative
                    .getDeclaredMethod("getDefault");

            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);

            Method mtdIActMag$getConfiguration = clzIActMag
                    .getDeclaredMethod("getConfiguration");

            Configuration config = (Configuration) mtdIActMag$getConfiguration
                    .invoke(objIActMag);

            config.locale = locale;

            Class clzConfig = Class
                    .forName("android.content.res.Configuration");
            java.lang.reflect.Field userSetLocale = clzConfig
                    .getField("userSetLocale");
            userSetLocale.set(config, true);

            Class[] clzParams = { Configuration.class };

            Method mtdIActMag$updateConfiguration = clzIActMag
                    .getDeclaredMethod("updateConfiguration", clzParams);

            mtdIActMag$updateConfiguration.invoke(objIActMag, config);

            BackupManager.dataChanged("com.android.providers.settings");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
