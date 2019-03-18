package com.shbst.kone_home;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.aiui.jni.AIUI;
import com.shbst.kone_home.Adapter.DefaultTransformer;
import com.shbst.kone_home.Adapter.ViewPageAdapter;
import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.Entity.FrameEntity.Frame0BaseInfo;
import com.shbst.kone_home.Entity.FrameEntity.Frame2BaseInfo;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.FixedSpeedScroller;
import com.shbst.kone_home.Utils.FrameFormatUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Utils.SysPrefer;
import com.shbst.kone_home.Views.BlueItemView;
import com.shbst.kone_home.Views.DrawLayoutView;
import com.shbst.kone_home.Views.FloorDisplayView;
import com.shbst.kone_home.Views.FloorFeaturesView;
import com.shbst.kone_home.Views.FloorItemView;
import com.shbst.kone_home.Views.PasswordCheckDialog;
import com.shbst.kone_home.Views.VerticalViewPager;
import com.shbst.kone_home.Views.WhitItemView;
import com.shbst.kone_home.aiui.AIUIManager;
import com.shbst.kone_home.aiui.DoorController;
import com.shbst.kone_home.aiui.FloorControl;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import protocol.sdk.shbst.com.singlechipsdk.services.ProtocolCallBack;

import static android.view.View.OVER_SCROLL_NEVER;

public class MainActivity extends BaseActivity
        implements View.OnClickListener, ProtocolCallBack
        , Handler.Callback {

    private HashMap<Integer, FloorItemView> viewMap;
    private HashMap<Integer, WhitItemView> whitViewMap;
    private LinearLayout leftViewLayout, stopService;
    private RelativeLayout backgroundLayout, blueLayout, show_setting;
    private View oldLoadBgView;

    private BlueItemView blueItemView;
    private FloorDisplayView displayView;
//    private WhitItemView whitItemView;

    private VerticalViewPager rightViewPager;
    private ViewPageAdapter pageAdapter;

    private int currentPhysicalFloor = 1; //当前所在物理楼层

    private ImageView menuBtn;

    private Handler mHanlder = new Handler(this);

    //    private boolean floorOldLoad = false;
//    private int alarmMode = -1;
    //常量定义
    private final int DEAL_FRAME_MESSAGE = 0;
    private final int TEST_SCROLL_MESSAGE = 1;
    private final int REFRESH_BLUE_VIEW_XY = 2;

    private boolean isTest = true;
    private TextView textView;


    DoorController  mDoorController ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication.getInstant().initAIUI();


        setContentView(R.layout.activity_main);
        Log.v("kone_home MainActivity onCreate");
        readViewCountAndCreate();
        readAndCreateRightView();
        selectRightDisplayView(currentPhysicalFloor);


//        bindAndStartService();

//        //test code  测试代码
//        mHanlder.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<Integer> registerFloor = new ArrayList<>();
//                registerFloor.add(2);
//                registerFloor.add(3);
//                //根据已注册楼层添加白色背景、更改文字颜色等
//                for (int floor : registerFloor) {
//                    FloorItemView itemView = viewMap.get(floor);
//                    if (itemView == null) {
//                        Log.w("viewMap.get(floor) is null,floor:" + floor);
//                        continue;
//                    }
//                    itemView.setWhiteTextColor();
//
//                    //读取 已注册楼层的 坐标，并将白色背景添加进去并设置 Y轴
//                    float aimsY = itemView.getY();
//                    float aimsX = itemView.getX();
//                    Log.v("item aims y:" + aimsY + " aimsX:" + aimsX);
//                    WhitItemView whitItemView = new WhitItemView(MainActivity.this, getItemViewHeight());
//                    backgroundLayout.addView(whitItemView);
//                    whitItemView.viewToAimsXY(aimsX, aimsY);
//                }
//                FloorItemView itemView = viewMap.get(2);
//                float aimsY = itemView.getY();
//                blueItemView.viewToAimsY(aimsY);
//
//                if (displayView != null)
//                    displayView.setCurrentFloor("2");
//                checkPageToOutOfService();
////                checkPageToAlarm(2);
//////                checkPageToOldLoad();
////
//                mHanlder.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        clearOldLoadStatus();
////                        clearAlarmMode();
//                        checkPageToAlarm(2);
//                    }
//                }, 2000);
//            }
//        }, 2000);

//        //测试代码
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isTest) {
//                    if (mHanlder == null)
//                        return;
//                    Message message = mHanlder.obtainMessage();
//                    message.what = TEST_SCROLL_MESSAGE;
//                    message.obj = 1;
//                    mHanlder.sendMessage(message);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//
//        // 方法2
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        String info = new String();
//        info += "宽度：" + dm.widthPixels + "\n";
//        info += "高度：" + dm.heightPixels + "\n";
//        info += "density：" + dm.density + "\n";
//        info += "绝对宽度：" + (dm.widthPixels * dm.density) + "\n";
//        info += "绝对高度：" + (dm.heightPixels * dm.density) + "\n";
//        info += "dpi：" + dm.densityDpi + "\n";
//        float width = dm.widthPixels * dm.density;
//        float height = dm.heightPixels * dm.density;
//        Log.v("info :" + info + " width:" + width + "  height:" + height);

        //startTempTest();


        //HermesEventBus.getDefault().register(this);





    }
//    public void onEventBusMessage(JSONObject  event){
//        Log.d("homelift","receive event from  aiui :"+event.toString());
//    }


    private BroadcastReceiver   mRegistReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int floor = intent.getIntExtra("floor",0);
            Log.d("aiui","regist floor :"+floor);

            if (floor != 0){
                Log.d("aiui","regist floor :"+floor);
                speakText("已登记去" + floor + "楼");
            }
        }
    };

    private void speakText(String str){
        Intent intent = new Intent("com.shbst.aiui.speak");
        intent.putExtra("text",str);
        sendBroadcast(intent);
    }

    private int value  = 1;



    private void startTempTest(){
        findViewById(R.id.tempView).setVisibility(View.VISIBLE);



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    value = value * 10;
                }
            }
        });
        thread.start();


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    value = value * 10;
                }
            }
        });
        thread1.start();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                File file = new File("sys/class/thermal/thermal_zone0/temp");
                try {
                    final BufferedReader br = new BufferedReader(new FileReader(file));
                    final String str = br.readLine();
                    br.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView)findViewById(R.id.tempView);
                            tv.setText("温度："+str+" , "+value);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        },1000,1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferManager.getInstence().getThemeMode(this) != Constant.THEME_BLOCKS) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        Log.v("MainActivity onResume," +
                "read preference file and Init data");
        refreshViewsPre();
        refreshRightView(currentPhysicalFloor);
        getCacheData();


        IntentFilter filter = new IntentFilter();
        filter.addAction("com.shbst.aiui.regist");
        registerReceiver(mRegistReceiver,filter);

        //mHanlder.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkPageToOldLoad();
//            }
//        },30*1000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //bindAndStartService();
                mApplication.getInstant().registerCallback(MainActivity.this);
            }
        },300);


        AIUIManager.getInstance().startService();

        AIUIManager.getInstance().setDebugStatusListener(new AIUIManager.DebugStatus() {
            @Override
            public void updateStatus(final  String str) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        TextView textView = findViewById(R.id.tempView);
                        textView.setText(str);
                    }
                });
            }
        });

        AIUIManager.getInstance().updateStatus("");

    }

    @Override
    protected void onPause() {
        super.onPause();
        mApplication.getInstant().unregisterCallback(this);

        unregisterReceiver(mRegistReceiver);
    }


    private void getCacheData() {
        if (mApplication.getInstant().protocolServiceIsNull())
            return;

        byte[] data0 = mApplication.getInstant().getmService().getCacheData(0);
        if (data0 != null) dealFrame(data0);

        byte[] data2 = mApplication.getInstant().getmService().getCacheData(2);
        if (data2 != null) dealFrame(data2);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void InitView() {
        //find view
        leftViewLayout = findViewById(R.id.left_layout);

        oldLoadBgView = findViewById(R.id.old_load_bg);
        backgroundLayout = findViewById(R.id.backgroud_layout);
        blueLayout = findViewById(R.id.blue_layout);
        rightViewPager = findViewById(R.id.right_view_pager);
        menuBtn = findViewById(R.id.l_menu);
        stopService = findViewById(R.id.stopService);
        ImageView closeDoorBtn = findViewById(R.id.close);
        ImageView openDoorBtn = findViewById(R.id.open);

        mDoorController = new DoorController(this,
                openDoorBtn,R.drawable.open_door_pressed,closeDoorBtn,R.drawable.close_door_pressed);

        mDoorController.create();

        //set onclick
        menuBtn.setOnClickListener(this);
        closeDoorBtn.setOnClickListener(this);
        openDoorBtn.setOnClickListener(this);

        openDoorBtn.setOnTouchListener(mOpenButtonListener);

        closeDoorBtn.setOnTouchListener(mCloseButtonListener);

        oldLoadBgView.setOnClickListener(this);
        menuBtn.setOnTouchListener(menuBtnTouchListener);

        openDoorBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        //view init
        rightViewPager.setPageTransformer(false, new DefaultTransformer());
        rightViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(rightViewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(rightViewPager, scroller);
            scroller.setmDuration(800);
        } catch (Exception e) {
            Log.e("反射修改 viewpage 滑动动画时长失败,error:" + Log.get(e));
        }
        ImageView delayImgView = findViewById(R.id.open_delay);
        if (!PreferManager.getInstence().OpenDoorDelay()) {
            View delayViewLine = findViewById(R.id.delay_opendoor_line);
            delayImgView.setVisibility(View.GONE);
            delayViewLine.setVisibility(View.GONE);
        } else {
            delayImgView.setOnClickListener(this);
        }
        show_setting = findViewById(R.id.show_setting);
        showPow();

    }


    private void showPow() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                show_setting.setVisibility(View.VISIBLE);//先设置显示
                AnimatorSet animatorSet = new AnimatorSet();//组合动画
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(show_setting, "scaleX", 0, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(show_setting, "scaleY", 0, 1f);

                animatorSet.setDuration(2000);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                animatorSet.start();

            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                AnimatorSet animatorSet = new AnimatorSet();//组合动画
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(show_setting, "scaleX", 1f, 0);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(show_setting, "scaleY", 1f, 0);

                animatorSet.setDuration(1000);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
                animatorSet.start();
//                show_setting.setVisibility(View.INVISIBLE);//先设置显示

            }
        }, 8000);
    }

    private void bindAndStartService() {
        mApplication.getInstant().registerCallback(this);
    }

    @SuppressLint("UseSparseArrays")
    private void readViewCountAndCreate() {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        int viewCount = SysPrefer.getInstence().getFloorCount();
        if (viewCount != viewMap.size()) {
            leftViewLayout.removeAllViews();
            backgroundLayout.removeAllViews();
            viewMap.clear();
//            int layoutHeight = leftViewLayout.getHeight();
            int viewHeight = getItemViewHeight(this);
            Log.v("veiw height:" + viewHeight);
            //create other view
            if(viewCount>3) {
                addOtherView();
            }

            //添加蓝色背景控件
            blueItemView = new BlueItemView(this, viewHeight);
            blueItemView.delayDissmissView(false);

//            whitItemView = new WhitItemView(this, viewHeight);
//            backgroundLayout.addView(whitItemView);
            blueLayout.addView(blueItemView);

            if (viewCount < 4) {
                displayView = new FloorDisplayView(this, getTopViewHeight(this));
                leftViewLayout.addView(displayView);
                leftViewLayout.addView(getLineView());
            }
            for (int i = viewCount; i >= 1; i--) {  //物理楼层保证从 1 开始
                FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(this, i);
                FloorItemView floorItemView = new FloorItemView(this, itemBean, viewHeight);
                floorItemView.setOnTouchListener(floorItemViewListener);
                viewMap.put(i, floorItemView);
                leftViewLayout.addView(floorItemView);
            }
            blueItemView.setViewMap(viewMap);
            Log.v("create view over,create view count:" + viewCount);
        }
    }
    public static int getTopViewHeight(Context context){

        int viewCount = SysPrefer.getInstence().getFloorCount();
        //当楼层数小于4时，需要增加电梯当前所在楼层，电梯上下行控件

        if(viewCount> 3) {
            return 0;
        }else{
            int layoutHeight = context.getResources().getInteger(R.integer.display_floor_layout_height);
            return layoutHeight *132/332;
        }
    }


    public static int getItemViewHeight(Context context){

        int viewCount = SysPrefer.getInstence().getFloorCount();
        //当楼层数小于4时，需要增加电梯当前所在楼层，电梯上下行控件
        int layoutHeight = context.getResources().getInteger(R.integer.display_floor_layout_height);

        return (layoutHeight -1 - getTopViewHeight(context)) / viewCount;
    }



    /**
     * 增加其他控件到该布局中
     */
    private void addOtherView() {
        leftViewLayout.addView(getLineView());
    }

    private View getLineView() {
        View lineView = new View(this);
        lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.lineColor));
        LinearLayout.LayoutParams lineViewLayout =
                new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , 1);
        lineView.setLayoutParams(lineViewLayout);
        return lineView;
    }

    /**
     * 刷新每一个控件的数据
     */
    private void refreshViewsPre() {
        Log.v("refresh view display form preference");
        for (Map.Entry<Integer, FloorItemView> entry : viewMap.entrySet()) {
            FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(this, entry.getKey());
            entry.getValue().refreshDisplayPre(itemBean);
        }
    }

    private void readAndCreateRightView() {
        Log.v("onresume , read and create right view");
        int physicalFloorCount = PreferManager.getInstence().getFloorCount();

        FloorItemBean currentItemBean = PreferManager.getInstence().getFloorItemBean(this, currentPhysicalFloor);
        FloorItemBean upItemBean = PreferManager.getInstence()
                .getFloorItemBean(this, (currentPhysicalFloor + 1) > physicalFloorCount ? 1 : (currentPhysicalFloor + 1));
        FloorItemBean dnItemBean = PreferManager.getInstence()
                .getFloorItemBean(this, (currentPhysicalFloor - 1) < 1 ? physicalFloorCount : (currentPhysicalFloor - 1));
        FloorFeaturesView currentFeaturesView = new FloorFeaturesView(this, currentItemBean);
        FloorFeaturesView upFeaturesView = new FloorFeaturesView(this, upItemBean);
        FloorFeaturesView dnFeaturesView = new FloorFeaturesView(this, dnItemBean);

        FloorFeaturesView upFeaturesView2 = new FloorFeaturesView(this, upItemBean);
        FloorFeaturesView dnFeaturesView2 = new FloorFeaturesView(this, dnItemBean);

        currentFeaturesView.setDrawingStateListener(mDrawingStatelistener);
        upFeaturesView.setDrawingStateListener(mDrawingStatelistener);
        dnFeaturesView.setDrawingStateListener(mDrawingStatelistener);
        upFeaturesView2.setDrawingStateListener(mDrawingStatelistener);
        dnFeaturesView2.setDrawingStateListener(mDrawingStatelistener);

        Log.v("up item:" + upItemBean.getPhysicalFloor());
        Log.v("dn item:" + dnItemBean.getPhysicalFloor());
        Log.v("current item:" + currentItemBean.getPhysicalFloor());
        Log.v("current item color mode:" + currentItemBean.getDisplayColorMode());
        LinkedList<FloorFeaturesView> linkedList = new LinkedList<>();
        linkedList.add(dnFeaturesView2);
        linkedList.add(upFeaturesView);
        linkedList.add(currentFeaturesView);
        linkedList.add(dnFeaturesView);
        linkedList.add(upFeaturesView2);
        pageAdapter = new ViewPageAdapter(linkedList, rightViewPager);
        rightViewPager.removeAllViews();
        rightViewPager.setAdapter(pageAdapter);
    }

    private DrawLayoutView.StateChangedListener mDrawingStatelistener = new DrawLayoutView.StateChangedListener() {
        @Override
        public void onStateChanged(boolean drawing) {
            //View control  = findViewById(R.id.control_panel);
            Log.d("draw","main activity status :"+drawing);
            View black = findViewById(R.id.black_overlay);
            if (drawing) {
                black.setVisibility(View.VISIBLE);
                black.setClickable(true);
            } else {
                black.setVisibility(View.INVISIBLE);
                black.setClickable(false);
            }
        }
    };

    private void refreshRightView(int floor) {
        pageAdapter.refreshRightView(this, floor);
    }

    /**
     * 设置当前选中楼层
     *
     * @param floor 当前右方显示楼层功能
     */
    private void selectRightDisplayView(int floor) {
        pageAdapter.selectRightDisplayView(this, currentPhysicalFloor, floor);
    }


    private void checkPageToOldLoad() {
        if (oldLoadBgView.getVisibility() != View.VISIBLE) {
            oldLoadBgView.setVisibility(View.VISIBLE);

        }
        Log.v("切换到 超载");
        pageAdapter.checkPageToOldLoad(this, currentPhysicalFloor);
    }

    private void clearOldLoadStatus() {
        if (oldLoadBgView.getVisibility() != View.GONE)
            oldLoadBgView.setVisibility(View.GONE);
        pageAdapter.clearOldLoadStatus(this, currentPhysicalFloor);
    }

    private void checkPageToOutOfService() {
        if (pageAdapter.checkPageToOutService(this, currentPhysicalFloor)
                && stopService.getVisibility() != View.VISIBLE) {
            TextView textView = (TextView) findViewById(R.id.out_of_service);
            if(textView != null)
                textView.setText(getString(R.string.out_of_service));
            stopService.setVisibility(View.VISIBLE);
        }
    }

    private void clearOutOfServiceStatus() {
        pageAdapter.clearOutServiceStatus(this, currentPhysicalFloor);
        if (stopService.getVisibility() != View.GONE)
            stopService.setVisibility(View.GONE);
    }

    private void checkPageToAlarm(int alarmMode) {
        clearOutOfServiceStatus();
        pageAdapter.checkPageToAlarm(this, currentPhysicalFloor, alarmMode);
    }

    private void clearAlarmMode() {
        pageAdapter.clearAlarmMode(this, currentPhysicalFloor);
    }

    private void sendRegisterFloorData(FloorItemBean floorItemBean) {
        int floor = floorItemBean.getPhysicalFloor();
        ArrayList<Integer> floorList = new ArrayList<>();
        floorList.add(floor);
        byte data[] = FrameFormatUtil.getInstance().getRegisterFloorData(floorList);
        sendData(data);
    }


    /**
     * 发送数据，判断是否发送成功
     *
     * @param data 数据
     */
    private void sendData(byte[] data) {
        if (data == null) {
            Log.e("send data is null");
            return;
        }
        if (!mApplication.getInstant().isProtocolServiceReady()) {
            Log.w("prodocol service not ready," +
                    "send data:" + Log.get(data));
            return;
        }
        if (!mApplication.getInstant().protocolServiceIsNull()) {
            mApplication.getInstant().getmService()
                    .sendMsgInUIThread(data);
            Log.v("send data size:" + data.length + " data:" + Log.get(data));
        } else {
            Log.e("mService is null" +
                    ", send data error，data：" + Log.get(data));
        }
    }


    boolean passDialogIsShow = false;

    @SuppressLint("RtlHardcoded")
    private void showInputPasswordDialog(View view) {
        if (passDialogIsShow) return;
        passDialogIsShow = true;
        PasswordCheckDialog myPopuWindow = new PasswordCheckDialog(this);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        myPopuWindow.setAnimationStyle(R.style.popwin_anim_style);
        myPopuWindow.showAtLocation(view, Gravity.RIGHT | Gravity.BOTTOM, location[1], location[0] - myPopuWindow.getHeight());
        myPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                passDialogIsShow = false;
            }
        });
    }

    /**
     * 跳转设置页面
     */
    @Override
    public void startSettingActivity() {
        Log.v("start setting activity.");
        if (mTestMode == true){
            Toast.makeText(this,"测试模式无需进入设置页面",Toast.LENGTH_LONG).show();
            return ;
        }
        //startActivity(new Intent(this, SettingActivity.class));
        Intent intent = new Intent("android.settings.WIFI_SETTINGS");
        startActivity(intent);

        overridePendingTransition(R.anim.fragment_slide_in_from_bottom, R.anim.fragment_slide_out_from_bottom);
    }

    /**
     * 处理 接收到的帧数据
     *
     * @param data 读取到的帧数据
     */


    private boolean mStatusOutOfService= false;
    private int     mStatusAlarm = -1;
    @SuppressLint("UseSparseArrays")
    private void dealFrame(byte[] data) {
//        if (isTest)
//            isTest = false;

        FloorItemView itemView = null;

        int tag = FrameFormatUtil.getInstance().getTAG(data);
//        Log.v("receive frame tag:" + tag);
        switch (tag) {
            case FrameFormatUtil.FRAME_TAG0:
                Frame0BaseInfo info0 = FrameFormatUtil.getInstance().getTAG0Entity(data);
                if (info0 == null)
                    break;
                //String currentFloorStr = info0.getCurrentFloor();
                String currentFloorStr = info0.getCurrentFloorIndex(this);

                int currentFloorInt = currentPhysicalFloor;
                if (!TextUtils.isEmpty(currentFloorStr)) {
                    try {
                        currentFloorInt = Integer.parseInt(currentFloorStr);
                        if (currentFloorInt < 1
                                || currentFloorInt > PreferManager.getInstence().getFloorCount()) {
                            Log.w("floor index out,floor:" + currentFloorInt);
                            currentFloorInt = currentPhysicalFloor;
                        }
                        itemView = viewMap.get(currentFloorInt);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if (itemView != null) {
                    float aimsY = itemView.getY();
                    Log.d("current item :"+currentFloorInt + "   aimsY:"+aimsY);

                    if (aimsY == 0) {
                        resendRefreshBlueMessage(itemView.getItemBean().getPhysicalFloor());
                    } else {
                        blueItemView.setToFloorIndex(itemView.getItemBean().getPhysicalFloor());
                        blueItemView.viewToAimsY(aimsY);
                    }
                    //刷新右方界面显示
                    selectRightDisplayView((itemView.getItemBean().getPhysicalFloor()));
                    //将当前所在物理楼层重置
                    currentPhysicalFloor = currentFloorInt;


                } else {
                    Log.e("viewMap.get(currentFloorInt) is null" +
                            ",currentFloor:" + currentFloorInt);
                }
                if (displayView != null) {
                    displayView.setCurrentFloor(currentFloorInt);
                    switch (info0.getArrowState()) {
                        case 0:
                            displayView.setRunImgDismiss();
                            break;
                        case 1:
                            displayView.setRunUpImg();
                            break;
                        case 2:
                            displayView.setRunDnImg();
                            break;
                        case 3:
                            displayView.setRunUpImg();
                            break;
                        case 4:
                            displayView.setRunDnImg();
                            break;
                        case 5:
                            displayView.setRunUpImg();
                            break;
                        case 6:
                            displayView.setRunDnImg();
                            break;
                    }

                }
                int[] status = info0.getSpecialState();
                boolean isOldLoad = false;
                boolean outOfService = false;
                for (int statu : status) {
                    if (statu == 1 || statu == 2)
                        isOldLoad = true;
                    if (statu == 9) {
                        outOfService = true;
                    }
                }

                mStatusOutOfService = outOfService;
                /*
                if (outOfService)
                    checkPageToOutOfService();
                else
                    clearOutOfServiceStatus();
                */

                if (isOldLoad)
                    checkPageToOldLoad();
                else {
                    clearOldLoadStatus();
                }

                break;
            case FrameFormatUtil.FRAME_TAG2:
                Frame2BaseInfo info1 = FrameFormatUtil.getInstance().getFrame2Entity(data);
                //首先清除所有背景控件
                if (whitViewMap == null)
                    whitViewMap = new HashMap<>();
                whitViewMap.clear();
                backgroundLayout.removeAllViews();
                //首先清除界面显示
                for (Map.Entry<Integer, FloorItemView> entry : viewMap.entrySet()) {
                    entry.getValue().clearTextColor();
                }
                //根据已注册楼层添加白色背景、更改文字颜色等
                LinkedList<Integer> list = info1.getRegisterFloor();
                for (int floor : list) {
                    itemView = viewMap.get(floor);
                    if (itemView == null) {
                        continue;
                    }

                    float aimsY = itemView.getY();
                    float aimsX = itemView.getX();
                    Log.d("getRegisterFloor whitview : "+aimsX + " , "+aimsY);
                    if (aimsY ==0){
                        continue;
                    }
                    if (blueItemView.getToFloorIndex() != floor)
                        itemView.setBlackTextColor();
                    else {
                        if (blueItemView.isRun()) {
                            itemView.setBlackTextColor();
                        }
                    }

                    //读取 已注册楼层的 坐标，并将白色背景添加进去并设置 Y轴
//                    if (blueItemView.getToFloorIndex() == floor) {
//                        continue;
//                    }
//                    if (blueItemView.getToFloorIndex() == floor) {
//                        itemView.setBlackTextColor();
//                    }



                    WhitItemView whitItemView = new WhitItemView(MainActivity.this, getItemViewHeight(this));
                    whitViewMap.put(itemView.getItemBean().getPhysicalFloor(), whitItemView);
                    backgroundLayout.addView(whitItemView);
                    whitItemView.viewToAimsXY(aimsX, aimsY);
                }
//                int bluviewFloor = blueItemView.getToFloorIndex();
//                if (whitViewMap.get(bluviewFloor) == null) {
//                    itemView = viewMap.get(bluviewFloor);
//                    if (itemView != null) {
//                        itemView.setBlackTextColor();
//                        float aimsY = itemView.getY();
//                        float aimsX = itemView.getX();
//                        WhitItemView whitItemView = new WhitItemView(MainActivity.this, getItemViewHeight());
//                        whitViewMap.put(itemView.getItemBean().getPhysicalFloor(), whitItemView);
//                        backgroundLayout.addView(whitItemView);
//                        whitItemView.viewToAimsXY(aimsX, aimsY);
//                    }
//                }

                blueItemView.setWhitViewMap(whitViewMap);
                boolean isAlarm = false;
                /*
                if (info1.isAlarmBell()) {
                    isAlarm = true;
                    checkPageToAlarm(0);
                }
                if (info1.isAlarmBellAnswer()) {
                    isAlarm = true;
                    checkPageToAlarm(1);
                }
                if (info1.isAlarmBellHangUp()) {
                    isAlarm = true;
                    checkPageToAlarm(2);
                }
                if (!isAlarm) {
                    clearAlarmMode();
                }*/

                if(info1.isAlarmBell()){
                    mStatusAlarm = 0;
                }else if(info1.isAlarmBellAnswer()){
                    mStatusAlarm = 1;
                }else if(info1.isAlarmBellHangUp()){
                    mStatusAlarm = 2;
                }else{
                    mStatusAlarm = -1;
                }




                break;
        }

        updateOutOfServiceAlarmStatus();
    }

    private void updateOutOfServiceAlarmStatus(){
        Log.v("update status: " +mStatusAlarm + " ," +mStatusOutOfService );

        if (mStatusAlarm != -1){
            checkPageToAlarm(mStatusAlarm);
        }else {
            clearAlarmMode();
            if(mStatusOutOfService){
                checkPageToOutOfService();
            }else{
                clearOutOfServiceStatus();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l_menu:
                Log.v("view onlick,view 1" );
//                showInputPasswordDialog(menuBtn);
                if (mStatusOutOfService == false && mStatusAlarm == -1) {
                    startSettingActivity();
                }
                break;
            case R.id.close:
                Log.v("view onlick,view 2");
                sendData(FrameFormatUtil.getInstance().getCloseDoorData());
                break;
            case R.id.open:
                Log.v("view onlick,view 3");
                sendData(FrameFormatUtil.getInstance().getOpenDoorData());

                break;
            case R.id.open_delay:
                Log.v("view onlick,view 4");

                sendData(FrameFormatUtil.getInstance().getDelayOpenDoorData());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mApplication.getInstant().unregisterCallback(this);
        super.onDestroy();
        mHanlder.removeCallbacksAndMessages(null);
        mHanlder = null;
        mDoorController.destroy();
        mDoorController = null;

    }

    @Override
    public void onReceiveData(byte[] data) {
        if (mHanlder == null) {
            Log.e("handler destroy");
            return;
        }
        Message message = mHanlder.obtainMessage();
        if (message == null) return;
        message.what = DEAL_FRAME_MESSAGE;
        message.obj = data;
        mHanlder.sendMessage(message);
    }

    @Override
    public void onUpdateProcess(int process) {
    }

    @Override
    public void onReady(boolean isNormalReady) {
    }

    private View.OnTouchListener floorItemViewListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (view instanceof FloorItemView) {
                    sendRegisterFloorData(((FloorItemView) view).getItemBean());
                }
            }
            return true;
        }
    };


    private int tmpFloor = 1;
    private boolean add = true;
    private boolean exit = false;

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case DEAL_FRAME_MESSAGE:
                dealFrame((byte[]) message.obj);
                break;
            case TEST_SCROLL_MESSAGE:
//                if (exit) return false;
                if (add)
                    tmpFloor++;
                else
                    tmpFloor--;
                if (tmpFloor > 5) {
                    add = false;
                    exit = true;
                    tmpFloor = 5;
                }
                if (tmpFloor < 1) {
                    add = true;
                    exit = true;
                    tmpFloor = 1;
                }
                selectRightDisplayView(tmpFloor);
                currentPhysicalFloor = tmpFloor;


                FloorItemView itemViewtmp = viewMap.get(tmpFloor);
                float aimsYtmp = itemViewtmp.getY();
                if (aimsYtmp != 0) {
                    blueItemView.setToFloorIndex(itemViewtmp.getItemBean().getPhysicalFloor());
                    blueItemView.viewToAimsY(aimsYtmp);
                }
                break;
            case REFRESH_BLUE_VIEW_XY:
                FloorItemView itemView = viewMap.get((int) message.obj);
                if (itemView == null) {
                    resendRefreshBlueMessage((int) message.obj);
                    break;
                }
                float aimsY = itemView.getY();
                if (aimsY == 0) {
                    resendRefreshBlueMessage((int) message.obj);
                } else {
                    blueItemView.setToFloorIndex(itemView.getItemBean().getPhysicalFloor());
                    blueItemView.viewToAimsY(aimsY);
                }
                break;
        }
        return false;
    }

    private void resendRefreshBlueMessage(int floor) {
        Message message = mHanlder.obtainMessage();
        message.what = REFRESH_BLUE_VIEW_XY;
        message.obj = floor;
        mHanlder.sendMessageDelayed(message, 300);
    }


    Runnable mOpenEvent = new Runnable() {
        @Override
        public void run() {

            Log.d("mopen event ");
            sendData(FrameFormatUtil.getInstance().getOpenDoorData());

            mHandler.postDelayed(this,1000);
        }
    };

    Handler mHandler = new Handler();

    View.OnTouchListener mOpenButtonListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                mHandler.post(mOpenEvent);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mHandler.removeCallbacks(mOpenEvent);
            }
            return false;
        }
    };


    Runnable mCloseEvent = new Runnable() {
        @Override
        public void run() {

            Log.d("mopen event ");
            sendData(FrameFormatUtil.getInstance().getCloseDoorData());

            mHandler.postDelayed(this,1000);
        }
    };


    View.OnTouchListener mCloseButtonListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                mHandler.post(mCloseEvent);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                mHandler.removeCallbacks(mCloseEvent);
            }
            return false;
        }
    };


    View.OnTouchListener menuBtnTouchListener = new View.OnTouchListener() {
        float startY = -1;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (motionEvent.getY() < startY) {
                        return view.callOnClick();
                    }
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            return true;
        else
            return super.onKeyDown(keyCode, event);
    }
}
