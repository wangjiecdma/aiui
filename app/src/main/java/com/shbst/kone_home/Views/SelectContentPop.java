package com.shbst.kone_home.Views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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


public class SelectContentPop extends PopupWindow {
    //    private RadioButton radioText;
//    private RadioButton radioTime;
//    private Context context;
    private RadioGroup radioGroup;
    private View ppwView;
    private FloorItemBean itemBean;

    public SelectContentPop(Context context, FloorItemBean itemBean) {
        this.itemBean = itemBean;
        ppwView = LayoutInflater.from(context).inflate(R.layout.set_background_layout, null);

        ImageView ivBgView = ppwView.findViewById(R.id.ivBgViewf);


        ivBgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        // 窗体添加布局
        this.setContentView(ppwView);
        // 窗体高铺满
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 窗体高自适应
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        // 弹出窗体可点击
        this.setFocusable(true);
//          弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_set_content);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        init(context);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = ppwView.findViewById(radioGroup.getCheckedRadioButtonId());
                String text = radioButton.getText().toString();
                Log.i("onCheckedChanged: floor:" + SelectContentPop.this.itemBean.getDisplayFloor() + "-name:-" + text);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_white:
                        setDisplayMode(0);
                        break;
                    case R.id.radio_text:
                        setDisplayMode(1);
                        break;
                    case R.id.radio_time:
                        setDisplayMode(2);
                        break;
                }
                dismiss();
            }

        });
    }

    /**
     * 0 显示画板
     * 1 显示文字
     * 2 显示时钟
     *
     * @param mode 显示模式
     */
    private void setDisplayMode(int mode) {
        Log.v("set display mode,mode:" + mode);
        itemBean.setDisplaySketchpadEnable(mode == 0);
        itemBean.setDisplayTextEnable(mode == 1);
        itemBean.setDisplayClockEnable(mode == 2);
    }

    private void init(Context context) {
        FontTextView floorText = ppwView.findViewById(R.id.floor_text);
//        RadioButton radioWhite = ppwView.findViewById(R.id.radio_white);
//        RadioButton  radioText = ppwView.findViewById(R.id.radio_text);
//        RadioButton  radioTime = ppwView.findViewById(R.id.radio_time);
        radioGroup = ppwView.findViewById(R.id.set_mode_radio);
//        LogUtils.e("选择楼层：" + floor);
        //floorText.setText(itemBean.getDisplayFloor());
        String format =   context.getString(R.string.select_media_content_for_floor);
        String str = String.format(format,itemBean.getDisplayFloor());
        floorText.setText(str);
    }
}
