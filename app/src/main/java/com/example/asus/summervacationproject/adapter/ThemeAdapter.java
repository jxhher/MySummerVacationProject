package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.ResultBeanData;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by ASUS on 2018/7/23.
 */

public class ThemeAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<ResultBeanData.ResultBean.ThemeInfoBean> themeList;

    public ThemeAdapter(Context context, List<ResultBeanData.ResultBean.ThemeInfoBean> theme_info){
        this.mContext = context;
        this.themeList = theme_info;
    }


    @Override
    public int getCount() {
        return themeList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_theme,null);
            viewHolder = new ThemeAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ResultBeanData.ResultBean.ThemeInfoBean theme = themeList.get(position);
        Log.e(TAG,"themeAdapter:"+theme.getImage()+"  "+theme.getName());
        Picasso.with(mContext).load(Config.BASE_URL_IMAGE+theme.getImage()).into(viewHolder.imageView);
        viewHolder.textView.setText(theme.getName());
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.theme_imageView) ImageView imageView;
        @BindView(R.id.theme_textView) TextView textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
