package com.example.asus.summervacationproject.fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;

import com.example.asus.summervacationproject.adapter.GoodCommentListViewAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.CommentsBean;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.RefreshLayout;
import com.example.asus.summervacationproject.utils.ToastUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.Util;

/**
 * Created by ASUS on 2018/7/28.
 * Updated by ASUS on 2018/7/30 完成商品评价页面的布局及显示
 * Updated by ASUS on 2018/8/9  完成商品评价数据的数据库填充，懒加载连接数据库获取数据
 */

public class Goods_Comment_Fragment extends BaseFragment {
    View view;
    public List<CommentsBean> commentBeanList = new ArrayList<>();
    public ListView commentListView;
    private boolean isFragmentVisible = false;
    private boolean isFirst = true;
    private GoodCommentListViewAdapter adapter;
    private int goodId = -1;
    private int startId = 1;
    private ProgressBar good_comment_progressBar;
    private int mListFocus;
    private int firstVisiblePositionTop;
    private RefreshLayout myRefreshListView;
    private TextView textView;
    private ProgressBar progressBar;

    public Goods_Comment_Fragment(int goodId) {
        this.goodId = goodId;
    }


    protected View initView() {
        view = View.inflate(mContext, R.layout.refresh_listview,null);
        ButterKnife.bind(this.view);
        commentListView = (ListView) view.findViewById(R.id.refresh_listView);
        myRefreshListView = (RefreshLayout)view.findViewById(R.id.swipe_layout);
        myRefreshListView.setColorSchemeResources(R.color.green,R.color.purple,R.color.orange,R.color.blue);
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 更新数据
                        getCommentsByInternet();
                        adapter.notifyDataSetChanged();
                        // 更新完后调用该方法结束刷新
                        myRefreshListView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        getCommentsByInternet();
                        move();
                        adapter.notifyDataSetChanged();
                        // 加载完后调用该方法
                        myRefreshListView.setLoading(false);
                    }
                }, 1000);

            }
        });
         progressBar = (ProgressBar) myRefreshListView.mListViewFooter.findViewById(R.id.pull_to_refresh_load_progress);
        textView = (TextView) myRefreshListView.mListViewFooter.findViewById(R.id.pull_to_refresh_loadmore_text);
        return view;
    }

    @Override
    protected void initDate() {
        adapter = new GoodCommentListViewAdapter(mContext, (ArrayList<CommentsBean>) commentBeanList,R.layout.item_good_comment);
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
        if (isFirst&&isFragmentVisible&&goodId!=-1) {
            getCommentsByInternet();
            return;
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisible) {
            isFragmentVisible = false;
        }

    }

    private void getCommentsByInternet() {

        new OkHttpUtils(Config.GET_COMMENT_URL+"?goodId="+goodId+"&startId="+startId, HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if("NO".equals(result)){
                    ToastUtils.getShortToastByString(mContext,"暂时还没有评论");
                    return;
                }
                startId+=8;
                ArrayList<CommentsBean>  newListData = (ArrayList<CommentsBean>) JSON.parseArray(result,CommentsBean.class);
                if(newListData.size()==0){
                    progressBar.setVisibility(View.GONE);
                    textView.setText("到 底 了。");
                    return;
                }
                commentBeanList.addAll(newListData);
                adapter = new GoodCommentListViewAdapter(mContext, (ArrayList<CommentsBean>) commentBeanList,R.layout.item_good_comment);
                commentListView.setAdapter(adapter);
                isFirst = false;

            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(mContext,"获取数据失败");
            }
        },null);

    }

    void move(){                         //调整位置
        commentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mListFocus = commentListView.getFirstVisiblePosition();
                    View item = commentListView.getChildAt(0);
                    firstVisiblePositionTop = (item == null) ? 0 : item.getTop();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        commentListView.setSelectionFromTop(mListFocus, firstVisiblePositionTop);
    }
}
