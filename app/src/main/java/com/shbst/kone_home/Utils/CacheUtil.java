package com.shbst.kone_home.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by hasee on 2018/3/13.
 * <p>
 * bitmap 缓存，解决页面切换时，加载页面卡顿问题
 * <p>
 * 最多占用内存 1/8
 */

public class CacheUtil {

    private static LruCache<String, Bitmap> lruCache;

    static {
        createCache();
    }

    public static void putThemeBitmap(Context context, int key, Bitmap bitmap) {
        putBitmap(getThemeInfo(context, key), bitmap);
    }

    public static Bitmap getThemeBitmap(Context context, int key) {
        return getBitmap(getThemeInfo(context, key));
    }

    public static void putBitmap(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    public static Bitmap getBitmap(String key) {
        return lruCache.get(key);
    }

    public static Bitmap removeBitmap(String key) {
        return lruCache.remove(key);
    }

    private static String getThemeInfo(Context context, int key) {
        int currentThemeMode = PreferManager.getInstence().getThemeMode(context);
        switch (currentThemeMode) {
            case Constant.THEME_BLOCKS:
                return "theme_blocks_" + key;
            case Constant.THEME_SPHERE:
                return "theme_sphere_" + key;
            default:
                throw new Resources.NotFoundException("未知主题");
        }
    }

    private static void createCache() {
        if (lruCache == null) {
//获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int mCacheSize = maxMemory / 8;
            //给LruCache分配1/8 4M
            lruCache = new LruCache<String, Bitmap>(mCacheSize) {
                //必须重写此方法，来测量Bitmap的大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }
    }
}
