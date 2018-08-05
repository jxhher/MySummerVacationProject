package com.example.asus.summervacationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.summervacationproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/6.
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
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

    }
    @OnClick(R.id.userInfo_linearLayout_sex)
    void OnSexClick(){

    }
    @OnClick(R.id.userInfo_linearLayout_siteOfReceive)
    void OnSiteOfReceiveClick(){
        Toast.makeText(this,"LinearLayout被点击",Toast.LENGTH_SHORT).show();
    }
}
