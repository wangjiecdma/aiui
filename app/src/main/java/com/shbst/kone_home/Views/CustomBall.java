package com.shbst.kone_home.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.ArithUtil;

/**
 * Created by xiehui on 2016/8/10.
 */
public class CustomBall extends View {
    private int width;
    private int height;

    private int windowWidth;
    private int windowHeight;
    private Paint roundPaint;
    private Paint fontPaint;
    private Paint progressPaint;
    private String centerText = "";
    private int centerTextColor;
    private float centerTextSize;
    private int ballColor;
    private int progressColor;
    private int currentProgress = 1;
    private int maxProgress = 100;
    private float radius;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Path path = new Path();
    private SingleTapThread singleTapThread;
    //    private GestureDetector detector;
    private int space = 30;
    private int move = 0;

    public CustomBall(Context context) {
        this(context, null);
    }

    public CustomBall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        windowWidth = getResources().getDisplayMetrics().widthPixels;
        windowHeight = getResources().getDisplayMetrics().heightPixels;
        getCustomAttribute(context, attrs, defStyleAttr);
        initPaint();
    }

    public void startAnimation() {
        startProgressAnimation();
    }

    public void stopAnimation() {
        if (singleTapThread != null)
            getHandler().removeCallbacks(singleTapThread);
        singleTapThread = null;
    }

    public void clearAnimation() {
        stopAnimation();
        currentProgress = 0;
        invalidate();
    }

    public void drawProgressAnimation(int progress) {
        stopAnimation();
        currentProgress = progress;
        invalidate();
    }

    /**
     * 获取自定义属性值
     */

    private void getCustomAttribute(Context context, AttributeSet attrs, int defStyleAttr) {

        /**
         * 获取自定义属性
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customBallView);
        centerText = typedArray.getString(R.styleable.customBallView_centerText);
        Log.e("TAG", "centerText" + centerText);
        centerTextSize = typedArray.getDimension(R.styleable.customBallView_centerTextSize, 24f);
        centerTextColor = typedArray.getColor(R.styleable.customBallView_centerTextColor, 0xFFFFFF);
        ballColor = typedArray.getColor(R.styleable.customBallView_ballColor, 0x3A8C6C);
        progressColor = typedArray.getColor(R.styleable.customBallView_progressColor, 0x00ff00);
        radius = typedArray.getDimension(R.styleable.customBallView_ballRadius, 260f);
        radius = Math.min(Math.min(windowWidth / 2, windowHeight / 2), radius);
        typedArray.recycle();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        roundPaint = new Paint();
        roundPaint.setColor(ballColor);
        roundPaint.setAntiAlias(true);
        fontPaint = new Paint();

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(progressColor);
        //取两层绘制交集。显示上层
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        fontPaint.setTextSize(centerTextSize);
        fontPaint.setColor(centerTextColor);
        fontPaint.setAntiAlias(true);
        fontPaint.setFakeBoldText(true);

        bitmap = Bitmap.createBitmap((int) radius * 2, (int) radius * 2, Bitmap.Config.ARGB_8888);

        bitmapCanvas = new Canvas(bitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int w;
        int h;
        if (widthMode == MeasureSpec.EXACTLY) {
            w = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            w = (int) Math.min(widthSize, radius * 2);
        } else {

            w = windowWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            h = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            h = (int) Math.min(heightSize, radius * 2);
        } else {
            h = windowHeight;
        }
        setMeasuredDimension(w, h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //这个绘制方法是用来绘制 水柱型，填满的动画
//        drawRound(canvas);

        //这个绘制方法是用来绘制圆形，顺时针转满的动画
        drawRoundLine(canvas);
    }

    private void drawRoundLine(Canvas canvas) {
        //将圆心设置在屏幕中心
        width = getWidth();
        height = getHeight();
        //画一个背景圆
        bitmapCanvas.drawCircle(width / 2, height / 2, radius, roundPaint);

        //画一个扇形
        RectF oval2 = new RectF(0, 0, width, height);// 设置个新的长方形，扫描测量
        int sweepAngle = (int) ArithUtil.mul(ArithUtil.div(currentProgress, 100), 360);
        bitmapCanvas.drawArc(oval2, 270, sweepAngle, true, progressPaint);
        //画一个小一点的圆遮盖
        bitmapCanvas.drawCircle(width / 2, height / 2, radius - 5, roundPaint);

        //应用
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void drawRound(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        bitmapCanvas.drawCircle(width / 2, height / 2, radius, roundPaint);

        path.reset();
        int count = (int) (radius + 1) * 2 / space;
        float y = (1 - (float) currentProgress / maxProgress) * radius * 2 + height / 2 - radius;
        move += 20;
        if (move > width) {
            move = width;
        }
        path.moveTo(-width + y, y);
        float d = (1 - (float) currentProgress / maxProgress) * space;
        //一条直线
        path.rQuadTo(space, d, space, 0);
        //水波线条
//        for (int i = 0; i < count; i++) {
//            path.rQuadTo(space, -d, space * 2, 0);
//            path.rQuadTo(space, d, space * 2, 0);
//        }
        path.lineTo(width, y);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
        bitmapCanvas.drawPath(path, progressPaint);
//        String text = currentProgress + "%";
        float textWidth = fontPaint.measureText(centerText);
        Paint.FontMetrics fontMetrics = fontPaint.getFontMetrics();
        float x = width / 2 - textWidth / 2;
        float dy = -(fontMetrics.descent + fontMetrics.ascent) / 2;
        float y1 = height / 2 + dy;
//        bitmapCanvas.drawText(text, x, y1, fontPaint);
        canvas.drawBitmap(bitmap, 0, 0, null);
        setClickable(true);

    }


    private void startProgressAnimation() {
        if (singleTapThread == null) {
            singleTapThread = new SingleTapThread();
            getHandler().postDelayed(singleTapThread, 100);
        }
    }

    private class SingleTapThread implements Runnable {
        @Override
        public void run() {
            if (currentProgress < maxProgress) {
                invalidate();
                getHandler().postDelayed(singleTapThread, 100);
                currentProgress++;
            } else {
                getHandler().removeCallbacks(singleTapThread);
            }
        }
    }
}
