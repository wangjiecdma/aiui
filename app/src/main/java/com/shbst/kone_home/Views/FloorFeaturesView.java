package com.shbst.kone_home.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.CacheUtil;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.ImageUtil;
import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.MyTime;
import com.shbst.kone_home.Utils.PreferManager;


/**
 * Created by zhouwenchao on 2018-02-08.
 * 楼层功能控件
 */

public class FloorFeaturesView extends RelativeLayout
        implements Handler.Callback {
    private Context context;
    private TextView displayFloor, welcomeText, textClock;
    private FloorItemBean itemBean;
    private Handler mainHandler = new Handler(Looper.getMainLooper(), this);
    private  DrawLayoutView  mDrawView= null;
    private  DrawLayoutView.StateChangedListener  mlistener;
    //将背景图片进行缓存，避免多次重复加载引起的性能bug
//    private HashMap<Integer, Bitmap> blockThemeBgTmp = new HashMap<>();

    public FloorFeaturesView(Context context, FloorItemBean itemBean) {
        super(context);
        InitView(context, itemBean);
    }

    private void InitView(Context context, FloorItemBean itemBean) {
        Log.v("create floor features view,physical floor:" + itemBean.getPhysicalFloor());
        this.context = context;
        refreshFloorBean(itemBean);
    }

    public void refreshFloorBean(FloorItemBean itemBean) {
        this.itemBean = itemBean;
        this.removeAllViews();
        stopFlushTextRunnable();
        clearView();
        LoadLayout();
    }
//
//    public boolean needHideFloorText(){
//        return itemBean.isDisplayTextEnable();
//    }

    private void LoadLayout() {
        if (itemBean.getAlarmMode() != -1) {
            loadAlarmBellLayout(context);
            return;
        }
//        if (itemBean.isOutOfService()) {
//            loadOutOfServiceLayout(context);
//            return;
//        }
        if (itemBean.isOldLoad()) {
            loadOldLoadlayout(context);
            return;
        }
        if (itemBean.isDisplayImageEnable()) {
            loadBgImgLayout();
        }
        if (itemBean.isDisplayColorEnable()) {
            this.setBackgroundColor(ContextCompat.getColor(context
                    , PreferManager.getInstence()
                            .getColorFromMode(itemBean.getDisplayColorMode())));
        }

        //读取颜色设置
        if (itemBean.isDisplayClockEnable()) {
            loadClockLayout(context);
        }
        if (itemBean.isDisplayTextEnable()) {
            loadTextLayout(context);
        }
        if (itemBean.isDisplaySketchpadEnable()) {
            this.setBackgroundColor(ContextCompat.getColor(context
                    , PreferManager.getInstence()
                            .getColorFromMode(2)));
            loadDrawLayout(context);
        }
    }


    private void loadBgImgLayout() {
//        this.setBackgroundResource(PreferManager.getInstence()
//                .getBgImgFromMode(itemBean.getDisplayBgImgMode()));

        /*
        Bitmap bkg;
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            bkg = CacheUtil.getThemeBitmap(context, itemBean.getDisplayBgImgMode());
            if (bkg == null) {
                bkg = ImageUtil.getBitmap(context.getResources()
                        , PreferManager.getInstence()
                                .getBgImgFromMode(getContext(),itemBean.getDisplayBgImgMode()));

                CacheUtil.putThemeBitmap(context, itemBean.getDisplayBgImgMode(), bkg);
            }
//            if (blockThemeBgTmp.size() > 0)
//                blockThemeBgTmp.clear();
        } else if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_BLOCKS) {
//            bkg = blockThemeBgTmp.get(PreferManager.getInstence()
//                    .getBgImgFromMode(itemBean.getDisplayBgImgMode()));
            bkg = CacheUtil.getThemeBitmap(context, itemBean.getDisplayBgImgMode());
            if (bkg == null) {
                bkg = ImageUtil.getBitmap(context.getResources()
                        , PreferManager.getInstence()
                                .getBgImgFromMode(getContext(),itemBean.getDisplayBgImgMode()));
                int newW = 400;
                int newH = bkg.getHeight();

                int startX = (bkg.getWidth() - newW) / 2;
                int startY = (bkg.getHeight() - newH) / 2;

                Bitmap newBM = Bitmap.createBitmap(bkg, startX, startY, newW, newH, null, false);
                if (!bkg.isRecycled()) {
                    bkg.recycle();
                }
                bkg = newBM;

                CacheUtil.putThemeBitmap(context, itemBean.getDisplayBgImgMode(), newBM);

//                blockThemeBgTmp.put(PreferManager.getInstence()
//                                .getBgImgFromMode(itemBean.getDisplayBgImgMode())
//                        , bkg);
            }

//            Bitmap overlay = Bitmap.createBitmap(400, bkg.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(overlay);
//            canvas.drawBitmap(bkg, -this.getLeft(), -this.getTop(), null);
//            RenderScript rs = RenderScript.create(context);
//            Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
//                blur.setInput(overlayAlloc);
//                //模糊背景
//                float radius = 2f;
//                blur.setRadius(radius);
//                blur.forEach(overlayAlloc);
//            }
//            overlayAlloc.copyTo(overlay);
//            bkg = overlay;
//            rs.destroy();
        } else {
            throw new Resources.NotFoundException("未知主题");
        }
        */

        //this.setBackground(new BitmapDrawable(getResources(), bkg));


        ImageView imageView = new ImageView(context);
        //imageView.setImageBitmap(bkg);

        imageView.setImageResource(PreferManager.getInstence()
                .getBgImgFromMode(getContext(),itemBean.getDisplayBgImgMode()));


        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(imageView, imageParams);

//


        View view = new View(context);
        view.setBackground(ContextCompat.getDrawable(context, R.drawable.mask_bg));
        RelativeLayout.LayoutParams viewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, viewLayoutParams);
//        this.setBackground(new BitmapDrawable(getResources(), overlay));
    }

    private void loadAlarmBellLayout(Context context) {
        Log.i("加载警铃布局");
        View view = LayoutInflater.from(context).inflate(R.layout.features_alarm_layout
                , null);
        LinearLayout AlarmLayout, Alarm_connect;
        TextView alarmTitle;

        AlarmLayout = view.findViewById(R.id.AlarmLayout);
        Alarm_connect = view.findViewById(R.id.Alarm_connect);
        alarmTitle = view.findViewById(R.id.alarm_title);

        if (itemBean.getAlarmMode() == 0) {//按了警龄
            AlarmLayout.setVisibility(View.VISIBLE);
            Alarm_connect.setVisibility(View.GONE);
            alarmTitle.setText(context.getString(R.string.press_second));
        }
        if (itemBean.getAlarmMode() == 1) {//正在接听
            AlarmLayout.setVisibility(View.VISIBLE);
            Alarm_connect.setVisibility(View.GONE);
            alarmTitle.setText(context.getString(R.string.calling));
        }
        if (itemBean.getAlarmMode() == 2) {//警铃已接通
            AlarmLayout.setVisibility(View.GONE);
            Alarm_connect.setVisibility(View.VISIBLE);
        }
        if (PreferManager.getInstence().getThemeMode(context)
                == Constant.THEME_SPHERE
                && AlarmLayout.getVisibility() == View.VISIBLE) {
            AlarmLayout.addView(getLineView());
        }
        if (PreferManager.getInstence().getThemeMode(context)
                == Constant.THEME_SPHERE
                && Alarm_connect.getVisibility() == View.VISIBLE) {
            Alarm_connect.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout leftlayout = Alarm_connect.findViewById(R.id.Alarm_connect_left);
            leftlayout.addView(getLineView());

            LinearLayout rightlayout = Alarm_connect.findViewById(R.id.Alarm_connect_right);
            rightlayout.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams rightlayoutParams = rightlayout.getLayoutParams();
            if (rightlayoutParams instanceof LinearLayout.LayoutParams) {
                rightlayoutParams.height = LayoutUtil.getDPIFromDP(context, 500);
                ((LinearLayout.LayoutParams) rightlayoutParams).setMargins(LayoutUtil.getDPIFromDP(context, 20), LayoutUtil.getDPIFromDP(context, 20)
                        , LayoutUtil.getDPIFromDP(context, 20), LayoutUtil.getDPIFromDP(context, 135));
            }

            LinearLayout rightChildView =
                    rightlayout.findViewById(R.id.Alarm_connect_right_childView);
            ViewGroup.LayoutParams rcvLayoutParams = rightChildView.getLayoutParams();
            if (rcvLayoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) rcvLayoutParams).leftMargin = 0;
            }
        }
        if (PreferManager.getInstence().getThemeMode(context)
                == Constant.THEME_BLOCKS
                && Alarm_connect.getVisibility() == View.VISIBLE) {
            LinearLayout rightlayout = Alarm_connect.findViewById(R.id.Alarm_connect_right);
            ViewGroup.LayoutParams rightlayoutParams = rightlayout.getLayoutParams();
            if (rightlayoutParams instanceof LinearLayout.LayoutParams) {
                rightlayoutParams.height = LayoutUtil.getDPIFromDP(context, 680);
            }
        }
        this.addView(view);
    }

//    private void loadOutOfServiceLayout(Context context) {
//        // TODO 加载布局
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT
//                , LinearLayout.LayoutParams.MATCH_PARENT
//        );
//        LinearLayout linearLayout = new LinearLayout(context);
//        linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        layoutParams.gravity = Gravity.CENTER;
//        linearLayout.setLayoutParams(layoutParams);
//
//        //图片
//        ImageView imageView = new ImageView(context);
//        LinearLayout.LayoutParams ivViewParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT
//                , LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        imageView.setBackground(ContextCompat.getDrawable(context
//                , R.drawable.status_stop));
//        linearLayout.addView(imageView, ivViewParams);
//
//        // 文字提示
//        FontTextView fontTextView = new FontTextView(context);
//        LinearLayout.LayoutParams ftViewParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT
//                , LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        ftViewParams.topMargin = LayoutUtil.getDPIFromDP(context, 20);
//        fontTextView.setTextSize(LayoutUtil.getDPIFromSP(context, 46));
//        fontTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
//        fontTextView.setText("OUT OF OPERATION");
//        linearLayout.addView(fontTextView, ftViewParams);
//
//        this.addView(linearLayout);
//    }

    private void loadOldLoadlayout(Context context) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.features_overload_layout
                , null);
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            LinearLayout llOverLoadLayout = view.findViewById(R.id.overLoad);
            llOverLoadLayout.addView(getLineView());
        }
        this.addView(view);
    }

    private void loadClockLayout(Context context) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.features_clock_layout, null);
        welcomeText = view.findViewById(R.id.welcome_text);
        judgeTime();
        startFlushTextRunnable();
        textClock = view.findViewById(R.id.clock_TextClock);
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            LinearLayout layout = view.findViewById(R.id.clockLayout);
            layout.addView(getLineView());

            textClock.setTextSize(LayoutUtil.getDPIFromSP(context, 24));
            ViewGroup.LayoutParams textClockParams = textClock.getLayoutParams();
            if (textClockParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) textClockParams)
                        .setMargins(0, LayoutUtil.getDPIFromDP(context, 30), 0, 0);
            }
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
////            textClock.setFormat12Hour(PreferManager.getInstence().getDateFormatStr(
////                    PreferManager.getInstence().getDateFormat(context)
////            ));
////            textClock.setFormat24Hour(PreferManager.getInstence().getDateFormatStr(
////                    PreferManager.getInstence().getDateFormat(context)
////            ));
//        }

        Log.v("this  add clock view");
        this.addView(view);
    }

    private void loadTextLayout(Context context) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.features_text_layout, null);

        //老代码已经失效
        DisplayTextView textView = view.findViewById(R.id.displayText);
        // 自定义控件和方法，设置显示文本
        textView.setTextColor(Color.WHITE);
        textView.setDisplayText(itemBean.getDisplayText());
        //end 失效代码view为gone

        // 第一行大写，剩余小写并且控制在最多五行
        TextView textBig = view.findViewById(R.id.display_big);
        TextView small = view.findViewById(R.id.display_small);
        String[] textArray = itemBean.getDisplayText().split("\n");
        String line1 = textArray[0];
        String lines = itemBean.getDisplayText().substring(line1.length());
        if(lines.length() > 0){
            lines = lines.trim();
        }
        textBig.setText(line1);
        small.setText(lines);


        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            LinearLayout layout = view.findViewById(R.id.tvViewLayout);
            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                ((MarginLayoutParams) layoutParams).leftMargin = ((MarginLayoutParams) layoutParams).leftMargin
                        + LayoutUtil.getDPIFromDP(context, 200);
            }

            layout.addView(getLineView());
            textView.setGravity(Gravity.BOTTOM);
//            ViewGroup.LayoutParams tvLayoutParams = textView.getLayoutParams();
//            if (tvLayoutParams instanceof RelativeLayout.LayoutParams) {
//                Log.v("bottomMargin:"+LayoutUtil.getDPIFromDP(context, 220));
//                ((LayoutParams) tvLayoutParams).topMargin = LayoutUtil.getDPIFromDP(context, 220);
//            }
//            textView.setLayoutParams(tvLayoutParams);
        }

        this.addView(view);
    }

    private int currentDrawTmpBitmapTheme;

    private void loadDrawLayout(Context context) {

        //图片缓存会导致主题切换后，依旧使用缓存图片，导致显示旧图，所以要判断主题切换，清除缓存图
        if (currentDrawTmpBitmapTheme != PreferManager.getInstence().getThemeMode(context)) {
            currentDrawTmpBitmapTheme = PreferManager.getInstence().getThemeMode(context);
            String imgFilePath = itemBean.getSketchpadImgPath();
            if (!TextUtils.isEmpty(imgFilePath)) {
                CacheUtil.removeBitmap(imgFilePath);
//                Bitmap tmpBitmap = CacheUtil.removeBitmap(imgFilePath);
//                if (tmpBitmap != null)
//                    tmpBitmap.recycle();
            }
        }

        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_BLOCKS) {
            DrawLayoutView view = new DrawLayoutView(context, itemBean);
            if(mlistener != null){
                view.setOnDrawingStateChanged(mlistener);
            }
            mDrawView = view;
            this.addView(view);
        } else if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            RelativeLayout.LayoutParams llParamas
                    = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            DrawLayoutView view = new DrawLayoutView(context, itemBean);
            if(mlistener != null){
                view.setOnDrawingStateChanged(mlistener);
            }
            mDrawView = view;
            LinearLayout.LayoutParams dvParamas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            dvParamas.setMargins(0, 0, 0, LayoutUtil.getDPIFromDP(context, 230));

            linearLayout.addView(view, dvParamas);

            this.addView(linearLayout, llParamas);
        } else {
            throw new Resources.NotFoundException("未知主题");
        }

    }

    @SuppressLint("SetTextI18n")
    private void judgeTime() {

    }


    public void setDrawingStateListener(DrawLayoutView.StateChangedListener listener){
        mlistener = listener;
        if(mDrawView != null){
            mDrawView.setOnDrawingStateChanged(listener);
        }
    }


    private View getLineView() {
        View lineView = new View(context);
        LinearLayout.LayoutParams lineViewParame = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutUtil.getDPIFromDP(context, 215));
        lineView.setLayoutParams(lineViewParame);
        return lineView;
    }

    private void clearView() {
        displayFloor = null;
        welcomeText = null;
    }


    private FreshRunnable freshRunnable;

    private void startFlushTextRunnable() {
        if (freshRunnable != null) freshRunnable.stop();
        freshRunnable = new FreshRunnable();
        new Thread(freshRunnable).start();
    }

    private void stopFlushTextRunnable() {
        if (freshRunnable != null) freshRunnable.stop();
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 0:
                if (welcomeText == null)
                    break;
                welcomeText.setText((String) message.obj);
                break;
            case 1:
                if (textClock != null) {
                    textClock.setText((String) message.obj);
                }
                break;
        }
        return false;
    }

    private class FreshRunnable implements Runnable {
        private boolean isExit = false;

        private void stop() {
            isExit = true;
        }

        @Override
        public void run() {
            while (!isExit) {
                if (welcomeText != null) {
                    if (mainHandler == null)
                        return;
                    Message message = mainHandler.obtainMessage();
                    message.what = 0;

                    int M = Integer.parseInt(MyTime.date_m());
                    int D = Integer.parseInt(MyTime.date_D());
                    // FIXME: 2018/4/4 时间格式
                    if (M == 1 && D >= 1 && D <= 7) {
                        //新年快乐
                        message.obj = getContext().getString(R.string.happy_new_year);
                    } else {
                        int hour = Integer.parseInt(MyTime.date_H());
                        if (hour > 5 && hour < 12) {
                            message.obj = getContext().getString(R.string.good_morning);
                        } else if (hour >= 12 && hour < 18) {
                            message.obj = getContext().getString(R.string.good_afternoon);
                        } else if (hour >= 18 && hour <= 24) {
                            message.obj = getContext().getString(R.string.good_evening);
                        } else if (hour >= 0 && hour <= 5) {
                            message.obj = getContext().getString(R.string.good_night);
                        } else {
                            message.obj = getContext().getString(R.string.good_morning);
                        }
                    }
                    mainHandler.sendMessage(message);
                }
                if (textClock != null) {
                    Message message = mainHandler.obtainMessage();
                    message.what = 1;
                    message.obj = PreferManager.getInstence().getDate(context);
                    mainHandler.sendMessage(message);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public FloorItemBean getItemBean() {
        return itemBean;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopFlushTextRunnable();
        if (mainHandler != null)
            mainHandler.removeCallbacksAndMessages(null);
        mainHandler = null;
        clearView();
    }

}
