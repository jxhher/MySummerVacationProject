package com.example.asus.summervacationproject.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;

import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/6.
 * Updated by ASUS on 2018/8/6 完成界面的基本布局及对应的数据显示
 */

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.userinfo_linearLayout_back)
    LinearLayout userinfo_button_back;
    @BindView(R.id.userInfo_linearLayout_name)
    RelativeLayout userInfo_linearLayout_name;
    @BindView(R.id.userInfo_linearLayout_sex)
    RelativeLayout userInfo_linearLayout_sex;
    @BindView(R.id.userInfo_linearLayout_imageView)
    RelativeLayout userInfo_linearLayout_imageView;
    @BindView(R.id.userInfo_linearLayout_siteOfReceive)
    RelativeLayout userInfo_linearLayout_siteOfReceive;
    @BindView(R.id.userInfo_name)
    TextView userInfo_name;
    @BindView(R.id.userInfo_sex)
    TextView userInfo_sex;

    private String updateFlag = null;
    private final int IMAGEURL = 0;
    private final int NAME = 1;
    private final int SEX = 2;
    private final int SITEOFRECEIVE = 3;
    private boolean flag = false;
    private int id = -1;
    private String type = null;
    private int updateType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id").toString());
        userInfo_name.setText(intent.getStringExtra("name"));
        userInfo_sex.setText(intent.getStringExtra("sex"));

    }
    @OnClick(R.id.userinfo_linearLayout_back)
    void OnBackButtonClick(){
        finish();
    }
    @OnClick(R.id.userInfo_linearLayout_imageView)
    void OnImageViewClick(){

    }
    @OnClick(R.id.userInfo_linearLayout_name)
    void OnNameClick(){
        final EditText et = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("设置你的名称")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        updateUserInfo(NAME,input);

                    }
                }).setNegativeButton("取消",null);
        topInputMethod(dialog,et);
    }




    private void updateUserInfo(int i,final String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        switch (i){
            case IMAGEURL:
                break;
            case NAME:
                type = "name";
                updateType = NAME;
                jsonObject.put(type,data);
                break;
            case SEX:
                type = "sex";
                updateType = SEX;
                jsonObject.put(type,data);
                break;
            case SITEOFRECEIVE:
                break;
        }

        new OkHttpUtils(Config.UPDATE_USER_URL, HttpMethod.POST,new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences sp = UserInfoActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(type,data);
                editor.commit();
                Message msg = new Message();
                msg.arg1 = 1;
                msg.arg2 = updateType;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                Message msg = new Message();
                msg.arg1 = 0;
                handler.sendMessage(msg);
            }
        },jsonObject.toString());
    }

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1){
                    case 0:
                        Snackbar.make(userInfo_linearLayout_siteOfReceive, "修改失败", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        switch (msg.arg2){
                            case NAME:
                                userInfo_name.setText((String) msg.obj);
                                Intent intent = new Intent("com.example.asus.summervacationproject.activity.LoginActivity");
                                LocalBroadcastManager.getInstance(UserInfoActivity.this).sendBroadcast(intent);
                                break;
                            case SEX:
                                userInfo_sex.setText((String)msg.obj);
                                break;
                        }
                        Snackbar.make(userInfo_linearLayout_siteOfReceive, "修改成功", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        };

    @OnClick(R.id.userInfo_linearLayout_sex)
    void OnSexClick(){
        final EditText et2 = new EditText(this);

        AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("设置你的性别")
                .setView(et2)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input2 = et2.getText().toString();
                        updateUserInfo(SEX,input2);
                    }
                }).setNegativeButton("取消",null);
        topInputMethod(dialog2,et2);
    }
    @OnClick(R.id.userInfo_linearLayout_siteOfReceive)
    void OnSiteOfReceiveClick(){
            Intent intent = new Intent(UserInfoActivity.this,ManageSiteOfReceiveActivity.class);
            startActivity(intent);
    }

    private void topInputMethod(AlertDialog.Builder dialog, final EditText et3) {

        //下面是弹出键盘的关键处
        AlertDialog tempDialog = dialog.create();
        tempDialog.setView(et3, 0, 0, 0, 0);

        tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et3, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        tempDialog.show();
    }
}
