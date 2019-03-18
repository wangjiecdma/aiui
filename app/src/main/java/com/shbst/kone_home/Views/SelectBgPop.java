package com.shbst.kone_home.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Log;


public class SelectBgPop extends PopupWindow {
    private Context context;
    private RadioGroup colorGroup, imageGroup;
    private RadioButton rb;
    private View ppwView;
    private FloorItemBean itemBean;

    public SelectBgPop(Context context, FloorItemBean itemBean) {
        this.context = context;
        this.itemBean = itemBean;
        ppwView = LayoutInflater.from(context).inflate(R.layout.set_floor_background_layout, null);
        FontTextView floorText = ppwView.findViewById(R.id.floor_text);
        colorGroup = ppwView.findViewById(R.id.set_color_radio);
        imageGroup = ppwView.findViewById(R.id.set_image_radio);
        ImageView ivBgView = ppwView.findViewById(R.id.ivBgViewf);
        String format = context.getString(R.string.select_background_content_for_floor);
        String str = String.format(format,itemBean.getDisplayFloor());
        floorText.setText(str);

        ivBgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // 窗体添加布局
        this.setContentView(ppwView);
        // 窗体宽铺满
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 窗体高自适应
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        // 弹出窗体可点击
        this.setFocusable(true);
//          弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_set_background);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 弹出窗体的背景
//        this.setBackgroundDrawable(dw);


        colorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectCount = -1;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_green:
                        selectCount = 0;
                        break;
                    case R.id.radio_red:
                        selectCount = 1;
                        break;
                    case R.id.radio_black:
                        selectCount = 2;
                        break;
                    case R.id.radio_white:
                        selectCount = 3;
                        break;
                    case R.id.radio_blue:
                        selectCount = 4;
                        break;
                    case R.id.radio_yellow:
                        selectCount = 5;
                        break;
                    case R.id.radio_kone_red:
                        selectCount = 6;
                        break;
                    case R.id.radio_gray:
                        selectCount = 7;
                        break;
                    case R.id.radio_gray_dark:
                        selectCount = 8;
                        break;
                }
                setDisplayColor(selectCount);
                dismiss();
            }
        });
        imageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectCount = -1;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.image_button_dragon:
                        selectCount = 0;
                        break;
                    case R.id.image_button_branch:
                        selectCount = 1;
                        break;
                    case R.id.image_button_bamboo:
                        selectCount = 2;
                        break;
                    case R.id.image_button_light:
                        selectCount = 3;
                        break;
                    case R.id.image_button_water:
                        selectCount = 4;
                        break;
                }
                setDisplayImg(selectCount);
                dismiss();
            }
        });
    }

    private void setDisplayColor(int mode) {
        Log.v("set display color,mode:" + mode);
        itemBean.setDisplayColorEnable(true);
        itemBean.setDisplayImageEnable(false);
        itemBean.setDisplayColorMode(mode);
    }

    private void setDisplayImg(int mode) {
        itemBean.setDisplayColorEnable(false);
        itemBean.setDisplayImageEnable(true);
        itemBean.setDisplayBgImgMode(mode);
    }
}
