package com.shbst.kone_home.aiui;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.shbst.kone_home.Utils.Log;

import java.util.Locale;

public class ElevatorTTS {
    TextToSpeech  mSpeech;

    private  final String TAG = "homelift_aiui";
    private SpeakCallback mListener;

    private Handler mHandler = new Handler();
    public ElevatorTTS(Context context , SpeakCallback callback){

        mListener = callback;
        mSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d(TAG,"tts init :"+status);
                if (status== TextToSpeech.SUCCESS) {
                    mSpeech.setLanguage(Locale.CHINA);
                    mSpeech.setPitch(0.5f);
                    //设定语速 ，默认1.0正常语速
                    mSpeech.setSpeechRate(1.0f);

                    speak("这是一个测试");

                }
            }
        });


    }

    public synchronized void speak(String text) {
        mListener.onSpeakStart();
        mSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        Log.d(TAG,"speak :"+text);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onSpeakover(null);
            }
        },3000);
    }



}
