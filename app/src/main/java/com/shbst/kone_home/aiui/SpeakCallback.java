package com.shbst.kone_home.aiui;

import com.iflytek.cloud.SpeechError;

/**
 * Created by suzhiming on 2017/12/20.
 */

public interface SpeakCallback {
    void onSpeakStart();
    void onSpeaking();
    void onSpeakover(SpeechError error);
}
