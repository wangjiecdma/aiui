package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by hegang on 2018-02-07.
 */

public abstract class SuperSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;

    public SuperSurfaceView(Context context) {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    public SuperSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    public SuperSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(new MyThread()).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            Canvas canvas = surfaceHolder.lockCanvas(null);//获取画布
            canvas.drawRect(new RectF(0, 0, 1000, 550), new Paint());
            surfaceHolder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像
        }
    }

    //将绘制图案的方法抽象出来，让子类实现，调用getBitmap方法时就会调用此方法
    protected abstract void doDraw(Canvas canvas);

    //调用该方法将doDraw绘制的图案绘制在自己的canvas上
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        doDraw(canvas);
        return bitmap;
    }
}
