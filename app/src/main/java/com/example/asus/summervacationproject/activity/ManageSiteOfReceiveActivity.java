package com.example.asus.summervacationproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.ListViewAdapter;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.bean.SiteOfReceive;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/6.
 * uPdatad by ASUS on 2018/8/7  完成收货地址模块的数据查找、增加、删除，解决数据传输的bug，
 */

public class ManageSiteOfReceiveActivity extends AppCompatActivity {

    @BindView(R.id.manage_add_button)
    Button manage_add_button;
    @BindView(R.id.manage_siteOfReceive_listView)
    ListView manage_siteOfReceive_listView;


    private ListAdapter adapter = null;
    private String jsonData = null;
    private List<SiteOfReceive> SiteOfReceiveList;
    private SharedPreferences sp;
    private IntentFilter intentFilter;

    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private boolean change = false;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_siteofreceive);
        ButterKnife.bind(this);
        if(getIntent().getStringExtra("id").equals("1")){
            //manage_add_button.setVisibility(View.GONE);
            change = true;
            manage_siteOfReceive_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("siteOfReceiveId",SiteOfReceiveList.get(position).getId()+"");
                    ManageSiteOfReceiveActivity.this.setResult(1,intent);
                    finish();
                }
            });
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        registerBroadCast();
        initListView();

    }

    private void initListView() {
        sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String ids = sp.getString("siteOfReceive","");
        System.out.println("ids:"+ids);
        if(!ids.equals("")) {
            String[] idList = ids.split(",");

            final JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < idList.length; i++) {
                JSONObject json = new JSONObject();
                try {
                    json.put("id", idList[i]);//JSONObject对象中添加键值对
                    jsonArray.add(json);//将JSONObject对象添加到Json数组中
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(idList[i] + "  ");
            }

            new OkHttpUtils(Config.FIND_SITEOFRECEIVE_URL, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    SiteOfReceiveList = JSON.parseArray(result, SiteOfReceive.class);
                    adapter = new ListViewAdapter(ManageSiteOfReceiveActivity.this, SiteOfReceiveList, R.layout.item_siteofreceive_listview,change);
                    manage_siteOfReceive_listView.setAdapter(adapter);
                }
            }, new OkHttpUtils.FailCallback() {
                @Override
                public void onFail() {

                }
            }, jsonArray.toString());

        }else{

        }
    }


    private void registerBroadCast() {
        intentFilter = new IntentFilter();                            //动态注册广播，等待接收信息
        intentFilter.addAction("com.example.asus.summervacationproject.activity.ManageSiteOfReceiveActivity");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initListView();                           //刷新页面
        }
    }
            @OnClick(R.id.manage_siteOfReceive_linearLayout_title)
        void OnBackClick(){
            finish();
        }

        @OnClick(R.id.manage_add_button)
        void OnAddButtonClick(){
            Intent intent = new Intent(ManageSiteOfReceiveActivity.this,AddSiteOfReceiveActivity.class);
            startActivity(intent);
        }




}
