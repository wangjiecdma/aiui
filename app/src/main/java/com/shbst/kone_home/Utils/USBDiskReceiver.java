package com.shbst.kone_home.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;

import java.text.SimpleDateFormat;


/**
 * Created by zhouwenchao on 2017-02-17.
 */
public class USBDiskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log("监听到USB广播"+getTime());
        String action = intent.getAction();
        Log("监听到USB广播的 Action：" + action);
        if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
            Log("媒体挂载");
            if (intent.getDataString() != null) {
                UpdataManager.startUpSource(intent.getDataString(),context);
            }
        }
        if (action.equals("android.intent.action.MEDIA_UNMOUNTED")) {
            Log("媒体卸载");
            UpdataManager.stopCopy();
        }
        if(action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
            Log("usb设备插入");
        }
        if(action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)){
            Log("usb设备拔出");
            UpdataManager.stopCopy();
        }
    }
    private void Log(String data){
        Log.i("USBDiskReceiver", "Log: "+data);
    }
    public String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }
}
