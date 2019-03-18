package com.shbst.kone_home.aiui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.shbst.kone_home.Entity.FloorItemBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AIUIManager implements SpeakCallback{
    private AIUIAgent mAIUIAgent;
    public  ElevatorSpeaker mElevatorSpeak;
    private AIUIEvent mAIUIEvent;
    private  final String TAG = "homelift_aiui";


    private ElevatorTTS mTts;
    private FloorControl mControl;

    private Context mContext;

    private AIUIManager(){

    }

    public void init(Context context, FloorControl control){

        mContext = context;
        mControl = control;

        gManager = this;
    }

    static AIUIManager  gManager = null;
    public static AIUIManager getInstance(){

        if (gManager == null){
            gManager = new AIUIManager();
            mHandler = new Handler();
        }
        return gManager;

    }


    public void startService(){
        if (isNetworkConnected(mContext) && mAIUIAgent == null){
            SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=" + AIUIConfig.APPID);
            mElevatorSpeak = new ElevatorSpeaker(mContext, this);
            //mElevatorSpeak.bindMsgTv(leftText);
//        mTts = new ElevatorTTS(context,this);

            mAIUIAgent = AIUIAgent.createAgent(mContext, AIUIConfig.getAIUIParams(mContext), mAIUIListener);

            externCommand.put("遗漏",1);
            externCommand.put("一楼",1) ;
            externCommand.put("倚楼",1);
            externCommand.put("二楼",2);
            externCommand.put("三楼",3);
            externCommand.put("四楼",4);
            externCommand.put("死喽",4);
            externCommand.put("五楼",5);
            externCommand.put("鼓楼",5);
            externCommand.put("古楼",5);
            externCommand.put("屋漏",5);
            externCommand.put("武隆",5);
            externCommand.put("骷髅",5);


            externCommand.put("灌南",-1);
            externCommand.put("观澜",-1);
            externCommand.put("太难",0);
            externCommand.put("打开家乡",0);
            externCommand.put("关闭家乡",-1);

            externCommand.put("厦门",0);
            externCommand.put("快男",0);
        }else{
            if (isNetworkConnected(mContext)== false){
                resetService();
            }
        }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();




            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }



    private static Handler mHandler = null;

    private static void resetService(){


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gManager.mAIUIAgent != null) {
                    gManager.mAIUIAgent.destroy();
                }

                gManager.mElevatorSpeak = null;
                gManager.mAIUIAgent = null;
                gManager.startService();
            }
        },5000);




    }



    private HashMap<String ,Integer> externCommand= new LinkedHashMap<>();


    private AIUIListener mAIUIListener = new AIUIListener() {
        @Override
        public void onEvent(AIUIEvent event) {
            mAIUIEvent = event;
            //Log.d(TAG,"aiuievent :"+event.eventType);
            switch (event.eventType) {
                case AIUIConstant.EVENT_RESULT: {
//                    if (mElevatorSpeak.isSpeaking()) {
//                        return;
//    }
                    try {
                        JSONObject data = new JSONObject(event.info).getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);
                        if (content.has("cnt_id")) {
                            String cnt_idStr = new String(event.data.getByteArray(content.getString("cnt_id")), "utf-8");
                            if (cnt_idStr.equals("")) {
                                return;
                            }
                            JSONObject cntJson = new JSONObject(cnt_idStr);
                            if ("nlp".equals(params.optString("sub"))) {


                                JSONObject intent = cntJson.getJSONObject("intent");
                                if (intent.toString().equals("{}")) {//empty
                                    return;
                                }
                                Log.v(TAG, "intent:" + intent.toString());

                                String text = intent.getString("text");


                                if (!intent.isNull("vendor") && intent.getString("vendor").equals("SHBST") && !intent.isNull("semantic")) {
                                    int floorvalue = intent.getJSONArray("semantic").getJSONObject(0).getJSONArray("slots").getJSONObject(0).getInt("normValue");

                                    ArrayList<Integer> list = new ArrayList<Integer>();
                                    list.add(floorvalue);
                                    registFloor(list);

                                    updateStatus("AIUI 语音识别结果："+text);

                                    return;
                                } else if (text.endsWith("关门")||text.equals("请关门")||text.equals("关闭") || text.equals("关闭轿厢") ){
                                    mElevatorSpeak.speak("请注意，正在关闭轿厢门");
                                    mControl.close();

                                    updateStatus("AIUI 语音识别结果："+text);

                                    return ;
                                }
                                else if(text.endsWith("开门")||text.equals("请开门")||text.equals("打开")||text.equals("打开轿厢")){
                                        mElevatorSpeak.speak("轿厢门打开中");
                                        mControl.open();

                                    updateStatus("AIUI 语音识别结果："+text);

                                    return;

                                }else {
                                    if(text.startsWith("去")|| text.startsWith("到")){
                                        text = text.replace("去","");
                                        text = text.replace("到","");

                                    }
                                    ArrayList<Integer> list = new ArrayList<Integer>();

                                    for (String key : externCommand.keySet()){
                                        if (key.endsWith(text)){
                                            Integer value = externCommand.get(key);
                                            if (value>0){
//                                                registFloor(value);
                                                if (value>0&& value<6) {
                                                    list.add(value);
                                                }
                                                break;
                                            }else if(value == 0){
                                                mElevatorSpeak.speak("轿厢门打开中");
                                                mControl.open();
                                                return;
                                            }else if (value ==-1){
                                                mElevatorSpeak.speak("请注意，正在关闭轿厢门");
                                                mControl.close();
                                                return;
                                            }
                                        }
                                    }


                                    if (text.contains("一楼")){
                                        list.add(1);
                                    }
                                    if (text.contains("二楼")){
                                        list.add(2);
                                    }
                                    if (text.contains("三楼")){
                                        list.add(3);
                                    }
                                    if (text.contains("四楼")){
                                        list.add(4);
                                    }
                                    if (text.contains("五楼")){
                                        list.add(5);
                                   }
                                   if (list.size()>0){
                                        registFloor(list);
                                   }

                                    updateStatus("AIUI 语音识别结果："+text);


                                }

                            }



                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                break;

                case AIUIConstant.EVENT_WAKEUP: {
                    Log.e(TAG, "唤醒了");
                    updateStatus("唤醒了");

                    //registFloor(1);
                    break;
                }

                case AIUIConstant.EVENT_SLEEP: {
                    Log.e(TAG, "进入休眠了");
                    mAIUIAgent.sendMessage(AIUIConfig.CMD_WAKEUP);
                    updateStatus("进入休眠了");

                    break;
                }

                case AIUIConstant.EVENT_ERROR: {
                    Log.e(TAG, "AIUI 错误了");
                    //mAIUIAgent.sendMessage(AIUIConfig.CMD_STOP);
                    //mAIUIAgent.sendMessage(AIUIConfig.CMD_START);
                    updateStatus("AIUI 错误");
                    resetService();
                }
                break;

                case AIUIConstant.EVENT_STATE: {    // 状态事件
                    Log.d(TAG,"event state :"+event.arg1);
                    if (AIUIConstant.STATE_READY == event.arg1) {
                        mAIUIAgent.sendMessage(AIUIConfig.CMD_WAKEUP);
                    } else if ((AIUIConstant.STATE_WORKING == event.arg1)) {
                        //if (!mElevatorSpeak.isSpeaking()) {
                            mAIUIAgent.sendMessage(AIUIConfig.CMD_START_RECORD);
                       // }
                    }
                    break;
                }
                case AIUIConstant.EVENT_STOP_RECORD:{
                    Log.d(TAG,"event  stop record");

                }
                case AIUIConstant.EVENT_START_RECORD:{
                    Log.d(TAG,"event start record");
                }
            }
        }

    };

//    private synchronized void registFloor(int floor) {
//
//        if (floor >=1 && floor<=5) {
//            mElevatorSpeak.speak("楼层已登记");
//            //mTts.speak("楼层已登记");
//            mControl.registerFloor(floor);
//        }
//
//        Log.d(TAG,"registFloor one : "+floor);
//
//    }

    private synchronized void registFloor(ArrayList<Integer> list){

        boolean ding= false;
        for (int i =0;i<list.size();i++){
            int floor = list.get(i);

            if (floor >=1 && floor <=5){
                mControl.registerFloor(floor);
                ding = true;
            }

        }
        if(ding) {
            mElevatorSpeak.speak("楼层已登记");
        }
        mRegisterCounter ++;
        Log.d(TAG,"registFloor list : "+list.size());

    }

    public void onSpeaking() {

        Log.d(TAG,"onSpeaking");
    }

    public void onSpeakStart() {
        if(mAIUIAgent !=null){
            Log.d(TAG,"stop record");
            mAIUIAgent.sendMessage(AIUIConfig.CMD_STOP_RECORD);
        }
    }

    public void onSpeakover(SpeechError error) {
        Log.d(TAG,"onSpeakover :"+error);
        if(mAIUIAgent != null){
            if (mAIUIEvent.eventType != AIUIConstant.EVENT_START_RECORD) {
                Log.d(TAG,"start record");
                mAIUIAgent.sendMessage(AIUIConfig.CMD_START_RECORD);
            }
        }

    }

    public interface DebugStatus {
        public void updateStatus(String str);
    }

    DebugStatus  mDebugStatus = null;
    public void setDebugStatusListener(DebugStatus debugStatusListener){

        mDebugStatus = debugStatusListener;
    }
    private long  mTotalDatas = 0;

    private long  mRegisterCounter = 0;


    public void updateStatus(String str){

        long used = 0;
        if (mTotalDatas == 0){
            mTotalDatas = TrafficStats.getTotalRxBytes()+ TrafficStats.getTotalTxBytes();

        }else{
            used = TrafficStats.getTotalRxBytes()+ TrafficStats.getTotalTxBytes() - mTotalDatas;
        }



        if (mDebugStatus!= null){
            String status = "当前网络数据使用量:"+(used/1024/1024)+"M "+(used/1024%1024)+"K";
            status +="\n";
            status +="当前呼梯成功次数："+mRegisterCounter+"\n";
            String help = "欢迎使用KONE智能语音呼梯系统\n\n语音呼梯示例：\n我要去X楼，去X楼，到X楼，X楼\n请开门，请关门\n\n";
            mDebugStatus.updateStatus(help +status +str);
        }
    }


}
