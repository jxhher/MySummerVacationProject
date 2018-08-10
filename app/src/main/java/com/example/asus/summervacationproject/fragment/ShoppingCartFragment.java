package com.example.asus.summervacationproject.fragment;

import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.activity.LoginActivity;
import com.example.asus.summervacationproject.activity.MainActivity;
import com.example.asus.summervacationproject.adapter.ShoppingCartRecyclerViewAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.PostType;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import butterknife.BindDimen;
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

    @BindView(R.id.shoppingCart_recyclerview)
    RecyclerView shoppingCart_recyclerview;
    @BindView(R.id.shoppingCart_progressBar)
    ProgressBar shoppingCart_progressBar;
    @BindView(R.id.shopCart_totalMoney)
    TextView shopCart_totalMoney;

    private ShoppingCartRecyclerViewAdapter shoppingCartRecyclerViewAdapter = null;
    private boolean isFragmentVisible = false;
    private boolean isFirst = true;
    private View view;
    private int userId = -1;
    private LinearLayout layout_empty_shopping_cart;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private HashMap<String,String> updateShopCartInfo = new HashMap<>();

    @Override
    public void onResume() {
    if(shoppingCartBeanList!=null&&UserInfo.getUserInfo(mContext,"id")!=""){
        setShoppingCartData();
    }
        super.onResume();
    }


    @Override

    public void onHiddenChanged(boolean hidden) {

        if (hidden) {

            //相当于Fragment的onPause
            Log.e(ShoppingCartFragment.class.getSimpleName(),"界面不可见");
            if(updateShopCartInfo.size()!=0){
                updatedShoppingCartData(updateShopCartInfo);
            }

        } else {

            // 相当于Fragment的onResume
            Log.e(ShoppingCartFragment.class.getSimpleName(),"界面可见");
            System.out.println("界面可见");

        }

    }

    @Override
    protected View initView() {
        Log.e(TAG,"购物车Fragment页面被初始化了");
        view = View.inflate(mContext, R.layout.fragment_shoppingcart,null);
        ButterKnife.bind(this,view);
        layout_empty_shopping_cart = (LinearLayout)view.findViewById(R.id.empty_shopping_cart);
        if (TextUtils.isEmpty(UserInfo.getUserInfo(mContext,"name"))) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivityForResult(intent,2);
            return view;
        }else{

            return view;
        }



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(data!=null){
           setShoppingCartData();
           Log.e(ShoppingCartFragment.class.getSimpleName(),"userId:"+userId);
       }else{
           ToastUtils.getShortToastByString(mContext,"同步数据失败");
           FragmentManager fragmentManager=getFragmentManager();
           Fragment fragment=fragmentManager.findFragmentByTag("bottom_shoppingCart");
           FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
           fragmentTransaction.remove(fragment);
           fragmentTransaction.commit();

       }
    }

    private void setShoppingCartData() {

        SharedPreferences sp = mContext.getSharedPreferences("shopping_cart", Context.MODE_PRIVATE);
        String jsonData = sp.getString("shopping_cart_"+UserInfo.getUserInfo(mContext,"id")+"","");
        shoppingCartBeanList = JSON.parseArray(jsonData,ShoppingCartBean.class);
        shoppingCartRecyclerViewAdapter = new ShoppingCartRecyclerViewAdapter(mContext,shoppingCartBeanList,shopCart_totalMoney,shopCart_checkbox_out_all,updateShopCartInfo);
        shoppingCart_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        shoppingCart_recyclerview.setAdapter(shoppingCartRecyclerViewAdapter);
    }



    private void updatedShoppingCartData(final HashMap<String,String> hashMap) {
        String json = JSON.toJSONString(hashMap);
        Log.e(ShoppingCartFragment.class.getSimpleName(),json);
        new OkHttpUtils(Config.UPDATE_SHOPPINGCART, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("TRUE"))updateLocalShopCartInfo(hashMap);
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(mContext,"请检查网络");
            }
        },json);
    }

    private void updateLocalShopCartInfo(HashMap<String,String> hashMap) {
        SharedPreferences sp = mContext.getSharedPreferences("shopping_cart",Context.MODE_PRIVATE);
        String jsonData = sp.getString("shopping_cart_"+UserInfo.getUserInfo(mContext,"id")+"","");
        List<ShoppingCartBean> shoppingCartBeen = (List<ShoppingCartBean>) JSON.parseArray(jsonData,ShoppingCartBean.class);
        SharedPreferences.Editor editor = sp.edit();
        for(String key:hashMap.keySet())
        {
            for(int i =0;i<shoppingCartBeen.size();i++){
                if(shoppingCartBeen.get(i).getShoppingCartId()==Integer.parseInt(key)){
                    shoppingCartBeen.get(i).setAmount(hashMap.get(key));
                    Log.e(ShoppingCartFragment.class.getSimpleName(),shoppingCartBeen.get(i).getGoodName()+"   "+hashMap.get(key));
                    break;
                }
            }
        }
        jsonData = JSON.toJSONString(shoppingCartBeen);
        editor.putString("shopping_cart_"+UserInfo.getUserInfo(mContext,"id")+"",jsonData);
        editor.commit();
        hashMap.clear();
        setShoppingCartData();
    }
}
