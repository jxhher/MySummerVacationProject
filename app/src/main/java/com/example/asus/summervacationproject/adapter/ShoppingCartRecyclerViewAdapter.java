package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.utils.AddSubView;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUS on 2018/8/10.
 */

public class ShoppingCartRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CheckBox shopCart_checkbox_out_all;
    private TextView shopCart_totalMoney;
    private Context mContext;
    private List<ShoppingCartBean> shoppingCartBeanList;
    private int amount;
    private HashMap<String,String> updateShopCartInfo;

    public ShoppingCartRecyclerViewAdapter(Context context,List<ShoppingCartBean> shoppingCartBeanList,
                                           TextView shopCart_totalMoney,CheckBox shopCart_checkbox_out_all,HashMap<String,String> updateShopCartInfo){
        this.mContext = context;
        this.shoppingCartBeanList = shoppingCartBeanList;
        this.shopCart_totalMoney = shopCart_totalMoney;
        this.shopCart_checkbox_out_all = shopCart_checkbox_out_all;
        this.updateShopCartInfo = updateShopCartInfo;
        showTotalPrice();
    }


    public ShoppingCartRecyclerViewAdapter(Context context,List<ShoppingCartBean> shoppingCartBeenList){
        this.mContext = context;
        this.shoppingCartBeanList = shoppingCartBeenList;
    }

    public ShoppingCartRecyclerViewAdapter() {

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_shopping_cart, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(shoppingCartBeanList.get(position));
        viewHolder.addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void onNumberChange(int value) {
                viewHolder.addSubView.setValue(value);
                shoppingCartBeanList.get(position).setAmount(value+"");
                String id = shoppingCartBeanList.get(position).getShoppingCartId()+"";
                String amount = shoppingCartBeanList.get(position).getAmount();
                updateShopCartInfo.put(id,amount);
                showTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartBeanList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private ImageView goodImageView;
        private TextView goodName;
        private TextView shopName;
        private TextView goodPrice;
        private AddSubView addSubView;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_shoppingCart_checkbox);
            goodImageView = (ImageView) itemView.findViewById(R.id.item_shoppingCart_imageView);
            goodName = (TextView) itemView.findViewById(R.id.item_shoppingCart_goodName);
            shopName = (TextView) itemView.findViewById(R.id.item_shoppingCart_shopName);
            goodPrice = (TextView) itemView.findViewById(R.id.item_shoppingCart_price);
            addSubView = (AddSubView) itemView.findViewById(R.id.item_shoppingCart_addSubView);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(v, getLayoutPosition());
                    }
                }
            });*/
        }

        public void setData(final ShoppingCartBean shoppingCartBean) {
            checkBox.setSelected(false);
            Picasso.with(mContext).load(Config.BASE_URL_IMAGE+shoppingCartBean.getImageUrl()).into(goodImageView);
            goodName.setText(shoppingCartBean.getGoodName());
            shopName.setText(shoppingCartBean.getShopName() );
            goodPrice.setText("￥ "+shoppingCartBean.getGoodPrice());
            addSubView.setValue(Integer.parseInt(shoppingCartBean.getAmount()));


        }
    }
    private double getTotalPrice() {
        double total = 0;
        if (shoppingCartBeanList != null && shoppingCartBeanList.size() > 0) {
            for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
                if (shoppingCartBean.isSelected())
                    total += Double.parseDouble(shoppingCartBean.getGoodPrice()) * Double.parseDouble(shoppingCartBean.getAmount() + "");
            }
        }
        return total;
    }

    private void showTotalPrice() {
        shopCart_totalMoney.setText(getTotalPrice() + "");
    }
}
