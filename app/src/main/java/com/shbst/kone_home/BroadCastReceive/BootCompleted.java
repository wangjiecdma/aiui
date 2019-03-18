package com.shbst.kone_home.BroadCastReceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shbst.kone_home.SplashActivity;
import com.shbst.kone_home.Utils.Log;


/**
 * Created by zhouwenchao on 2017-03-16.
 */
public class BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("接收到开机广播");
        Intent newIntent = new Intent(context, SplashActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //注意，必须添加这个标记，否则启动会失败
        //开机广播监听,启动本应用
        context.startActivity(newIntent);
    }
}
