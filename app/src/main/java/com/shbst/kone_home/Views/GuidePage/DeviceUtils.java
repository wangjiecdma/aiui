package com.shbst.kone_home.Views.GuidePage;

import android.content.ContentResolver;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jescen on 2018/3/6.
 */

public class DeviceUtils {

    public static int getTimeFormat(Context context) {
        //获得内容提供者
        ContentResolver mResolver = context.getContentResolver();
        //获得系统时间制
        String timeFormat = android.provider.Settings.System.getString(mResolver, android.provider.Settings.System.TIME_12_24);
        //判断时间制
        if ("24".equals(timeFormat)) {
            return 0;
        } else {
            //12小时制
            //获得日历
            Calendar mCalendar = Calendar.getInstance();
            if (mCalendar.get(Calendar.AM_PM) == 0) {
                //白天
                return 1;
            } else {
                //晚上
                return 2;
            }
        }
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     * @param date   时间日期
     * @param template yyyy-MM-dd   。。。
     * @return
     */
    public static boolean isValidDate(String date, String template) {
        boolean convertSuccess = true;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            format.parse(date);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
