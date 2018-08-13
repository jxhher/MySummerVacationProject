package com.example.asus.summervacationproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作用：存储个人信息类
 * Created by ASUS on 2018/8/9.
 */

public class UserInfo {

    public static String getUserInfo(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void saveUserInfo(Context context, String key,String value) {
        SharedPreferences sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getShoppingCartInfo(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("shopping_cart",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void saveShoppingCartInfo(Context context, String key,String value) {
        SharedPreferences sp = context.getSharedPreferences("shopping_cart",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
}
