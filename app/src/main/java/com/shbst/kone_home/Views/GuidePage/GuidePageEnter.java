package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Views.GifView;


/**
 * Created by zhouwenchao on 2018-03-12.
 */

public class GuidePageEnter extends RelativeLayout {
    private ScreenSurfaceView page_setup_complete;

    public GuidePageEnter(Context context) {
        super(context);
        InitLayout(context);
    }

    public GuidePageEnter(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public GuidePageEnter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        View view = LayoutInflater.from(context).inflate(R.layout.guide_enter_page
                , null);

        this.addView(view, layoutParams);
        InitView(context);
    }


    private void InitView(Context context) {
        page_setup_complete = this.findViewById(R.id.page_setup_complete);
    }
    public void setPage_setup_complete(){
        if(page_setup_complete != null){
            page_setup_complete.setRunImageDrawableList();
        }
    }

}
