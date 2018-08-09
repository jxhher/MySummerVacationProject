package com.example.asus.summervacationproject.fragment;


import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;

import com.example.asus.summervacationproject.adapter.GoodCommentListViewAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.CommentsBean;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/28.
 * Updated by ASUS on 2018/7/30 完成商品评价页面的布局及显示
 * Updated by ASUS on 2018/8/9  完成商品评价数据的数据库填充，懒加载连接数据库获取数据
 */

public class Goods_Comment_Fragment extends BaseFragment {
    View view;
    public ArrayList<CommentsBean> commentBeanList = new ArrayList<>();
    public ListView commentListView;
    private boolean isFragmentVisible = false;
    private boolean isFirst = true;
    private GoodCommentListViewAdapter adapter;
    private int shopId = -1;
    private ProgressBar good_comment_progressBar;
    public Goods_Comment_Fragment(int shopId) {
        this.shopId = shopId;
    }


    protected View initView() {
        view = View.inflate(mContext, R.layout.fragments_goods_comment,null);
        ButterKnife.bind(this.view);
        commentListView = (ListView) view.findViewById(R.id.good_comment_listView);
        initDate();
        return view;
    }

    @Override
    protected void initDate() {
        adapter = new GoodCommentListViewAdapter(mContext,commentBeanList,R.layout.item_good_comment);
        commentListView.setAdapter(adapter);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isFragmentVisible = true;
        }
        if (view == null) {
            return;
        }

        //可见，并且没有加载过
        if (isFirst&&isFragmentVisible&&shopId!=-1) {
            getCommentsByInternet();
            return;
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            isFragmentVisible = false;
        }

    }

    private void getCommentsByInternet() {
        good_comment_progressBar = (ProgressBar) view.findViewById(R.id.good_comment_progressBar);
        good_comment_progressBar.setVisibility(View.VISIBLE);

        new OkHttpUtils(Config.GET_COMMENT_URL+"?shopId="+shopId, HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if("NO".equals(result)){
                    ToastUtils.getShortToastByString(mContext,"暂时还没有评论");
                    return;
                }
                commentBeanList = (ArrayList<CommentsBean>) JSON.parseArray(result,CommentsBean.class);
                adapter = new GoodCommentListViewAdapter(mContext,commentBeanList,R.layout.item_good_comment);
                commentListView.setAdapter(adapter);
                isFirst = false;
                good_comment_progressBar.setVisibility(View.GONE);
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                good_comment_progressBar.setVisibility(View.GONE);
                ToastUtils.getShortToastByString(mContext,"获取数据失败");
            }
        },null);

    }
}
