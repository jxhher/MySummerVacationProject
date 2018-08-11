package com.example.asus.summervacationproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.ViewPagerAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.GoodsBean;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.fragment.Goods_Details_Fragment;
import com.example.asus.summervacationproject.fragment.Goods_Comment_Fragment;
import com.example.asus.summervacationproject.fragment.Goods_Main_Fragment;
import com.example.asus.summervacationproject.utils.AddSubView;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;
import com.example.asus.summervacationproject.utils.VirtualkeyboardHeight;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/7/23.
 * Updated by ASUS on 2018/8/8   更新优化界面布局，增加店铺布局，完成联网获取数据及显示
 * Updated by ASUS on 2018/8/9   优化代码结构，更新界面UI图标
 * Updated by ASUS on 2018/8/9   完成加入购物车功能的popupwindow悬浮框
 * Updated by ASUS on 2018/8/10  完成加入购物车数据及个人数据的联网更新和本地更新
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
    public int amount = 1;
    private LinearLayout view;
    private boolean exit = false;
    private String alreadyIdOfShoppingCart;
    private String idOfShoppingCart;

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

        }

        @OnClick(R.id.good_bottom_service)
        public void OnServiceClick(){

        }
        @OnClick(R.id.good_bottom_shopping)
        public void OnShoppingClick(){

        }

        @OnClick(R.id.good_back_imageButton48)
        public void OnBackButtonClick(){

            this.finish();
        }

        @OnClick(R.id.goods_bottom_bug_now)
        public void OnBugClick(){

        }

        @OnClick(R.id.good_bottom_add_cart)
        public void OnAddCartClick()
        {
            showPopwindow();

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            Log.e(GoodsInfoActivity.class.getSimpleName(),"userId"+data.getStringExtra("userId"));
            int userId = Integer.parseInt(data.getStringExtra("userId"));
            addToShoppingCart(userId,updateGoodBean.getShopId());
        }

    }


    private void showPopwindow() {
        // 1 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_add_shopping_cart, null);

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.good_popup_backButton);
        Button affirmButton = (Button) view.findViewById(R.id.good_popup_affirmButton);
        ImageView imageView = (ImageView) view.findViewById(R.id.good_popup_image);
        TextView good_inventory = (TextView) view.findViewById(R.id.good_popup_inventory);
        TextView good_price = (TextView) view.findViewById(R.id.good_popup_price);
        AddSubView goodAmount = (AddSubView)view.findViewById(R.id.good_popup_goodAmount);

        Picasso.with(this).load(Config.BASE_URL_IMAGE+updateGoodBean.getImageUrl()).into(imageView);
        good_inventory.setText("库存: "+updateGoodBean.getInventory()+"件");
        good_price.setText("¥ "+updateGoodBean.getCover_price());
        goodAmount.setMaxValue(Integer.parseInt(updateGoodBean.getInventory()));
        goodAmount.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                amount = value;
            }
        });

        // 2下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                510);

        // 3 参数设置
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1);
                window.dismiss();
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);

        // 5 在底部显示
        window.showAtLocation(GoodsInfoActivity.this.findViewById(R.id.good_bottom),
                Gravity.BOTTOM, 0, VirtualkeyboardHeight.getBottomStatusHeight(GoodsInfoActivity.this));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        affirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(1);
                window.dismiss();
                if(TextUtils.isEmpty(UserInfo.getUserInfo(GoodsInfoActivity.this,"name"))){
                    Intent intent = new Intent(GoodsInfoActivity.this,LoginActivity.class);
                    startActivityForResult(intent,1);
                }else{
                    addToShoppingCart(Integer.parseInt(UserInfo.getUserInfo(GoodsInfoActivity.this,"id")),updateGoodBean.getGoodId());
                }
            }

        });

    }

    private void backgroundAlpha(int bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void addToShoppingCart(int userId, final int goodId) {
        new OkHttpUtils(Config.ADD_SHOPPINGCART+"?userId="+userId+"&goodId="+goodId+"&amount="+amount, HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(!("-1".equals(result))){
                    updateShoppingCart(result);
                    ToastUtils.getShortToastByString(GoodsInfoActivity.this,"添加成功");
                    updateLocalShoppingCart();
                }else{
                    ToastUtils.getShortToastByString(GoodsInfoActivity.this,"添加成功");
                }
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(GoodsInfoActivity.this,"添加失败");
            }
        },null);
    }


    private void updateShoppingCart(String result) {        //更新本地数据
        String idsOfShoppingCart = UserInfo.getUserInfo(GoodsInfoActivity.this,"idOfShoppingCart");
        String[] results = result.split(",");
        exit = false;
        idOfShoppingCart = null;
        if(results[0].equals("amount")){
            amount = Integer.parseInt(results[1]);
            alreadyIdOfShoppingCart = results[2];
            exit = true;
            return;
        }else{
            idOfShoppingCart = results[1];
        }
        Log.e(GoodsInfoActivity.class.getSimpleName(),"idOfShoppingCart"+idsOfShoppingCart);
       // String[] idsOfShopCartArray = idsOfShoppingCart.split(",");
        if("".equals(idsOfShoppingCart)){
            idsOfShoppingCart=idOfShoppingCart;
        }else{
            idsOfShoppingCart=idsOfShoppingCart+","+idOfShoppingCart;
        }

        Log.e(GoodsInfoActivity.class.getSimpleName(),"updateIdsOfShoppingCart:"+idsOfShoppingCart);
        UserInfo.saveUserInfo(GoodsInfoActivity.this,"idOfShoppingCart",idsOfShoppingCart);
    }

    private void updateLocalShoppingCart() {
        SharedPreferences sp = this.getSharedPreferences("shopping_cart",MODE_PRIVATE);
        String jsonData = sp.getString("shopping_cart_"+UserInfo.getUserInfo(GoodsInfoActivity.this,"id")+"","");
        List<ShoppingCartBean> shoppingCartBeanList = JSON.parseArray(jsonData,ShoppingCartBean.class);
        if(shoppingCartBeanList==null)shoppingCartBeanList = new ArrayList<>();
        if(exit){
            for(int i=0;i<shoppingCartBeanList.size();i++){
                if(shoppingCartBeanList.get(i).getShoppingCartId()==Integer.parseInt(alreadyIdOfShoppingCart)){
                    shoppingCartBeanList.get(i).setAmount(amount+"");
                    break;
                }
            }
        }else{
            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
            shoppingCartBean.setAmount(amount+"");
            shoppingCartBean.setShoppingCartId(Integer.parseInt(idOfShoppingCart));
            shoppingCartBean.setImageUrl(updateGoodBean.getImageUrl());
            shoppingCartBean.setShopName(shop.getShopName());
            shoppingCartBean.setGoodPrice(updateGoodBean.getCover_price()+"");
            shoppingCartBean.setGoodName(updateGoodBean.getName());
            shoppingCartBeanList.add(shoppingCartBean);
        }
        jsonData = JSON.toJSONString(shoppingCartBeanList);
        Log.e(GoodsInfoActivity.class.getSimpleName(),"shopppingCart:"+jsonData.toString());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("shopping_cart_"+UserInfo.getUserInfo(GoodsInfoActivity.this,"id")+"",jsonData);
        editor.commit();
    }
}

