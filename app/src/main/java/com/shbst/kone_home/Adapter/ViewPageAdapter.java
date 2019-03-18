package com.shbst.kone_home.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.FloorFeaturesView;

import java.util.LinkedList;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

public class ViewPageAdapter extends PagerAdapter
        implements ViewPager.OnPageChangeListener {

    private LinkedList<FloorFeaturesView> viewList;
    //    private LinkedList<FloorFeaturesView> originalList;
    private ViewPager viewPager;

    private int currentPage;

    private int alarmMode = -1;
    private boolean outOfService = false;
    private boolean floorOldLoad = false;


    public ViewPageAdapter(LinkedList<FloorFeaturesView> viewList, ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        InitViewList(0, viewList);
    }

    private void InitViewList(int mode, LinkedList<FloorFeaturesView> viewList) {
        this.viewList = viewList;
        this.notifyDataSetChanged();
        checkIndex();
    }

    @Override
    public int getCount() {
        if (viewList == null)
            return 0;
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
//        Log.v("current position:" + position);
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        Log.v("destory item,,position:" + position);
        container.removeView((View) object);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.v("onPageScrolled,position:" + position);
    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
//        Log.v("page select ,currentPage:" + currentPage);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.v("page scroll state changed,state:" + state);
//        若viewpager滑动未停止，直接返回
        if (state != ViewPager.SCROLL_STATE_IDLE) return;
//        若当前为第一张，设置页面为倒数第二张
        checkIndex();
    }

    private void checkIndex() {
        if (currentPage == 0) {
            viewPager.setCurrentItem(viewList.size() - 2, false);
        } else if (currentPage == viewList.size() - 1) {
//        若当前为倒数第一张，设置页面为第二张
            viewPager.setCurrentItem(1, false);
        }
    }


    public void refreshUpItemBean(FloorItemBean itemBean) {
        if (currentPage <= 1) {
            refreshTailItemBean(itemBean);
        } else {
            viewList.get(currentPage - 1).refreshFloorBean(itemBean);
        }
        InitViewList(1, viewList);
    }

    public void refreshTailItemBean(FloorItemBean itemBean) {
        viewList.get(0).refreshFloorBean(itemBean);
        viewList.get(viewList.size() - 2).refreshFloorBean(itemBean);
    }

    public void refreshCurrentItemBean(FloorItemBean itemBean) {
        viewList.get(currentPage).refreshFloorBean(itemBean);
        if (getPageIsTail()) {
            refreshTailItemBean(itemBean);
        }
        if (getPageIsHead()) {
            refreshHeadItemBean(itemBean);
        }
        InitViewList(2, viewList);
    }

    public boolean getPageIsTail() {
        return currentPage == 0 || currentPage == viewList.size() - 2;
    }

    public boolean getPageIsHead() {
        return currentPage == 1 || currentPage == viewList.size() - 1;
    }

    public void refreshDnItemBean(FloorItemBean itemBean) {
        if ((currentPage + 1) >= viewList.size() - 2) {
            refreshHeadItemBean(itemBean);
        } else {
            viewList.get((currentPage + 1))
                    .refreshFloorBean(itemBean);
        }
        InitViewList(3, viewList);
    }

    public void refreshHeadItemBean(FloorItemBean itemBean) {
        viewList.get(1)
                .refreshFloorBean(itemBean);
        viewList.get(viewList.size() - 1)
                .refreshFloorBean(itemBean);
    }

    public int getUpPageIndex() {
        if ((currentPage + 1) >= viewList.size() - 2) {
            return 1;
        } else {
            return currentPage + 1;
        }
    }

    public int getDnPageIndex() {
        if (currentPage <= 1) {
            return viewList.size() - 2;
        } else {
            return currentPage - 1;
        }
    }

    public void checkPageToTail() {
        Log.v("check page to tail");
        if (getPageIsTail()) {
            Log.v("page is tail:" + currentPage);
            return;
        }
        FloorItemBean currentBeamTmp = viewList.get(currentPage).getItemBean();
        FloorItemBean dnBeamTmp = viewList.get(getDnPageIndex()).getItemBean();
        FloorItemBean upBeamTmp = viewList.get(getUpPageIndex()).getItemBean();

        viewPager.setCurrentItem(viewList.size() - 2, false);
        refreshCurrentItemBean(currentBeamTmp);
        refreshDnItemBean(dnBeamTmp);
        refreshUpItemBean(upBeamTmp);
    }

    public void checkPageToHead() {
        Log.v("check page to head");
        if (getPageIsHead()) {
            Log.v("page is tail:" + currentPage);
            return;
        }
        FloorItemBean currentBeamTmp = viewList.get(currentPage).getItemBean();
        FloorItemBean dnBeamTmp = viewList.get(getDnPageIndex()).getItemBean();
        FloorItemBean upBeamTmp = viewList.get(getUpPageIndex()).getItemBean();

        viewPager.setCurrentItem(1, false);
        refreshCurrentItemBean(currentBeamTmp);
        refreshDnItemBean(dnBeamTmp);
        refreshUpItemBean(upBeamTmp);
    }


    /**
     * 设置当前选中楼层
     *
     * @param tofloor 当前右方显示楼层功能
     */
    public void selectRightDisplayView(Context context, int currentPhysicalFloor, int tofloor) {
        if (floorOldLoad || alarmMode != -1 || outOfService) {
            return;
        }
        Log.v("currentPhysicalFloor:" + currentPhysicalFloor + " tofloor:" + tofloor);
        if (currentPhysicalFloor == tofloor) {
            return;
//            viewPager.setCurrentItem(viewPager.getCurrentItem(), true);
        }
        if (currentPhysicalFloor < tofloor) {  //向下
            this.checkPageToTail();
            viewPager.setCurrentItem(this.getDnPageIndex(), true);
        }
        if (currentPhysicalFloor > tofloor) { //向上
            this.checkPageToHead();
            viewPager.setCurrentItem(this.getUpPageIndex(), true);
        }
        /*启动选择动画*/
        /*重置当前选中页面数据*/
        refreshRightView(context, tofloor, 0, alarmMode, outOfService, floorOldLoad);
    }

    public void refreshRightView(Context context, int floor) {
        if (floorOldLoad || alarmMode != -1 || outOfService) return;

        this.refreshRightView(context, floor, 0, alarmMode, outOfService, floorOldLoad);
    }

    public void refreshRightView(Context context, int floor, int mode, int alarmMode, boolean outOfService, boolean floorOldLoad) {
//        Log.v("refreshRightView:" + floor);
        int physicalFloorCount = PreferManager.getInstence().getFloorCount();
        FloorItemBean currentItemBean = PreferManager.getInstence().getFloorItemBean(context, floor);
//        FloorItemBean upItemBean = PreferManager.getInstence()
//                .getFloorItemBean(context, (floor + 1) > physicalFloorCount ? 1 : (floor + 1));
//        FloorItemBean dnItemBean = PreferManager.getInstence()
//                .getFloorItemBean(context, (floor - 1) < 1 ? physicalFloorCount : (floor - 1));

        FloorItemBean upItemBean = PreferManager.getInstence()
                .getFloorItemBean(context, (floor - 1) < 1 ? physicalFloorCount : (floor - 1));
        FloorItemBean dnItemBean = PreferManager.getInstence()
                .getFloorItemBean(context, (floor + 1) > physicalFloorCount ? 1 : (floor + 1));
        if (alarmMode != -1) {
            Log.v("alarmtest", "down item beam :" + dnItemBean.getPhysicalFloor());
            dnItemBean.setAlarmMode(alarmMode);
        }

        if (floor == 1) {
            Log.v(" up item floor:" + upItemBean.getPhysicalFloor());
            Log.v("dn item floor:" + dnItemBean.getPhysicalFloor());
        }
        dnItemBean.setOldLoad(floorOldLoad);
        if (mode == 0) {
            this.refreshCurrentItemBean(currentItemBean);
            this.refreshUpItemBean(upItemBean);
            this.refreshDnItemBean(dnItemBean);
        }
        if (mode == 1) {
            this.refreshCurrentItemBean(dnItemBean);
            this.refreshUpItemBean(upItemBean);
            this.refreshDnItemBean(currentItemBean);
        }
    }

    public void checkPageToOldLoad(Context context, int currentPhysicalFloor) {
        if (floorOldLoad) return;

        floorOldLoad = true;
        if (alarmMode != -1) {
            Log.w("current alarmMode :" + alarmMode);
            return;
        }
        if (outOfService) return;
        this.checkPageToHead();
        Log.v("向上滑动page,current:" + viewPager.getCurrentItem() + " next:"
                + (viewPager.getCurrentItem() + 1));
        viewPager.setCurrentItem(this.getUpPageIndex(), true);
        refreshRightView(context, currentPhysicalFloor, 1, alarmMode, outOfService, floorOldLoad);
    }

    public void clearOldLoadStatus(Context context, int currentPhysicalFloor) {
        if (!floorOldLoad) return;
        this.checkPageToTail();
        Log.v("向下滑动page" + viewPager.getCurrentItem() + " next:"
                + (viewPager.getCurrentItem() - 1));
        viewPager.setCurrentItem(this.getDnPageIndex(), true);

        refreshRightView(context, currentPhysicalFloor, 0, alarmMode, outOfService, floorOldLoad);
        floorOldLoad = false;
    }

    public boolean checkPageToOutService(Context context, int currentPhysicalFloor) {
//        if (outOfService) return true;

        outOfService = true;
        if (alarmMode != -1) {
            Log.w("current alarmMode :" + alarmMode);
            return false;
        }
        return true;
//        this.checkPageToHead();
//        Log.v("向上滑动page" + viewPager.getCurrentItem() + " next:"
//                + (viewPager.getCurrentItem() - 1));
//        viewPager.setCurrentItem(this.getUpPageIndex(), true);
//        refreshRightView(context, currentPhysicalFloor, 1, alarmMode, outOfService, floorOldLoad);
    }

    public void clearOutServiceStatus(Context context, int currentPhysicalFloor) {
        if (!outOfService) return;
        outOfService = false;
//        this.checkPageToTail();
//        Log.v("向下滑动page,current:" + viewPager.getCurrentItem() + " next:"
//                + (viewPager.getCurrentItem() + 1));
//        viewPager.setCurrentItem(this.getDnPageIndex(), true);
//        refreshRightView(context, currentPhysicalFloor, 0, alarmMode, outOfService, floorOldLoad);
    }

    public void checkPageToAlarm(Context context, int currentPhysicalFloor, int alarmMode) {
        if (this.alarmMode == alarmMode) return;
        if (this.alarmMode == -1) {   //只有在第一次进入 警铃模式时才使用滑动切换，
            this.checkPageToHead();
            Log.v("向上滑动page,current:" + viewPager.getCurrentItem() + " next:"
                    + (viewPager.getCurrentItem() + 1));
            viewPager.setCurrentItem(this.getUpPageIndex(), true);
        }
        this.alarmMode = alarmMode;
        refreshRightView(context, currentPhysicalFloor, 1, alarmMode, outOfService, floorOldLoad);
    }

    /**
     * clear 可以完美实现清除状态功能，但是本以为会有问题的
     * 运行起来没有问题，所以就没有深究其中的流程，
     */
    public void clearAlarmMode(Context context, int currentPhysicalFloor) {
        if (alarmMode == -1) return;
        this.checkPageToTail();
        Log.v("向下滑动page" + viewPager.getCurrentItem() + " next:"
                + (viewPager.getCurrentItem() - 1));
        Log.v("alarmtest", "pageAdapter.getUpPageIndex():" + this.getUpPageIndex());
        viewPager.setCurrentItem(this.getDnPageIndex(), true);
        refreshRightView(context, currentPhysicalFloor, 0, alarmMode, outOfService, floorOldLoad);
        this.alarmMode = -1;
    }
}
