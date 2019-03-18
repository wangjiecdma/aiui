package com.shbst.kone_home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;

import com.shbst.kone_home.Utils.LayoutUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhouwenchao on 2018-02-26.
 */

public class BaseActivity extends AppCompatActivity {


    public static boolean  mTestMode = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeAppLanguage();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        changeAppLanguage();
    }


    @Override
    public void setContentView(View view) {
        LayoutUtil.setFullScreenAndHidMenu(this);
        super.setContentView(view);
        InitView();
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutUtil.setFullScreenAndHidMenu(this);
        super.setContentView(layoutResID);
        InitView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        LayoutUtil.setFullScreenAndHidMenu(this);
        super.setContentView(view, params);
        InitView();
    }


    protected void InitView() {
        throw new RuntimeException("not override InitView method");
    }

    public void startSettingActivity(){
        throw new RuntimeException("not override InitView method");
    }

    public void changeAppLanguage() {
//        eventBus.register(BaseAc.this);
        String sta = PreferManager.getInstence().getLanguageMode(this);
        Log.i("set app language:" + sta);
        if (sta != null && !"".equals(sta)) {
            // 本地语言设置
            Locale myLocale = new Locale(sta);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }

    public void changeSystemLanguage(){
        Log.d("logtest","in changeSystemLanguage");

        String sta = PreferManager.getInstence().getLanguageMode(this);
        if(sta != null && !"".equals(sta)){
            Log.d("logtest","changeSystemLanguage for  :"+sta);

            Locale myLocal = null;
            boolean isPinbin = true;
            if (sta.equals("en")){
                myLocal = Locale.ENGLISH;

           }else if(sta.equals("zh_CN")){
                myLocal = Locale.SIMPLIFIED_CHINESE;
            }else {
                myLocal = new Locale("zh","HK");
                isPinbin = false;
            }
            Log.d("logtest","local :"+myLocal.getLanguage() + " , "+myLocal.getCountry());

            PreferManager.setLanguage(myLocal);
            setInputManagerPinyin(isPinbin);

        }

    }

    public void setInputManagerPinyin(boolean isPinyin ){
        try {
            Process ime;
            String cmd = "/system/bin/ime";
            if (isPinyin){
                cmd += " set com.android.inputmethod.pinyin/.PinyinIME";
            }else{
                cmd += " set com.googlecode.tcime/.ZhuyinIME";
            }

            ime = Runtime.getRuntime().exec(cmd);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("logtest","ime cmd exe error "+e);
        }




    }

/*
    public void setInputManagerPinyin( boolean isPinbin,Locale locale){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        List<InputMethodInfo> list =  imm.getInputMethodList();



        for (InputMethodInfo info : list){
            Log.d("logtest","input method :"+info.getId()+" , "+info.getServiceName()+" , "+info.getPackageName() );
            Log.d("logtest"," input method subtypes :"+info.getSubtypeCount());
            if (isPinbin){
                if ("com.android.inputmethod.pinyin.PinyinIME".equals(info.getServiceName())){
                    Log.d("logtest","set inputmethod pinyin");
                    imm.setInputMethod(null,info.getId());
                }
            }else{
                if ("com.googlecode.tcime.ZhuyinIME".equals(info.getServiceName())){
                    imm.setInputMethod(null,info.getId());
                    imm.setAdditionalInputMethodSubtypes();
                }
            }
        }

        Log.d("logtest","get default input method :"+Settings.Secure.getString(getContentResolver(),Settings.Secure.DEFAULT_INPUT_METHOD));
    }
    */
}
