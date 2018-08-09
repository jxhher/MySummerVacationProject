package com.example.asus.summervacationproject.fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/19.
 */

public class ShoppingCartFragment extends BaseFragment {
    private static final String TAG = ShoppingCartFragment.class.getSimpleName();
    @BindView(R.id.shopCart_attention_button)
    Button shopCart_attention_button;
    @BindView(R.id.shopCart_check_delete_all)
    CheckBox shopCart_check_delete_all;
    @BindView(R.id.shopCart_delete_button)
    Button shopCart_delete_button;

    @BindView(R.id.shopCart_checkbox_out_all)
    CheckBox shopCart_checkbox_out_all;
    @BindView(R.id.shopCart_check_out_button)
    Button shopCart_check_out_button;


    @Override
    protected View initView() {
        Log.e(TAG,"购物车Fragment页面被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_shoppingcart,null);
        ButterKnife.bind(this,view);
        return view;
    }
}
