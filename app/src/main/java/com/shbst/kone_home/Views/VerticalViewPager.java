package com.shbst.kone_home.Views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhouwenchao on 2018-02-08.
 * 竖向滑动的 ViewPager
 */

public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(Context context) {
        super(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        event.setLocation((event.getY() / height) * width, (event.getX() / width) * height);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
//        return super.onInterceptTouchEvent(swapTouchEvent(MotionEvent.obtain(event)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
//        return super.onTouchEvent(swapTouchEvent(MotionEvent.obtain(ev)));
    }

}
