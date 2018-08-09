package com.example.asus.summervacationproject.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.GoodsBean;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/7/28.
 * Updated by ASUS on 2018/7/30  完成商品信息主页面的的布局
 * Updated by ASUS on 2018/8/8   完成商品信息的联网获取,完成店铺相关信息的联网获取
 */

public class Goods_Main_Fragment extends BaseFragment {
    public View view = null;
    public GoodsBean goodBean;
    public Shop shop;

    @BindView(R.id.good_main_name)
    TextView good_main_name;
    @BindView(R.id.good_main_imageView)
    ImageView good_main_imageView;
    @BindView(R.id.good_main_expressage)
    TextView good_main_expressage;
    @BindView(R.id.good_main_site)
    TextView good_main_site;
    @BindView(R.id.good_main_salesVolume)
    TextView good_main_salesVolume;
    @BindView(R.id.good_main_price)
    TextView good_main_price;
    @BindView(R.id.good_main_shopName)
    TextView good_main_shopName;
    @BindView(R.id.good_main_attention_amount)
    TextView good_main_attention_amount;
    @BindView(R.id.good_main_goods_amount)
    TextView good_main_goods_amount;
    @BindView(R.id.good_main_introduce)
    TextView good_main_introduce;
    @BindView(R.id.good_main_merchantService)
    TextView good_main_merchantService;
    @BindView(R.id.good_main_expressageService)
    TextView good_main_expressageService;



    public Goods_Main_Fragment(GoodsBean goodBean, Shop shop) {
        this.goodBean = goodBean;
        this.shop = shop;
    }



    @Override
    protected View initView() {
        view = View.inflate(mContext, R.layout.fragment_goods_main,null);
      //  TextView goods_main_textView  = (TextView) view.findViewById(R.id.goods_main_textView);
        ButterKnife.bind(this,view);
        initDate();
        return view;
    }

    @Override
    protected void initDate() {
        if(goodBean!=null){
            Picasso.with(mContext).load(Config.BASE_URL_IMAGE+goodBean.getImageUrl()).into(good_main_imageView);
            good_main_price.setText("¥ "+goodBean.getCover_price()+".00");
            good_main_expressage.setText("快递费:"+goodBean.getExpressage_price()+".00");
            good_main_salesVolume.setText("月销量:"+goodBean.getSalesVolume());
            good_main_site.setText(goodBean.getSite());
            good_main_name.setText(goodBean.getName());
        }
        if(shop!=null){
            good_main_shopName.setText(shop.getShopName());
            good_main_attention_amount.setText(shop.getAttentionAmount());
            good_main_goods_amount.setText(shop.getGoodsAmount());
            good_main_introduce.setText(shop.getIntroduce());
            good_main_merchantService.setText(shop.getMerchantService());
            good_main_expressageService.setText(shop.getExpressageService());
        }
    }
}
