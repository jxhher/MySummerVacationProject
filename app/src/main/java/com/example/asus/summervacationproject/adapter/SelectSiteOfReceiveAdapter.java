package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.activity.SelectSiteOfReceiveActivity;
import com.example.asus.summervacationproject.bean.OrderFormBean;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.utils.AddSubView;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;

import java.net.Inet4Address;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作用：确认订单页面的数据适配器
 * Created by ASUS on 2018/8/12.
 */

public class SelectSiteOfReceiveAdapter extends BaseAdapter{
    private Context mContext;
    private List<ShoppingCartBean> shoppingCartBeanList;
    public SelectSiteOfReceiveAdapter(Context mContext, List<ShoppingCartBean> orderFormBeanList) {
        this.mContext = mContext;
        this.shoppingCartBeanList = orderFormBeanList;
    }

    @Override
    public int getCount() {
        return shoppingCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(mContext,R.layout.item_select_site_receive_listview,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        Picasso.with(mContext).load(Config.BASE_URL_IMAGE+shoppingCartBean.getImageUrl()).into(viewHolder.receive_imageView);
        viewHolder.receive_goodName.setText(shoppingCartBean.getGoodName());
        viewHolder.receive_shopName.setText(shoppingCartBean.getShopName());
        viewHolder.receiver_addSubView.setValue(Integer.parseInt(shoppingCartBean.getAmount()));
        viewHolder.receiver_addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                SelectSiteOfReceiveActivity activity = (SelectSiteOfReceiveActivity) mContext;
                shoppingCartBean.setAmount(value+"");
                activity.updateTotalPrice(shoppingCartBeanList);
            }
        });
        viewHolder.receiver_price.setText("¥ "+shoppingCartBean.getGoodPrice());
        viewHolder.receive_item_removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shoppingCartBeanList.size()!=0)shoppingCartBeanList.remove(position);
                notifyDataSetChanged();
                SelectSiteOfReceiveActivity activity = (SelectSiteOfReceiveActivity) mContext;
                activity.updateTotalPrice(shoppingCartBeanList);
            }
        });

        return convertView;
    }


     static class ViewHolder{
         @BindView(R.id.receive_shopName)TextView receive_shopName;
         @BindView(R.id.receive_imageView)ImageView receive_imageView;
         @BindView(R.id.receive_goodName)TextView receive_goodName;
         @BindView(R.id.receiver_price)TextView receiver_price;
         @BindView(R.id.receiver_addSubView)AddSubView receiver_addSubView;
         @BindView(R.id.receive_item_removeButton)ImageButton receive_item_removeButton;
         public ViewHolder(View view){
             ButterKnife.bind(this,view);
         }

     }
}
