package com.shbst.kone_home.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.PreferManager;

/**
 * Created by zhouwenchao on 2018-02-28.
 */

public class WhitItemView extends View {
    private Context mContext;

    public WhitItemView(Context context, int viewHeight) {
        super(context);
        InitView(context, viewHeight);
    }

    public WhitItemView(Context context, @Nullable AttributeSet attrs, int viewHeight) {
        super(context, attrs);
        InitView(context, viewHeight);
    }

    public WhitItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int viewHeight) {
        super(context, attrs, defStyleAttr);
        InitView(context, viewHeight);
    }

    private void InitView(Context context, int wOrh) {
        this.mContext = context;
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            this.setBackground(ContextCompat.getDrawable(context, R.drawable.whit_round));
        } else {
            this.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                    , RelativeLayout.LayoutParams.MATCH_PARENT);
        }
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            layoutParams.width = wOrh;
        }
        layoutParams.height = wOrh;
        this.setLayoutParams(layoutParams);
    }

    public void viewToAimsXY(float x, float y) {
        this.setX(x);
        this.setY(y);
        invalidate();
    }
}
