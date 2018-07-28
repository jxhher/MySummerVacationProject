package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

/**
 * Created by ASUS on 2018/7/27.
 */

public class DiscountAdapter extends BaseAdapter {
    private Context mcContext;
    private List<ResultBeanData.ResultBean.DiscountInfoBean> discountList;

    public DiscountAdapter(Context context, List<ResultBeanData.ResultBean.DiscountInfoBean> discountList) {
        this.mcContext = context;
        this.discountList = discountList;
    }



    @Override
    public int getCount() {
        return discountList.size();
    }

    @Override
    public Object getItem(int position) {
        return discountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(mcContext, R.layout.item_discount,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ResultBeanData.ResultBean.DiscountInfoBean discount = discountList.get(position);
        Log.e(TAG,"discountAdapter:"+discount.getImage());
        Picasso.with(mcContext).load(Config.BASE_URL_IMAGE+discount.getImage()).into(viewHolder.imageView);
        viewHolder.textView.setText(discount.getName());
        return convertView;

    }
        static class ViewHolder{
        @BindView(R.id.discount_image)ImageView imageView;
        @BindView(R.id.discount_textView)TextView textView;

            public ViewHolder(View convertView) {
                ButterKnife.bind(this,convertView);
            }
        }
}
