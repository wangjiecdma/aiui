package com.shbst.msetupwizard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shbst.kone_home.Adapter.GuidePageAdapter;
import com.shbst.kone_home.BaseActivity;
import com.shbst.kone_home.GuideActivity;
import com.shbst.kone_home.R;
import com.shbst.kone_home.SettingActivity;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.shbst.kone_home.Utils.Constant.locals;

public class ProductionActivity extends BaseActivity {

    private ViewPager       mViewPage;
    private RadioGroup      mLanguageSelect;
    private RadioGroup      mFloorSelect;
    private RadioGroup      mDOESelect;

    private int             mIndexLan;
    private int             mIndexFloor;
    private int             mIndexDOE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);

        setTitle("生产配置");
        List<View> viewList = new ArrayList<View>();
        viewList.add(addPageOne());
        viewList.add(addPageTwo());
        viewList.add(addPageThree());
        viewList.add(addPageFour());


        mViewPage = findViewById(R.id.setupViewPager);
        mViewPage.setAdapter(new GuidePageAdapter(viewList));


        readTest();
        Log.d("test","build time :"+ Build.VERSION.INCREMENTAL);
    }

    private View addPageOne(){
        View view = LayoutInflater.from(this).inflate(R.layout.setup_one,null);
        view.findViewById(R.id.last_page).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.next_page).setOnClickListener(nextPageListener);

        mLanguageSelect = view.findViewById(R.id.language_radio);

        mLanguageSelect.setOnCheckedChangeListener(selectListener);

        TextView version =view.findViewById(R.id.version_text);
        version.setText("版本(Build Time)\n"+Build.VERSION.INCREMENTAL);
        return view;
    }

    RadioGroup.OnCheckedChangeListener selectListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

           if (checkedId == R.id.language_1){
               mIndexLan = 0;
           }else if(checkedId == R.id.language_2){
               mIndexLan = 1;
           }else if(checkedId == R.id.language_3){
               mIndexLan = 2;
           }else if(checkedId == R.id.floor_number2){
               mIndexFloor = 2;
               updateEnableFloor();
           }else if(checkedId == R.id.floor_number3){
               mIndexFloor = 3;
               updateEnableFloor();

           }else if(checkedId == R.id.floor_number4){
               mIndexFloor = 4;
               updateEnableFloor();

           }else if(checkedId == R.id.floor_number5) {
               mIndexFloor = 5;
               updateEnableFloor();

           }else if(checkedId == R.id.doe_no){
               mIndexDOE = 0;
           }else if (checkedId == R.id.doe_yes){
               mIndexDOE = 1;
           }
        }
    };





    private View addPageTwo(){
        View view = LayoutInflater.from(this).inflate(R.layout.setup_two,null);
        view.findViewById(R.id.last_page).setOnClickListener(lastPageListener);
        view.findViewById(R.id.next_page).setOnClickListener(nextPageListener);
        mFloorSelect = (RadioGroup)view.findViewById(R.id.floor_number_select);
        mFloorSelect.setOnCheckedChangeListener(selectListener);
        return view;
    }

    private View addPageThree(){
        View view = LayoutInflater.from(this).inflate(R.layout.setup_three,null);
        view.findViewById(R.id.last_page).setOnClickListener(lastPageListener);
        view.findViewById(R.id.next_page).setOnClickListener(nextPageListener);
        return view;
    }

    private View addPageFour(){
        View view = LayoutInflater.from(this).inflate(R.layout.setup_four,null);
        view.findViewById(R.id.last_page).setOnClickListener(lastPageListener);
        view.findViewById(R.id.next_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complateSetup();
            }
        });

        mDOESelect = (RadioGroup)view.findViewById(R.id.doe_select) ;
        mDOESelect.setOnCheckedChangeListener(selectListener);
//
//        TextView textView = (TextView) view.findViewById(R.id.next_page);
//        textView.setText("完成");
        return view;
    }

    @Override
    protected void InitView() {

    }

    private void complateSetup(){

        if (mDOESelect.getCheckedRadioButtonId() ==-1){
            showToast("请选择DOE");
            return ;
        }

        StringBuilder stringBuilder = new StringBuilder("");
        String lanStr[] = {"英文(English)","简体中文","繁体中文"};
        stringBuilder.append("语言 ："+lanStr[mIndexLan]);
        stringBuilder.append("\n"+mIndexFloor+"层站 ("+mIndexFloor+" floors)");

        if (findViewById(R.id.floor_5).getVisibility()== View.VISIBLE){
            EditText editText = (EditText)findViewById(R.id.floor_5);
            stringBuilder.append("\n"+editText.getText().toString());

        }
        if (findViewById(R.id.floor_4).getVisibility()== View.VISIBLE){
            EditText editText = (EditText)findViewById(R.id.floor_4);
            stringBuilder.append("\n"+editText.getText().toString());

        }
        if (findViewById(R.id.floor_3).getVisibility()== View.VISIBLE){
            EditText editText = (EditText)findViewById(R.id.floor_3);
            stringBuilder.append("\n"+editText.getText().toString());

        }

        {
            EditText editText = (EditText) findViewById(R.id.floor_2);
            stringBuilder.append("\n" + editText.getText().toString());
        }
        {
            EditText editText = (EditText) findViewById(R.id.floor_1);
            stringBuilder.append("\n" + editText.getText().toString());
        }

        if (mIndexDOE ==0){
            stringBuilder.append("\n\n 没有 DOE按钮 (Disable DOE)");
        }else{
            stringBuilder.append("\n\n 有 DOE按钮 (Enable DOE)" );
        }



        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("生产配置项")
                    .setMessage(stringBuilder.toString())
                    .setPositiveButton("确定并重启(OK reboot)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            writeIni();
                            String languegeValue = locals[0];

                            if (mIndexLan ==0){
                                languegeValue = locals[1];
                            }else if(mIndexLan ==1){
                                languegeValue = locals[0];
                            }else if(mIndexLan ==2){
                                languegeValue = locals[2];
                            }



                            PreferManager.getInstence().putLanguageMode(ProductionActivity.this, languegeValue);
                            Log.d("lang 1");
                            changeAppLanguage();
                            Log.d("lang 2");


                            writeConfigure();

                            dialog.dismiss();

                            PowerManager pManager = (PowerManager) getSystemService(Context.POWER_SERVICE); //reboot to fastboot
                            pManager.reboot("");

                        }
                    }).setNegativeButton("取消(Cancel)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return ;
                    }
                });

        builder.create().show();


    }


    private View.OnClickListener nextPageListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int index  = mViewPage.getCurrentItem();
            if (index == 0){
                int id =  mLanguageSelect.getCheckedRadioButtonId();
                Log.d("test","select langeuage :"+id);
                if (id ==-1){
                    showToast("请选择系统语言");
                    return ;
                }

            }else if(index == 1){
                int id = mFloorSelect.getCheckedRadioButtonId();
                if(id == -1){
                    showToast("全选择楼层数量");
                    return ;
                }
            }else if(index == 3){
                int id = mDOESelect.getCheckedRadioButtonId();
                if (id == -1){
                    showToast("全选择是否有DOE按钮");
                    return ;
                }
            }

            mViewPage.setCurrentItem(mViewPage.getCurrentItem()+1);
        }
    };

    private void updateEnableFloor(){
        findViewById(R.id.floor_3).setVisibility(View.VISIBLE);
        findViewById(R.id.floor_4).setVisibility(View.VISIBLE);
        findViewById(R.id.floor_5).setVisibility(View.VISIBLE);

        if (mIndexFloor ==2){
            findViewById(R.id.floor_3).setVisibility(View.GONE);
            findViewById(R.id.floor_4).setVisibility(View.GONE);
            findViewById(R.id.floor_5).setVisibility(View.GONE);
        }else if (mIndexFloor == 3){

            findViewById(R.id.floor_4).setVisibility(View.GONE);
            findViewById(R.id.floor_5).setVisibility(View.GONE);

        }else if(mIndexFloor == 4){
            findViewById(R.id.floor_5).setVisibility(View.GONE);
        }
    }
    private View.OnClickListener lastPageListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            mViewPage.setCurrentItem(mViewPage.getCurrentItem()-1);
        }
    };

    private void showToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    public static final String INIT_FILE_PATH = "/data/tworkshop.ini";

    private void readTest(){
        try {
            File file = new File(INIT_FILE_PATH);
            FileReader ps = new FileReader(file);
            char buf[] = new char[1024];
            int len = ps.read(buf);
            String str = new String(buf,0,len);
            Log.d("test","read init :"+str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeIni(){

        try {
            File file = new File(INIT_FILE_PATH);
            FileWriter ps = new FileWriter(file);

            ps.write("language=0\n");
            ps.write("resolution=1");
            ps.write("direction=0");
            ps.write("dhcp=1");
            ps.write("ip=192.168.0.123");
            Log.d("test","writeIni init sucess .");
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //生成假的配置文件到本地
    private void writeConfigure() {
        File outFile = new File(Constant.PREFER_FILE_PATH);
        JSONObject testJsonObj = new JSONObject();
        JSONArray testJsonArray = new JSONArray();
        OutputStreamWriter inputStreamReader = null;
        FileOutputStream fileOutputStream = null;
        try {
            // TODO 模拟的物理楼层

            JSONObject childJsonData = new JSONObject();
            childJsonData.put(Constant.PHYSICAL_FLOOR, 1);

            childJsonData.put(Constant.DISPLAY_FLOOR, ((TextView)findViewById(R.id.floor_1)).getText().toString());
            childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
            testJsonArray.put(childJsonData);


            childJsonData = new JSONObject();
            childJsonData.put(Constant.PHYSICAL_FLOOR, 2);

            childJsonData.put(Constant.DISPLAY_FLOOR, ((TextView)findViewById(R.id.floor_2)).getText().toString());
            childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
            testJsonArray.put(childJsonData);

            if(mIndexFloor>=3){
                childJsonData = new JSONObject();
                childJsonData.put(Constant.PHYSICAL_FLOOR, 3);

                childJsonData.put(Constant.DISPLAY_FLOOR, ((TextView)findViewById(R.id.floor_3)).getText().toString());
                childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
                testJsonArray.put(childJsonData);
            }

            if(mIndexFloor>=4){
                childJsonData = new JSONObject();
                childJsonData.put(Constant.PHYSICAL_FLOOR, 4);

                childJsonData.put(Constant.DISPLAY_FLOOR, ((TextView)findViewById(R.id.floor_4)).getText().toString());
                childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
                testJsonArray.put(childJsonData);
            }

            if(mIndexFloor>=5){
                childJsonData = new JSONObject();
                childJsonData.put(Constant.PHYSICAL_FLOOR, 5);

                childJsonData.put(Constant.DISPLAY_FLOOR, ((TextView)findViewById(R.id.floor_5)).getText().toString());
                childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
                testJsonArray.put(childJsonData);
            }
            testJsonObj.put(Constant.FLOOR_DATA_ITEM_KEY, testJsonArray);

            if(mIndexDOE == 0){
                testJsonObj.put(Constant.OPEN_DOOR_DELAY, false);
            }else{
                testJsonObj.put(Constant.OPEN_DOOR_DELAY, true);
            }


            //写入
            inputStreamReader = new OutputStreamWriter(fileOutputStream = new FileOutputStream(outFile)
                    , "utf-8");
            inputStreamReader.write(testJsonObj.toString());

            PreferManager.getInstence().clearSetupEnable(this);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("测试 json 数据初始化失败");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("测试 json 数据文件未找到");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("测试 json 数据文件编码异常");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("测试 json 数据文件输出流异常");
        } finally {
            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
