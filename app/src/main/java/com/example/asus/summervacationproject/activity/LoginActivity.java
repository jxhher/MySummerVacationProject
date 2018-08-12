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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.bean.ShoppingCartList;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/7/30.
 * Updated by ASUS on 2018/8/5 完成登录功能及获取服务器端数据，完成对应UI的数据显示
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_linearLayout_back)
    LinearLayout login_linearLayout_back;
    @BindView(R.id.loginPage_progressBar)
    ProgressBar loginPage_ProgressBar;
    @BindView(R.id.et_login_number)
    EditText et_login_number;
    @BindView(R.id.et_login_pwd)
    EditText et_login_pwd;
    final String TAG = LoginActivity.class.getSimpleName();
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        login_linearLayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_login)
      void login() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_login_pwd.getWindowToken(), 0);   //收回软键盘

        loginPage_ProgressBar.setVisibility(View.VISIBLE);
        String phoneNumber = et_login_number.getText().toString().trim();
        String password = et_login_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            loginPage_ProgressBar.setVisibility(View.GONE);
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            loginPage_ProgressBar.setVisibility(View.GONE);
            return;
        } else {
            Log.e(TAG, phoneNumber + "  " + password);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phoneNumber", phoneNumber);
                jsonObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new OkHttpUtils(Config.LOGINPAGE_URL, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    if (result.equals("ERROR_FIND")) {
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        User user = JSON.parseObject(result, User.class);
                        saveUser(user);
                        setShoppingCart();
                        changeUI();

                        Intent intent = new Intent();
                        Log.e(GoodsInfoActivity.class.getSimpleName(), "login_userId" + user.getId());
                        intent.putExtra("shoppingCartBeanList",user.getIdOfShoppingCart()+"");
                        intent.putExtra("userId",user.getId()+"");
                        intent.putExtra("siteOfReceiveId",user.getSiteOfReceive()+"");
                        LoginActivity.this.setResult(1,intent);
                        LoginActivity.this.setResult(2,intent);
                        LoginActivity.this.setResult(3,intent);

                        finish();

                        Log.e(TAG, "登录成功");
                    }
                }
            }, new OkHttpUtils.FailCallback() {
                @Override
                public void onFail() {
                    Toast.makeText(LoginActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "联网失败");
                }
            }, jsonObject.toString());
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
        editor.putString("age",user.getAge()+"");
        editor.putString("idOfBuyed",user.getIdOfBuyed());
        editor.putString("idOfShoppingCart",user.getIdOfShoppingCart());
        editor.putString("siteOfReceive",user.getSiteOfReceive());
        editor.putString("idOfOrderForm",user.getIdOfOrderForm());
        editor.commit();//必须提交，否则保存不成功
    }


    @OnClick(R.id.login_register)
      void register(){
        loginPage_ProgressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void setShoppingCart() {
        getIds();
        if(jsonArray!=null){
            new OkHttpUtils(Config.GET_SHOPPINGCART, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                 //   List<ShoppingCartBean> shoppingCartBeanList = JSON.parseArray(result,ShoppingCartBean.class);
                    EventBus.getDefault().post(new ShoppingCartList(JSON.parseArray(result,ShoppingCartBean.class)));
                    Log.e(LoginActivity.class.getSimpleName(),"login发送信息");
                    saveShoppingCartInfo(result);
                }
            }, new OkHttpUtils.FailCallback() {
                @Override
                public void onFail() {

                }
            },jsonArray.toString());
        }
    }


    private void getIds() {
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String ids = sp.getString("idOfShoppingCart","");
        if(ids.equals(""))return;
        System.out.println("ids:"+ids);
        if(!ids.equals("")) {
            String[] idList = ids.split(",");

           jsonArray = new JSONArray();

            for (int i = 0; i < idList.length; i++) {
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                try {
                    json.put("shoppingCartId", idList[i]);//JSONObject对象中添加键值对
                    jsonArray.add(json);//将JSONObject对象添加到Json数组中
                } catch (com.alibaba.fastjson.JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(idList[i] + "  ");
            }
        }
    }



    private void saveShoppingCartInfo(String result) {
        Log.e(LoginActivity.class.getSimpleName(),result.toString());
        SharedPreferences sp = LoginActivity.this.getSharedPreferences("shopping_cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("shopping_cart_"+ UserInfo.getUserInfo(LoginActivity.this,"id")+"",result);
        editor.commit();
    }
}
