package com.example.asus.summervacationproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.asus.summervacationproject.baseclass.BaseFragment;

import java.util.ArrayList;

/**
 * 作用：个人关注页面的数据适配器
 * Created by ASUS on 2018/8/13.
 */

public class MyAttentionViewPagerAdapter extends FragmentPagerAdapter {
    public final ArrayList<BaseFragment> fragments;
    public String title = null;

    public MyAttentionViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                title = "商品";
                break;
            case 1:
                title = "店铺";
                break;
        }
        return title;
    }
}
