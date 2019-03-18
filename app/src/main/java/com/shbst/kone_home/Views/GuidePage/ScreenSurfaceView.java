package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Log;

import java.io.InputStream;

/**
 * Created by hegang on 2017-02-21.
 */
public class ScreenSurfaceView extends SurfaceView{
    private static final String TAG = "ScreenSurfaceView";
    private Context mContext;
    private AnimationDrawable anim;
    private int runTime = 50;
    private int completeGifList [] = {R.drawable.complete_00000, R.drawable.complete_00001, R.drawable.complete_00002, R.drawable.complete_00003,
            R.drawable.complete_00004, R.drawable.complete_00005, R.drawable.complete_00006, R.drawable.complete_00007,
            R.drawable.complete_00008, R.drawable.complete_00009, R.drawable.complete_00010, R.drawable.complete_00011,
            R.drawable.complete_00012, R.drawable.complete_00013, R.drawable.complete_00014};

    public ScreenSurfaceView(Context context) {
        super(context);
        mContext = context;
    }

    public ScreenSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ScreenSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
    /**
     * 启动轮播
     */
    public void setRunImageDrawableList() {
        Log.i(TAG, "setRunImageDrawableList: "+runTime);
        this.runTime = runTime;
        if (anim != null) {
            tryRecycleAnimationDrawable(anim);
            anim = new AnimationDrawable();
            System.gc();
            runFarmDrawable(completeGifList);
        } else {
            anim = new AnimationDrawable();
            runFarmDrawable(completeGifList);
        }
    }

    /**
     * 设置图片轮播效果
     *
     * @param pathList 图片路径
     */
    private void runFarmDrawable(int [] pathList) {
        if (pathList != null) {
            for (int i = 0; i < pathList.length; i++) {
                InputStream is = getResources().openRawResource(pathList[i]);
                BitmapDrawable bmpDraw = new BitmapDrawable(is);
                if(runTime == 0){
                    runTime = 100;
                }
                anim.addFrame(bmpDraw, runTime);
            }
            anim.setOneShot(true);
            setBackgroundDrawable(anim);
            anim.start();
        } else {
            anim.stop();
            anim = null;
        }
    }
    /**
     * AnimationDrawable动画图片资源回收
     *
     * @param animationDrawable 动画
     */
    private void tryRecycleAnimationDrawable(AnimationDrawable animationDrawable) {
        if (animationDrawable != null) {
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
        }
    }
}
