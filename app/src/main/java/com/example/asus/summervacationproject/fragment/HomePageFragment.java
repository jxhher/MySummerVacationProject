package com.example.asus.summervacationproject.fragment;


import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.HomePageAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;

import com.example.asus.summervacationproject.bean.ResultBeanData;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;



/**
 * Created by ASUS on 2018/7/19.
 * Updated by ASUS on 2018/7/20  完成主页的联网请求，成功连接到服务器端
 */

public class HomePageFragment extends BaseFragment {

    private static final String TAG = HomePageFragment.class.getSimpleName();
    private ResultBeanData.ResultBean resultBean;      //获得解析返回的数据
    private HomePageAdapter adapter;
    private RecyclerView recyclerView;
    private View view;
    private ImageButton button_top;
    private Toolbar mToolBar;
    private TextView titleTextView;


    @Override
    protected View initView() {

        Log.e(TAG,"首页Fragment页面开始初始化...");

        view = View.inflate(mContext, R.layout.fragment_homepage,null);
        SearchView searchView = (SearchView) view.findViewById(R.id.homePage_searchView);
        recyclerView = (RecyclerView) view.findViewById(R.id.homePage_recyclerView);
        button_top = (ImageButton) view.findViewById(R.id.homePage_imageButton);
        button_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override                    //点击搜索按钮时触发
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        initDate();
        adapter = new HomePageAdapter(mContext,resultBean);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//        }
        return view;
    }

    @Override
    protected void initDate() {
        new OkHttpUtils(Config.HOMEPAGE_URL, HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext, "联网获取数据成功", Toast.LENGTH_SHORT).show();
                final String TAG = HomePageFragment.class.getSimpleName();
                // Log.e(TAG,"initData"+result);
                processData(result);
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                Toast.makeText(mContext, "联网获取数据失败", Toast.LENGTH_SHORT).show();
            }
        },null);
    }

    private void processData(String json) {
        ResultBeanData resultBeanData = JSON.parseObject(json,ResultBeanData.class);
        resultBean = resultBeanData.getResult();
        int code = resultBeanData.getCode();
        String msg = resultBeanData.getMsg();
        // Log.e(TAG,"code:"+code+"msg:"+msg);
        if(resultBean!=null){
            Log.e(TAG,"第二次adapter");
            Log.e(TAG,"banner的大小："+resultBean.getBanner_info().size());
            Log.e(TAG,"themeList的大小："+resultBean.getThemeInfoBean().size());
            Log.e(TAG,"brandList的大小"+resultBean.getBrandInfoBean().size());
            Log.e(TAG,"discount的大小:"+resultBean.getDiscountInfoBean().size());
            Log.e(TAG,"recommend的大小:"+resultBean.getRecommend_info().size());
            adapter = new HomePageAdapter(mContext,resultBean);
            recyclerView.setAdapter(adapter);
            GridLayoutManager manager =  new GridLayoutManager(mContext,1);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position <= 3){
                        //隐藏
                        button_top.setVisibility(View.GONE);
                    }else{
                        //显示
                        button_top.setVisibility(View.VISIBLE);
                    }
                    return 1;
                }
            });
            recyclerView.setLayoutManager(manager);
        }else{
            Log.e(TAG,"resultBean为null");
        }
    }

    @Override
    public void onResume() {
        if (mToolBar == null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            mToolBar = (Toolbar) activity.findViewById(R.id.toolbar);
            if (titleTextView == null) {
                titleTextView = (TextView) mToolBar.findViewWithTag("title");
                titleTextView.setTextSize(21);
            }
        }
        titleTextView.setText("首页");
        super.onResume();
        LayoutInflater.from(mContext);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden) {


        }else{
            titleTextView = (TextView) mToolBar.findViewWithTag("title");
            titleTextView.setText("首页");
        }

    }
}