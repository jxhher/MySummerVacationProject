package com.example.asus.summervacationproject.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.ListViewAdapter;
import com.example.asus.summervacationproject.adapter.MyOrderFormAdapter;
import com.example.asus.summervacationproject.bean.OrderFormBean;
import com.example.asus.summervacationproject.bean.SiteOfReceive;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/12.
 */

public class MyOrderFormsActivity extends AppCompatActivity {
    @BindView(R.id.myOrderForm_listView)
    ListView myOrderForm_listView;
    @BindView(R.id.myOrderForm_progressBar)
    ProgressBar myOrderForm_progressBar;
    @BindView(R.id.myOrderForm_empty)
    TextView myOrderForm_empty;
    private SharedPreferences sp;
    private List<OrderFormBean> orderFormBeanList;
    private MyOrderFormAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_form);
        ButterKnife.bind(this);
        initListView();
    }

    @OnClick(R.id.myOrderForm_linearLayout_back)
    void OnBackClick(){

        finish();
    }


    private void initListView() {
        myOrderForm_progressBar.setVisibility(View.VISIBLE);
        sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String ids = sp.getString("idOfOrderForm","");
        if(TextUtils.isEmpty(ids)){
            myOrderForm_progressBar.setVisibility(View.GONE);
            myOrderForm_empty.setVisibility(View.VISIBLE);
            return;
        }else{
            myOrderForm_empty.setVisibility(View.GONE);
        }
        Log.e(MyOrderFormsActivity.class.getSimpleName(),ids);
        if(!TextUtils.isEmpty(ids)) {
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

            new OkHttpUtils(Config.GET_ORDERFORM_LIST, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    orderFormBeanList = JSON.parseArray(result,OrderFormBean.class);
                    adapter = new MyOrderFormAdapter(MyOrderFormsActivity.this, orderFormBeanList, R.layout.item_my_order_form_listview);
                    myOrderForm_listView.setAdapter(adapter);
                    myOrderForm_progressBar.setVisibility(View.GONE);
                }
            }, new OkHttpUtils.FailCallback() {
                @Override
                public void onFail() {

                }
            }, jsonArray.toString());

        }else{

        }
    }
}
