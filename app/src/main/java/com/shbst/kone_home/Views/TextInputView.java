package com.shbst.kone_home.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.Utils.Log;

import static com.shbst.kone_home.Utils.Constant.DISPLAY_MAX_LINES;

/**
 * Created by zhouwenchao on 2018-03-20.
 */

public class TextInputView extends FontEditView implements TextWatcher {
    private Context mContext;
    private Paint textPaint;
    //    private String[] textList;
    //    private float measuringY = 0;
//    private boolean isMeasuring = false;
    private float enlarge = 1.65f;

    private int DisplayTextWidth = 482;
    private int DisplayTextLeft = 30;

    private int paintTextSize = 40;

    private String currentText;
    private FloorItemBean mItemBean;

    public TextInputView(Context context, FloorItemBean floorItemBean) {
        super(context);
        InitView(context, floorItemBean);
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs, FloorItemBean floorItemBean) {
        super(context, attrs);
        InitView(context, floorItemBean);
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, FloorItemBean floorItemBean) {
        super(context, attrs, defStyleAttr);
        InitView(context, floorItemBean);
    }

    public TextInputView(Context context) {
        super(context);
        InitView(context, null);
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView(context, null);
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context, null);
    }

    private void InitView(Context context, FloorItemBean floorItemBean) {
        this.mContext = context;
        this.mItemBean = floorItemBean;
        InitPaint();
        this.addTextChangedListener(this);
    }

    public void setmItemBean(FloorItemBean floorItemBean) {
        this.mItemBean = floorItemBean;
    }

    public FloorItemBean getmItemBean() {
        return mItemBean;
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

        textPaint.setTextSize(paintTextSize);
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
        this.currentText = text;
        getTextList(text);
//        isMeasuring = false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setDisplayText(this.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mItemBean != null)
            mItemBean.setDisplayText(this.getText().toString());
    }

    private String[] measuringY(String[] textList) {
        long time = SystemClock.elapsedRealtime();
//        float currentY = -1;
        for (int i = 0; i < textList.length && i < DISPLAY_MAX_LINES; i++) {
            String text = textList[i];
            if (TextUtils.isEmpty(text)) {
                continue;
            }
            if (i == 0) {
                textPaint.setTextSize(paintTextSize * enlarge);
            } else {
                if (textPaint.getTextSize() != paintTextSize)
                    textPaint.setTextSize(paintTextSize);
            }
            //测量文字宽度
//            float x = 0;
            float stringWidth = textPaint.measureText(text);
            if (stringWidth > DisplayTextWidth - DisplayTextLeft) {

                String tmpText = text;
                float tmpWidth = textPaint.measureText(tmpText);
                int splitIndex = tmpText.length();

                //增加这些判断是为了减少 while 循环次数，提高速度
                boolean isExit = false;
                boolean isDn = true;
                int append = 10;

                while ((tmpWidth > DisplayTextWidth - DisplayTextLeft)) {

                    tmpText = text.substring(0, --splitIndex);

                    tmpWidth = textPaint.measureText(tmpText);
                    splitIndex = tmpText.length();
                }

//                while ((tmpWidth > DisplayTextWidth - DisplayTextLeft) && !isExit) {
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
//                    if (isDn && (tmpWidth < DisplayTextWidth - DisplayTextLeft)) {
//                        isDn = false;
//                        append--;
//                    }
//                    if (!isDn && (tmpWidth > DisplayTextWidth - DisplayTextLeft)) {
//                        isDn = true;
//                        append--;
//                    }
//
//                    if (append == 1) {
//                        isExit = true;
//                    }
//                }

                String tmpNewList[] = new String[textList.length + 1];

                System.arraycopy(textList, 0, tmpNewList, 0, i);

                tmpNewList[i] = text.substring(0, splitIndex);
//                tmpNewList[i +1] = text.substring(splitIndex, text.length());
//                for (int i2 = i + 1; i2 < textList.length && i < DISPLAY_MAX_LINES; i2++) {
//                    tmpNewList[i2 + 1] = textList[i2];
//                }
                for (int i2 = i + 1; i2 < textList.length && i < DISPLAY_MAX_LINES; i2++) {
                    if (i2 == (i + 1)) {
                        String line2 = text.substring(splitIndex, text.length());
                        tmpNewList[i2] = TextUtils.isEmpty(line2) ? textList[i2] :
                                line2 + textList[i2];
                        continue;
                    }
                    tmpNewList[i2] = textList[i2];
                }
                textList = tmpNewList;
                return measuringY(textList);
            }
        }
        return textList;
    }

    private void getTextList(String text) {
        new Thread(new drawTextRunnable(text)).start();


//        textList = text.split("\\\n");
//
//        if (textList.length > DISPLAY_MAX_LINES) {
//            Log.w("textlist 分割长度大于" + DISPLAY_MAX_LINES);
//        }
    }

    private class drawTextRunnable implements Runnable {
        //固定不能动的
        private final String mText;

        private drawTextRunnable(String text) {
            this.mText = text;
        }

        @Override
        public void run() {
//            String tmpInputStr = mText.replace("\n\n", "\n");
//            if (!tmpInputStr.equals(mText)) {
//                post(new setTextRunnable(tmpInputStr));
//                return;
//            }

            boolean enter = false;
            if (mText.length() > 0) {
                String enterStr = mText.substring(mText.length() - 1, mText.length());
                enter = enterStr.equals("\n");
            }

            String[] textList = mText.split("\\\n");

            String[] newTextList = measuringY(textList);

            StringBuilder stringBuilder = new StringBuilder();
            String tmpStr;
            for (int i1 = 0; i1 < newTextList.length
                    && i1 < DISPLAY_MAX_LINES; i1++) {
                tmpStr = newTextList[i1];
                stringBuilder.append(tmpStr == null ? "" : tmpStr);
                if (i1 != newTextList.length - 1 || enter)
                    stringBuilder.append("\n");
            }
            String newStr = stringBuilder.toString();

            //如果文字匹配上了，则设置新的文字到里面去
            if (mText.equals(currentText)
                    && !newStr.equals(mText)) {
                Log.v("new text:" + newStr);
                //post(new setTextRunnable(newStr));
            } else {
                Log.v("mText.equals(currentText):"
                        + mText.equals(currentText)
                        + " !newStr.equals(mText):" +
                        !newStr.equals(mText));
            }
        }
    }

    private class setTextRunnable implements Runnable {
        //固定不能动的
        private final String mText;

        private setTextRunnable(String text) {
            this.mText = text;
        }

        @Override
        public void run() {
            Log.v("TextInputView.this.setText(mText):"
                    + mText);
            TextInputView.this.setText(mText);
            TextInputView.this.setSelection(
                    TextInputView.this.getText().length());
        }
    }
}
