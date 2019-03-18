package com.shbst.kone_home.Utils;

import android.text.format.Time;

/**
 * Created by tongshile on 2017-11-23.
 */
public class MyTime {
    static Time localTime = new Time();

    /**
     *
     * @return 年-月-日
     */
    public static String date() {
        localTime.setToNow();
        return localTime.format("%Y-%m-%d");
    }

    /**
     *
     * @return 时：分：秒
     */
    public static String time() {
        localTime.setToNow();
        return localTime.format("%H:%M:%S");
    }

    /**
     *
     * @return 年
     */
    public static String date_Y() {
        localTime.setToNow();
        return localTime.format("%Y");
    }

    /**
     *
     * @return 分
     */
    public static String date_M() {
        localTime.setToNow();
        return localTime.format("%M");
    }

    /**
     *
     * @return 日
     */
    public static String date_D() {
        localTime.setToNow();
        return localTime.format("%d");
    }

    /**
     *
     * @return 时
     */
    public static String date_H() {
        localTime.setToNow();
        return localTime.format("%H");
    }

    /**
     *
     * @return 月
     */
    public static String date_m() {
        localTime.setToNow();
        return localTime.format("%m");
    }

}
