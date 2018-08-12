package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.OrderFormBean;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/8/12.
 */

public class MyOrderFormAdapter extends BaseAdapter {
    private Context context;
    private List<OrderFormBean> orderFormBeen;
    private int listItemId;

    public MyOrderFormAdapter(Context context, List<OrderFormBean> orderFormBeen, int listItemId) {
        this.context = context;
        this.orderFormBeen = orderFormBeen;
        this.listItemId = listItemId;
    }

    @Override
    public int getCount() {
        return orderFormBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return orderFormBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(context,R.layout.item_my_order_form_listview,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderFormBean orderFormBean = orderFormBeen.get(position);
        viewHolder.myOrderForm_date.setText("下单日期："+orderFormBean.getDate());
        viewHolder.myOrderForm_shopName.setText(orderFormBean.getShopName());
        viewHolder.myOrderForm_gooodName.setText(orderFormBean.getGoodName());
        viewHolder.myOrderForm_price.setText("¥ "+orderFormBean.getPrice());
        viewHolder.myOrderForm_total_price.setText("共 "+orderFormBean.getAmount()+"件商品，"
                +"合 计 ¥ "+Integer.parseInt(orderFormBean.getPrice())*orderFormBean.getAmount()+"元");
        Picasso.with(context).load(Config.BASE_URL_IMAGE+orderFormBean.getImageUrl()).into(viewHolder.myOrderForm_imageView);
        viewHolder.myOrderForm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.myOrderForm_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.myOrderForm_shopName)
        TextView myOrderForm_shopName;
        @BindView(R.id.myOrderForm_goodName)
        TextView myOrderForm_gooodName;
        @BindView(R.id.myOrderForm_price)
        TextView myOrderForm_price;
        @BindView(R.id.myOrderForm_total_price)
        TextView myOrderForm_total_price;
        @BindView(R.id.myOrderForm_delete)
        Button myOrderForm_delete;
        @BindView(R.id.myOrderForm_comment)
        Button myOrderForm_comment;
        @BindView(R.id.myOrderForm_imageView)
        ImageView myOrderForm_imageView;
        @BindView(R.id.myOrderForm_date)
        TextView myOrderForm_date;

    public ViewHolder(View view){
        ButterKnife.bind(this,view);
        }
    }
}
