package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.FontEditView;
import com.shbst.kone_home.Views.FontTextView;
import com.shbst.kone_home.Views.GifView;
import com.shbst.kone_home.Views.SelectBgPop;
import com.shbst.kone_home.Views.SelectContentPop;
import com.shbst.kone_home.Views.TextInputView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouwenchao on 2018-02-27.
 */

public class GuidePageFeatures extends RelativeLayout {
    private static final String TAG = "GuidePageFour";
    private ListView set_floor_item_ll;
    Map<Integer, View> floorMap = new HashMap<>();
    Map<Integer, FontTextView> floorTextViewMap = new HashMap<>();

    Map<Integer, FontTextView> mediaTextViewMap = new HashMap<>();
    Map<Integer, TextInputView> inputTextViewMap = new HashMap<>();

    Map<Integer, FontTextView> backgroundTextViewMap = new HashMap<>();
    Map<Integer, ImageView> mediaImageViewMap = new HashMap<>();
    Map<Integer, ImageView> backGroundImageViewMap = new HashMap<>();
    Map<Integer, FloorItemBean> floorItemMap = new HashMap<>();
    List<Integer> floorCountList = new ArrayList();
    private Context mContext;
    FloorItemBean mFloorItemBean;
    private int select_FloorItem = 0;

    public GuidePageFeatures(Context context) {
        super(context);
        InitLayout(context);
    }

    public GuidePageFeatures(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public GuidePageFeatures(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        this.invalidate();
        View view = LayoutInflater.from(context).inflate(R.layout.guide_four_page
                , null);

        this.addView(view);
        //add view and init view
        int floorCount = PreferManager.getInstence().getFloorCount();

        for (int i = 0; i < floorCount; i++) {
            floorCountList.add(i);
        }

        InitView(context);

    }


    private void InitView(Context context) {
        this.mContext = context;

        GifView gifView = this.findViewById(R.id.pageFourGif);
        gifView.setGifResource(R.drawable.media_gif);
        set_floor_item_ll = this.findViewById(R.id.set_floor_item_ll);


        MyListAdapter adapter = new MyListAdapter(getContext(), R.layout.guide_four_page_floor_item);
        set_floor_item_ll.setAdapter(adapter);

    }

    public class MyListAdapter extends ArrayAdapter<Object> {
        int mTextViewResourceID = 0;
        private Context mContext;

        public MyListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            mTextViewResourceID = textViewResourceId;
            mContext = context;
        }

        public int getCount() {
            return floorCountList.size();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            FontTextView floorTextView = null;
            FontTextView mediaTextView = null;
            TextInputView inputTextView = null;
            FontTextView backgroundTextView = null;
            ImageView mediaImageView = null;
            ImageView backGroundImageView = null;

            if (convertView == null) {
                FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(getContext()
                        , (floorCountList.size() - (floorCountList.get(position))));

                convertView = LayoutInflater.from(mContext).inflate(
                        mTextViewResourceID, null);
                floorTextView = (FontTextView) convertView.findViewById(R.id.floorTextView);

                mediaImageView = convertView.findViewById(R.id.mediaImageView);

                backGroundImageView = convertView.findViewById(R.id.backgroundImageView);

                mediaImageView.setImageResource(R.drawable.set_white);

                mediaTextView = convertView.findViewById(R.id.mediaTextView);
                inputTextView = convertView.findViewById(R.id.inputTextView);
                inputTextView.setmItemBean(itemBean);
                inputTextView.setText(itemBean.getDisplayText());
                // FIXME: 2018/4/8 设置FTU 引导页面楼层选择 ITEM 输入法中的next 设置为DONE
                inputTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);

                backgroundTextView = convertView.findViewById(R.id.backgroundTextView);


                floorTextView.setY(12);
                backGroundImageView.setY(12);
                mediaImageView.setY(12);

                floorMap.put(position, convertView);
                floorTextViewMap.put(position, floorTextView);
                mediaImageViewMap.put(position, mediaImageView);
                mediaTextViewMap.put(position, mediaTextView);
                inputTextViewMap.put(position, inputTextView);

                backgroundTextViewMap.put(position, backgroundTextView);
                backGroundImageViewMap.put(position, backGroundImageView);


                floorItemMap.put(position, itemBean);
                floorTextView.setText(itemBean.getDisplayFloor());

                setShowItem(select_FloorItem);


                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select_FloorItem = position;
                        setShowItem(position);
                        Log.i(TAG, "选择的楼层是: " + (floorCountList.size() - (floorCountList.get(position))));

                    }
                });

                mediaImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        select_FloorItem = position;
                        setShowItem(position);

                        FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(getContext(),
                                (floorCountList.size() - (floorCountList.get(position))));

                        setShowPopuWindow(arg0, itemBean);

                        Log.i(TAG, "onClick: " + itemBean.toString());
                    }
                });
                backGroundImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select_FloorItem = position;
                        setShowItem(position);

                        FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(getContext(),
                                (floorCountList.size() - (floorCountList.get(position))));
                        setShowBackWindow(view, itemBean);
                        Log.i(TAG, "onClick: " + itemBean.toString());
                    }
                });

                setShowFloorTextView(mediaTextView, inputTextView, mediaImageView, itemBean);

                if (itemBean.isDisplayImageEnable())
                    backGroundImageView.setBackgroundResource(PreferManager.getInstence()
                            .getBgImgSetFromMode(itemBean.getDisplayBgImgMode()));
                if (itemBean.isDisplayColorEnable()) {
                    backGroundImageView.setImageResource(
                            PreferManager.getInstence()
                                    .getColorResFromMode(itemBean.getDisplayColorMode()));
                }
            }
            return convertView;
        }
    }

    private void setShowFloorTextView(FontTextView mediaTextView, FontEditView inputEdtView
            , ImageView mediaImageView, FloorItemBean floorItemBean) {
        mediaImageView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round));
        if (floorItemBean.isDisplaySketchpadEnable()) {
            mediaImageView.setImageResource(R.drawable.set_white);
        }
        if (floorItemBean.isDisplayTextEnable()) {
            mediaImageView.setImageResource(R.drawable.set_text);
            LayoutParams params3 = new LayoutParams(ListView.LayoutParams.WRAP_CONTENT,
                    ListView.LayoutParams.WRAP_CONTENT);
            params3.leftMargin = 140;
            params3.topMargin = 85;
            LayoutParams params4 = new LayoutParams(120,
                    50);
            params4.leftMargin = 140;
            params4.topMargin = 115;
            mediaTextView.setLayoutParams(params3);
            mediaTextView.setVisibility(VISIBLE);

            inputEdtView.setLayoutParams(params4);
            inputEdtView.setVisibility(VISIBLE);

        }
        if (floorItemBean.isDisplayClockEnable()) {
            mediaImageView.setImageResource(R.drawable.set_time);
        }
    }

    private void setShowItem(int index) {
        ListView.LayoutParams params_select = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 170);//设置宽度和高度

        ListView.LayoutParams params_no = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 60);//设置宽度和高度
        for (Integer i : floorMap.keySet()) {
            if (i == index) {

                floorMap.get(i).setLayoutParams(params_select);
                //todo color
                floorMap.get(i).setBackgroundColor(
                        getResources().getColor(R.color.guide_page_four_select_item));
                floorTextViewMap.get(i).setTextSize(68);

                LayoutParams params = new LayoutParams(68,
                        68);

                LayoutParams params2 = new LayoutParams(68,
                        68);

                LayoutParams params4 = new LayoutParams(ListView.LayoutParams.WRAP_CONTENT,
                        ListView.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 128;
                params2.leftMargin = 307;

                params4.leftMargin = 290;
                params4.topMargin = 85;
//                mediaTextViewMap.get(i).setVisibility(VISIBLE);
                backgroundTextViewMap.get(i).setVisibility(VISIBLE);


                int mode =  floorItemMap.get(i).getDisplayBgImgMode();

                String text = getContext().getString(R.string.image)+":"+PreferManager.getInstence().getBgImgStrFromMode(getContext(),mode);

                if (floorItemMap.get(i).isDisplayColorEnable()) {
                    text = getResources().getString(R.string.color)+":" + PreferManager.getInstence().getColorStrFormMode(getContext(),
                            floorItemMap.get(i).getDisplayColorMode()
                    );

                }

                backgroundTextViewMap.get(i).setText(text);


                backgroundTextViewMap.get(i).setLayoutParams(params4);

                mediaImageViewMap.get(i).setLayoutParams(params);

                backGroundImageViewMap.get(i).setLayoutParams(params2);

                setShowFloorTextView(mediaTextViewMap.get(i), inputTextViewMap.get(i)
                        , mediaImageViewMap.get(i), floorItemMap.get(i));
            } else {

                floorMap.get(i).setLayoutParams(params_no);
                floorMap.get(i).setBackgroundColor(getResources().getColor(R.color.ultraDarkGray));
                floorTextViewMap.get(i).setTextSize(38);
                LayoutParams params = new LayoutParams(40,
                        40);
                LayoutParams params2 = new LayoutParams(40,
                        40);
                params.leftMargin = 147;
                params2.leftMargin = 271;
                mediaTextViewMap.get(i).setVisibility(GONE);
                inputTextViewMap.get(i).setVisibility(GONE);
                backgroundTextViewMap.get(i).setVisibility(GONE);
                mediaImageViewMap.get(i).setLayoutParams(params);
                backGroundImageViewMap.get(i).setLayoutParams(params2);

            }
        }
    }


    public void setShowPopuWindow(final View view, final FloorItemBean mItemBean) {
        SelectContentPop mySetPopu = new SelectContentPop(view.getContext(), mItemBean);
        mySetPopu.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, 0, -260);
        mySetPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.i(mItemBean.toString());
                PreferManager.getInstence().putFloorItemBean(view.getContext(), mItemBean);
                flushItemBean();
                InitView(getContext());
            }
        });
    }

    public void setShowBackWindow(final View view, final FloorItemBean mItemBean) {
        SelectBgPop myBackgroundPopu = new SelectBgPop(view.getContext(), mItemBean);
        myBackgroundPopu.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, 0, -260);
        myBackgroundPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.i(mItemBean.toString());
                PreferManager.getInstence().putFloorItemBean(view.getContext(), mItemBean);
                flushItemBean();
                InitView(getContext());
            }
        });
    }

    public void flushItemBean() {
        for (Map.Entry<Integer, TextInputView> entry : inputTextViewMap.entrySet()) {
            //交换一下文字
            FloorItemBean oldItemBeam = entry.getValue().getmItemBean();
            FloorItemBean itemBean = PreferManager.getInstence().getFloorItemBean(mContext,
                    oldItemBeam.getPhysicalFloor());
            itemBean.setDisplayText(oldItemBeam.getDisplayText());
            PreferManager.getInstence().putFloorItemBean(mContext, itemBean);

            Log.v("保存itemBean physicalFloor:" + itemBean.getPhysicalFloor() + " text:" + itemBean.getDisplayText());
        }
    }
}
