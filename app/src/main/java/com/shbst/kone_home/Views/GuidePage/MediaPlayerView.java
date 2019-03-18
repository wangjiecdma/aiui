package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hegang on 2018-02-05.
 */

public class MediaPlayerView extends SuperSurfaceView {
    private static final String TAG = "MediaPlayerView";
    SurfaceHolder surfaceHolder;
    private MediaPlayer mMediaPlayer = null;
    /**
     * 视频文件地址
     */
    private String mPath = "";
    /**
     * 本地視頻文件路徑
     */
    private  AssetFileDescriptor assetsFile = null ;//获取视频资源

    private Context mContext;

    private int playerIndex = 0;   //视频播放ID

    private List<String> mediaPathList = new ArrayList<>();

    private boolean playFlag = false;    //false 播放path 路徑文件視頻   true. 播放Assets 文件下的路徑
    public MediaPlayerView(Context context) {
        super(context);
        initMediaView(context);
    }

    @Override
    protected void doDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        canvas.drawRect(new RectF(0, 0, 1000, 550), mPaint);
    }

    public MediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMediaView(context);
    }

    public MediaPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMediaView(context);
    }

    private void initMediaView(Context context) {
        this.mContext = context;
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) throws IOException {
        Log.i(TAG, "MediaPlayer setVideoPath: 判断是否是新的视频文件: " + path);
        if (TextUtils.equals("", mPath)) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            Log.i(TAG, "MediaPlayer setVideoPath: 创建新的surfaceView");
            mPath = path;
            playFlag  = false;
            createMediaView();
        } else {
            //否则就直接load
            Log.i(TAG, "MediaPlayer setVideoPath: 直接播放视频文件");
            mPath = path;
            load();
        }
    }
    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDataSource(AssetFileDescriptor path){
        Log.i(TAG, "MediaPlayer setVideoPath: 判断是否是新的视频文件: " + path);
        if (assetsFile == null) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            Log.i(TAG, "MediaPlayer setVideoPath: 创建新的surfaceView");
            assetsFile = path;
            playFlag  = true;
            createMediaView();
        } else {
            //否则就直接load
            Log.i(TAG, "MediaPlayer setVideoPath: 直接播放视频文件");
            assetsFile = path;
            try {
                loadAssets();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载Assets视频
     */
    private void loadAssets() throws IOException {
        //每次都要重新创建MediaPlayer
        Log.i(TAG, "MediaPlayer load: 重新创建IMediaPlayer");
        createPlayer();
        Log.i(TAG, "MediaPlayer load: setDataSource："+assetsFile);
        //给mediaPlayer设置视图
        mMediaPlayer.setDisplay(this.getHolder());
        post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Log.i(TAG, "MediaPlayer load: prepareAsync");
                try {
                    mMediaPlayer.setDataSource(assetsFile.getFileDescriptor(), assetsFile.getStartOffset(), assetsFile.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.prepareAsync();

            }
        });

    }

    public void setMediaPathList(List<String> pathList) {
        this.mediaPathList = pathList;
        try {
            setVideoPath(pathList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 加载视频
     */
    private void load() throws IOException {
        //每次都要重新创建MediaPlayer
        Log.i(TAG, "MediaPlayer load: 重新创建IMediaPlayer");
        createPlayer();
        Log.i(TAG, "MediaPlayer load: setDataSource："+mPath);
        //给mediaPlayer设置视图
        mMediaPlayer.setDisplay(this.getHolder());
        post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "MediaPlayer load: prepareAsync");
                try {
                    mMediaPlayer.setDataSource(mPath);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 创建MediaScreen Player
     */
    private void createPlayer() {
        Log.i(TAG, "MediaPlayer createPlayer: 判断mMediaPlayer是否存在?");
        if (mMediaPlayer != null) {
            Log.i(TAG, "MediaPlayer createPlayer: MediaPlayer存在 停止并重置release");
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        Log.i(TAG, "MediaPlayer createPlayer: 判断listener ?");
        if (listener != null) {
            Log.i(TAG, "MediaPlayer createPlayer: listener 不为空");
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
            mMediaPlayer.setOnCompletionListener(listener);
        }
    }

    /**
     * MediaPlayer 监听
     */
    MediaScreenListener listener = new MediaScreenListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.i(TAG, "MediaPlayer onPrepared: 视频准备完成开始播放视频文件");
            mp.start();
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "MediaPlayer onError: 视频播放出错: " + what + " error code :" + extra);
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            setVisibility(INVISIBLE);
//            mp.start();
//            if (mediaPathList.size() == 1) {
//                mp.start();
//            } else {
//                playerIndex = setMediaPath(mediaPathList, playerIndex);
//                if (playerIndex >= mediaPathList.size() - 1) {
//                    playerIndex = 0;
//                } else {
//                    playerIndex++;
//                }
//            }
//            Log.i(TAG, "MediaPlayer onCompletion: mp.start 检测到视频播放完成继续轮播此视频");

        }
    };

    /**
     * 设置媒体播放路径
     * @param list  多媒体列表
     * @param index 多媒体item
     * @return 当前播放的项
     */
    private int setMediaPath(List<String> list, int index) {
        try {
            setVideoPath(list.get(index));
        } catch (IOException e) {
            if (index >= list.size() - 1) {
                index = 0;
            } else {
                index++;
            }
//            Toast.makeText(mContext,"视频播放错误，播放下一条视频",Toast.LENGTH_SHORT).show();
            setMediaPath(list,index);
            Log.i(TAG, "onCompletion: 视频播放错误，播放下一条视频");
        }
        return index;
    }

    private void createMediaView() {
        getHolder().addCallback(new MySurfaceCallback());
    }

    /**
     * surfaceView的监听器
     */
    private class MySurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //surfaceview创建成功后，加载视频
            Log.i(TAG, "MediaPlayer surfaceChanged: SurfaceView创建成功，加载视频");
            try {
                if(!playFlag){
                    load();
                }else{
                    loadAssets();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }
    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

}
