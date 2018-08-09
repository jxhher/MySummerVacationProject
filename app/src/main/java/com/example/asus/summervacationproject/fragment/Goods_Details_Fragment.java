package com.example.asus.summervacationproject.fragment;

import android.view.View;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.GoodsBean;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/28.
 * Updated by ASUS on 2018/7/30  完成商品详情页面的布局
 * Updated by ASUS on 2018/8/8   完成商品详情页面的数据获取及显示
 */

public class Goods_Details_Fragment extends BaseFragment {
    View view;
    GoodsBean goodBean;
    @BindView(R.id.good_details_good_brand)
    TextView good_details_brand;
    @BindView(R.id.good_details_good_expiration_date)
    TextView getGood_details_good_expiration_date;
    @BindView(R.id.good_details_good_manufacturing_enterprise)
    TextView good_details_good_manufacturing_enterprise;
    @BindView(R.id.good_details_good_version)
    TextView good_details_good_version;


    public Goods_Details_Fragment(GoodsBean goodsBean) {
        this.goodBean = goodsBean;
    }

    @Override
    protected View initView() {
        view = View.inflate(mContext, R.layout.fragments_goods_detaisl,null);
        ButterKnife.bind(this,view);
        initDate();
        return view;
    }

    @Override
    protected void initDate() {
        if(goodBean!=null){
            good_details_brand.setText(goodBean.getBrand());
            getGood_details_good_expiration_date.setText(goodBean.getExpirationDate());
            good_details_good_manufacturing_enterprise.setText(goodBean.getManufacturing_enterprise());
            good_details_good_version.setText(goodBean.getVersion());
        }

    }
}

