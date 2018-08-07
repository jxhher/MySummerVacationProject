package com.example.asus.summervacationproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/7/30.
 * Updated by ASUS on 2018/8/5 完成登录功能及获取服务器端数据，完成对应UI的数据显示
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_button_back)
    ImageButton login_button_back;
    @BindView(R.id.loginPage_progressBar)
    ProgressBar loginPage_ProgressBar;
    @BindView(R.id.et_login_number)
    EditText et_login_number;
    @BindView(R.id.et_login_pwd)
    EditText et_login_pwd;
    final String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        login_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_login)
      void login(){
        InputMethodManager imm =(InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_login_pwd.getWindowToken(), 0);   //收回软键盘

        loginPage_ProgressBar.setVisibility(View.VISIBLE);
        String phoneNumber = et_login_number.getText().toString().trim();
        String password = et_login_pwd.getText().toString().trim();
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"账号不能为空",Toast.LENGTH_SHORT).show();
            loginPage_ProgressBar.setVisibility(View.GONE);
            return;
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            loginPage_ProgressBar.setVisibility(View.GONE);
            return;
        }else{
            Log.e(TAG,phoneNumber+"  "+password);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phoneNumber",phoneNumber);
                jsonObject.put("password",password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new OkHttpUtils(Config.LOGINPAGE_URL, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    if(result.equals("ERROR_FIND")){
                        Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        User user = JSON.parseObject(result,User.class);
                        saveUser(user);
                        changeUI();
                        finish();
                        Log.e(TAG,"登录成功");
                    }
                }
            }, new OkHttpUtils.FailCallback() {
                   @Override
                public void onFail() {
                    Toast.makeText(LoginActivity.this,"联网失败",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"联网失败");
                }
            },jsonObject.toString());
            loginPage_ProgressBar.setVisibility(View.GONE);
        }
    }

    private void changeUI() {
        Intent intent = new Intent("com.example.asus.summervacationproject.activity.LoginActivity");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);     //发送广播更新抽屉，显示用户名及头像
    }

    private void saveUser(User user) {
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);  //保存数据到本地
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id",user.getId()+"");
        editor.putString("name",user.getName());
        editor.putString("password",user.getPassword());
        editor.putString("imageUrl",user.getImageUrl());
        editor.putString("phoneNumber",user.getPhoneNumber()+"");
        editor.putString("sex", user.getSex());
        editor.putString("idOfBuyed",user.getIdOfBuyed());
        editor.putString("idOfShoppingCart",user.getIdOfShoppingCart());
        editor.putString("siteOfReceive",user.getSiteOfReceive());
        editor.commit();//必须提交，否则保存不成功
    }


    @OnClick(R.id.btn_register)
      void register(){
        loginPage_ProgressBar.setVisibility(View.VISIBLE);
    }
}
