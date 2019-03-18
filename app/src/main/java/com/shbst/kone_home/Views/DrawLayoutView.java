package com.shbst.kone_home.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shbst.kone_home.Entity.FloorItemBean;
import com.shbst.kone_home.R;
import com.shbst.kone_home.Utils.CacheUtil;
import com.shbst.kone_home.Utils.Constant;
import com.shbst.kone_home.Utils.ImageUtil;
import com.shbst.kone_home.Utils.Log;
import com.shbst.kone_home.Utils.PreferManager;

import java.io.File;

/**
 * Created by zhouwenchao on 2018-02-28.
 */

public class DrawLayoutView extends RelativeLayout {
    private Context mContext;
    private DrawSufaceView drawSufaceView;
    private ImageView showImage, deleImg;
    private LinearLayout initTouchView;
    private FloorItemBean mItemBean;
    private static StateChangedListener  mStateListener = null;


    public DrawLayoutView(Context context, FloorItemBean itemBean) {
        super(context);
        InitView(context, itemBean);
    }

    public DrawLayoutView(Context context, AttributeSet attrs, FloorItemBean itemBean) {
        super(context, attrs);
        InitView(context, itemBean);
    }

    public DrawLayoutView(Context context, AttributeSet attrs, int defStyleAttr, FloorItemBean itemBean) {
        super(context, attrs, defStyleAttr);
        InitView(context, itemBean);
    }

    private void InitView(Context context, FloorItemBean itemBean) {
        this.mContext = context;
        this.mItemBean = itemBean;
        this.removeAllViews();
        addViewToLayout();
        //if (TextUtils.isEmpty(itemBean.getSketchpadImgPath())) {
        if(drawSufaceView.isEmpty()){
            showInitTouch();
        } else {
            showSketchpad();
        }

        Log.d("draw","DrawLayoutView create new    ");

    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void addViewToLayout() {
        drawSufaceView = new DrawSufaceView(mContext, this);
        drawSufaceView.setOnTouchListener(drawSufaceTouchListener);
        RelativeLayout.LayoutParams drawViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        showImage = new ImageView(mContext);
        showImage.setOnTouchListener(ImgTouchListener);
        showImage.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        String imgFilePath = mItemBean.getSketchpadImgPath();
//        Log.v("read imgfile path:" + imgFilePath);
        if (!TextUtils.isEmpty(imgFilePath)) {
            File imgFile = new File(imgFilePath);
            if (imgFile.exists() && imgFile.isFile()) {
                Log.v("show image file is file and file exists,show imagefile");
                LoadImgTask task = new LoadImgTask();
                task.execute(imgFilePath);
            }
        }

        deleImg = new ImageView(mContext);
        RelativeLayout.LayoutParams deleImgLayouParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (PreferManager.getInstence().getThemeMode(mContext) == Constant.THEME_BLOCKS) {
            deleImgLayouParams.leftMargin = 400;
            deleImgLayouParams.topMargin = 20;
        }
        if (PreferManager.getInstence().getThemeMode(mContext) == Constant.THEME_SPHERE) {
            deleImgLayouParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            deleImgLayouParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            deleImgLayouParams.topMargin = 20;
            deleImgLayouParams.rightMargin = 20;
        }
        deleImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.dele));
        deleImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSketchpadImgPath();
                if (drawSufaceView != null) {
                    drawSufaceView.clean();
                }
                showInitTouch();
            }
        });

        //add layout
        initTouchView = new LinearLayout(mContext);
        initTouchView.setOrientation(LinearLayout.VERTICAL);
        initTouchView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams initTouchViewParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        ImageView initImg = new ImageView(mContext);
        LinearLayout.LayoutParams initImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        initImg.setBackground(ContextCompat.getDrawable(mContext, R.drawable.paint));
        initTouchView.setOnTouchListener(InitTouchListener);
        initTouchView.addView(initImg, initImgParams);

        FontTextView initTextView = new FontTextView(mContext);
        LinearLayout.LayoutParams initTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        initTextView.setTextColor(ContextCompat.getColor(mContext, R.color.drawInitTextColor));
        initTextView.setTextSize(40);
        initTextView.setText("TOUCH TO DRAW");
        initTouchView.addView(initTextView, initTextParams);

        //add view
        this.addView(drawSufaceView, drawViewLayoutParams);
        this.addView(showImage, drawViewLayoutParams);
        this.addView(deleImg, deleImgLayouParams);
        this.addView(initTouchView, initTouchViewParams);
    }

    public void showDeleteView() {
        removeCallbacks(dissmissDeletImgRunnable);
        if (deleImg.getVisibility() != View.VISIBLE)
            deleImg.setVisibility(View.VISIBLE);
        //客户指明延迟 3s
        postDelayed(dissmissDeletImgRunnable, 3000);
    }


    public void dismissDeleteView() {
        onDrawingStateChanged(false);
        removeCallbacks(dissmissDeletImgRunnable);
        deleImg.setVisibility(View.INVISIBLE);
    }

    public void showInitTouchView() {
        initTouchView.setVisibility(View.VISIBLE);
    }

    public void dismissInitTouchView() {
        initTouchView.setVisibility(View.INVISIBLE);
    }

    public void showImgView() {
        showImage.setVisibility(View.VISIBLE);
    }

    public void dismissImgView() {
        showImage.setVisibility(View.INVISIBLE);
    }

    public void showDrawView() {
        drawSufaceView.setVisibility(View.VISIBLE);
    }

    public void dismissDrawView() {
        drawSufaceView.setVisibility(View.INVISIBLE);
    }

    public void clearSketchpadImgPath() {
        saveSketchpadImgPath("");
    }

    public void saveSketchpadImgPath(String path) {
        Log.v("saveSketchpadImgPath,path:" + path);
        mItemBean.setSketchpadImgPath(path);
        CacheUtil.removeBitmap(path);

//        Bitmap tmpBitmap = CacheUtil.removeBitmap(path);
//        if (tmpBitmap != null) {
//            tmpBitmap.recycle();
//        }

        PreferManager.getInstence().putFloorItemBean(mContext, mItemBean);
    }

    private void showSketchpad() {
        dismissImgView();
        dismissInitTouchView();
        showDeleteView();
        showDrawView();
    }

    private void showInitTouch() {
        dismissDeleteView();
        dismissImgView();
        dismissDrawView();
        showInitTouchView();
    }

    public FloorItemBean getmItemBean() {
        return mItemBean;
    }

    private View.OnTouchListener InitTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    showSketchpad();
                    break;
            }
            return false;
        }
    };
    private View.OnTouchListener ImgTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    showSketchpad();
                    break;
            }
            return false;
        }
    };

    private Runnable dissmissDeletImgRunnable = new Runnable() {
        @Override
        public void run() {
            dismissDeleteView();
        }
    };
    private View.OnTouchListener drawSufaceTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                onDrawingStateChanged(true);
            }
            showDeleteView();
            Log.d("draw"," surface view OnTouchListener "+motionEvent.getAction());
            return false;
        }
    };

    public  interface StateChangedListener {
         void onStateChanged(boolean drawing);
    }
    private void onDrawingStateChanged(boolean drawing){
        Log.d("draw","ondrawing state changed :"+drawing);
        if(mStateListener != null){
            mStateListener.onStateChanged(drawing);
            Log.d("draw","ondrawing state call listener ");
        }
    }
    public void setOnDrawingStateChanged(StateChangedListener lis){
        Log.d("draw","setOnDrawingStateChanged   "+lis);

        mStateListener = lis;
    }

    @SuppressLint("StaticFieldLeak")
    class LoadImgTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = CacheUtil.getBitmap(strings[0]);
            if (bitmap == null) {
                bitmap = ImageUtil.getBitmap(strings[0], showImage);
                if (bitmap != null)
                    CacheUtil.putBitmap(strings[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null && showImage != null)
                showImage.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //从窗口移除时，清除线程
        removeCallbacks(dissmissDeletImgRunnable);
    }
}
