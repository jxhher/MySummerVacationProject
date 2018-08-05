package com.example.asus.summervacationproject.fragment;


import android.view.View;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;

/**
 * Created by ASUS on 2018/7/28.
 * Updated by ASUS on 2018/7/30  完成商品信息主页面的的布局
 */

public class Goods_Main_Fragment extends BaseFragment {
    View view = null;
    @Override
    protected View initView() {
        view = View.inflate(mContext, R.layout.fragment_goods_main,null);
      //  TextView goods_main_textView  = (TextView) view.findViewById(R.id.goods_main_textView);
        return view;
    }
}
