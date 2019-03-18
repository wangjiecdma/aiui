package com.shbst.kone_home.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.InterceptStrUtil;
import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

import static com.shbst.kone_home.Utils.Constant.DISPLAY_MAX_LINES;

/**
 * Created by zhouwenchao on 2018-02-26.
 */

public class SettingItemView extends LinearLayout {
    private TextView displayFloor;
    private EditText floorDescription;
    private ImageView contentView, mediaBgView;
    private TextInputView fontEditView;

    private FloorItemBean mItemBean;
    private Context mContext;

    public SettingItemView(Context context, FloorItemBean itemBean) {
        super(context);
        InitView(context, itemBean);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, FloorItemBean itemBean) {
        super(context, attrs);
        InitView(context, itemBean);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, FloorItemBean itemBean) {
        super(context, attrs, defStyleAttr);
        InitView(context, itemBean);
    }

    @SuppressLint("SetTextI18n")
    private void InitView(Context context, FloorItemBean itemBean) {
        this.mContext = context;
        this.mItemBean = itemBean;
        this.removeAllViews();
        this.clearView();

        LinearLayout lineLinearLayout = new LinearLayout(context);
        lineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams linearLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
                context.getResources().getInteger(R.integer.setting_floor_item_height));
        linearLayout.setMargins(0, context.getResources().getInteger(R.integer.setting_floor_item_margintop)
                , 0, 0);
        linearLayout.gravity = Gravity.CENTER_VERTICAL;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(layoutParams);

        //创建控件和对应布局
        displayFloor = new FontTextView(context);
        LayoutParams displayFloorlayout = new LayoutParams(60, LayoutParams.MATCH_PARENT);
        displayFloor.setGravity(Gravity.CENTER_VERTICAL);
        displayFloor.setTextColor(ContextCompat.getColor(context, R.color.settingFloorItemColor));
        displayFloor.setTextSize(60);
        displayFloor.setText(itemBean.getDisplayFloor());

        floorDescription = new FontEditView(context);
        LayoutParams floorDescriptionlayout = new LayoutParams(LayoutUtil.getDPIFromDP(context, 300), LayoutParams.WRAP_CONTENT);
        floorDescriptionlayout.leftMargin = 30;
        floorDescriptionlayout.gravity = Gravity.CENTER;
        floorDescription.setBackground(ContextCompat.getDrawable(context, R.drawable.square));
        floorDescription.setTextColor(ContextCompat.getColor(context, R.color.settingFloorItemColor));
        floorDescription.setTextSize(30);
        //将输入法置为 down
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
                mItemBean.setFloorDescription(value);
//                    InitView(mContext, mItemBean);
            }
        });

        contentView = new ImageView(context);
        LayoutParams contentViewlayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        contentViewlayout.leftMargin = LayoutUtil.getDPIFromDP(context, 45);
        contentView.setBackground(ContextCompat.getDrawable(context, R.drawable.round));
        if (itemBean.isDisplaySketchpadEnable()) {
            contentView.setImageResource(R.drawable.set_white);
        }
        LayoutParams fontEditLayout = null;
        if (itemBean.isDisplayTextEnable()) {
            contentView.setImageResource(R.drawable.set_text);

//            fontEditView = new FontEditView(context);
            fontEditView = new  TextInputView(context,itemBean);
            fontEditLayout = new LayoutParams(LayoutUtil.getDPIFromDP(context, 280)
                    , LayoutUtil.getDPIFromDP(context, 100));
            fontEditLayout.leftMargin = LayoutUtil.getDPIFromDP(context, 417);
            fontEditLayout.topMargin = LayoutUtil.getDPIFromDP(context, 20);
            fontEditView.setBackground(ContextCompat.getDrawable(context, R.drawable.square));
            fontEditView.setTextColor(ContextCompat.getColor(context, R.color.white));
            //将输入法置为 down
            fontEditView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            fontEditView.setMaxLines(DISPLAY_MAX_LINES); //只允许输入6行
            fontEditView.setLines(DISPLAY_MAX_LINES);
            fontEditView.setText(itemBean.getDisplayText());
            fontEditView.setTextSize(13);
            //fontEditView.setText("hello test");
//            fontEditView.addTextChangedListener(new TextWatcher() {
//                String value;
//
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    Log.v("beforeTextChanged");
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    //限制输入字符，只允许同时存在一个换行字符，最长输入 DISPLAY_MAX_LINES 行
//                    String inputStr = fontEditView.getText().toString();
//                    Log.v("onTextChanged :" + inputStr + " length:" + inputStr.length());
//                    if (inputStr.length() > 170) {
//                        inputStr = inputStr.substring(0, 170);
//                        fontEditView.setText(inputStr);
//                        return;
//                    }
//
//                    String tmpInputStr = inputStr.replace("\n\n", "\n");
//                    boolean replace = false;
//                    if (!tmpInputStr.equals(inputStr)) {
//                        replace = true;
//                        inputStr = tmpInputStr;
//                    }
//                    String[] textList = inputStr.split("\\\n");
//                    Log.v("textList:" + Log.get(textList));
//
//
////                    boolean replaceLine = false;
////                    StringBuilder appendStrBuidler = new StringBuilder();
////                    int lineLength;
////                    for (int i3 = 0; i3 < textList.length
////                            && i3 < DISPLAY_MAX_LINES; i3++) {
////                        if (i3 == 0) {
////                            lineLength = 12;
////                        } else {
////                            lineLength = 24;
////                        }
////                        if (textList[i3].getBytes().length > lineLength) {
////
////                            replaceLine = true;
//////                            String line1 = textList[i3].substring(0, lineLength);
//////                            String line2 = textList[i3].substring(lineLength, textList[i3].length());
////
////                            String line1 = InterceptStrUtil.interceptStr(textList[i3], lineLength);
////
////                            while (line1.getBytes().length > lineLength) {
////                                line1 = InterceptStrUtil.interceptStr(textList[i3], --lineLength);
////                            }
////
//////                            String line2 = textList[i3].substring(lineLength, textList[i3].length());
////                            Log.v("line1 length:" + line1.getBytes().length);
////                            for (int i4 = 0; i4 < i3
////                                    && i4 < DISPLAY_MAX_LINES; i4++) {
////                                appendStrBuidler.append(textList[i4]);
////                                if (i4 != textList.length - 1) {
////                                    appendStrBuidler.append("\n");
////                                }
////                            }
////
////                            appendStrBuidler.append(line1);
////                            appendStrBuidler.append("\n");
//////                            appendStrBuidler.append(line2);
//////                            appendStrBuidler.append("\n");
////
////                            for (int i4 = i3 + 1; i4 < textList.length
////                                    && i4 < DISPLAY_MAX_LINES; i4++) {
////                                appendStrBuidler.append(textList[i4]);
////                                if (i4 != textList.length - 1) {
////                                    appendStrBuidler.append("\n");
////                                }
////                            }
////                        }
////                    }
////                    if (replaceLine) {
////                        value = appendStrBuidler.toString();
////                        Log.v("value:" + value);
////                        fontEditView.setText(value);
////                        return;
////                    }
//
//
//                    if (textList.length > DISPLAY_MAX_LINES) {
////                        Log.w("textlist 分割长度大于DISPLAY_MAX_LINES:"+DISPLAY_MAX_LINES);
//                        StringBuilder tmpTextBuilder = new StringBuilder();
//                        for (int i3 = 0; i3 < DISPLAY_MAX_LINES; i3++) {
//                            tmpTextBuilder.append(textList[i3]);
//                            if (i3 != DISPLAY_MAX_LINES - 1) {
//                                tmpTextBuilder.append("\n");
//                            }
//                        }
//                        value = tmpTextBuilder.toString();
//                        fontEditView.setText(value);
//                        fontEditView.setSelection(fontEditView.getText().length());
//                    } else {
//
//                        value = inputStr;
//                        if (replace) {
//                            fontEditView.setText(value);
//                            fontEditView.setSelection(fontEditView.getText().length());
//                        }
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    Log.v("afterTextChanged");
//                    mItemBean.setDisplayText(value);
////                    InitView(mContext, mItemBean);
//                }
//            });

        }
        if (itemBean.isDisplayClockEnable()) {
            contentView.setImageResource(R.drawable.set_time);
        }
        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowPopuWindow(contentView);
            }
        });

        mediaBgView = new ImageView(context);
        LayoutParams mediaBgViewlayout = new LayoutParams(LayoutUtil.getDPIFromDP(context,90), LayoutUtil.getDPIFromDP(context,90));
        mediaBgViewlayout.leftMargin = LayoutUtil.getDPIFromDP(context, 50);
        if (itemBean.isDisplayImageEnable())
            mediaBgView.setBackgroundResource(PreferManager.getInstence()
                    .getBgImgSetFromMode(itemBean.getDisplayBgImgMode()));
        if (itemBean.isDisplayColorEnable()) {
            mediaBgView.setImageResource(
                    PreferManager.getInstence()
                            .getColorResFromMode(itemBean.getDisplayColorMode()));
        }
        mediaBgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowBackWindow(view);
            }
        });

        //
        RelativeLayout infoLinearView = new RelativeLayout(context);
//        infoLinearView.setOrientation(LinearLayout.HORIZONTAL);
        infoLinearView.setPadding(LayoutUtil.getDPIFromDP(context, 418)
                , LayoutUtil.getDPIFromDP(context, 20), LayoutUtil.getDPIFromDP(context, 215), 0);
        LinearLayout.LayoutParams infoLinearParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );

        FontTextView featuresTVView = new FontTextView(context);
        RelativeLayout.LayoutParams featuresTVParams = new RelativeLayout.LayoutParams(
                LayoutUtil.getDPIFromDP(context, 80), RelativeLayout.LayoutParams.WRAP_CONTENT
        );
//        featuresTVParams.leftMargin = LayoutUtil.getDPIFromDP(context, 20);
//        featuresTVParams.topMargin = LayoutUtil.getDPIFromDP(context, 20);
        featuresTVView.setGravity(Gravity.CENTER);
        featuresTVView.setTextColor(ContextCompat.getColor(context, R.color.white));
//        featuresTVView.setText("test view");
        if (itemBean.isDisplaySketchpadEnable()) {
            featuresTVView.setText(getResources().getString(R.string.whiteboard));
        }
        if (itemBean.isDisplayTextEnable()) {
            featuresTVView.setText(getResources().getString(R.string.text));
        }
        if (itemBean.isDisplayClockEnable()) {
            featuresTVView.setText(getResources().getString(R.string.time));
        }

        FontTextView bgInfoTVView = new FontTextView(context);
        bgInfoTVView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams bgInfoTVParams = new RelativeLayout.LayoutParams(
                LayoutUtil.getDPIFromDP(context, 150), RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        bgInfoTVParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        bgInfoTVView.setTextColor(ContextCompat.getColor(context, R.color.white));

        if (itemBean.isDisplayImageEnable()) {
            bgInfoTVView.setText(getResources().getString(R.string.image)+":" + PreferManager.getInstence().getBgImgStrFromMode(getContext(),
                    itemBean.getDisplayBgImgMode()
            ));
        }
        if (itemBean.isDisplayColorEnable()) {
            bgInfoTVView.setText(getResources().getString(R.string.color)+":" + PreferManager.getInstence().getColorStrFormMode(getContext(),
                    itemBean.getDisplayColorMode()
            ));
        }

        infoLinearView.addView(featuresTVView, featuresTVParams);
        infoLinearView.addView(bgInfoTVView, bgInfoTVParams);

        //添加控件
        lineLinearLayout.addView(displayFloor, displayFloorlayout);
        lineLinearLayout.addView(floorDescription, floorDescriptionlayout);
        lineLinearLayout.addView(contentView, contentViewlayout);
        lineLinearLayout.addView(mediaBgView, mediaBgViewlayout);
        this.addView(lineLinearLayout, linearLayout);
        this.addView(infoLinearView, infoLinearParams);
        if (fontEditView != null)
            this.addView(fontEditView, fontEditLayout);
    }

    public void setShowPopuWindow(View view) {
        SelectContentPop mySetPopu = new SelectContentPop(view.getContext(), mItemBean);
        mySetPopu.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, 0, -260);
//        setBoard.setVisibility(View.VISIBLE);
        mySetPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                InitView(mContext, mItemBean);
//                setBoard.setVisibility(View.GONE);
            }
        });
    }

    public void setShowBackWindow(View view) {

        SelectBgPop myBackgroundPopu = new SelectBgPop(view.getContext(), mItemBean);
        myBackgroundPopu.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, 0, -260);
//        setBoard.setVisibility(View.VISIBLE);
        myBackgroundPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                InitView(mContext, mItemBean);
//                setBoard.setVisibility(View.GONE);
            }
        });
    }

    private void clearView() {
        displayFloor = null;
        displayFloor = null;
        floorDescription = null;
        contentView = null;
        mediaBgView = null;
        fontEditView = null;
    }


    /**
     * 在退出设置页面，销毁该view时，保存控件状态
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v("detached from window,save view state to preference" +
                ",Physical floor:" + mItemBean.getPhysicalFloor());
        mContext = null;
        mItemBean = null;
    }

    public void freshItemBeam() {
        PreferManager.getInstence().putFloorItemBean(mContext, mItemBean);
    }
}
