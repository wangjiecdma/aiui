package com.shbst.kone_home.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

import static com.shbst.kone_home.Utils.Constant.DISPLAY_MAX_LINES;

/**
 * Created by hasee on 2018/3/14.
 * 自定义控件，绘制字体放大，居左等。
 */

public class DisplayTextView extends FontTextView {
    private Context mContext;
    private Paint textPaint;
    private String[] textList;
    private float measuringY = 0;
    private boolean isMeasuring = false;

    private float enlarge = 1.65f;

    public DisplayTextView(Context context) {
        super(context);
        InitView(context);
    }

    public DisplayTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
    }

    public DisplayTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }

    private void InitView(Context context) {
        this.mContext = context;
        InitPaint();
    }


    private void InitPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        if (this.getGravity() == Gravity.LEFT)
            textPaint.setTextAlign(Paint.Align.LEFT);
        if (this.getGravity() == Gravity.CENTER)
            textPaint.setTextAlign(Paint.Align.CENTER);
        if (this.getGravity() == Gravity.RIGHT)
            textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setColor(this.getCurrentTextColor());
        textPaint.setTextSize(this.getTextSize());
//        CurrentPaint.setTypeface(Typeface.SERIF);
        textPaint.setTypeface(this.getTypeface());
        textPaint.setFakeBoldText(true);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        textPaint.setColor(color);
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        textPaint.setTextSize(size);
    }

    public void setDisplayText(String text) {
        super.setText(text);
        getTextList();
        isMeasuring = false;
    }

    private void getTextList() {
        textList = this.getText().toString().split("\\\n");

        if (textList.length > DISPLAY_MAX_LINES) {
            Log.w("textlist 分割长度大于" + DISPLAY_MAX_LINES);
        }
    }

    long inTime;

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (textList == null || textList.length == 0) {
            Log.w("textList is null ,return");
            return;
        }
//        measuringY();
//        drawText(canvas);
        if (!isMeasuring) {
            inTime = SystemClock.elapsedRealtime();

            measuringY();
            invalidate();

//            new Thread(measuringYRunnable).start();
        } else {
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas) {
        long time = SystemClock.elapsedRealtime();

        float currentY = -1;

        int themeMode = PreferManager.getInstence().getThemeMode(this.getContext());
        int viewHeight = getHeight();
        int viewWidth = getWidth();

        for (int i = 0; i < textList.length && i < DISPLAY_MAX_LINES; i++) {
            String text = textList[i];
            if (TextUtils.isEmpty(text)) {
                continue;
            }
            if (i == 0) {
                textPaint.setTextSize(getTextSize() * enlarge);
            } else {
                if (textPaint.getTextSize()
                        != getTextSize())
                    textPaint.setTextSize(getTextSize());
            }
            //文字的x轴坐标,如果文字居左
            float x = 0;
            float stringWidth = textPaint.measureText(text);
            if (getGravity() == Gravity.LEFT) {
                x = 0;
            }
            if (getGravity() == Gravity.CENTER || getGravity() == Gravity.CENTER_HORIZONTAL) {

                x = (viewWidth - stringWidth) / 2;  //宽度居中
            }
            if (getGravity() == Gravity.RIGHT) {
                x = viewWidth - stringWidth;//宽度居右边
            }

            x += getLeft();

//            float stringWidth = textPaint.measureText(text);
//            x = (viewWidth - stringWidth) / 2;  //宽度居中

            float y = 0;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            if (getGravity() == Gravity.TOP) {
                y = 0;
            } else if (getGravity() == Gravity.BOTTOM) {
                y = viewHeight - (fontMetrics.ascent + (fontMetrics.descent / 2));
            } else {
                //文字的y轴坐标
                if (themeMode == Constant.THEME_BLOCKS) {  //如果主题时 block ，则文字位于底部
                    y = viewHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
                    y = y + (measuringY);
                } else {  // 其他主题的话，默认文字居中显示
                    y = viewHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
                    y = y - (measuringY / 2);
                }
            }

            if (i == 2) {
                y -= 5;
            }

//            Log.v("currentY :" + currentY + " y:" + y + "  getHeight:" + getHeight());
            if (currentY == -1) {
                currentY = y;
            } else {
                currentY = currentY + (Math.abs(fontMetrics.ascent)
                        + (fontMetrics.descent / 2)) + getTop();
            }

            y = currentY;
//            Log.v("绘制 x:" + x + " y:" + y + " text size:"
//                    + textPaint.getTextSize() + "  measuringY：" + measuringY);
            canvas.drawText(text, x, y, textPaint);  //y 轴 居中
        }
    }


    private void measuringY() {
        long time = SystemClock.elapsedRealtime();

        float currentY = -1;
        for (int i = 0; i < textList.length && i < DISPLAY_MAX_LINES; i++) {
            String text = textList[i];
            if (TextUtils.isEmpty(text)) {
                continue;
            }
            Log.v("getWidth:" + getWidth() + " getLeft:" + getLeft());

            if (i == 0) {
                textPaint.setTextSize(getTextSize() * enlarge);
            } else {
                if (textPaint.getTextSize() != getTextSize())
                    textPaint.setTextSize(getTextSize());
            }
//            //测量文字宽度
//            float x = 0;
//            float stringWidth = textPaint.measureText(text);
//
//            if (stringWidth > getWidth() - getLeft()) {
//                Log.v("时间1：" + (SystemClock.elapsedRealtime() - time));
//
//                String tmpText = text;
//                float tmpWidth = textPaint.measureText(tmpText);
//                int splitIndex = tmpText.length();
//
//                //增加这些判断是为了减少 while 循环次数，提高速度
//                boolean isExit = false;
//                boolean isDn = true;
//                int append = 10;
//
////                while ((tmpWidth > getWidth() - getLeft())) {
////
////                    tmpText = text.substring(0, --splitIndex);
////
////                    tmpWidth = textPaint.measureText(tmpText);
////                    splitIndex = tmpText.length();
////
////                }
//
//                while ((tmpWidth > getWidth() - getLeft()) && !isExit) {
//                    if (isDn)
//                        splitIndex = splitIndex - append;
//                    else
//                        splitIndex = splitIndex + append;
//
//                    tmpText = text.substring(0, splitIndex);
//
//                    tmpWidth = textPaint.measureText(tmpText);
//                    splitIndex = tmpText.length();
//
//                    if (isDn && (tmpWidth < getWidth() - getLeft())) {
//                        isDn = false;
//                        append--;
//                    }
//                    if (!isDn && (tmpWidth > getWidth() - getLeft())) {
//                        isDn = true;
//                        append--;
//                    }
//
//                    if (append == 1) {
//                        isExit = true;
//                    }
//                }
//                Log.v("时间2：" + (SystemClock.elapsedRealtime() - time));
//                String tmpNewList[] = new String[textList.length + 1];
//                for (int i1 = 0; i1 < i; i1++) {
//                    tmpNewList[i1] = textList[i1];
//                }
//                tmpNewList[i] = text.substring(0, splitIndex);
//                tmpNewList[i + 1] = text.substring(splitIndex, text.length());
//                for (int i2 = i + 1; i2 < textList.length && i < DISPLAY_MAX_LINES; i2++) {
//                    tmpNewList[i2 + 1] = textList[i2];
//                }
//                textList = tmpNewList;
//                measuringY();
//                return;
//            }

            float y;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            if (getGravity() == Gravity.TOP) {
                y = 0;
            } else if (getGravity() == Gravity.BOTTOM) {
                y = getHeight() - (fontMetrics.ascent + fontMetrics.descent + 100);
            } else {
                //文字的y轴坐标
                y = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
            }
            if (currentY == -1) {
                currentY = y;
            } else {
                currentY = currentY + (Math.abs(fontMetrics.ascent) + fontMetrics.descent) + getTop();
            }
        }
        if (currentY != -1)
            measuringY = (this.getHeight() - currentY) - 10;

        isMeasuring = true;
    }

    private Runnable measuringYRunnable = new Runnable() {
        @Override
        public void run() {
            measuringY();
            post(drawTextRunnable);
        }
    };
    private Runnable drawTextRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}
