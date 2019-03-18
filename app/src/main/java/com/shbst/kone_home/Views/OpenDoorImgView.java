package com.shbst.kone_home.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.ArithUtil;
import com.shbst.kone_home.Utils.FrameFormatUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

/**
 * Created by zhouwenchao on 2018-03-30.
 */

@SuppressLint("AppCompatCustomView")
public class OpenDoorImgView extends RelativeLayout
        implements View.OnTouchListener, Handler.Callback {
    private final int DELAY_DISPLAY = 1;
    private final int CANCEL_ANIMATION = 2;
    private final int DELAY_SEND_MESSAGE = 3;
    private final long delay_time = 500;

    private Context mContext;
    private LongTouchCallBack mCallback;
    private Handler mHandler;
    private long touchDnTime = -1;

    private CustomBall customBall;
    private ImageView openDoorlyaoutView;
    private boolean isDelaySend = false;


    public OpenDoorImgView(Context context) {
        super(context);
        InitView(context);
    }

    public OpenDoorImgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
    }

    public OpenDoorImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitView(Context context) {
        Log.v("初始化控件");
        this.mContext = context;
        mHandler = new Handler(this);

        View view = LayoutInflater.from(context).inflate(R.layout.opendoor_view_layout, null);
        customBall = view.findViewById(R.id.open_door_view);
        openDoorlyaoutView = view.findViewById(R.id.opendoor_layout_img);

        drawProgressAnimation(0);
        this.addView(view);
        //只有开门延时为 true 才采用

        openDoorlyaoutView.setOnTouchListener(this);
    }

    @Override
    public boolean performClick() {
        Log.v("performClick");
        return super.performClick();
    }

    Runnable mOpenEvent = new Runnable() {
        @Override
        public void run() {

            Log.d("mopen event ");
            //sendData(FrameFormatUtil.getInstance().getCloseDoorData());

            mCallback.oneSecondTouch();
            mHandler.postDelayed(this,1000);
        }
    };
    public void registerLongTouchCallBack(LongTouchCallBack callBack) {
        this.mCallback = callBack;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                openDoorlyaoutView.setImageResource(R.drawable.w_u_open2);
                if (PreferManager.getInstence().OpenDoorDelay()) {
                    touchDnTime = SystemClock.elapsedRealtime();
                    Message message = mHandler.obtainMessage();
                    message.what = DELAY_DISPLAY;
                    mHandler.sendMessage(message);
                }else{
                    mOpenEvent.run();
                }

                break;
            case MotionEvent.ACTION_UP:
                openDoorlyaoutView.setImageResource(R.drawable.w_theme1_open);
                if (PreferManager.getInstence().OpenDoorDelay()) {
                    if (SystemClock.elapsedRealtime() - touchDnTime < delay_time) {
                        this.callOnClick();
//                    if (mCallback != null)
//                        mCallback.clickView();
                    }
                    touchDnTime = -1;
                    Message cancelMsg = mHandler.obtainMessage();
                    cancelMsg.what = CANCEL_ANIMATION;
                    mHandler.sendMessage(cancelMsg);
                }else{
                    mHandler.removeCallbacks(mOpenEvent);
                }
                break;
        }

        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DELAY_DISPLAY:
                if (touchDnTime == -1) {
                    break;
                } else {
                    Message message = mHandler.obtainMessage();
                    message.what = DELAY_DISPLAY;
                    mHandler.sendMessageDelayed(message, 16); //每隔16毫秒刷新一次界面显示
                }
                //还没有到达延时时间
                if (SystemClock.elapsedRealtime() - delay_time < touchDnTime) {
                    break;
                }
                //在一秒延时动画内
                if (SystemClock.elapsedRealtime() - 1000 - delay_time < touchDnTime) {
//                    drawAnimation();
                    openDoorlyaoutView.setImageResource(R.drawable.b_open);
                    openDoorlyaoutView.setBackground(null);
                    int progress = (int) ArithUtil.mul(ArithUtil.div((SystemClock.elapsedRealtime() - touchDnTime - delay_time), 1000), 100);
                    drawProgressAnimation(progress);
                    break;
                }
                isDelaySend = true;
                mHandler.removeMessages(DELAY_DISPLAY);
                //超过 1s 延时，停止动画
                stopAnimation();
                drawProgressAnimation(100);

                openDoorlyaoutView.setImageResource(R.drawable.doe_w);
                openDoorlyaoutView.setBackground(ContextCompat.getDrawable(this.getContext(), R.drawable.un_round_3));

                if (mCallback != null)
                    mCallback.LongTouch();

                Message delaySendMsg = mHandler.obtainMessage();
                delaySendMsg.what = DELAY_SEND_MESSAGE;
                mHandler.sendMessageDelayed(delaySendMsg, 1000);
                break;
            case CANCEL_ANIMATION:
                mHandler.removeMessages(DELAY_DISPLAY);
                if (!isDelaySend) {
                    drawProgressAnimation(0);
                    openDoorlyaoutView.setImageResource(R.drawable.w_theme1_open);
                    openDoorlyaoutView.setBackground(ContextCompat.getDrawable(this.getContext()
                            , R.drawable.un_round_2));
                }
                break;
            case DELAY_SEND_MESSAGE:
                isDelaySend = false;
                Message cancelMsg = mHandler.obtainMessage();
                cancelMsg.what = CANCEL_ANIMATION;
                mHandler.sendMessage(cancelMsg);
                break;
        }
        return false;
    }

    public interface LongTouchCallBack {
        void LongTouch();
        void oneSecondTouch();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void drawAnimation() {
        customBall.startAnimation();
    }

    private void stopAnimation() {
        customBall.stopAnimation();
    }

    private void cancelAnimation() {
        customBall.clearAnimation();
    }

    private void drawProgressAnimation(int progress) {
        if (progress == 0)
            customBall.setVisibility(View.INVISIBLE);
        else
            customBall.setVisibility(View.VISIBLE);

        customBall.drawProgressAnimation(progress);
    }
}
