package com.shbst.kone_home.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by tongshile on 2017-11-13.
 */
public class FontEditView extends android.support.v7.widget.AppCompatEditText {
    public FontEditView(Context context) {
        super(context);
    }

    public FontEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontEditView(Context context, AttributeSet attrs, int defStyleAttr) {
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
