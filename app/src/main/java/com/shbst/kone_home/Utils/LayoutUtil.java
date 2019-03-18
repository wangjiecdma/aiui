package com.shbst.kone_home.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zhouwenchao on 2018-02-08.
 */

public class LayoutUtil {
    public static int getDPIFromDP(Context context, int dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dp * dm.density);
    }

    public static float getDPIFromSP(Context context, int sp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (sp * dm.density);
    }
    /**
     * 设置全屏并隐藏底部导航栏菜单
     */
    public static void setFullScreenAndHidMenu(Activity activity) {
        Log.d("设置全屏并隐藏底部导航栏菜单");
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //一下为设置虚拟按键为三个小圆点
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        activity.getWindow().setAttributes(params);
    }
}
