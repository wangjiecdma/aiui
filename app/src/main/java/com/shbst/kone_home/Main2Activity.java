package com.shbst.kone_home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shbst.kone_home.Adapter.DefaultTransformer;
import com.shbst.kone_home.Adapter.ViewPageAdapter;
import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.Entity.FrameEntity.Frame0BaseInfo;
import com.shbst.kone_home.Entity.FrameEntity.Frame2BaseInfo;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.FixedSpeedScroller;
import com.shbst.kone_home.Utils.FrameFormatUtil;
import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Utils.SysPrefer;
import com.shbst.kone_home.Views.BlueItemView;
import com.shbst.kone_home.Views.DrawLayoutView;
import com.shbst.kone_home.Views.FloorDisplayView;
import com.shbst.kone_home.Views.FloorFeaturesView;
import com.shbst.kone_home.Views.FloorItemView;
import com.shbst.kone_home.Views.OpenDoorImgView;
import com.shbst.kone_home.Views.PasswordCheckDialog;
import com.shbst.kone_home.Views.VerticalViewPager;
import com.shbst.kone_home.Views.WhitItemView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import protocol.sdk.shbst.com.singlechipsdk.services.ProtocolCallBack;

import static android.view.View.OVER_SCROLL_NEVER;

public class Main2Activity extends BaseActivity
        implements View.OnClickListener, ProtocolCallBack, Handler.Callback
        , OpenDoorImgView.LongTouchCallBack {
    private Handler mHanlder = new Handler(this);

    private HashMap<Integer, FloorItemView> viewMap;
    private HashMap<Integer, WhitItemView> whitViewMap;
    private LinearLayout leftViewLayout, stopService;
    private RelativeLayout backgroundLayout, blueLayout;
    private ImageView menuBtn;


    private BlueItemView blueItemView;
    private FloorDisplayView displayView;

    private VerticalViewPager rightViewPager;
    private ViewPageAdapter pageAdapter;

    private int currentPhysicalFloor = 1; //当前所在物理楼层

    //常量定义
    private final int DEAL_FRAME_MESSAGE = 0;
    private final int TEST_SCROLL_MESSAGE = 1;
    private final int REFRESH_BLUE_VIEW_XY = 2;

    private boolean isTest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        readViewCountAndCreate();
        readAndCreateRightView();
        selectRightDisplayView(currentPhysicalFloor);
        //bindAndStartService();

        stopService.bringToFront();

        //test code  测试代码
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
//                    WhitItemView whitItemView = new WhitItemView(Main2Activity.this, getItemViewHeight());
//                    backgroundLayout.addView(whitItemView);
//                    whitItemView.viewToAimsXY(aimsX, aimsY);
//                }
//                FloorItemView itemView = viewMap.get(2);
//                float aimsX = itemView.getX();
//                float aimsY = itemView.getY();
//                blueItemView.viewToAimsXY(aimsX, aimsY);
//
////                if (displayView != null)
////                    displayView.setCurrentFloor("2");
//
////                checkPageToAlarm(2);
//////                checkPageToOldLoad();
////
////                mHanlder.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
//////                        clearOldLoadStatus();
////                        clearAlarmMode();
//////                        checkPageToAlarm(2);
////                    }
////                }, 2000);
//            }
//        }, 2000);


//        //测试代码
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isTest) {
//                    if (mHanlder == null) return;
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
    }

    @Override
    protected void InitView() {
        leftViewLayout = findViewById(R.id.left_layout);

        backgroundLayout = findViewById(R.id.backgroud_layout);
        blueLayout = findViewById(R.id.blue_layout);
        OpenDoorImgView openDoorBtn = findViewById(R.id.open);
//        ImageView openDoorBtn = findViewById(R.id.open);
        ImageView closeDoorBtn = findViewById(R.id.close);
        menuBtn = findViewById(R.id.l_menu);
        rightViewPager = findViewById(R.id.right_view_pager);
        stopService = findViewById(R.id.stopService);
        //set listener
        openDoorBtn.setEnabled(true);

        openDoorBtn.setOnClickListener(this);
        openDoorBtn.registerLongTouchCallBack(this);


        closeDoorBtn.setOnClickListener(this);
        closeDoorBtn.setOnTouchListener(mCloseButtonListener);



        menuBtn.setOnClickListener(this);
        menuBtn.setOnTouchListener(menuBtnTouchListener);
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
    }

    @SuppressLint("UseSparseArrays")
    private void readViewCountAndCreate() {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        int viewCount = SysPrefer.getInstence().getFloorCount();
        if (viewCount != viewMap.size()) {
            for (Map.Entry<Integer, FloorItemView> entry : viewMap.entrySet()) {
                leftViewLayout.removeView(entry.getValue());
            }
            backgroundLayout.removeAllViews();
            viewMap.clear();
//            int layoutHeight = leftViewLayout.getHeight();
            int viewHeight = getItemViewHeight();
            Log.v("veiw height:" + viewHeight);

            //添加灰色背景控件
            blueItemView = new BlueItemView(this, viewHeight);
            if (viewCount < 1) {
                blueItemView.delayDissmissView(true);
            } else {
                blueItemView.delayDissmissView(false);
            }
            blueItemView.setVisibility(View.INVISIBLE);
            blueLayout.addView(blueItemView);

            displayView = new FloorDisplayView(this, viewHeight);
            displayView.setVisibility(View.VISIBLE);

            RelativeLayout rlRootView = findViewById(R.id.activity_root_view);
            rlRootView.addView(displayView);

            for (int i = 1; i <= viewCount; i++) {  //物理楼层保证从 1 开始
                FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(this, i);
                FloorItemView floorItemView = new FloorItemView(this, itemBean, viewHeight);
                floorItemView.setOnTouchListener(floorItemViewListener);
                viewMap.put(i, floorItemView);
                leftViewLayout.addView(floorItemView);

                if (viewCount == 2) {
                    if (i == 1) {
                        floorItemView.checkTextToImg(false);
                    } else {
                        floorItemView.checkTextToImg(true);
                    }
                }
            }
            blueItemView.setViewMap(viewMap);
            Log.v("create view over,create view count:" + viewCount);
        }
    }


    LinkedList<FloorFeaturesView> mFloorFeaturesList;

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
        mFloorFeaturesList = linkedList;
        pageAdapter = new ViewPageAdapter(linkedList, rightViewPager);
        rightViewPager.removeAllViews();
        rightViewPager.setAdapter(pageAdapter);

    }

    Handler mHandler = new Handler();
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



    private DrawLayoutView.StateChangedListener mDrawingStatelistener = new DrawLayoutView.StateChangedListener() {
        @Override
        public void onStateChanged(boolean drawing) {
            //View control  = findViewById(R.id.control_panel);
            View black = findViewById(R.id.black_overlay);
            if(drawing){
                black.setVisibility(View.VISIBLE);
                black.setClickable(true);
            }else{
                black.setVisibility(View.INVISIBLE);
                black.setClickable(false);
            }
        }
    };
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

    private void refreshRightView(int floor) {
        pageAdapter.refreshRightView(this, floor);
    }

    private void bindAndStartService() {
        mApplication.getInstant().registerCallback(this);
    }

    /**
     * 设置当前选中楼层
     *
     * @param floor 当前右方显示楼层功能
     */
    private void selectRightDisplayView(int floor) {

        if (PreferManager.getInstence().getFloorItemBean(this, floor)
                .isDisplayClockEnable()
                || PreferManager.getInstence().getFloorItemBean(this, floor)
                .isDisplaySketchpadEnable()  || (mStatusAlarm !=-1)) {
            if (displayView.getVisibility() != View.INVISIBLE)
                displayView.setVisibility(View.INVISIBLE);
        } else {
            if (displayView.getVisibility() != View.VISIBLE)
                displayView.setVisibility(View.VISIBLE);
        }

//        FloorFeaturesView view = mFloorFeaturesList.get(floor);



        pageAdapter.selectRightDisplayView(this, currentPhysicalFloor, floor);
    }


    private void checkPageToOldLoad() {
        pageAdapter.checkPageToOldLoad(this, currentPhysicalFloor);
    }

    private void clearOldLoadStatus() {
        pageAdapter.clearOldLoadStatus(this, currentPhysicalFloor);
    }

    private void checkPageToOutOfService() {
        if (pageAdapter.checkPageToOutService(this, currentPhysicalFloor)
                && stopService.getVisibility() != View.VISIBLE){

            TextView textView = (TextView) stopService.findViewById(R.id.out_of_service);
            if(textView != null) {
                textView.setText(getString(R.string.out_of_service));
            }
            stopService.setVisibility(View.VISIBLE);
        }

    }

    private void clearOutOfServiceStatus() {
        pageAdapter.clearOutServiceStatus(this, currentPhysicalFloor);
        if (stopService.getVisibility() != View.GONE)
            stopService.setVisibility(View.GONE);
    }

    private void checkPageToAlarm(int alarmMode) {
        pageAdapter.checkPageToAlarm(this, currentPhysicalFloor, alarmMode);
    }

    private void clearAlarmMode() {
        pageAdapter.clearAlarmMode(this, currentPhysicalFloor);
    }


    private int getItemViewHeight() {
//        return LayoutUtil.getDPIFromDP(this, 140);
        return LayoutUtil.getDPIFromDP(this, 115);
    }


    private boolean mStatusOutOfService= false;
    private int     mStatusAlarm = -1;

    /**
     * 处理 接收到的帧数据
     *
     * @param data 读取到的帧数据
     */
    @SuppressLint("UseSparseArrays")
    private void dealFrame(byte[] data) {
//        if (isTest)
//            isTest = false;

        FloorItemView itemView = null;

        int tag = FrameFormatUtil.getInstance().getTAG(data);
        Log.v("receive frame tag:" + tag);
        switch (tag) {
            case FrameFormatUtil.FRAME_TAG0:
                Frame0BaseInfo info0 = FrameFormatUtil.getInstance().getTAG0Entity(data);
                if (info0 == null) return;
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
                    float aimsX = itemView.getX();
                    float aimsY = itemView.getY();

                    Log.d("update item "+currentFloorInt + "  X :"+aimsX);
                    if (blueItemView.getVisibility() != View.VISIBLE && aimsX != 0)
                        blueItemView.setVisibility(View.VISIBLE);

                    if (aimsX == 0) {
                        resendRefreshBlueMessage(itemView.getItemBean().getPhysicalFloor());
                    } else {
                        blueItemView.setToFloorIndex(itemView.getItemBean().getPhysicalFloor());
                        blueItemView.viewToAimsXY(aimsX, aimsY);
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

                Log.d("status"," overlaod :"+isOldLoad +"   out of service :"+outOfService);

                /*
                if (outOfService)
                    checkPageToOutOfService();
                else
                    clearOutOfServiceStatus();

                */
                mStatusOutOfService = outOfService;


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
                for (int floor : info1.getRegisterFloor()) {
                    itemView = viewMap.get(floor);
                    if (itemView == null) {
//                        Log.w("viewMap.get(floor) is null,floor:" + floor);
                        continue;
                    }

                    float aimsY = itemView.getY();
                    float aimsX = itemView.getX();
                    Log.d("www","white view :"+aimsX+ " , "+aimsY);


                    if (aimsX<100){
                        continue;
                    }

                    if (blueItemView.getToFloorIndex() != floor)
                        itemView.setBlackTextColor();
                    else {
                        if (blueItemView.isRun()) {
                            itemView.setBlackTextColor();
                        }else{
                            continue;
                        }
                    }
//                    if (blueItemView.getToFloorIndex() != floor)
//                        itemView.setBlackTextColor();
//
//
//                    //读取 已注册楼层的 坐标，并将白色背景添加进去并设置 Y轴
//                    if (blueItemView.getToFloorIndex() == floor)
//                        continue;



                    WhitItemView whitItemView = new WhitItemView(Main2Activity.this, getItemViewHeight());
                    whitViewMap.put(itemView.getItemBean().getPhysicalFloor(), whitItemView);
                    backgroundLayout.addView(whitItemView);
                    whitItemView.viewToAimsXY(aimsX, aimsY);
                }
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



                Log.d("status"," isAlarm :"+isAlarm);


//                if (!isAlarm) {
//                    clearAlarmMode();
//                }
                break;
        }
        updateOutOfServiceAlarmStatus();

    }


    private void updateOutOfServiceAlarmStatus(){
        Log.v("update status: " +mStatusAlarm + " ," +mStatusOutOfService );

        if (mStatusAlarm != -1){
            checkPageToAlarm(mStatusAlarm);
            clearOutOfServiceStatus();
            //displayView.setVisibility(View.INVISIBLE);
        }else {
            //displayView.setVisibility(View.VISIBLE);

            clearAlarmMode();
            if(mStatusOutOfService){
                checkPageToOutOfService();
            }else{
                clearOutOfServiceStatus();
            }
        }


        if (PreferManager.getInstence().getFloorItemBean(this, currentPhysicalFloor)
                .isDisplayClockEnable()
                || PreferManager.getInstence().getFloorItemBean(this, currentPhysicalFloor)
                .isDisplaySketchpadEnable()  || (mStatusAlarm !=-1)) {
            if (displayView.getVisibility() != View.INVISIBLE)
                displayView.setVisibility(View.INVISIBLE);
        } else {
            if (displayView.getVisibility() != View.VISIBLE)
                displayView.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 跳转设置页面
     */
    @Override
    public void startSettingActivity() {
        Log.v("start setting activity.");
        startActivity(new Intent(this, SettingActivity.class));
        overridePendingTransition(R.anim.fragment_slide_in_from_bottom, R.anim.fragment_slide_out_from_bottom);
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

    private void sendRegisterFloorData(FloorItemBean floorItemBean) {
        int floor = floorItemBean.getPhysicalFloor();
        ArrayList<Integer> floorList = new ArrayList<>();
        floorList.add(floor);
        byte data[] = FrameFormatUtil.getInstance().getRegisterFloorData(floorList);
        sendData(data);
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferManager.getInstence().getThemeMode(this) != Constant.THEME_SPHERE) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        refreshViewsPre();
        refreshRightView(currentPhysicalFloor);
        getCacheData();


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //bindAndStartService();
                mApplication.getInstant().registerCallback(Main2Activity.this);
            }
        },300);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mApplication.getInstant().unregisterCallback(this);
    }

    private void getCacheData() {
        if (mApplication.getInstant().protocolServiceIsNull())
            return;

        byte[] data0 = mApplication.getInstant().getmService().getCacheData(0);
        if (data0 != null) dealFrame(data0);

        byte[] data2 = mApplication.getInstant().getmService().getCacheData(2);
        if (data2 != null) dealFrame(data2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l_menu:
//                showInputPasswordDialog(menuBtn);
                if (mStatusOutOfService == false && mStatusAlarm == -1) {
                    startSettingActivity();
                }
                break;
            case R.id.open:
                sendData(FrameFormatUtil.getInstance().getOpenDoorData());
                break;
            case R.id.close:
                sendData(FrameFormatUtil.getInstance().getCloseDoorData());
                //checkPageToOutOfService();
                break;
        }
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

    private int tmpFloor = 1;
    private boolean add = false;

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case DEAL_FRAME_MESSAGE:
                dealFrame((byte[]) message.obj);
                break;
            case TEST_SCROLL_MESSAGE:
                if (add)
                    tmpFloor++;
                else
                    tmpFloor--;
                if (tmpFloor > 3) {
                    add = false;
                    tmpFloor = 3;
                }
                if (tmpFloor < 1) {
                    add = true;
                    tmpFloor = 1;
                }

                selectRightDisplayView(tmpFloor);
                currentPhysicalFloor = tmpFloor;


                FloorItemView itemViewtmp = viewMap.get(tmpFloor);
//                float aimsYtmp = itemViewtmp.getY();
//                Log.v("aimsYtmp:" + aimsYtmp+"  tmpFloor:"+tmpFloor);
//                if (aimsYtmp != 0) {
//                    blueItemView.setToFloorIndex(itemViewtmp.getItemBean().getPhysicalFloor());
//                    blueItemView.viewToAimsY(aimsYtmp);
//                }

                float aimsXtmp = itemViewtmp.getX();
                float aimsY = itemViewtmp.getY();
                if (blueItemView.getVisibility() != View.VISIBLE)
                    blueItemView.setVisibility(View.VISIBLE);

                if (aimsXtmp != 0) {
                    blueItemView.setToFloorIndex(itemViewtmp.getItemBean().getPhysicalFloor());
                    blueItemView.viewToAimsXY(aimsXtmp, aimsY);
                }

                break;
            case REFRESH_BLUE_VIEW_XY:
                FloorItemView itemView = viewMap.get((int) message.obj);
                if (itemView == null) {
                    resendRefreshBlueMessage((int) message.obj);
                    break;
                }
                float aimsX = itemView.getX();
                if (aimsX == 0) {
                    resendRefreshBlueMessage((int) message.obj);
                } else {
                    blueItemView.setToFloorIndex(itemView.getItemBean().getPhysicalFloor());
                    blueItemView.viewToAimsXY(aimsX, 0);
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

    @Override
    protected void onDestroy() {
        mApplication.getInstant().unregisterCallback(this);
        super.onDestroy();
        mHanlder.removeCallbacksAndMessages(null);
        mHanlder = null;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            return true;
        else
            return super.onKeyDown(keyCode, event);
    }

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
    public void LongTouch() {
        Log.v("长按触摸回调");
        sendData(FrameFormatUtil.getInstance().getDelayOpenDoorData());
    }

    @Override
    public void oneSecondTouch() {
        Log.v("one sec touch open door button");
        sendData(FrameFormatUtil.getInstance().getOpenDoorData());

    }
}
