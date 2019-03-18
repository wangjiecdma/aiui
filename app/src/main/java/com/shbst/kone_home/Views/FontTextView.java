package com.shbst.kone_home.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

public class FontTextView extends android.support.v7.widget.AppCompatTextView {

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Typeface createTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }


    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(createTypeface(getContext(), "fonts/kone.otf"), style);
    }
}
