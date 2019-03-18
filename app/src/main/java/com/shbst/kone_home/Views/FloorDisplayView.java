package com.shbst.kone_home.Views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.ArithUtil;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

/**
 * Created by zhouwenchao on 2018-03-02.
 */

public class FloorDisplayView extends RelativeLayout {
    private Context mContext;
    private FontTextView currentFloor;
    private ImageView runImg;

    public FloorDisplayView(Context context, int viewHeight) {
        super(context);
        InitView(context, viewHeight);
    }

    public FloorDisplayView(Context context, AttributeSet attrs, int viewHeight) {
        super(context, attrs);
        InitView(context, viewHeight);
    }

    public FloorDisplayView(Context context, AttributeSet attrs, int defStyleAttr, int viewHeight) {
        super(context, attrs, defStyleAttr);
        InitView(context, viewHeight);
    }

    private void InitView(Context context, int viewHeight) {
        this.mContext = context;

        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_BLOCKS) {
            LoadThemeBlockLayout(viewHeight);
            return;
        }
        if (PreferManager.getInstence().getThemeMode(context) == Constant.THEME_SPHERE) {
            LoadThemeShpereLayout(viewHeight);
            return;
        }

        throw new Resources.NotFoundException("未找到的主题类型");
    }

    private void LoadThemeBlockLayout(int viewHeight) {

        LinearLayout.LayoutParams layoutParamas = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT
                , viewHeight);

        //this.setLayoutParams(layoutParamas);

        //当前楼层控件
        RelativeLayout.LayoutParams currentFloorParamas = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        currentFloorParamas.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        currentFloorParamas.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        currentFloorParamas.rightMargin = 140;
        currentFloor = new FontTextView(mContext);
        currentFloor.setTextSize(viewHeight * 0.8f);
        currentFloor.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//        currentFloor.setText("1");
        setCurrentFloor(1);
       // this.addView(currentFloor, currentFloorParamas);

        //楼层上下行图片
//        RelativeLayout.LayoutParams runImgParams = new RelativeLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
//        );

        RelativeLayout.LayoutParams runImgParams = new RelativeLayout.LayoutParams(
                (int) Math.round(ArithUtil.div(viewHeight, 1.5))
                , (int) Math.round(ArithUtil.div(viewHeight, 1.5))
        );
        runImgParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        runImg = new ImageView(mContext);
        setRunUpImg();
        setRunImgDismiss();

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;


        LinearLayout.LayoutParams runImgParams2 = new LinearLayout.LayoutParams(
                (int) Math.round(ArithUtil.div(viewHeight, 1.5))
                , (int) Math.round(ArithUtil.div(viewHeight, 1.5))
        );
        layout.addView(runImg);
        layout.addView(currentFloor,textParams);
        layout.setPadding(50,0,0,0);
        this.addView(layout, layoutParamas);


        //setPadding(0,0,50,0);




    }

    private void LoadThemeShpereLayout(int vOrh) {
        RelativeLayout.LayoutParams layoutParamas = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT);
        layoutParamas.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParamas.leftMargin = LayoutUtil.getDPIFromDP(mContext, 10);
        this.setLayoutParams(layoutParamas);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(linearLayout);

        //楼层上下行图片
        LinearLayout.LayoutParams runImgParams = new LinearLayout.LayoutParams(
                vOrh * 2, vOrh * 2
        );
        runImgParams.gravity = Gravity.CENTER_HORIZONTAL;
        runImg = new ImageView(mContext);
        setRunUpImg();
        linearLayout.addView(runImg, runImgParams);

        //当前楼层控件
        LinearLayout.LayoutParams currentFloorParamas = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        currentFloorParamas.gravity = Gravity.CENTER_HORIZONTAL;
        currentFloorParamas.bottomMargin = LayoutUtil.getDPIFromDP(mContext, 135);
        currentFloor = new FontTextView(mContext);
        currentFloor.setTextSize(vOrh * 2);
        currentFloor.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//        currentFloor.setText("1");
        setCurrentFloor(1);
        linearLayout.addView(currentFloor, currentFloorParamas);
    }

    public void setCurrentFloor(int currentFloor) {
        Log.v("setCurrentFloor,floor:" + currentFloor);
        FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(this.getContext()
                , currentFloor);

        this.currentFloor.setText(itemBean.getDisplayFloor());
    }

    public void setRunUpImg() {
        runImg.setVisibility(View.VISIBLE);
        runImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.arrow_up_w));
    }

    public void setRunDnImg() {
        runImg.setVisibility(View.VISIBLE);
        runImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.arrow_dn_w));
    }

    public void setRunImgDismiss() {
        runImg.setVisibility(View.INVISIBLE);
    }
}
