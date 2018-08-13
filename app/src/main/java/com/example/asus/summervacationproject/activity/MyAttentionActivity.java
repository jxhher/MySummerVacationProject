package com.example.asus.summervacationproject.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.MyAttentionViewPagerAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.fragment.MyAttention_Shop_Fragment;
import com.example.asus.summervacationproject.fragment.MyAttention_goods_Fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作用：显示个人关注
 * Created by ASUS on 2018/8/13.
 */

public class MyAttentionActivity extends AppCompatActivity {

    @BindView(R.id.myAttention_tabLayout)
    TabLayout myAttention_tabLayout;
    @BindView(R.id.myAttention_viewPager)
    ViewPager myAttention_viewPager;


    private ArrayList<BaseFragment> fragments ;
    private MyAttentionViewPagerAdapter myAttentionViewPagerAdapter;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();
        fragments.add(new MyAttention_goods_Fragment());
        fragments.add(new MyAttention_Shop_Fragment());
        //设置ViewPager的适配器
        myAttentionViewPagerAdapter = new MyAttentionViewPagerAdapter(getSupportFragmentManager(),fragments);
        myAttention_viewPager.setAdapter(myAttentionViewPagerAdapter);
        //关联ViewPager
        myAttention_tabLayout.addTab(myAttention_tabLayout.newTab().setText("商品"));
        myAttention_tabLayout.addTab(myAttention_tabLayout.newTab().setText("店铺"));
        myAttention_tabLayout.setupWithViewPager(myAttention_viewPager);
        //设置固定的
        myAttention_tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @OnClick(R.id.myAttention_linearLayout_back)
    void OnBackClick(){
        finish();
    }
}
