package com.shbst.kone_home.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

@SuppressLint("ViewConstructor")
public class FloorItemView extends RelativeLayout {
    private Context mContext;
    private FloorItemBean itemBean;
    private TextView displayFloor, floorDescription;
    private ImageView displayImg;
    private boolean arrwoIsUp = false;

    public FloorItemView(Context context, FloorItemBean itemBean, int viewHeight) {
        super(context);
        InitView(context, itemBean, viewHeight);
    }

    public FloorItemView(Context context, @Nullable AttributeSet attrs, FloorItemBean itemBean, int viewHeight) {
        super(context, attrs);
        InitView(context, itemBean, viewHeight);
    }

    public FloorItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, FloorItemBean itemBean, int viewHeight) {
        super(context, attrs, defStyleAttr);
        InitView(context, itemBean, viewHeight);
    }

    private void InitView(Context context, FloorItemBean itemBean, int viewHeight) {
        this.itemBean = itemBean;
        this.mContext = context;
        Log.v("create floor item view,physicalFloor is:" + itemBean.getPhysicalFloor());
//        int viewHeight = context.getResources().getInteger(R.integer.floor_item_height);  //设定布局
        switch (PreferManager.getInstence().getThemeMode(context)) {
            case Constant.THEME_BLOCKS:
                LoadThemeBlocksLayout(context, itemBean, viewHeight);
                break;
            case Constant.THEME_SPHERE:
                LoadThemeSphereLayout(context, itemBean, viewHeight);
                break;
            default:
                throw new Resources.NotFoundException("未知主题");
        }
        Log.v("create floor item view over ,not error .");
    }

    private void LoadThemeBlocksLayout(Context context, FloorItemBean itemBean, int viewHeight) {
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, viewHeight);

        layoutParams.height = viewHeight;
        this.setLayoutParams(layoutParams);
//        this.setGravity(Gravity.CENTER);
//        this.(LinearLayout.HORIZONTAL);

        //添加其他控件
        //添加楼层显示控件
        displayFloor = new FontTextView(context); // 得到AttributeSet);  //add  to layout
        displayFloor.setTextColor(ContextCompat.getColor(context, R.color.white));
        displayFloor.setTextSize(LayoutUtil.getDPIFromSP(context,
                context.getResources().getInteger(R.integer.mainactivity_floor_text_size)));
        float size = viewHeight*0.8f;
        displayFloor.setTextSize(size);

        displayFloor.setText(itemBean.getDisplayFloor());
        Log.v("get display floor:" + itemBean.getDisplayFloor() + "  textSize :"+size *2);
        displayFloor.setGravity(Gravity.CENTER);
//        LayoutParams displayFloorLayoutParams =
//                new LayoutParams(context.getResources().getInteger(R.integer.mainactivity_floor_item_height)
//                        , LayoutParams.MATCH_PARENT);
//
        int width = context.getResources().getInteger(R.integer.mainactivity_floor_item_height);
        if (PreferManager.getInstence().getFloorCount() <5){
            width = 130;
        }
        LayoutParams displayFloorLayoutParams = new LayoutParams(width, LayoutParams.MATCH_PARENT);


//        LayoutParams displayFloorLayoutParams =
//                new LayoutParams(LayoutParams.WRAP_CONTENT
//                        , LayoutParams.MATCH_PARENT);
        displayFloorLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        this.addView(displayFloor, displayFloorLayoutParams);
        //添加楼层描述控件
        floorDescription = new FontTextView(context); // 得到AttributeSet);  //add  to layout
        floorDescription.setTextColor(Color.rgb(255, 255, 255));
        floorDescription.setTextSize(LayoutUtil.getDPIFromSP(context,
                context.getResources().getInteger(R.integer.mainactivity_floor_info_size)));

        size = viewHeight*0.35f;
        floorDescription.setTextSize(size);

        floorDescription.setGravity(Gravity.CENTER);
        floorDescription.setText(itemBean.getFloorDescription());
        LayoutParams floorDescriptionLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT
                        , LayoutParams.MATCH_PARENT);
        floorDescriptionLayoutParams.leftMargin = LayoutUtil.getDPIFromDP(context,width +10);
        floorDescriptionLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        this.addView(floorDescription, floorDescriptionLayoutParams);


        View lineView = new View(context);
        lineView.setBackgroundColor(ContextCompat.getColor(context, R.color.lineColor));
        LayoutParams lineViewLayout =
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT
                        , 1);
        lineViewLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        lineViewLayout.addRule(RelativeLayout.ALIGN_BOTTOM, TRUE);
        this.addView(lineView, lineViewLayout);
    }

    private void LoadThemeSphereLayout(Context context, FloorItemBean itemBean, int viewHOrW) {
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams == null)
            layoutParams = new LinearLayout.LayoutParams(viewHOrW, viewHOrW);

        layoutParams.width = viewHOrW;
        layoutParams.height = viewHOrW;
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(5, 0, 5, 0);
        }
        this.setLayoutParams(layoutParams);

        ImageView bgImgView = new ImageView(context);
        bgImgView.setBackground(ContextCompat.getDrawable(context, R.drawable.un_round));
        LayoutParams bgImgViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(bgImgView, bgImgViewParams);

        displayFloor = new FontTextView(context);
        displayFloor.setTextColor(ContextCompat.getColor(context, R.color.white));
        displayFloor.setTextSize(LayoutUtil.getDPIFromSP(context,
                context.getResources().getInteger(R.integer.main2activity_floor_text_size)));
        displayFloor.setText(itemBean.getDisplayFloor());
        displayFloor.setGravity(Gravity.CENTER);
        LayoutParams displayTViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        displayTViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.addView(displayFloor, displayTViewParams);
    }

    public FloorItemBean getItemBean() {
        return itemBean;
    }

    /**
     * 在楼层只有俩个时，显示的是上下箭头
     */
    public void checkTextToImg(boolean isUp) {
        this.removeView(displayFloor);
        displayFloor = null;
        LayoutParams displayIViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        displayIViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        displayImg = new ImageView(mContext);
        displayImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.arrwoIsUp = isUp;
        this.addView(displayImg, displayIViewParams);
        clearTextColor();
    }

    public void refreshDisplayPre(FloorItemBean itemBean) {
        this.itemBean = itemBean;

        if (displayFloor != null)
            displayFloor.setText(itemBean. getDisplayFloor());

        if (floorDescription != null)
            floorDescription.setText(itemBean.getFloorDescription());
    }

    public void setBlackTextColor() {
        if (displayFloor != null)
            displayFloor.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        if (displayImg != null) {
            if (arrwoIsUp)
                displayImg.setImageResource(R.drawable.arrow_up_b);
            else
                displayImg.setImageResource(R.drawable.arrow_dn_b);
        }
        if (floorDescription != null)
            floorDescription.setTextColor(ContextCompat.getColor(mContext, R.color.black));
    }

    public void clearTextColor() {
        if (displayFloor != null)
            displayFloor.setTextColor(ContextCompat.getColor(mContext, R.color.white));

        if (displayImg != null) {
            if (arrwoIsUp)
                displayImg.setImageResource(R.drawable.arrow_up_w);
            else
                displayImg.setImageResource(R.drawable.arrow_dn_w);
        }
        if (floorDescription != null)
            floorDescription.setTextColor(ContextCompat.getColor(mContext, R.color.white));
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
