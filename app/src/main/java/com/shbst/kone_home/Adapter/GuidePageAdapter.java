package com.shbst.kone_home.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.shbst.kone_home.Utils.Log;

import java.util.List;

/**
 * Created by zhouwenchao on 2018-02-27.
 */

public class GuidePageAdapter extends PagerAdapter
        implements ViewPager.OnPageChangeListener {

    private List<View> views;

    public GuidePageAdapter(List<View> viewList) {
        this.views = viewList;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.v("destory item,,position:" + position);
        container.removeView((View) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
