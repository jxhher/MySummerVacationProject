package com.example.asus.summervacationproject.fragment;

import android.view.View;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;

/**
 * Created by ASUS on 2018/7/28.
 */

public class Goods_Details_Fragment extends BaseFragment {
    View view;
    @Override
    protected View initView() {
        view = View.inflate(mContext, R.layout.fragments_goods_detaisl,null);
        TextView goods_details_textView  = (TextView) view.findViewById(R.id.goods_details_textView);
        return view;
    }
}
