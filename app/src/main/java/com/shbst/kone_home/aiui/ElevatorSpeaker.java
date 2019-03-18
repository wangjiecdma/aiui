package com.shbst.kone_home.aiui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class ElevatorSpeaker {

    private static final String TAG = "SZMElevatorSpeak";

    private Context mContext;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SynthesizerListener mSynthesizerListener;
    private SpeakCallback mSpeakCallback;
    private String speakStr;

    private PCMPlayer mPlayer;
    private final boolean PlayDingDong = true;


    public ElevatorSpeaker(Context mContext, SpeakCallback mSpeakCallback) {
        this.mSpeakCallback = mSpeakCallback;
        this.mContext = mContext;
        initSpeechSynthesizer();
        mPlayer = new PCMPlayer(mContext,mSpeakCallback);

    }



    private void initSpeechSynthesizer() {
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext.getApplicationContext(), null);
        configParam();
        mSynthesizerListener = new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {
                mSpeakCallback.onSpeakStart();
                Log.v(TAG, "SynthesizerListener onSpeakBegin");
            }

            @Override
            public void onSpeakPaused() {
                Log.v(TAG, "SynthesizerListener onSpeakPaused");
            }

            @Override
            public void onSpeakResumed() {
                Log.v(TAG, "SynthesizerListener onSpeakResumed");
            }

            @Override
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            }

            @Override
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                mSpeakCallback.onSpeaking();
            }

            @Override
            public void onCompleted(SpeechError error) {
                mSpeakCallback.onSpeakover(error);
                if (error == null) {
                    Log.v(TAG, "SynthesizerListener onCompleted");
                } else {
                    Log.v(TAG, error.getPlainDescription(true));
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            }
        };
    }

    private void configParam() {
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mSpeechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "yinger");
        //设置合成语速
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "45");
        //设置合成音调
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "53");
        //设置合成音量
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
        //设置播放器音频流类型
        mSpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//        mSpeechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    public synchronized void speak(String text) {

        Log.d("aiui","speak :"+text);
        if (PlayDingDong){
            mPlayer.play();
        }else {

            this.speakStr = text;
            if (mSpeechSynthesizer.isSpeaking()) {
                mSpeechSynthesizer.stopSpeaking();
            }
            mSpeechSynthesizer.startSpeaking(text, mSynthesizerListener);
        }
    }

    public boolean isSpeaking() {
        return mSpeechSynthesizer.isSpeaking();
    }
}
