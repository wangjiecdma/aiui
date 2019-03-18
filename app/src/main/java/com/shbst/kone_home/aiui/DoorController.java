package com.shbst.kone_home.aiui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;



public class DoorController extends BroadcastReceiver {


    private Context mContext;
    ImageView mClose;
    int closeID;
    ImageView mOpen;
    int openID;
    Handler mHander =new Handler();
    private final static  String OPEN_ACTION = "com.kone_home.opendoor";
    private final static String CLOSE_ACTION = "com.kone_home.closedoor";

    public DoorController(Context context, ImageView openDoor,int opendraw,ImageView closeDoor,int closedraw){

        super();
        mContext = context;
        mClose = closeDoor;
        closeID = closedraw;

        mOpen = openDoor;
        openID = opendraw;

    }


    public void create(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OPEN_ACTION);
        intentFilter.addAction(CLOSE_ACTION);

        mContext.registerReceiver(this,intentFilter);
    }

    public void destroy(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(OPEN_ACTION)){
            final Drawable lastOpenDraw =  mOpen.getDrawable();
            mOpen.setImageDrawable(context.getResources().getDrawable(openID));

            mHander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOpen.setImageDrawable(lastOpenDraw);
                }
            },1000);

        }else if (intent.getAction().equalsIgnoreCase(CLOSE_ACTION)){
            final Drawable lastCloseDraw =  mClose.getDrawable();
            mClose.setImageDrawable(context.getResources().getDrawable(closeID));
            mHander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mClose.setImageDrawable(lastCloseDraw);
                }
            },1000);

        }
    }

    public static void closeDoor(Context context){
        Intent intent = new Intent(CLOSE_ACTION);
        context.sendBroadcast(intent);
    }
    public static void openDoor(Context context){
        Intent intent = new Intent(OPEN_ACTION);
        context.sendBroadcast(intent);
    }
}
