package com.example.asus.summervacationproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.asus.summervacationproject.baseclass.BaseFragment;

import java.util.ArrayList;

/**
 * Created by ASUS on 2018/7/28.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public final ArrayList<BaseFragment> fragments;
    public String title = null;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
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
               title = "详情";
               break;
           case 2:
               title = "评价";
               break;
       }
        return title;
    }
}
