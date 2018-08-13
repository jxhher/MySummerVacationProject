package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.activity.EvaluateActivity;
import com.example.asus.summervacationproject.activity.MyOrderFormsActivity;
import com.example.asus.summervacationproject.bean.OrderFormBean;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作用：已完成订单页面的数据适配器
 * Created by ASUS on 2018/8/12.
 */

public class MyOrderFormAdapter extends BaseAdapter {
    private Context context;
    private List<OrderFormBean> orderFormBeen;
    private int listItemId;
    private int id;
    private ArrayList<String> list;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(context,R.layout.item_my_order_form_listview,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final OrderFormBean orderFormBean = orderFormBeen.get(position);
        viewHolder.myOrderForm_date.setText("下单日期："+orderFormBean.getDate());
        viewHolder.myOrderForm_shopName.setText(orderFormBean.getShopName());
        viewHolder.myOrderForm_gooodName.setText(orderFormBean.getGoodName());
        viewHolder.myOrderForm_price.setText("¥ "+orderFormBean.getPrice());
        viewHolder.myOrderForm_total_price.setText("共 "+orderFormBean.getAmount()+"件商品，"
                +"合 计 ¥ "+Integer.parseInt(orderFormBean.getPrice())*orderFormBean.getAmount()+"元");
        Picasso.with(context).load(Config.BASE_URL_IMAGE+orderFormBean.getImageUrl()).into(viewHolder.myOrderForm_imageView);
        if(orderFormBean.getIsEvaluate()==1){
            viewHolder.myOrderForm_comment.setText("已评价");
            viewHolder.myOrderForm_comment.setFocusable(false);
        }else{
            viewHolder.myOrderForm_comment.setText("评 价");
            viewHolder.myOrderForm_comment.setFocusable(true);
        }

        viewHolder.myOrderForm_delete.setTag(R.id.btn,orderFormBeen.get(position).getId());
        viewHolder.myOrderForm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = orderFormBean.getId();
                new OkHttpUtils(Config.DELETE_ORDERFORM+"?orderFormId="+orderFormBean.getId()+"&userId="+ UserInfo.getUserInfo(context,"id"), HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if(result.equals("true")){
                            if(orderFormBeen.size()!=0)orderFormBeen.remove(position);
                            updateLocalOrderFormData();
                            notifyDataSetChanged();
                            ToastUtils.getShortToastByString(context,"删除成功");
                        }
                    }
                }, new OkHttpUtils.FailCallback() {
                    @Override
                    public void onFail() {
                        ToastUtils.getShortToastByString(context,"删除失败");
                    }
                },null);
            }
        });

        if(orderFormBean.getIsEvaluate()==1){                       //更新按钮状态
            viewHolder.myOrderForm_comment.setText("已评价");
            viewHolder.myOrderForm_comment.setFocusable(false);
        }else{
            viewHolder.myOrderForm_comment.setText("评 价");
            viewHolder.myOrderForm_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(context, EvaluateActivity.class);
                    int goodId = orderFormBean.getGoodId();
                    int orderFormId = orderFormBean.getId();
                    Log.e(MyOrderFormAdapter.class.getSimpleName(),"goodId"+goodId+"orderFormId"+orderFormId);
                    intent2.putExtra("goodId",goodId);
                    intent2.putExtra("orderFormId",orderFormId);
                    context.startActivity(intent2);
                    MyOrderFormsActivity activity = (MyOrderFormsActivity) context;
                    activity.finish();

                }
            });
        }

        return convertView;
    }

    private void updateLocalOrderFormData() {
            SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String[] ids = sp.getString("idOfOrderForm","").split(",");
            SharedPreferences.Editor editor = sp.edit();
            if (ids.length != 1) {
                list = new ArrayList<>();
                for(int i=0;i<ids.length;i++){
                    if (Integer.parseInt(ids[i])==id)continue;
                    list.add(ids[i]);
                }
            }else if(ids.length==1){
                editor.putString("idOfOrderForm","");
                editor.commit();
                return;
            }
            Log.e(MyOrderFormAdapter.class.getSimpleName(),"adapter:ids "+StringUtils.join(list,","));
            editor.putString("idOfOrderForm",StringUtils.join(list, ","));
            editor.commit();
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
