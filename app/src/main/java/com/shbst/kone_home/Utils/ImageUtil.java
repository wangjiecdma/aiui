package com.shbst.kone_home.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Created by zhouwenchao on 2017-04-06.
 */
public class ImageUtil {

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmapToDrawable(null, bitmap);
    }

    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap) {
        return new BitmapDrawable(res, bitmap);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }

        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w <= 0 || h <= 0) {
            w = drawable.getBounds().width();
            h = drawable.getBounds().height();
        }
        return drawableToBitmap(drawable, w, h);
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int w, int h) {
        if (w <= 0 || h <= 0) {
            return null;
        }
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }


    public static Bitmap getBitmap(Resources res, int resId,
                                   int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = Math.round(calculateInSampleSize(options, reqWidth, reqHeight));
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap getBitmap(Resources res, int resId,
                                   View imageView) {

        ImageSize imageSize = getImageViewWidth(imageView);
        // 调用上面定义的方法计算inSampleSize值
        return getBitmap(res, resId, imageSize.width, imageSize.height);
    }

    public static Bitmap getBitmap(Resources res, int resId) {
        return getBitmap(res, resId, 1920, 1920);
    }

    public static Bitmap getBitmap(String path,
                                   int reqWidth, int reqHeight) {

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = Math.round(calculateInSampleSize(options, reqWidth, reqHeight));
//        options.inJustDecodeBounds = true;
//        // 调用上面定义的方法计算inSampleSize值
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getBitmap(String path,
                                   View imageView) {

        ImageSize imageSize = getImageViewWidth(imageView);
        return getBitmap(path, imageSize.width, imageSize.height);
    }

    public static Bitmap getBitmap(String path,
                                   Context context) {

        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = context
                .getResources().getDisplayMetrics();
        imageSize.width = displayMetrics.widthPixels;
        imageSize.height = displayMetrics.heightPixels;
        return getBitmap(path, imageSize.width, imageSize.height);

    }

    public static Bitmap getBitmap(String path) {
        return getBitmap(path, 1920, 1920);

    }

    private static float calculateInSampleSize(BitmapFactory.Options options,
                                               int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        return calculateInSampleSize(width, height, reqWidth, reqHeight);
    }

    private static float calculateInSampleSize(int outWidth, int outHeight,
                                               int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        float inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            float heightRatio = div(outHeight, reqHeight);
            float widthRatio = div(outWidth, reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 根据ImageView获得适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    public static ImageSize getImageViewWidth(View imageView) {
        if (imageView == null) return null;
        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = imageView.getContext()
                .getResources().getDisplayMetrics();
        final LayoutParams params = imageView.getLayoutParams();
        int width = 0;
        int height = 0;
        if (params != null) {
            width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
                    .getWidth(); // Get actual image width
            if (width <= 0)
                width = params.width; // Get layout width parameter

            height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
                    .getHeight(); // Get actual image height
            if (height <= 0)
                height = params.height; // Get layout height parameter
        }
        if (width <= 0)
            width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
        // maxWidth
        // parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;

        if (height <= 0)
            height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
        // maxHeight
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;

        if (width <= 0 || height <= 0) return null;

        imageSize.width = width;
        imageSize.height = height;

        return imageSize;
    }

    public static class ImageSize {
        public int width;
        public int height;
    }

    /**
     * 反射获得ImageView设置的最大宽度和高度
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
        }
        return value;
    }


    //相除
    public static float div(float d1, float d2) {
        return div(d1, d2, 10);
    }

    public static float div(float d1, float d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
