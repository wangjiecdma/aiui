package com.shbst.kone_home.Views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Selection;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shbst.kone_home.BaseActivity;
import com.shbst.kone_home.MainActivity;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.PreferManager;

/**
 * Created by tongshile on 2017-11-17.
 */
public class PasswordCheckDialog extends PopupWindow {
    RelativeLayout key1, key2, key3, key4, key5, key6, key7,
            key8, key9, key0, keyCancel, keyEnter;
    ImageView keyImage1, keyImage2, keyImage3, keyImage4, keyImage5,
            keyImage6, keyImage7, keyImage8, keyImage9, keyImage0,
            keyEnterImage, keyCancelImage, keyCancelContent, keyEnterContent;
    TextView keyText0, keyText1, keyText2, keyText3, keyText4, keyText5,
            keyText6, keyText7, keyText8, keyText9;
    FontEditView floorPassEdit;
    View ppwView;
    ImageView imageView;
    BaseActivity context;

    public PasswordCheckDialog(BaseActivity context) {
        ppwView = LayoutInflater.from(context).inflate(R.layout.pass_dialog_layout, null);
        init(ppwView);
        this.context = context;
        // 窗体添加布局
        this.setContentView(ppwView);
        // 窗体高铺满
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 窗体高自适应
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setKeyBoard();


        // 弹出窗体可点击
        this.setFocusable(true);
//          弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        dismiss();
                        break;
                }
                return true;
            }
        });

//        ppwView.setOnTouchListener(new View.OnTouchListener() {


//            public boolean onTouch(View v, MotionEvent event) {
//
//
////                int upper = ppwView.findViewById(R.id.ll_ppw_city).getTop();
////                int height = ppwView.findViewById(R.id.ll_ppw_city).getHeight();
//
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    if (y < upper || y> (upper+height)  ) {
//                    dismiss();
////                    }
//                }
//                return true;
//            }
//        });
    }

    public void init(View view) {
        imageView = view.findViewById(R.id.pass_set_menu);
        floorPassEdit = view.findViewById(R.id.set_pass);
        floorPassEdit.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        key1 = view.findViewById(R.id.key1);
        key2 = view.findViewById(R.id.key2);
        key3 = view.findViewById(R.id.key3);
        key4 = view.findViewById(R.id.key4);
        key5 = view.findViewById(R.id.key5);
        key6 = view.findViewById(R.id.key6);
        key7 = view.findViewById(R.id.key7);
        key8 = view.findViewById(R.id.key8);
        key9 = view.findViewById(R.id.key9);
        key0 = view.findViewById(R.id.key0);
        keyCancel = view.findViewById(R.id.key_cancel);
        keyEnter = view.findViewById(R.id.key_enter);
        keyImage1 = view.findViewById(R.id.key_image1);
        keyImage2 = view.findViewById(R.id.key_image2);
        keyImage3 = view.findViewById(R.id.key_image3);
        keyImage4 = view.findViewById(R.id.key_image4);
        keyImage5 = view.findViewById(R.id.key_image5);
        keyImage6 = view.findViewById(R.id.key_image6);
        keyImage7 = view.findViewById(R.id.key_image7);
        keyImage8 = view.findViewById(R.id.key_image8);
        keyImage9 = view.findViewById(R.id.key_image9);
        keyImage0 = view.findViewById(R.id.key_image0);
        keyEnterImage = view.findViewById(R.id.key_enter_image);
        keyCancelImage = view.findViewById(R.id.key_cancel_image);
        keyCancelContent = view.findViewById(R.id.key_cancel_content);
        keyEnterContent = view.findViewById(R.id.key_enter_content);
        keyText0 = view.findViewById(R.id.key_text0);
        keyText1 = view.findViewById(R.id.key_text1);
        keyText2 = view.findViewById(R.id.key_text2);
        keyText3 = view.findViewById(R.id.key_text3);
        keyText4 = view.findViewById(R.id.key_text4);
        keyText5 = view.findViewById(R.id.key_text5);
        keyText6 = view.findViewById(R.id.key_text6);
        keyText7 = view.findViewById(R.id.key_text7);
        keyText8 = view.findViewById(R.id.key_text8);
        keyText9 = view.findViewById(R.id.key_text9);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setKeyBoard() {
        key1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage1.setBackgroundResource(R.drawable.whit_round);
                        keyText1.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        keyImage1.setBackgroundResource(R.drawable.un_round);
                        keyText1.setTextColor(Color.parseColor("#ffffff"));
                        String number1 = floorPassEdit.getText().toString() + "1";
                        floorPassEdit.setText(number1);
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage2.setBackgroundResource(R.drawable.whit_round);
                        keyText2.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number2 = floorPassEdit.getText().toString() + "2";
                        floorPassEdit.setText(number2);
                        keyImage2.setBackgroundResource(R.drawable.un_round);
                        keyText2.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage3.setBackgroundResource(R.drawable.whit_round);
                        keyText3.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number3 = floorPassEdit.getText().toString() + "3";
                        floorPassEdit.setText(number3);
                        keyImage3.setBackgroundResource(R.drawable.un_round);
                        keyText3.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage4.setBackgroundResource(R.drawable.whit_round);
                        keyText4.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number4 = floorPassEdit.getText().toString() + "4";
                        floorPassEdit.setText(number4);
                        keyImage4.setBackgroundResource(R.drawable.un_round);
                        keyText4.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage5.setBackgroundResource(R.drawable.whit_round);
                        keyText5.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number5 = floorPassEdit.getText().toString() + "5";
                        floorPassEdit.setText(number5);
                        keyImage5.setBackgroundResource(R.drawable.un_round);
                        keyText5.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage6.setBackgroundResource(R.drawable.whit_round);
                        keyText6.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number6 = floorPassEdit.getText().toString() + "6";
                        floorPassEdit.setText(number6);
                        keyImage6.setBackgroundResource(R.drawable.un_round);
                        keyText6.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage7.setBackgroundResource(R.drawable.whit_round);
                        keyText7.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number7 = floorPassEdit.getText().toString() + "7";
                        floorPassEdit.setText(number7);
                        keyImage7.setBackgroundResource(R.drawable.un_round);
                        keyText7.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage8.setBackgroundResource(R.drawable.whit_round);
                        keyText8.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number8 = floorPassEdit.getText().toString() + "8";
                        floorPassEdit.setText(number8);
                        keyImage8.setBackgroundResource(R.drawable.un_round);
                        keyText8.setTextColor(Color.parseColor("#ffffff"));
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage9.setBackgroundResource(R.drawable.whit_round);
                        keyText9.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number9 = floorPassEdit.getText().toString() + "9";
                        floorPassEdit.setText(number9);
                        keyImage9.setBackgroundResource(R.drawable.un_round);
                        keyText9.setTextColor(Color.parseColor("#ffffff"));
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
                        Editable cancelValue = floorPassEdit.getText();
                        Selection.setSelection(cancelValue, cancelValue.length());
                        //获取EditText光标所在位置
                        int index = floorPassEdit.getSelectionStart();
                        String string = floorPassEdit.getText().toString();
                        //判断输入框不为空，执行删除
                        if (!string.equals("")) {
                            floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                            floorPassEdit.getText().delete(index - 1, index);
                        }
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
                        floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                        keyImage0.setBackgroundResource(R.drawable.whit_round);
                        keyText0.setTextColor(Color.parseColor("#3b3b3b"));
                        break;
                    case MotionEvent.ACTION_UP:
                        String number0 = floorPassEdit.getText().toString() + "0";
                        floorPassEdit.setText(number0);
                        keyImage0.setBackgroundResource(R.drawable.un_round);
                        keyText0.setTextColor(Color.parseColor("#ffffff"));
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
                        String pass = PreferManager.getInstence().getPassword(context);
                        keyEnterImage.setBackgroundResource(R.drawable.un_round);
                        keyEnterContent.setBackgroundResource(R.drawable.key_un_enter);
                        if (floorPassEdit.getText().toString().equals(pass)) {
                            floorPassEdit.setTextColor(Color.parseColor("#004987"));
                            dismiss();
                            context.startSettingActivity();
                        } else {
                            floorPassEdit.setTextColor(Color.parseColor("#FDC427"));
                        }
//                        else {
//                            floorPassEdit.setTextColor(Color.parseColor("#fcc400"));
//                        }
//                        Toast.makeText(context, floorPassEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        keyCancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.e("qqqq", "onLongClick: ");
                floorPassEdit.setTextColor(Color.parseColor("#ffffff"));
                floorPassEdit.setText("");
                return true;
            }
        });
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}
