package com.shbst.kone_home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shbst.kone_home.Adapter.GuidePageAdapter;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Utils.SizeUtil;
import com.shbst.kone_home.Views.FontTextView;
import com.shbst.kone_home.Views.GuidePage.GuidePageEnter;
import com.shbst.kone_home.Views.GuidePage.GuidePageFeatures;
import com.shbst.kone_home.Views.GuidePage.GuidePageFloor;
import com.shbst.kone_home.Views.GuidePage.GuidePageTheme;
import com.shbst.kone_home.Views.GuidePage.GuidePageTime;
import com.shbst.kone_home.Views.GuidePage.MediaPlayerView;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity
        implements View.OnClickListener, Handler.Callback {
    private List<View> viewList;
    private ViewPager viewPager;
    private GuidePageAdapter pageAdapter;
    private int mPointWidth;// 圆点间的距离
    private LinearLayout llPointGroup;// 引导圆点的父控件
    private RelativeLayout point_rllayout;

    private FontTextView viewRedPoint;// 小红点
    private int mGuideIndex = 0;
    private List<FontTextView> pointList = new ArrayList<>();
    private ImageView leftBtn, rightBtn;

    private GuidePageListener guidePageListener = new GuidePageListener();

    private Handler mHanlder = new Handler(this);
    private final static int START_MAINACTIVITY = 0;
    private MediaPlayerView video_ftu;    //FTU 引导界面视频
    int play_FTU_Index= 0;
    GuidePageEnter pageEnter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        video_ftu = findViewById(R.id.video_ftu);
        video_ftu.setVisibility(View.INVISIBLE);
        // FIXME: 2018/4/8 hegang FTU 增加视频文件,目前视频文件错误，待客户给出最近视频文件时，将注释打开；
//        try {
//            video_ftu.setVideoPath(FTU_VIDEO_PATH);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        video_ftu.start();
//        video_ftu.setOnClickListener(this);
        //楼层对应控件


        //int flag =checkPermission("android.permission.RECORD_AUDIO", Process.myPid(),Process.myUid());

        //android.util.Log.d("logtest","checkPermissio :"+Process.myPid() + " ,"+Process.myUid() +" :"+flag + " value "+ PackageManager.PERMISSION_GRANTED);


        Log.d("www","app version code :"+BuildConfig.VERSION_CODE);

        InitAdapter();


        findViewById(R.id.test_mode).setOnTouchListener(new View.OnTouchListener() {
            private long startTime=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    startTime = System.currentTimeMillis();
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    long diff = (System.currentTimeMillis() - startTime)/1000;
                    Log.d("test","test click:"+diff);

                    if(diff > 3 && (diff < 10)){
                        if(mTestMode == false) {
                            mTestMode = true;
                            initResetButton();
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void InitView() {
        Log.v(" gui init ");
        //find view from layout
        viewPager = findViewById(R.id.guideViewPager);
        llPointGroup = findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
        leftBtn = findViewById(R.id.gui_left_btn);
        rightBtn = findViewById(R.id.gui_right_btn);
        point_rllayout = findViewById(R.id.point_rl);

        if (mGuideIndex == 0) {
            viewRedPoint.setTextColor(getResources().getColor(R.color.white));
            viewRedPoint.setText("1");
        }

        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

        //init data
        if (leftBtn.getVisibility() != View.INVISIBLE)
            leftBtn.setVisibility(View.INVISIBLE);

    }

    private void initResetButton(){


        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format= PixelFormat.RGBA_8888;//设置背景图片
        wmParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ;//
        wmParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.TOP;//
        wmParams.x = 0;   //设置位置像素
        wmParams.y = 50;
        wmParams.width=WindowManager.LayoutParams.WRAP_CONTENT; //设置图片大小
        wmParams.height=WindowManager.LayoutParams.WRAP_CONTENT;

        View view =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_mode,null);

        wm.addView(view, wmParams);

        view.findViewById(R.id.reset_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerManager pm = (PowerManager) v.getContext().getSystemService(Context.POWER_SERVICE);
                pm.reboot("");
            }
        });



        viewList = new ArrayList<>();
        viewList.add(new GuidePageTime(this));
        pageAdapter = new GuidePageAdapter(viewList);
        viewPager.setAdapter(pageAdapter);

    }

    private void InitAdapter() {
        //add view to list
        viewList = new ArrayList<>();
        viewList.add(new GuidePageTime(this));
        viewList.add(new GuidePageFloor(this));
//        viewList.add(new GuidePageThree(this));
        viewList.add(new GuidePageFeatures(this));
        viewList.add(new GuidePageTheme(this));

//        GuidePagePass pagePass = new GuidePagePass(this);
//        pagePass.setEnterListener(new PageLisenter() {
//            @Override
//            public void passwordEnter() {
//                nextPage();
//            }
//        });
//        viewList.add(pagePass);
        pageEnter = new GuidePageEnter(this);

        viewList.add(pageEnter);

        pageAdapter = new GuidePageAdapter(viewList);

        // 初始化引导页的小圆点
        for (int i = 0; i < viewList.size(); i++) {
            FontTextView point = new FontTextView(this);
            pointList.add(point);
            point.setText(String.valueOf(i + 1));
            point.setTextColor(getResources().getColor(R.color.white));
            point.setGravity(Gravity.CENTER);
            point.setBackgroundResource(R.drawable.round_poit_no_select);// 设置引导页默认圆点

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    SizeUtil.Dp2Px(this, 28), SizeUtil.Dp2Px(this, 28));
            if (i > 0) {
                params.leftMargin = SizeUtil.Dp2Px(this, 48);// 设置圆点间隔
            }

            point.setLayoutParams(params);// 设置圆点的大小

            llPointGroup.addView(point);// 将圆点添加给线性布局
        }
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        llPointGroup.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointWidth = llPointGroup.getChildAt(1).getLeft()
                                - llPointGroup.getChildAt(0).getLeft();
                    }
                });
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(guidePageListener);
    }

    @Override
    public void finish() {
        //TODO 注释此处，每次都进入引导页面 。取消注释，只进入一次。
        if(mTestMode == false) {
            PreferManager.getInstence().cancelGuiEnable(this);
        }
        if (PreferManager.getInstence().getThemeMode(this) == Constant.THEME_BLOCKS) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (PreferManager.getInstence().getThemeMode(this) == Constant.THEME_SPHERE) {
            startActivity(new Intent(this, Main2Activity.class));
        }
        super.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gui_left_btn:
                int i = viewPager.getCurrentItem() < 0 ? 0 : viewPager.getCurrentItem();
                viewPager.setCurrentItem(i -1 , true);
                break;
            case R.id.gui_right_btn:
                nextPage();
//                if (mGuideIndex == pageAdapter.getCount() - 1) {
//                    finish();
//                } else {
//                    if (rightBtn.getVisibility() != View.VISIBLE)
//                        rightBtn.setVisibility(View.VISIBLE);
//                }
                break;
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case START_MAINACTIVITY:
                finish();
                break;
        }
        return false;
    }

    /**
     * viewpager的滑动监听
     */
    private class GuidePageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

//            if (position == pageAdapter.getCount() - 1) {
//                return;
//            }
//            if (position == pageAdapter.getCount() - 2) {
//                positionOffset = 0;
//            }
            Log.v("position:" + position + " positionOffset:" + positionOffset
                    + " positionOffsetPixels:" + positionOffsetPixels);


            int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();

            params.leftMargin = len;
            viewRedPoint.setLayoutParams(params);
        }

        boolean isFlush = false;

        // 某个页面被选中
        @Override
        public void onPageSelected(int position) {
            mGuideIndex = position;

            if (viewList.get(position) instanceof GuidePageFeatures) {

                isFlush = false;
            } else {
                if (!isFlush) {
                    for (View childView : viewList) {
                        if ((childView instanceof GuidePageFeatures)) {
                            ((GuidePageFeatures) childView).flushItemBean();
                            isFlush = true;
                        }
                    }
                }
            }
            Log.i( "onPageSelected: "+pageAdapter.getCount());
            if (position == pageAdapter.getCount()-1) {
                Log.i( "onPageSelected: setPage_setup_complete");
                pageEnter.setPage_setup_complete();
//                point_rllayout.setVisibility(View.INVISIBLE);
                rightBtn.setImageResource(R.drawable.right_enter);
//                pointList.get(pointList.size()-1).setBackgroundResource(R.drawable.gray_round);// 设置引导页默认圆点
//                pointList.get(pointList.size()-1).setTextColor(getResources().getColor(R.color.black));
//                return;
            } else {
//                point_rllayout.setVisibility(View.VISIBLE);
                rightBtn.setImageResource(R.drawable.gui_right_w);
            }
            viewRedPoint.setTextColor(getResources().getColor(R.color.white));
            viewRedPoint.setText(String.valueOf(position + 1));
            for (int i = 0; i < pointList.size(); i++) {
                Log.i( "onPageSelected: "+position+"  "+pointList.size());
                if (i < position || position == pointList.size()-1) {
                    pointList.get(i).setBackgroundResource(R.drawable.gray_round);// 设置引导页默认圆点
                    pointList.get(i).setTextColor(getResources().getColor(R.color.black));
                }
            }
        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {
            Log.v("onPageScrollStateChanged :" + state+"  mGuideIndex: "+mGuideIndex);
//            if (state != ViewPager.SCROLL_STATE_IDLE)
//                return;

            if (mGuideIndex == 0) {
                if (leftBtn.getVisibility() != View.INVISIBLE)
                    leftBtn.setVisibility(View.INVISIBLE);
            } else {
                if (leftBtn.getVisibility() != View.VISIBLE)
                    leftBtn.setVisibility(View.VISIBLE);
            }


//            if (mGuideIndex == pageAdapter.getCount() - 1) {
//                if (rightBtn.getVisibility() != View.INVISIBLE)
//                    rightBtn.setVisibility(View.INVISIBLE);
//            } else {
//                if (rightBtn.getVisibility() != View.VISIBLE)
//                    rightBtn.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void nextPage() {
        if (mTestMode == true){
            finish();
            return ;
        }
        if (mGuideIndex == pageAdapter.getCount()-1) {
            finish();
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1 > pageAdapter.getCount() ?
                pageAdapter.getCount() : viewPager.getCurrentItem() + 1, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHanlder.removeCallbacksAndMessages(null);
        mHanlder = null;
        viewPager.removeOnPageChangeListener(guidePageListener);
    }
}
