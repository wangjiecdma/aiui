package com.shbst.kone_home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.msetupwizard.ProductionActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //finish 后续代码会继续执行，在跳转后使用 return 结束


        if(PreferManager.getInstence().getSetupEnable(this)){
            startActivity(new Intent(this, ProductionActivity.class));
            ExitActivty();
            return ;
        }

//        //TODO 在实际设备中，应该要检查GUI引导
        if (PreferManager.getInstence().getGuiEnable(this)) {//检查是否需要 引导
            startGuideActivity();
            ExitActivty();
            return;
        }


        if (PreferManager.getInstence().getThemeMode(this) == Constant.THEME_BLOCKS) {
            startThemeBlocks();
            ExitActivty();
            return;
        }
        if (PreferManager.getInstence().getThemeMode(this) == Constant.THEME_SPHERE) {
            startThemeSphere();
            ExitActivty();
        }
    }

    // 通过设置 Activity 的theme ，使其在切换Activity时，无缝切换
    private void ExitActivty() {
//        overridePendingTransition(R.anim.fragment_slide_in_from_bottom, 0);
//        overridePendingTransition(0, 0);
        finish();
    }


    private void startGuideActivity() {
        startActivity(new Intent(this, GuideActivity.class));
    }

    private void startThemeBlocks() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void startThemeSphere() {
        startActivity(new Intent(this, Main2Activity.class));
    }
}
