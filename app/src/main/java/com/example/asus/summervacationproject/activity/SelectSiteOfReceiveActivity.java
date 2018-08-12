package com.example.asus.summervacationproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.OrderFormBean;
import com.example.asus.summervacationproject.bean.SiteOfReceive;
import com.example.asus.summervacationproject.utils.AddSubView;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/11.
 */

public class SelectSiteOfReceiveActivity extends AppCompatActivity {

    @BindView(R.id.receiver_name)
    TextView receiverName;
    @BindView(R.id.receiver_phoneNumber)
    TextView receiver_phoneNumber;
    @BindView(R.id.receiver_site)
    TextView receiver_site;

    @BindView(R.id.receive_shopName)
    TextView receive_shopName;
    @BindView(R.id.receive_imageView)
    ImageView receive_imageView;
    @BindView(R.id.receive_goodName)
    TextView receive_goodName;
    @BindView(R.id.receiver_price)
    TextView receiver_price;
    @BindView(R.id.receiver_total_price)
    TextView receiver_total_price;
    @BindView(R.id.receiver_addSubView)
    AddSubView receiver_addSubView;
    @BindView(R.id.receiver_siteOfReceive_linearLayout)
    LinearLayout receiver_siteOfReceive_linearLayout;

    private  OrderFormBean  orderFormBean= null;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_site_of_receive);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        receiver_addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                receiver_total_price.setText("共 ¥ "+Double.parseDouble(orderFormBean.getPrice())*value);
            }
        });

        receiver_siteOfReceive_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSiteOfReceiveActivity.this,ManageSiteOfReceiveActivity.class);
                intent.putExtra("id","1");
                startActivityForResult(intent,1);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void readStickyMsg(OrderFormBean event){
        orderFormBean = event;
        Picasso.with(SelectSiteOfReceiveActivity.this).load(Config.BASE_URL_IMAGE+event.getImageUrl()).into(receive_imageView);
        receive_shopName.setText(event.getShopName());
        receive_goodName.setText(event.getGoodName());
        receiver_price.setText("¥ "+event.getPrice()+".00");
        receiver_total_price.setText("共 ¥ "+Integer.parseInt(event.getPrice())*event.getAmount()+".00");
        receiver_addSubView.setValue(event.getAmount());
        getSiteOfReceive(event.getSiteOfReceiveId());

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().removeStickyEvent(OrderFormBean.class);
        super.onDestroy();
    }

    @OnClick(R.id.receive_linearLayout_back)
    void OnBackButtonClick(){
        finish();
    }

    private void getSiteOfReceive(final int siteOfReceiveId) {
        new OkHttpUtils(Config.GET_SITEOFRECEIVE+"?siteOfReceiveId="+siteOfReceiveId, HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(!result.equals("ERROR")){
                    SiteOfReceive siteOfReceive = JSON.parseObject(result,SiteOfReceive.class);
                    Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"siteOfReceive:"+siteOfReceive);
                    receiverName.setText(siteOfReceive.getName());
                    receiver_phoneNumber.setText(siteOfReceive.getPhoneNumber());
                    receiver_site.setText(siteOfReceive.getSite());
                }
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {

            }
        },null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String siteOfReceiveId = data.getStringExtra("siteOfReceiveId");
            orderFormBean.setSiteOfReceiveId(Integer.parseInt(siteOfReceiveId));
            getSiteOfReceive(Integer.parseInt(siteOfReceiveId));
        }
    }


    @OnClick(R.id.receiver_button)
    void OnAffirmButtonClick(){
        orderFormBean.setUserId(Integer.parseInt(UserInfo.getUserInfo(SelectSiteOfReceiveActivity.this,"id")));

        String jsonData = JSON.toJSONString(orderFormBean);

        new OkHttpUtils(Config.ADD_ORDERFORM, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(!result.equals("ERROR")){
                    updateLocalUserInfo(result);
                    ToastUtils.getShortToastByString(SelectSiteOfReceiveActivity.this,"下单成功");
                    finish();
                }else{
                    ToastUtils.getShortToastByString(SelectSiteOfReceiveActivity.this,"下单失败");
                }
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(SelectSiteOfReceiveActivity.this,"提交失败");
            }
        },jsonData);
    }

    private void updateLocalUserInfo(String orderFormIds) {
        UserInfo.saveUserInfo(SelectSiteOfReceiveActivity.this,"idOfOrderForm",orderFormIds);
    }

}
