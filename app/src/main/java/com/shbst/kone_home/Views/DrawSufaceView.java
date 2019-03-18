package com.shbst.kone_home.Views;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shbst.kone_home.Utils.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by tongshile on 2017-11-28.
 */
public class DrawSufaceView extends View{
    private Paint mPaint;
    private Path mPath;
    private float mPreX, mPreY;
    private float mLastPathX;
    private float mLastPathY;
    private String mFilePath;
    private DrawLayoutView drawLayoutView;
    private Bitmap   mBitmap;

    public DrawSufaceView(Context context, DrawLayoutView layoutView) {
        super(context);
        init(layoutView);
    }

    public DrawSufaceView(Context context, AttributeSet attrs, DrawLayoutView layoutView) {
        super(context, attrs);
        init(layoutView);
    }

    public DrawSufaceView(Context context, AttributeSet attrs, int defStyleAttr, DrawLayoutView layoutView) {
        super(context, attrs, defStyleAttr);
        init(layoutView);
    }

    public void init(DrawLayoutView layoutView) {
        this.drawLayoutView = layoutView;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        // TODO 线条像素待确认修改
        mPaint.setStrokeWidth(32);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
       // Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.RGBA_F16)
    }

    private void loadBitmap(int width , int height){
        if (mBitmap == null){
            mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);

            Canvas canvas =new Canvas(mBitmap);
            canvas.drawColor(Color.BLACK);

            try{
                FileInputStream fileInputStream = getContext().openFileInput("draw.data");
                int len = fileInputStream.available();

                if(len == 2*width*height ){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2*width*height);
                    fileInputStream.read(byteBuffer.array());
                    mBitmap.copyPixelsFromBuffer(byteBuffer);

                   // Log.d("draw","load data from draw.data");
                }
                fileInputStream.close();
            }catch (Exception e){
                e.printStackTrace();
                Log.d("draw",e);
            }

        }
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loadBitmap(canvas.getWidth(),canvas.getHeight());
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.drawPath(mPath, mPaint);

       // Log.d("draw","width : "+getWidth()+" , "+getHeight());
    }

    public void clean() {
        mPath.reset();
        mBitmap = null;
        try{
            File file = getContext().getFileStreamPath("draw.data");
            if (file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        invalidate();

    }

    public boolean isEmpty(){
        if(mBitmap != null ){
            return false;
        }
        File file = getContext().getFileStreamPath("draw.data");
        if (file.exists()) {
            return false;
        }
        return true;
    }


    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("onTouchEvent: "+event.getX()+"event.getY() "+event.getY());
//                if (drawLayoutView != null) drawLayoutView.clearSketchpadImgPath();
                //mPath.reset();
                mPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                mLastPathX = mPreX;
                mLastPathY = mPreY;

                return true;
            case MotionEvent.ACTION_MOVE:

                float dx = Math.abs(event.getX() - mPreX);
                float dy = Math.abs(event.getY() - mPreY);



                if(dx >=5 || dy >= 5) {
                    float endX = (mPreX + event.getX()) / 2f;
                    float endY = (mPreY + event.getY()) / 2f;


                    mPath.quadTo(mLastPathX, mLastPathY, endX, endY);

                    mLastPathX = endX;
                    mLastPathY = endY;

                    mPreX = event.getX();
                    mPreY = event.getY();



                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:

                /*
                if (drawLayoutView != null) {
                    String fileName = "sketchpad_"
                            + drawLayoutView.getmItemBean().getPhysicalFloor() + ".png";
                    boolean seccuss = saveToGallery(fileName
                            , "picture", "tmpdir", Bitmap.CompressFormat.PNG, 99);
                    Log.v("保存图片:" + seccuss);
                } else {
                    Log.v("保存图片: is null");
                }
                */
                if(mBitmap != null) {
                    Canvas canvas = new Canvas(mBitmap);
                    canvas.drawPath(mPath,mPaint);
                }
                mPath.reset();
                saveBitmap();

                break;
        }
        return super.onTouchEvent(event);
    }


    private void saveBitmap(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mBitmap != null){
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(2*mBitmap.getWidth()*mBitmap.getHeight());
                        mBitmap.copyPixelsToBuffer(byteBuffer);
                        FileOutputStream outputStream  = getContext().openFileOutput("draw.data",Context.MODE_PRIVATE);
                        outputStream.write(byteBuffer.array());
                        outputStream.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public boolean saveToGallery(String fileName, String subFolderPath, String fileDescription, Bitmap.CompressFormat format, int quality) {

        // 控制图片质量
        if (quality < 0 || quality > 100)
            quality = 50;

        long currentTime = System.currentTimeMillis();

        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM/" + subFolderPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.v("mkdir error");
                return false;
            }
        }

        String mimeType;
        switch (format) {
            case PNG:
                mimeType = "image/png";
                if (!fileName.endsWith(".png"))
                    fileName += ".png";
                break;
            case WEBP:
                mimeType = "image/webp";
                if (!fileName.endsWith(".webp"))
                    fileName += ".webp";
                break;
            case JPEG:
            default:
                mimeType = "image/jpeg";
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")))
                    fileName += ".jpg";
                break;
        }

        mFilePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream out;
        try {
            out = new FileOutputStream(mFilePath);

            Bitmap b = getChartBitmap();
            b.compress(format, quality, out);

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("io error");
            return false;
        }

        long size = new File(mFilePath).length();

        ContentValues values = new ContentValues(8);

        // store the details
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.DATE_ADDED, currentTime);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        values.put(MediaStore.Images.Media.DESCRIPTION, fileDescription);
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, mFilePath);
        values.put(MediaStore.Images.Media.SIZE, size);

        if (drawLayoutView != null)
            drawLayoutView.saveSketchpadImgPath(mFilePath);
        return true;
    }

    public Bitmap getChartBitmap() {
        // 创建一个bitmap 根据我们自定义view的大小
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        // 绑定canvas
        Canvas canvas = new Canvas(returnedBitmap);
        // 获取视图的背景
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null)
            // 如果有就绘制
            bgDrawable.draw(canvas);
        else
            // 没有就绘制白色
            canvas.drawColor(Color.BLACK);
        // 绘制
        draw(canvas);
        return returnedBitmap;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        drawLayoutView = null;
    }


}
