package com.shbst.kone_home.aiui;

import android.content.Context;
import android.content.res.AssetManager;

import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by suzhiming on 2017/11/6.
 */

public class AIUIConfig {

    public static final String APPID = "59e9a548";

    public static final AIUIMessage CMD_WAKEUP = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
    public static final AIUIMessage CMD_START = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, "", null);
    public static final AIUIMessage CMD_STOP = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, "", null);
    public static final AIUIMessage CMD_START_RECORD = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, "sample_rate=16000,data_type=audio", null);
    public static final AIUIMessage CMD_STOP_RECORD = new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, "", null);


    public static String getAIUIParams(Context cxt) {
        String params = "";
        AssetManager assetManager = cxt.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }
}
