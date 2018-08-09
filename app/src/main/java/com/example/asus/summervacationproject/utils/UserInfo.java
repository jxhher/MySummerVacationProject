package com.example.asus.summervacationproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
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
}
