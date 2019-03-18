package com.shbst.kone_home.Views.GuidePage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.GifView;

/**
 * Created by zhouwenchao on 2018-03-12.
 */

public class GuidePagePass extends RelativeLayout {
    private Context mContext;
    RelativeLayout key1, key2, key3, key4, key5, key6, key7,
            key8, key9, key0, keyCancel, keyEnter;
    ImageView keyImage1, keyImage2, keyImage3, keyImage4, keyImage5,
            keyImage6, keyImage7, keyImage8, keyImage9, keyImage0,
            keyEnterImage, keyCancelImage, keyCancelContent, keyEnterContent;
    TextView keyText0, keyText1, keyText2, keyText3, keyText4, keyText5,
            keyText6, keyText7, keyText8, keyText9;

    EditText inputETView;

    private PageLisenter mLisenter;

    public GuidePagePass(Context context) {
        super(context);
        InitView(context);
    }

    public GuidePagePass(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
    }

    public GuidePagePass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }

    public void setEnterListener(PageLisenter listener) {
        this.mLisenter = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitView(Context context) {
        this.mContext = context;
        LoadLayout();
        inputETView = this.findViewById(R.id.inputETView);

        key1 = this.findViewById(R.id.key1);
        key2 = this.findViewById(R.id.key2);
        key3 = this.findViewById(R.id.key3);
        key4 = this.findViewById(R.id.key4);
        key5 = this.findViewById(R.id.key5);
        key6 = this.findViewById(R.id.key6);
        key7 = this.findViewById(R.id.key7);
        key8 = this.findViewById(R.id.key8);
        key9 = this.findViewById(R.id.key9);
        key0 = this.findViewById(R.id.key0);
        keyCancel = this.findViewById(R.id.key_cancel);
        keyEnter = this.findViewById(R.id.key_enter);

        keyImage1 = this.findViewById(R.id.key_image1);
        keyImage2 = this.findViewById(R.id.key_image2);
        keyImage3 = this.findViewById(R.id.key_image3);
        keyImage4 = this.findViewById(R.id.key_image4);
        keyImage5 = this.findViewById(R.id.key_image5);
        keyImage6 = this.findViewById(R.id.key_image6);
        keyImage7 = this.findViewById(R.id.key_image7);
        keyImage8 = this.findViewById(R.id.key_image8);
        keyImage9 = this.findViewById(R.id.key_image9);
        keyImage0 = this.findViewById(R.id.key_image0);
        keyEnterImage = this.findViewById(R.id.key_enter_image);
        keyCancelImage = this.findViewById(R.id.key_cancel_image);
        keyCancelContent = this.findViewById(R.id.key_cancel_content);
        keyEnterContent = this.findViewById(R.id.key_enter_content);

        keyText0 = this.findViewById(R.id.key_text0);
        keyText1 = this.findViewById(R.id.key_text1);
        keyText2 = this.findViewById(R.id.key_text2);
        keyText3 = this.findViewById(R.id.key_text3);
        keyText4 = this.findViewById(R.id.key_text4);
        keyText5 = this.findViewById(R.id.key_text5);
        keyText6 = this.findViewById(R.id.key_text6);
        keyText7 = this.findViewById(R.id.key_text7);
        keyText8 = this.findViewById(R.id.key_text8);
        keyText9 = this.findViewById(R.id.key_text9);
        key1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage1.setBackgroundResource(R.drawable.whit_round);
                        keyText1.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage1.setBackgroundResource(R.drawable.un_round);
                        keyText1.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("1");
                        break;
                }
                return true;
            }
        });
        key2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage2.setBackgroundResource(R.drawable.whit_round);
                        keyText2.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage2.setBackgroundResource(R.drawable.un_round);
                        keyText2.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("2");
                        break;
                }
                return true;
            }
        });
        key3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage3.setBackgroundResource(R.drawable.whit_round);
                        keyText3.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage3.setBackgroundResource(R.drawable.un_round);
                        keyText3.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("3");
                        break;
                }
                return true;
            }
        });
        key4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage4.setBackgroundResource(R.drawable.whit_round);
                        keyText4.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage4.setBackgroundResource(R.drawable.un_round);
                        keyText4.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("4");
                        break;
                }
                return true;
            }
        });
        key5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage5.setBackgroundResource(R.drawable.whit_round);
                        keyText5.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage5.setBackgroundResource(R.drawable.un_round);
                        keyText5.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("5");
                        break;
                }
                return true;
            }
        });
        key6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage6.setBackgroundResource(R.drawable.whit_round);
                        keyText6.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage6.setBackgroundResource(R.drawable.un_round);
                        keyText6.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("6");
                        break;
                }
                return true;
            }
        });
        key7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage7.setBackgroundResource(R.drawable.whit_round);
                        keyText7.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage7.setBackgroundResource(R.drawable.un_round);
                        keyText7.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("7");
                        break;
                }
                return true;
            }
        });
        key8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage8.setBackgroundResource(R.drawable.whit_round);
                        keyText8.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage8.setBackgroundResource(R.drawable.un_round);
                        keyText8.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("8");
                        break;
                }
                return true;
            }
        });
        key9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage9.setBackgroundResource(R.drawable.whit_round);
                        keyText9.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage9.setBackgroundResource(R.drawable.un_round);
                        keyText9.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("9");
                        break;
                }
                return true;
            }
        });
        keyCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyCancelImage.setBackgroundResource(R.drawable.whit_round);
                        keyCancelContent.setBackgroundResource(R.drawable.key_sel_cancel);
                        break;
                    case MotionEvent.ACTION_UP:
                        keyCancelImage.setBackgroundResource(R.drawable.un_round);
                        keyCancelContent.setBackgroundResource(R.drawable.key_un_cancel);
                        inputViewTextCancel();
                        break;
                }
                return true;
            }
        });
        key0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyImage0.setBackgroundResource(R.drawable.whit_round);
                        keyText0.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage0.setBackgroundResource(R.drawable.un_round);
                        keyText0.setTextColor(Color.parseColor("#ffffff"));
                        inputViewTextInput("0");
                        break;
                }
                return true;
            }
        });

        keyEnter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        keyEnterImage.setBackgroundResource(R.drawable.whit_round);
                        keyEnterContent.setBackgroundResource(R.drawable.key_sel_enter);
                        break;
                    case MotionEvent.ACTION_UP:
                        keyEnterImage.setBackgroundResource(R.drawable.un_round);
                        keyEnterContent.setBackgroundResource(R.drawable.key_un_enter);
                        inputViewTextEnter();
                        break;
                }
                return true;
            }
        });
    }

    private void LoadLayout() {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        this.invalidate();
        View view = LayoutInflater.from(mContext).inflate(R.layout.guide_pass_page
                , null);

        this.addView(view);

        GifView gifView = this.findViewById(R.id.pagePassGif);
        gifView.setGifResource(R.drawable.page_setting_gif);
    }

    @SuppressLint("SetTextI18n")
    private void inputViewTextInput(String input) {
        inputETView.setText(inputETView.getText().toString() + input);
    }

    private void inputViewTextCancel() {
        String value = inputETView.getText().toString();
        if (value.length() >= 1) {
            inputETView.setText(value.substring(0, value.length() - 1));
        }
    }

    private void inputViewTextEnter() {
        String value = inputETView.getText().toString();
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        PreferManager.getInstence().setPassword(this.getContext(), value);
        if (mLisenter != null) {
            mLisenter.passwordEnter();
        }
    }


}
