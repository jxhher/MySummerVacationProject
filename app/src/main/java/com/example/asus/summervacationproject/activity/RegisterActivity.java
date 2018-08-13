package com.example.asus.summervacationproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作用：个人注册页面
 * Created by ASUS on 2018/8/8.
 * Updated by ASUS on 2018/8/8 完成用户注册功能、头像切换功能，解决数据传输bug，优化代码结构
 */

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.register_name)
    EditText register_name;
    @BindView(R.id.register_phoneNumber)
    EditText register_phoneNumber;
    @BindView(R.id.register_sex)
    EditText register_sex;
    @BindView(R.id.register_age)
    EditText register_age;
    @BindView(R.id.register_pwd)
    EditText register_pwd;
    @BindView(R.id.register_pwdagain)
    EditText register_pwdagain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_linearLayout_back)
    void OnBackClick(){
        finish();
    }

    @OnClick(R.id.register_button)
    void OnRegisterButtonClick(){
        String name = register_name.getText().toString();
        String phoneNumber = register_phoneNumber.getText().toString();
        String sex = register_sex.getText().toString();
        String age = register_age.getText().toString();
        String password = register_pwd.getText().toString();
        String passwordAgain = register_pwdagain.getText().toString();

        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.getShortToastByString(this,"手机号不能为空");
            return;
        }else if(TextUtils.isEmpty(name)){
            ToastUtils.getShortToastByString(this,"用户名不能为空");
            return;
        }else if(TextUtils.isEmpty(sex)){
            ToastUtils.getShortToastByString(this,"性别不能为空");
            return;
        }else if(!(sex.equals("男")||sex.equals("女"))){
            ToastUtils.getShortToastByString(this,"性别填写错误");
            return;
        }else if(TextUtils.isEmpty(age)) {
            ToastUtils.getShortToastByString(this, "年龄不能为空");
            return;
        }else if(!(Integer.parseInt(age)>0&&Integer.parseInt(age)<=120)){
            ToastUtils.getShortToastByString(this,"年龄填写错误");
            return;
        }else if(TextUtils.isEmpty(password)){
            ToastUtils.getShortToastByString(this,"密码不能为空");
            return;
        }else if(TextUtils.isEmpty(passwordAgain)){
            ToastUtils.getShortToastByString(this,"确认密码不能为空");
            return;
        }else if(!(password.equals(passwordAgain))){
            ToastUtils.getShortToastByString(this,"密码错误");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("phoneNumber",phoneNumber);
            jsonObject.put("sex",sex);
            jsonObject.put("age",age);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new OkHttpUtils(Config.REGISTER_UTL, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                ToastUtils.getShortToastByString(RegisterActivity.this,"注册成功");
                finish();
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(RegisterActivity.this,"注册失败");
            }
        },jsonObject.toString());
    }
}
