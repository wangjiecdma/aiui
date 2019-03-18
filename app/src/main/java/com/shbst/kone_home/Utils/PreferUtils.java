package com.shbst.kone_home.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.Set;

/**
 * Created by zhouwenchao on 2018-02-27.
 */
@SuppressLint("ApplySharedPref")
public class PreferUtils {
    private static final String  preferences_file = "bst_preferences.conf";

    static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_file, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }


    static void setBoolean(Context cont, String key, boolean value) {
        Log.d("prefer","set boolean :"+key+"  boolean :"+value);

        SharedPreferences sp = cont.getSharedPreferences(preferences_file, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferences_file, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    static void setInt(Context cont, String key, int value) {
        Log.d("prefer","set int  :"+key+"  boolean :"+value);

        SharedPreferences sp = cont.getSharedPreferences(preferences_file, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    static String getString(Context ctx, String key, String defaultValue) {
        if (ctx != null && key != null) {
            SharedPreferences sp = ctx.getSharedPreferences(preferences_file, Context.MODE_PRIVATE);
            return sp.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    static void setString(Context ctx, String key, String value) {
        Log.d("prefer"," setString  :"+key+"  boolean :"+value);


        SharedPreferences sp = ctx.getSharedPreferences(preferences_file,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    static Set<String> getSetString(Context ctx, String key, Set<String> value) {
//        if (ctx != null && key != null) {
        SharedPreferences sp = ctx.getSharedPreferences(preferences_file, Context.MODE_PRIVATE);
        return sp.getStringSet(key, value);
//        } else {
//            return value;
//        }
    }

    static void setSetString(Context cont, String key, Set<String> value) {
        Log.d("prefer"," setSetString  :"+key+"  boolean :"+value);


        SharedPreferences sp = cont.getSharedPreferences(preferences_file,
                Context.MODE_PRIVATE);
        sp.edit().putStringSet(key, value).commit();
    }

    public static void clearAppData(Context context){
        SharedPreferences sp = context.getSharedPreferences(preferences_file,
                Context.MODE_PRIVATE);
        sp.edit().clear().commit();


        File file =  context.getFileStreamPath("draw.data");
        if (file.exists()){
            file.delete();
        }

    }
}

