package com.example.wing.schoollibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作用：缓存工具类
 */
public class CacheUtils {



    /**
     * 保持数据
     * @param context
     * @param key
     * @param values
     */
    public static  void putString(Context context,String key,String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("wuyi",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).commit();
    }

    /**
     * 得到缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("wuyi",Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,"");
    }


    /**
     * 清除数据
     * @param context
     */
    public static  void clean(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("wuyi",Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }



}
