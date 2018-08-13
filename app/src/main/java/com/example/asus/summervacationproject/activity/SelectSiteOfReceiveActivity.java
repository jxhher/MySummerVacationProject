package com.example.asus.summervacationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.SelectSiteOfReceiveAdapter;
import com.example.asus.summervacationproject.bean.OrderFormBean;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.bean.SiteOfReceive;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作用：确认订单页面，可实现收货地址的选择
 * Created by ASUS on 2018/8/11.
 * Updated by ASUS on 2018/8/12  解决数据传输问题及Fragment生命周期问题
 */

public class SelectSiteOfReceiveActivity extends AppCompatActivity {

    @BindView(R.id.receiver_name)
    TextView receiverName;
    @BindView(R.id.receiver_phoneNumber)
    TextView receiver_phoneNumber;
    @BindView(R.id.receiver_site)
    TextView receiver_site;

    @BindView(R.id.receiver_total_price)
    TextView receiver_total_price;
    @BindView(R.id.receiver_siteOfReceive_linearLayout)
    LinearLayout receiver_siteOfReceive_linearLayout;
    @BindView(R.id.receiver_button)
    Button receiver_button;

    @BindView(R.id.receiver_listView)
    ListView receiver_listView;
    private List<ShoppingCartBean> shoppingCartBeanList = null;
    private SelectSiteOfReceiveAdapter selectSiteOfReceiveAdapter = null;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_site_of_receive);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userId = Integer.parseInt(UserInfo.getUserInfo(this,"id"));
        Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"userID"+userId);

        Intent intent = getIntent();
        shoppingCartBeanList = (List<ShoppingCartBean>) intent.getSerializableExtra("shopping_cart_list");
        if(shoppingCartBeanList!=null&&shoppingCartBeanList.size()!=0){
            Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"购物车"+shoppingCartBeanList.toString());
            initListView(shoppingCartBeanList);
        }

        receiver_siteOfReceive_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSiteOfReceiveActivity.this, ManageSiteOfReceiveActivity.class);
                intent.putExtra("id", "1");
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initListView(List<ShoppingCartBean> shoppingCartBeanList2) {
        if (shoppingCartBeanList2 != null && shoppingCartBeanList2.size() != 0) {
            selectSiteOfReceiveAdapter = new SelectSiteOfReceiveAdapter(this, shoppingCartBeanList2);
            receiver_listView.setAdapter(selectSiteOfReceiveAdapter);
            updateTotalPrice(shoppingCartBeanList2);
        }
    }

    public void updateTotalPrice(List<ShoppingCartBean> shoppingCartBeanList3) {
        int total = 0;
        for (int i = 0; i < shoppingCartBeanList3.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList3.get(i);
            total += Double.parseDouble(shoppingCartBean.getAmount()) * Double.parseDouble(shoppingCartBean.getGoodPrice());
        }
        receiver_total_price.setText("共 ¥ " + total);
        if (shoppingCartBeanList3.size() == 0) {
            ToastUtils.getShortToastByString(this, "没有要提交的订单");
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void readStickyMsg(OrderFormBean event) {
        ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
        shoppingCartBean.setAmount(event.getAmount() + "");
        shoppingCartBean.setGoodName(event.getGoodName());
        shoppingCartBean.setGoodPrice(event.getPrice());
        shoppingCartBean.setImageUrl(event.getImageUrl());
        shoppingCartBean.setShopName(event.getShopName());
        shoppingCartBean.setUserId(event.getUserId());
        shoppingCartBean.setGoodId(event.getGoodId());
        final List<ShoppingCartBean> shoppingCartBeen = new ArrayList<>();
        shoppingCartBeen.add(shoppingCartBean);
        shoppingCartBeanList= shoppingCartBeen;
        Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"1    "+shoppingCartBeanList.toString());
        initListView(shoppingCartBeanList);
        getSiteOfReceive(event.getSiteOfReceiveId());

        receiver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonData = JSON.toJSONString(shoppingCartBeen);
                Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"2    "+shoppingCartBeen.toString());
                Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"jsonData"+jsonData);
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
        });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().removeStickyEvent(OrderFormBean.class);
        super.onDestroy();
    }

    @OnClick(R.id.receive_linearLayout_back)
    void OnBackButtonClick() {
        finish();
    }

    private void getSiteOfReceive(final int siteOfReceiveId) {
        new OkHttpUtils(Config.GET_SITEOFRECEIVE + "?siteOfReceiveId=" + siteOfReceiveId, HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (!result.equals("ERROR")) {
                    SiteOfReceive siteOfReceive = JSON.parseObject(result, SiteOfReceive.class);
                    Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(), "siteOfReceive:" + siteOfReceive);
                    if (siteOfReceive != null) {
                        receiverName.setText(siteOfReceive.getName());
                        receiver_phoneNumber.setText(siteOfReceive.getPhoneNumber());
                        receiver_site.setText(siteOfReceive.getSite());
                    }
                }
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {

            }
        }, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String siteOfReceiveId = data.getStringExtra("siteOfReceiveId");
            getSiteOfReceive(Integer.parseInt(siteOfReceiveId));
        }
    }



    @OnClick(R.id.receiver_button)
    void OnAffirmButtonClick(){

        String jsonData = JSON.toJSONString(shoppingCartBeanList);

        Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"2    "+shoppingCartBeanList.toString());
        Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"jsonData"+jsonData);
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
        Log.e(SelectSiteOfReceiveActivity.class.getSimpleName(),"orderFormId"+orderFormIds);
        UserInfo.saveUserInfo(SelectSiteOfReceiveActivity.this,"idOfOrderForm",orderFormIds);
    }

}
