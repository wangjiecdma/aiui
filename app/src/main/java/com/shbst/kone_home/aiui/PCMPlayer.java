package com.shbst.kone_home.aiui;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;

import com.shbst.kone_home.Utils.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PCMPlayer {

    byte pcmdata []= new byte[18*1024];
    int dataLen = 0;

    ByteArrayInputStream inputStream ;
    AudioTrack audioTrack ;

    SpeakCallback mListener;

    AssetFileDescriptor mp3File;

    public PCMPlayer(Context context,SpeakCallback listener){
        try {
            InputStream stream = context.getAssets().open("dingdong.PCM");
            int len =stream.read(pcmdata);
            //inputStream = new ByteArrayInputStream(pcmdata,0,len);

            int bufferSize = AudioTrack.getMinBufferSize(8000,AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT);

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,8000,AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,bufferSize,AudioTrack.MODE_STREAM);
            dataLen = len;

            mListener = listener;

            mp3File = context.getAssets().openFd("ding.mp3");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        if (mp3File!= null){
            playMP3();
        }else{
            playPCM();
        }
    }


    private void playMP3(){


        MediaPlayer player = null;
        try {
            if (mListener != null){
                mListener.onSpeakStart();
            }
            player = new MediaPlayer();
            Log.d("aiui","file path :"+mp3File.getFileDescriptor());

            player.setDataSource(mp3File.getFileDescriptor(),mp3File.getStartOffset(),
                    mp3File.getLength());


            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mListener != null){
                        mListener.onSpeakover(null);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            if (mListener != null){
                mListener.onSpeakover(null);
            }
        }

    }


    private void playPCM(){
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {

                if (mListener != null){
                    mListener.onSpeakStart();
                }
                audioTrack.play();

                int len =0;
                while (len < dataLen){

                    int ret =  audioTrack.write(pcmdata,len,dataLen-len);

                    if (ret >0){
                        len += ret;
                    }
                }

                try{
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                audioTrack.stop();
                if (mListener != null){
                    mListener.onSpeakover(null);
                }
            }
        });
        thread.start();
    }

}
