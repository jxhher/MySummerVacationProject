package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.ResultBeanData;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/27.
 */

public class RecommendAdapter extends BaseAdapter {
    private Context mContext;
    private List<ResultBeanData.ResultBean.RecommendInfoBean> recommendList;

    public RecommendAdapter(Context context, List<ResultBeanData.ResultBean.RecommendInfoBean> recommendList) {
        this.mContext = context;
        this.recommendList = recommendList;
    }

    @Override
    public int getCount() {
        return recommendList.size();
    }

    @Override
    public Object getItem(int position) {
        return recommendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(mContext,R.layout.item_recommend,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ResultBeanData.ResultBean.RecommendInfoBean recommend = recommendList.get(position);
        Picasso.with(mContext).load(Config.BASE_URL_IMAGE+recommend.getImage()).into(viewHolder.imageView);
        viewHolder.textView_introduce.setText(recommend.getName());
        viewHolder.textView_price.setText("¥"+recommend.getCover_price());
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.recommend_imageView) ImageView imageView;
        @BindView(R.id.recommend_textView_introduce) TextView textView_introduce;
        @BindView(R.id.recommend_textView_price) TextView textView_price;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this,convertView);
        }
    }

}
