package com.example.asus.summervacationproject.fragment;

import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/7/19.
 * Updated by ASUS on 2018/8/10 完成数据的联网获取及显示
 * Updated by ASUS on 2018/8/11 完成数据的本地及数据库实时更新，增加和减少
 * Updated by ASUS on 2018/8/11 完成Fragment中对toolbar的管理，完成删除布局与结算布局的切换,完成购物车数据的联网及本地删除
 */

public class ShoppingCartFragment extends BaseFragment{
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
    @BindView(R.id.shopCart_linearLayout_delete)
    LinearLayout shopCart_linearLayout_delete;

    private ShoppingCartRecyclerViewAdapter shoppingCartRecyclerViewAdapter = null;
    private View view;
    private int userId = -1;
    private LinearLayout layout_empty_shopping_cart;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private HashMap<String, String> updateShopCartInfo = new HashMap<>();
    private Toolbar mToolBar;
    private TextView edit;
    private TextView titleTextView;
    private LinearLayout shopCart_check_linearLayout_all;
    private boolean login = false;


    @Override
    public void onResume() {
        setShoppingCartData();
        if(mToolBar==null){
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mToolBar = (Toolbar) activity.findViewById(R.id.toolbar);
            edit = new TextView(mContext);
            edit.setText("编辑");
            edit.setTextSize(21);
            edit.setTag("edit");
            edit.setVisibility(View.GONE);
            mToolBar.addView(edit);
            titleTextView = (TextView) mToolBar.findViewWithTag("title");
            //为ImageView设置参数
            Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) edit.getLayoutParams();
            layoutParams.gravity = GravityCompat.END;
            layoutParams.rightMargin = 23;

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTextViewUI();
                }
            });
        }
        titleTextView.setText("购物车");
        setHasOptionsMenu(true);
        //mToolBar.setVisibility(View.GONE);
        super.onResume();
    }


    private void updateTextViewUI() {
        if(edit.getText().equals("编辑")){
            shopCart_check_linearLayout_all.setVisibility(View.GONE);
            shopCart_linearLayout_delete.setVisibility(View.VISIBLE);
            edit.setText("完成");
        }else{
            shopCart_check_linearLayout_all.setVisibility(View.VISIBLE);
            shopCart_linearLayout_delete.setVisibility(View.GONE);
            edit.setText("编辑");
        }
    }


    @Override

    public void onHiddenChanged(boolean hidden) {

        if (hidden) {
            if (updateShopCartInfo.size() != 0) {
                setHasOptionsMenu(true);
                updatedShoppingCartData(updateShopCartInfo);
            }
                 edit = (TextView) mToolBar.findViewWithTag("edit");
                 edit.setVisibility(View.GONE);

         } else{
                titleTextView = (TextView) mToolBar.findViewWithTag("title");
                titleTextView.setText("购物车");
                edit = (TextView) mToolBar.findViewWithTag("edit");
                if(shoppingCartRecyclerViewAdapter!=null)
                    if(shoppingCartRecyclerViewAdapter.getItemCount()!=0)edit.setVisibility(View.VISIBLE);


        }

    }

    @Override
    protected View initView() {
        Log.e(TAG, "购物车Fragment页面被初始化了");
        view = View.inflate(mContext, R.layout.fragment_shoppingcart, null);
        ButterKnife.bind(this, view);
        layout_empty_shopping_cart = (LinearLayout) view.findViewById(R.id.empty_shopping_cart);
        shopCart_check_linearLayout_all = (LinearLayout) view.findViewById(R.id.shopCart_check_linearLayout_all);

        if (TextUtils.isEmpty(UserInfo.getUserInfo(mContext, "name"))) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivityForResult(intent, 2);
            return view;
        } else {
            return view;
        }




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            onResume();
            Log.e(ShoppingCartFragment.class.getSimpleName(), "userId:" + userId);
        } else {
            ToastUtils.getShortToastByString(mContext, "同步数据失败");
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("bottom_shoppingCart");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();

        }
    }

    private void setShoppingCartData() {

        SharedPreferences sp = mContext.getSharedPreferences("shopping_cart", Context.MODE_PRIVATE);
        String jsonData = sp.getString("shopping_cart_" + UserInfo.getUserInfo(mContext, "id") + "", "");
        shoppingCartBeanList = JSON.parseArray(jsonData, ShoppingCartBean.class);
        if(shoppingCartBeanList==null){
            if(!UserInfo.getUserInfo(mContext,"id").equals(""))           //点击购物车时判断是否登录用以设置布局显示
                layout_empty_shopping_cart.setVisibility(View.VISIBLE);
                shopCart_check_linearLayout_all.setVisibility(View.GONE);
            if(mToolBar!=null){
                edit = (TextView) mToolBar.findViewWithTag("edit");
                edit.setVisibility(View.GONE);
            }
        }else{
            if(shoppingCartBeanList.size()!=0){
                layout_empty_shopping_cart.setVisibility(View.GONE);
                shopCart_check_linearLayout_all.setVisibility(View.VISIBLE);
            }
            if(mToolBar!=null){
                edit = (TextView) mToolBar.findViewWithTag("edit");
                edit.setVisibility(View.VISIBLE);
            }
            shoppingCartRecyclerViewAdapter = new ShoppingCartRecyclerViewAdapter(mContext, shoppingCartBeanList,
                    shopCart_totalMoney, shopCart_checkbox_out_all, updateShopCartInfo, shopCart_check_delete_all);
            shoppingCart_recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            shoppingCart_recyclerview.setAdapter(shoppingCartRecyclerViewAdapter);
        }


    }


    private void updatedShoppingCartData(final HashMap<String, String> hashMap) {
        String json = JSON.toJSONString(hashMap);
        Log.e(ShoppingCartFragment.class.getSimpleName(), json);
        new OkHttpUtils(Config.UPDATE_SHOPPINGCART, HttpMethod.POST, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("TRUE")) updateLocalShopCartInfo(hashMap);
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(mContext, "请检查网络");
            }
        }, json);
    }

    private void updateLocalShopCartInfo(HashMap<String, String> hashMap) {
        SharedPreferences sp = mContext.getSharedPreferences("shopping_cart", Context.MODE_PRIVATE);
        String jsonData = sp.getString("shopping_cart_" + UserInfo.getUserInfo(mContext, "id") + "", "");
        List<ShoppingCartBean> shoppingCartBeen = JSON.parseArray(jsonData, ShoppingCartBean.class);
        SharedPreferences.Editor editor = sp.edit();
        for (String key : hashMap.keySet()) {
            for (int i = 0; i < shoppingCartBeen.size(); i++) {
                if (shoppingCartBeen.get(i).getShoppingCartId() == Integer.parseInt(key)) {
                    shoppingCartBeen.get(i).setAmount(hashMap.get(key));
                    Log.e(ShoppingCartFragment.class.getSimpleName(), shoppingCartBeen.get(i).getGoodName() + "   " + hashMap.get(key));
                    break;
                }
            }
        }
        jsonData = JSON.toJSONString(shoppingCartBeen);
        editor.putString("shopping_cart_" + UserInfo.getUserInfo(mContext, "id") + "", jsonData);
        editor.commit();
        hashMap.clear();
        setShoppingCartData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.shopCart_delete_button)
    void OnDeleteButtonClick(){
        shoppingCartRecyclerViewAdapter.deleteData();
        shoppingCartRecyclerViewAdapter.showTotalPrice();
        shoppingCartRecyclerViewAdapter.checkAll();
        if(shoppingCartRecyclerViewAdapter.getItemCount()==0){
            layout_empty_shopping_cart.setVisibility(View.VISIBLE);
            shopCart_check_linearLayout_all.setVisibility(View.GONE);
            shopCart_linearLayout_delete.setVisibility(View.GONE);
            edit.setText("编辑");
            edit.setVisibility(View.GONE);
        }
    }
}
