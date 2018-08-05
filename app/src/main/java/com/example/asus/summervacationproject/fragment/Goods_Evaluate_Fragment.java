package com.example.asus.summervacationproject.fragment;


import android.provider.Settings;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.GoodEvaluateListViewAdapter;
import com.example.asus.summervacationproject.adapter.ListViewAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.bean.EvaluateBean;

import java.util.ArrayList;

/**
 * Created by ASUS on 2018/7/28.
 * Updated by ASUS on 2018/7/30 完成商品评价页面的布局及显示
 */

public class Goods_Evaluate_Fragment extends BaseFragment {
    View view;
    public ArrayList<EvaluateBean> evaluateBeanList = new ArrayList<>();
    public ListView evaluateListView;
    protected View initView() {
        view = View.inflate(mContext, R.layout.fragments_goods_evaluate,null);
        evaluateListView = (ListView) view.findViewById(R.id.good_evaluate_listView);
        initDate();
        return view;
    }

    @Override
    protected void initDate() {

        for(int i =0;i<5;i++){
            EvaluateBean e = new EvaluateBean();
            e.setUserName(i+"");
            evaluateBeanList.add(e);
        }
        GoodEvaluateListViewAdapter adapter = new GoodEvaluateListViewAdapter(mContext,evaluateBeanList,R.layout.item_good_evaluate);
        evaluateListView.setAdapter(adapter);

    }
}
