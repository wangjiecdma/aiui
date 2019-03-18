package com.shbst.kone_home.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.SizeUtil;

import java.util.Calendar;

/**
 * Created by tongshile on 2017-11-29.
 */
public class ClockView extends View {
    private float mRadius; //外圆半径
    private float mPadding; //边距
    private float mTextSize; //文字大小
    private float mHourPointWidth; //时针宽度
    private float mMinutePointWidth; //分针宽度
    private float mSecondPointWidth; //秒针宽度
    private int mPointRadius; // 指针圆角
    private float mPointEndLength; //指针末尾的长度

    private int mColorLong; //长线的颜色
    private int mColorShort; //短线的颜色
    private int mHourPointColor; //时针的颜色
    private int mMinutePointColor; //分针的颜色
    private int mSecondPointColor; //秒针的颜色

    private Paint mPaint; //画笔
    private Paint zPaint, kPaint;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttrs(attrs);
        init();
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.WatchBoard);
            mPadding = array.getDimension(R.styleable.WatchBoard_wb_padding, SizeUtil.Dp2Px(getContext(), 15));
            mTextSize = array.getDimension(R.styleable.WatchBoard_wb_text_size, SizeUtil.Dp2Px(getContext(), 16));
            mHourPointWidth = array.getDimension(R.styleable.WatchBoard_wb_hour_pointer_width, SizeUtil.Dp2Px(getContext(), 5));
            mMinutePointWidth = array.getDimension(R.styleable.WatchBoard_wb_minute_pointer_width, SizeUtil.Dp2Px(getContext(), 3));
            mSecondPointWidth = array.getDimension(R.styleable.WatchBoard_wb_second_pointer_width, SizeUtil.Dp2Px(getContext(), 2));
            mPointRadius = (int) array.getDimension(R.styleable.WatchBoard_wb_pointer_corner_radius, SizeUtil.Dp2Px(getContext(), 10));
            mPointEndLength = array.getDimension(R.styleable.WatchBoard_wb_pointer_end_length, SizeUtil.Dp2Px(getContext(), 10));

            mColorLong = array.getColor(R.styleable.WatchBoard_wb_scale_long_color, Color.argb(225, 0, 0, 0));
            mColorShort = array.getColor(R.styleable.WatchBoard_wb_scale_short_color, Color.argb(125, 0, 0, 0));
            mMinutePointColor = array.getColor(R.styleable.WatchBoard_wb_minute_pointer_color, Color.WHITE);
            mHourPointColor = array.getColor(R.styleable.WatchBoard_wb_minute_pointer_color, Color.WHITE);
            mSecondPointColor = array.getColor(R.styleable.WatchBoard_wb_second_pointer_color, Color.WHITE);
        } catch (Exception e) {
            //一旦出现错误全部使用默认值
            mPadding = SizeUtil.Dp2Px(getContext(), 15);
            mTextSize = SizeUtil.Dp2Px(getContext(), 16);
            mHourPointWidth = SizeUtil.Dp2Px(getContext(), 5);
            mMinutePointWidth = SizeUtil.Dp2Px(getContext(), 3);
            mSecondPointWidth = SizeUtil.Dp2Px(getContext(), 2);
            mPointRadius = (int) SizeUtil.Dp2Px(getContext(), 10);
            mPointEndLength = SizeUtil.Dp2Px(getContext(), 10);

            mColorLong = Color.argb(225, 0, 0, 0);
            mColorShort = Color.argb(125, 0, 0, 0);
            mMinutePointColor = Color.WHITE;
            mHourPointColor = Color.WHITE;
            mSecondPointColor = Color.WHITE;
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    //画笔初始化
    private void init() {
        mPaint = new Paint();
        zPaint = new Paint();
        zPaint.setAntiAlias(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w, h) - mPadding) / 2;
        mPointEndLength = 0; //尾部指针默认为半径的六分之一
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        paintPointer(canvas);
        paintCircle(canvas);
        paintScale(canvas);
        canvas.restore();
        //刷新
        postInvalidateDelayed(1000);
    }

    //绘制外圆背景
    public void paintCircle(Canvas canvas) {
        zPaint.setColor(Color.WHITE);
        zPaint.setStrokeWidth(15);
        zPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, mRadius, zPaint);
    }

    //绘制刻度
    private void paintScale(Canvas canvas) {
        mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1));
//        int lineWidth = 0;
//        for (int i = 0; i < 60; i++) {
//            if (i % 5 == 0) { //整点
//                mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1.5f));
//                mPaint.setColor(mColorLong);
//                lineWidth = 40;
//                mPaint.setTextSize(mTextSize);
//                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
//                Rect textBound = new Rect();
//                mPaint.getTextBounds(text, 0, text.length(), textBound);
//                mPaint.setColor(Color.BLACK);
//                canvas.save();
//                canvas.translate(0, -mRadius + SizeUtil.Dp2Px(getContext(), 5) + lineWidth + (textBound.bottom - textBound.top));
//                canvas.rotate(-6 * i);
//                mPaint.setStyle(Paint.Style.FILL);
//                canvas.drawText(text, -(textBound.right - textBound.left) / 2, textBound.bottom, mPaint);
//                canvas.restore();
//            } else { //非整点
//                lineWidth = 30;
//                mPaint.setColor(mColorShort);
//                mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1));
//            }
//            canvas.drawLine(0, -mRadius + SizeUtil.Dp2Px(getContext(), 10), 0, -mRadius + SizeUtil.Dp2Px(getContext(), 10) + lineWidth, mPaint);
//            canvas.rotate(6);
//        }
        canvas.restore();
    }

    private void paintPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        int angleHour = (hour % 12) * 360 / 12; //时针转过的角度
        angleHour += minute*360/12/60;

        int angleMinute = minute * 360 / 60; //分针转过的角度
        int angleSecond = second * 360 / 60; //秒针转过的角度
        //绘制时针
        canvas.save();
        canvas.rotate(angleHour); //旋转到时针的角度
        RectF rectFHour = new RectF(-mHourPointWidth / 2, -mRadius * 3 / 5, mHourPointWidth / 2, mPointEndLength);
        mPaint.setColor(mHourPointColor); //设置指针颜色
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth); //设置边界宽度
        canvas.drawRoundRect(rectFHour, mPointRadius, mPointRadius, mPaint); //绘制时针
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        RectF rectFMinute = new RectF(-mMinutePointWidth / 2, -mRadius * 3.5f / 5, mMinutePointWidth / 2, mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectFMinute, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        RectF rectFSecond = new RectF(-mSecondPointWidth / 2, -mRadius + 15, mSecondPointWidth / 2, mPointEndLength);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawRoundRect(rectFSecond, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制中心小圆
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(mSecondPointColor);
//        canvas.drawCircle(0, 0, mSecondPointWidth * 4, mPaint);
    }
}
