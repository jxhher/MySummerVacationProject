package com.example.asus.summervacationproject.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.asus.summervacationproject.R;
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
 * Created by ASUS on 2018/8/6.
 * Updated by ASUS on 2018/8/7  完成界面布局及连接数据库进行操作
 */

public class AddSiteOfReceiveActivity extends AppCompatActivity{
    @BindView(R.id.add_receiveName)
    EditText editTextName;
    @BindView(R.id.add_receiveSite)
    EditText editTextSite;
    @BindView(R.id.add_receivePhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.add_receivePostalCode)
    EditText editTextPostalCode;
    @BindView(R.id.manage_add_siteOfReceive_linearLayout)
    LinearLayout backButton;
    @BindView(R.id.add_saveButton)
    Button saveButton;
    private SharedPreferences sp;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_siteofreceive);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
    }

    @OnClick(R.id.manage_add_siteOfReceive_linearLayout)
    void OnBackButtonClick(){
        finish();
    }

    @OnClick(R.id.add_saveButton)
    void OnSaveButtonClick()
    {
        String name = editTextName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String site = editTextSite.getText().toString();
        String postalCode = editTextPostalCode.getText().toString();

        if(TextUtils.isEmpty(name)){
            ToastUtils.getShortToastByString(this,"名称不能为空");
            return;
        }else if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.getShortToastByString(this,"手机号码不能为空");
            return;
        }else if(TextUtils.isEmpty(site)){
            ToastUtils.getShortToastByString(this,"收货地址不能为空");
            return;
        }else if (TextUtils.isEmpty(postalCode)){
            ToastUtils.getShortToastByString(this,"邮政编码称不能为空");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("site",site);
            jsonObject.put("phoneNumber",phoneNumber);
            jsonObject.put("postalCode", postalCode);
            jsonObject.put("userId", sp.getString("id", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        new OkHttpUtils(Config.ADD_SITEOFRECEIVE, HttpMethod.POST,new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                Snackbar.make(saveButton,"添加成功",Snackbar.LENGTH_SHORT).show();
                updateData(result);  //更新本地数据
                broadCastToUpdate();
                finish();
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                Snackbar.make(saveButton,"添加失败",Snackbar.LENGTH_SHORT).show();
            }
        },jsonObject.toString());
    }

    private void broadCastToUpdate() {
        Intent intent = new Intent("com.example.asus.summervacationproject.activity.ManageSiteOfReceiveActivity");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);     //发送广播更新收货地址
    }

    private void updateData(String result) {
        sp =this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String siteOfeReceive = sp.getString("siteOfReceive","");
        System.out.println("result:"+result);
        System.out.println("siteOfReceive:"+siteOfeReceive);
        if("".equals(siteOfeReceive)){
            siteOfeReceive=siteOfeReceive+result;
        }else{
            siteOfeReceive=siteOfeReceive+","+result;
        }
        editor.putString("siteOfReceive",siteOfeReceive);
        editor.commit();
    }
}

