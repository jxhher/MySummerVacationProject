package com.example.asus.summervacationproject.fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
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


    @Override
    protected View initView() {
        Log.e(TAG,"首页Fragment页面被初始化了...");

        View view = View.inflate(mContext, R.layout.fragment_homepage,null);
        SearchView searchView = (SearchView) view.findViewById(R.id.homePage_searchView);
        recyclerView = (RecyclerView) view.findViewById(R.id.homePage_recyclerView);

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


        //viewPager.setAdapter();
        return view;
    }

    @Override
    protected void initDate() {
        new OkHttpUtils(Config.SERVEL_URL+"/servlet/initHomePage", HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext, "联网获取数据成功", Toast.LENGTH_SHORT).show();
                final String TAG = HomePageFragment.class.getSimpleName();
                Log.e(TAG,result);
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
        Log.d("HomePage","解析得到的数据:"+resultBean.getBanner_info());
        if(resultBean!=null){
            adapter = new HomePageAdapter(mContext,resultBean);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
        }else{

        }
    }


}
