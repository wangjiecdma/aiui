package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.FontEditView;
import com.shbst.kone_home.Views.FontTextView;
import com.shbst.kone_home.Views.GifView;

/**
 * Created by zhouwenchao on 2018-02-27.
 */

public class GuidePageFloor extends RelativeLayout {

    public GuidePageFloor(Context context) {
        super(context);
        InitLayout(context);
    }

    public GuidePageFloor(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public GuidePageFloor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    private void InitLayout(final Context context) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        this.invalidate();
        View view = LayoutInflater.from(context).inflate(R.layout.guide_two_page
                , null);

        LinearLayout floorItemLayout = view.findViewById(R.id.floorItemLayout);
        //start

        for (int i = PreferManager.getInstence().getFloorCount(); i >= 1; i--) {
            final FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(context, i);

            LinearLayout lineLinearLayout = new LinearLayout(context);
            lineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    context.getResources().getInteger(R.integer.setting_floor_item_height));
            linearLayout.setMargins(0, context.getResources().getInteger(R.integer.setting_floor_item_margintop)
                    , 0, 0);
            linearLayout.gravity = Gravity.CENTER_VERTICAL;

            FontTextView displayFloor = new FontTextView(context);
            LinearLayout.LayoutParams displayFloorlayout = new LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.MATCH_PARENT);
            displayFloor.setGravity(Gravity.CENTER_VERTICAL);
            displayFloor.setTextColor(ContextCompat.getColor(context, R.color.settingFloorItemColor));
            displayFloor.setTextSize(60);
            displayFloor.setText(itemBean.getDisplayFloor());

            final FontEditView floorDescription = new FontEditView(context);
            LinearLayout.LayoutParams floorDescriptionlayout = new LinearLayout.LayoutParams(LayoutUtil.getDPIFromDP(context, 240), LinearLayout.LayoutParams.WRAP_CONTENT);
            floorDescriptionlayout.leftMargin = 30;
            floorDescriptionlayout.gravity = Gravity.CENTER;
            floorDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.square));
            floorDescription.setTextColor(ContextCompat.getColor(context, R.color.settingFloorItemColor));
            floorDescription.setTextSize(30);
            floorDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);



            floorDescription.setText(itemBean.getFloorDescription());
            floorDescription.addTextChangedListener(new TextWatcher() {
                String value;

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.v("beforeTextChanged");
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.v("beforeTextChanged :" + floorDescription.getText());
                    value = floorDescription.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.v("beforeTextChanged");
                    itemBean.setFloorDescription(value);
                    PreferManager.getInstence().putFloorItemBean(context, itemBean);
//                    InitView(mContext, mItemBean);
                }
            });

            lineLinearLayout.addView(displayFloor, displayFloorlayout);
            lineLinearLayout.addView(floorDescription, floorDescriptionlayout);

            floorItemLayout.addView(lineLinearLayout, linearLayout);
        }
        //end
        this.addView(view);

        //add view and init view
        InitView();
    }

    private void InitView() {
        GifView gifView = this.findViewById(R.id.pageTwoGif);
        gifView.setGifResource(R.drawable.floors_gif);
    }
}
