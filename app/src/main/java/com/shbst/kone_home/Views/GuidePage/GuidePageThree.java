package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Views.GifView;

/**
 * Created by zhouwenchao on 2018-02-27.
 */

public class GuidePageThree extends RelativeLayout {

    public GuidePageThree(Context context) {
        super(context);
        InitLayout(context);
    }

    public GuidePageThree(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public GuidePageThree(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        this.invalidate();
        View view = LayoutInflater.from(context).inflate(R.layout.guide_three_page
                , null);

        this.addView(view);
        //add view and init view
        InitView();
    }

    private void InitView() {
        GifView gifView = this.findViewById(R.id.pageThreeGif);
        gifView.setGifResource(R.drawable.lock_gif);
    }
}
