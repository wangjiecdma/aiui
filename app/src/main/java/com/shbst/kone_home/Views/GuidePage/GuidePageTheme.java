package com.shbst.kone_home.Views.GuidePage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.PreferManager;
import com.shbst.kone_home.Views.FontTextView;
import com.shbst.kone_home.Views.GifView;

/**
 * Created by zhouwenchao on 2018-02-27.
 */

public class GuidePageTheme extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "GuidePageFive";
    FontTextView set_theme_guide_blocks, set_theme_minimal_spheres;
    Drawable drawable_select;
    Drawable drawable_un;
    ImageView bgImg;

    public GuidePageTheme(Context context) {
        super(context);
        InitLayout(context);
    }

    public GuidePageTheme(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public GuidePageTheme(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    private void InitLayout(Context context) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        );
        this.setLayoutParams(layoutParams);
        this.invalidate();
        View view = LayoutInflater.from(context).inflate(R.layout.guide_five_page
                , null);

        this.addView(view);
        //add view and init view
        InitView();
    }


    private void InitView() {
        bgImg = this.findViewById(R.id.pageFileBgImg);
        set_theme_guide_blocks = this.findViewById(R.id.set_theme_guide_blocks);
        set_theme_minimal_spheres = this.findViewById(R.id.set_theme_minimal_spheres);
        set_theme_guide_blocks.setOnClickListener(this);
        set_theme_minimal_spheres.setOnClickListener(this);

        drawable_select = getResources().getDrawable(R.drawable.button_select);
        drawable_select.setBounds(0, 0, drawable_select.getIntrinsicWidth(), drawable_select.getIntrinsicHeight());
        drawable_un = getResources().getDrawable(R.drawable.button_un);
        drawable_un.setBounds(0, 0, drawable_un.getIntrinsicWidth(), drawable_un.getIntrinsicHeight());

        bgImg.setImageResource(R.drawable.theme_blocks_gui_bg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_theme_guide_blocks:
                set_theme_guide_blocks.setCompoundDrawables(drawable_select, null, null, null);
                set_theme_minimal_spheres.setCompoundDrawables(drawable_un, null, null, null);
                bgImg.setImageResource(R.drawable.theme_blocks_gui_bg);
                PreferManager.getInstence().putThemeMode(view.getContext(), 0);
                break;
            case R.id.set_theme_minimal_spheres:
                set_theme_guide_blocks.setCompoundDrawables(drawable_un, null, null, null);
                set_theme_minimal_spheres.setCompoundDrawables(drawable_select, null, null, null);
                bgImg.setImageResource(R.drawable.theme_sphere_gui_bg);
                PreferManager.getInstence().putThemeMode(view.getContext(), 1);
                break;
        }
    }
}
