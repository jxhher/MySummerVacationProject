package com.example.asus.summervacationproject.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.ViewPagerAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.GoodsBean;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.fragment.Goods_Details_Fragment;
import com.example.asus.summervacationproject.fragment.Goods_Comment_Fragment;
import com.example.asus.summervacationproject.fragment.Goods_Main_Fragment;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/7/23.
 * Updated by ASUS on 2018/8/8   更新优化界面布局，增加店铺布局，完成联网获取数据及显示
 * Updated by ASUS on 2018/8/9   优化代码结构，更新界面UI图标
 */

public class GoodsInfoActivity extends AppCompatActivity {
    @BindView(R.id.good_tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.good_viewPager)
    ViewPager viewPager;


    public GoodsBean goodsBean;
    public GoodsBean updateGoodBean;
    public ArrayList<BaseFragment> fragments;
    public ViewPagerAdapter viewPagerAdapter;
    public Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        goodsBean = (GoodsBean) getIntent().getSerializableExtra("goodBean");
        System.out.println("shopId:"+goodsBean.getShopId());
        if (goodsBean != null) {
            Toast.makeText(this, "goodId=="+goodsBean.getGoodId(), Toast.LENGTH_SHORT).show();
            fragments = new ArrayList<>();
            initData(goodsBean);
        }
        setData();
    }

    private void setData() {
        if(updateGoodBean!=null){
            fragments.add(new Goods_Main_Fragment(updateGoodBean,shop));
            fragments.add(new Goods_Details_Fragment(updateGoodBean));
            fragments.add(new Goods_Comment_Fragment(goodsBean.getShopId()));
            //设置ViewPager的适配器
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
            viewPager.setAdapter(viewPagerAdapter);
            //关联ViewPager
            tabLayout.setupWithViewPager(viewPager);
            //设置固定的
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private void initData(GoodsBean goodsBean) {
        new OkHttpUtils(Config.GET_GOOD_URL + "?goodId=" + goodsBean.getGoodId(), HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("ERROR")){
                    ToastUtils.getShortToastByString(GoodsInfoActivity.this,"获取数据失败");
                    return;
                }else{
                    updateGoodBean = JSON.parseObject(result,GoodsBean.class);
                    new OkHttpUtils(Config.GET_SHOP_URL+"?shopId="+updateGoodBean.getShopId(), HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
                        @Override
                        public void onSuccess(String result) {
                            shop = JSON.parseObject(result,Shop.class);
                            setData();
                        }
                    }, new OkHttpUtils.FailCallback() {
                        @Override
                        public void onFail() {

                        }
                    },null);
                 }

             }
         }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
            }
        },null);



    }

    @OnClick(R.id.good_bottom_collection)
        public void OnCollectionClick(){
            Toast.makeText(this,"收藏被点击",Toast.LENGTH_SHORT).show();
        }

        @OnClick(R.id.good_bottom_service)
        public void OnServiceClick(){
            Toast.makeText(this,"客服被点击",Toast.LENGTH_SHORT).show();
        }
        @OnClick(R.id.good_bottom_shopping)
        public void OnShoppingClick(){
            Toast.makeText(this,"店铺被点击",Toast.LENGTH_SHORT).show();
        }

        @OnClick(R.id.good_back_imageButton48)
        public void OnBackButtonClick(){
            Toast.makeText(this,"返回被点击",Toast.LENGTH_SHORT).show();
            this.finish();
        }

        @OnClick(R.id.goods_bottom_bug_now)
        public void OnBugClick(){
            Toast.makeText(this,"购买被点击",Toast.LENGTH_SHORT).show();
        }

        @OnClick(R.id.good_bottom_add_cart)
        public void OnAddCartClick(){
            Toast.makeText(this,"加入购物车被点击",Toast.LENGTH_SHORT).show();
        }
}
