package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.EvaluateBean;
import com.example.asus.summervacationproject.fragment.Goods_Evaluate_Fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/30.
 */

public class GoodEvaluateListViewAdapter extends BaseAdapter {
    public Context mContext;
    public ArrayList<EvaluateBean> evaluateBeanList;
    public  int listItemId;

    public GoodEvaluateListViewAdapter(Context context, ArrayList<EvaluateBean> evaluateBeanList, int item_good_evaluate) {
        this.mContext = context;
        this.evaluateBeanList = evaluateBeanList;
        this.listItemId = item_good_evaluate;
    }

    @Override
    public int getCount() {
        return evaluateBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return evaluateBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_good_evaluate, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.evaluate_evaluate_TextView.setText(evaluateBeanList.get(position).getUserName()+"");
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.good_evaluate_evaluate)TextView evaluate_evaluate_TextView;
        @BindView(R.id.good_evaluate_time)TextView evaluate_time_TextView;
        @BindView(R.id.good_evaluate_username)TextView evaluate_userName_TextView;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }


    }
}
