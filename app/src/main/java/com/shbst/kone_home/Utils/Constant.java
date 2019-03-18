package com.shbst.kone_home.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhouwenchao on 2018-02-26.
 */

public class Constant {

    //0：中文，1：英文，3 香港。
    public final static String LANGUAGE_MODE_KEY = "LANGUAGE_MODE_KEY";
    //进入设置的密码
    public final static String PASSWORD_KEY = "PASSWORD_KEY";
    //时间格式
    public final static String TIME_FORMAT_KEY = "TIME_FORMAT_KEY";
    //日期格式
    public final static String DATA_FORMAT_KEY = "DATA_FORMAT_KEY";
    //主题,根据不同主题加载不同的布局
    public final static String THEME_KEY = "THEME_KEY";

    /*由于物理楼层的可变性，所以物理取得物理楼层对应信息的key为 “FLOOR_”+?
    * 如1楼对应的key为 FLOOR_1*/
    public final static String FLOOR_LEFT_APPEND_KEY = "FLOOR_";

    public static final String[] locals = {"zh_CN", "en", "hk"};

    public static final String GUIDEENABLE = "GUIDE_ENABLE";
    public static final String SETUPENABLE = "SETUP_ENABLE";

    public static final String TIME_FORMAT = "time_format";

    public static final String DATA_FORMAT = "data_format";

    public static final int THEME_BLOCKS = 0;
    public static final int THEME_SPHERE = 1;

    public static final String copyFilePath = "copyFilePath"; //拷贝文件目录

    public static final int DISPLAY_MAX_LINES = 6;

    public static final String PREFER_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "prefer_file.json";

    /*本地配置文件 JSON 节点*/
    public static final String FLOOR_DATA_ITEM_KEY = "floor_data_item_key";

    //物理楼层
    public static final String PHYSICAL_FLOOR = "physical_floor";
    //显示的文字，如 P G 2 3 5
    public static final String DISPLAY_FLOOR = "display_floor";
    //楼层描述， 默认全是 Floor
    public static final String FLOOR_DESCRIPTION = "floor_description";
    //是否开门延迟
    public static final String OPEN_DOOR_DELAY = "open_door_delay";

    public static final String releaseDate = "releaseDate"; //保存U盘升级apk 日期

    public static final String FTU_VIDEO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "Movies/FTU.mp4";



}

