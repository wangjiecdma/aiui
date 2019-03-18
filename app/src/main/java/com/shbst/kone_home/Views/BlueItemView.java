package com.shbst.kone_home.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.shbst.kone_home.MainActivity;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Utils.SysPrefer;

import java.util.HashMap;

/**
 * Created by zhouwenchao on 2018-02-28.
 */

public class BlueItemView extends View {
    private Context mContext;

    private float aimsY = -1;

    private int toFloorIndex = 0;

    private HashMap<Integer, WhitItemView> whitViewMap;
    private HashMap<Integer, FloorItemView> viewMap;

    private boolean isRun;

    private boolean delayDismissView = false;

    public BlueItemView(Context context, int viewHeight) {
        super(context);
        InitView(context, viewHeight);
    }

    public BlueItemView(Context context, @Nullable AttributeSet attrs, int viewHeight) {
        super(context, attrs);
        InitView(context, viewHeight);
    }

    public BlueItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int viewHeight) {
        super(context, attrs, defStyleAttr);
        InitView(context, viewHeight);
    }

    private void InitView(Context context, int vOrh) {
        this.mContext = context;
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_BLOCKS) {
            this.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_color_b));
        }
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            // todo 定义其他颜色
//            this.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_color));
            this.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round));
        }
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, vOrh);
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_BLOCKS
                && layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            int viewCount = SysPrefer.getInstence().getFloorCount();
            //当楼层数小于4时，需要增加电梯当前所在楼层，电梯上下行控件
            if (viewCount < 4) viewCount += 1;
            int layoutHeight = this.getResources().getInteger(R.integer.display_floor_layout_height);

            ((RelativeLayout.LayoutParams) layoutParams).bottomMargin = MainActivity.getItemViewHeight(getContext());
        }
        if (PreferManager.getInstence().getThemeMode(context)
                == Constant.THEME_SPHERE) {
            layoutParams.width = vOrh;
            int itemCount = PreferManager.getInstence().getFloorCount();
            float viewX = 14 + (itemCount - 1) * (vOrh + 10);
            this.setX(viewX);
        }
        layoutParams.height = vOrh;
        this.setLayoutParams(layoutParams);
    }


    private float startY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (aimsY == -1) {
            callBackAnimationOver();
            return;
        }
        float currentY = this.getY();
        float append = 4;
        int viewHeight = this.getHeight();
        float tmp = viewHeight;
        tmp = Math.abs(tmp);
        float appendTmp = tmp / 4;
//        if (currentY < startY + appendTmp
//                || currentY > (startY + tmp / 2 + appendTmp)
//                || currentY > (startY - appendTmp)
//                || currentY < (startY - tmp / 2 - appendTmp)) {
//            Log.v("currentY:" + currentY + " appendTmp:" + appendTmp
//                    + "  (tmp / 2 + appendTmp):" + (tmp / 2 + appendTmp));
//            append = 2;
//        } else {
//            Log.v("加速");
//            append = 4;
//        }

//        if (Math.abs(viewHeight - aimsY) / 2 < tmp) {
//            append = 4;
//        } else {
//            append = 2;
//        }

        if (currentY > aimsY) {
            if (currentY > (startY - appendTmp)
                    || currentY < (startY - tmp / 2 - appendTmp)) {
                append = 2;
            } else {
//                Log.v("加速");
                append = 4;
            }
            currentY -= append;
            if (currentY < aimsY) currentY = aimsY;

            this.setY(currentY);
            invalidate();
        }
        if (currentY < aimsY) {
            if (currentY < startY + appendTmp
                    || currentY > (startY + tmp / 2 + appendTmp)) {
                append = 2;
            } else {
//                Log.v("加速");
                append = 4;
            }
            currentY += append;
            if (currentY > aimsY) currentY = aimsY;

            this.setY(currentY);
            invalidate();
        }
        if (currentY == aimsY) {
            callBackAnimationOver();
        }
    }


    public void setToFloorIndex(int floorIndex) {
        toFloorIndex = floorIndex;
    }

    public int getToFloorIndex() {
        return toFloorIndex;
    }

    public void viewToAimsY(float y) {
        Log.d("update view xy :"+y);
        if (this.getVisibility() != View.VISIBLE)
            this.setVisibility(View.VISIBLE);
        if (this.getY() == y) {
            callBackAnimationOver();
            return;
        }
//        if (this.getY() < y) {
//            moveDelWithAnimation(this, 800, 1000, false);
//            return;
//        }
        setRun(true);
        this.aimsY = y;
        removeCallbacks(setBgRunnable);
        if (PreferManager.getInstence().getThemeMode(mContext)
                == Constant.THEME_BLOCKS) {
            this.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_color));
        }
        startY = this.getY();
        invalidate();
    }

    public void viewToAimsXY(float x, float y) {
        if (this.getVisibility() != View.VISIBLE)
            this.setVisibility(View.VISIBLE);
        Log.v("x:" + x + "  y:" + y);
        this.aimsY = -1;
        if (this.getX() == x && this.getY() == y) {
            callBackAnimationOver();
            return;
        }
        //未修改前是 39 ，现在是14
        if (x <= 0)
            x = 14;
        this.setX(x);
        this.setY(y);
        invalidate();
    }

    public void delayDissmissView(boolean dissmiss) {
        this.delayDismissView = dissmiss;
    }

    //TODO 在动画完成后，通知主界面更改某些动画
    private void callBackAnimationOver() {
        setRun(false);
        //延时一秒后设置背景
        if (PreferManager.getInstence().getThemeMode(mContext) == Constant.THEME_BLOCKS) {
            postDelayed(setBgRunnable, 1000);
        }
        if (whitViewMap != null) {
            WhitItemView itemView = whitViewMap.get(toFloorIndex);
            if (itemView != null) {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
        if (viewMap != null) {
            FloorItemView floorItemView = viewMap.get(toFloorIndex);
            if (floorItemView != null) {
                floorItemView.clearTextColor();
            }
        }
        removeCallbacks(delayDissmissViewRunnable);
        if (delayDismissView) {
            postDelayed(delayDissmissViewRunnable, 1000);
        }
        Log.v("callBackAnimationOver");
    }

    public HashMap<Integer, WhitItemView> getWhitViewMap() {
        return whitViewMap;
    }

    public void setWhitViewMap(HashMap<Integer, WhitItemView> whitViewMap) {
        this.whitViewMap = whitViewMap;
    }

    public HashMap<Integer, FloorItemView> getViewMap() {
        return viewMap;
    }

    public void setViewMap(HashMap<Integer, FloorItemView> viewMap) {
        this.viewMap = viewMap;
    }

    //下运行
    public void moveDelWithAnimation(final View view, int duration, int delay, final boolean isBack) {
        //创建位移动画
        android.util.Log.v("szm", "moveDelWithAnimation");
        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, view.getHeight());
        ani.setInterpolator(new AccelerateDecelerateInterpolator());//设置加速器
        ani.setDuration(duration);//设置动画时间
        ani.setStartOffset(delay);//设置动画延迟时间
        //监听动画播放状态
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int deltaX = 0;
                int deltaY = 0;
//                int deltaX = (int) (xToDeltaDistance - xFromDeltaDistance);
//                int deltaY = (int) (yToDeltaDistance - yFromDeltaDistance);
                int layoutX = view.getLeft();
                int layoutY = view.getTop();
                int tempWidth = view.getWidth();
                int tempHeight = view.getHeight();
                view.clearAnimation();
//                if (isBack == false) {
                layoutX += deltaX;
                layoutY += deltaY;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(layoutX, layoutY, 0, 0);// 通过自定义坐标来放置你的控件
                view.setLayoutParams(params);
//                    view.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
//                } else {
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                    params.setMargins(layoutX, layoutY, 0, 0);// 通过自定义坐标来放置你的控件
//                    view.setLayoutParams(params);
//                }
            }
        });
        view.startAnimation(ani);
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    private Runnable setBgRunnable = new Runnable() {
        @Override
        public void run() {
            BlueItemView.this.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_color_b));
        }
    };

    //主题2 中，当只有两层站的时候，没有已到达楼层显示--所以使用延时线程，指定延时后隐藏到站背景
    private Runnable delayDissmissViewRunnable = new Runnable() {
        @Override
        public void run() {
            BlueItemView.this.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //从窗口移除时，清除延时线程
        removeCallbacks(setBgRunnable);
        removeCallbacks(delayDissmissViewRunnable);
    }
}
