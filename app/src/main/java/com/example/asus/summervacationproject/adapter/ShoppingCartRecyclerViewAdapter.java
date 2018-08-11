package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.Shop;
import com.example.asus.summervacationproject.bean.ShoppingCartBean;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.utils.AddSubView;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ASUS on 2018/8/10.
 */

public class ShoppingCartRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CheckBox shopCart_checkbox_out_all;
    private CheckBox shopCart_check_delete_all;
    private TextView shopCart_totalMoney;
    private Context mContext;
    private List<ShoppingCartBean> shoppingCartBeanList;
    private int amount;
    private HashMap<String,String> updateShopCartInfo;
    private String newIds;

    public ShoppingCartRecyclerViewAdapter(Context context, final List<ShoppingCartBean> shoppingCartBeanList,
                                           TextView shopCart_totalMoney,
                                           CheckBox shopCart_checkbox_out_all,
                                           HashMap<String,String> updateShopCartInfo, CheckBox shopCart_check_delete_all){
        this.mContext = context;
        this.shoppingCartBeanList = shoppingCartBeanList;
        this.shopCart_totalMoney = shopCart_totalMoney;
        this.shopCart_check_delete_all = shopCart_check_delete_all;
        this.shopCart_checkbox_out_all = shopCart_checkbox_out_all;
        this.updateShopCartInfo = updateShopCartInfo;
        showTotalPrice();

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
                shoppingCartBean.setSelected(!shoppingCartBean.isSelected());
                notifyItemChanged(position);
                checkAll();
                showTotalPrice();
            }
        });




        //设置全选点击事件
        shopCart_checkbox_out_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = getCheckboxAll().isChecked();
                checkAll_none(checked);
                showTotalPrice();
            }
        });


        //设置全选点击事件
        shopCart_check_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = getCheckboxDeleteAll().isChecked();
                checkAll_none(checked);
                showTotalPrice();
            }
        });


    }

    private CheckBox getCheckboxDeleteAll() {
        return shopCart_check_delete_all;
    }

    public void  checkAll_none(boolean checked) {
        if (shoppingCartBeanList != null && shoppingCartBeanList.size() > 0) {
            for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                shoppingCartBeanList.get(i).setSelected(checked);
                shopCart_checkbox_out_all.setChecked(checked);
                notifyItemChanged(i);
            }
        } else {
            shopCart_checkbox_out_all.setChecked(false);

        }
    }

    public CheckBox getCheckboxAll() {
        return shopCart_checkbox_out_all;
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

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = viewHolder.checkBox.isChecked();
                shoppingCartBeanList.get(position).setSelected(checked);
                showTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartBeanList.size();
    }

    public void checkAll() {
        if (shoppingCartBeanList != null && shoppingCartBeanList.size() > 0) {
            for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                if (!shoppingCartBeanList.get(i).isSelected()) {
                    shopCart_checkbox_out_all.setChecked(false);
                    shopCart_check_delete_all.setChecked(false);
                    return;
                } else {
                    shopCart_checkbox_out_all.setChecked(true);
                    shopCart_check_delete_all.setChecked(true);
                }
            }
        }else{
            shopCart_checkbox_out_all.setChecked(false);
            shopCart_check_delete_all.setChecked(false);
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private ImageView goodImageView;
        private TextView goodName;
        private TextView shopName;
        private TextView goodPrice;
        private AddSubView addSubView;

        ViewHolder(final View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_shoppingCart_checkbox);
            goodImageView = (ImageView) itemView.findViewById(R.id.item_shoppingCart_imageView);
            goodName = (TextView) itemView.findViewById(R.id.item_shoppingCart_goodName);
            shopName = (TextView) itemView.findViewById(R.id.item_shoppingCart_shopName);
            goodPrice = (TextView) itemView.findViewById(R.id.item_shoppingCart_price);
            addSubView = (AddSubView) itemView.findViewById(R.id.item_shoppingCart_addSubView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(v, getLayoutPosition());
                    }
                }
            });
        }



        public void setData(final ShoppingCartBean shoppingCartBean) {
            checkBox.setChecked(shoppingCartBean.isSelected());
            Picasso.with(mContext).load(Config.BASE_URL_IMAGE+shoppingCartBean.getImageUrl()).into(goodImageView);
            goodName.setText(shoppingCartBean.getGoodName());
            shopName.setText(shoppingCartBean.getShopName() );
            goodPrice.setText("￥ "+shoppingCartBean.getGoodPrice());
            addSubView.setValue(Integer.parseInt(shoppingCartBean.getAmount()));

        }
    }


        //回调点击事件的监听
        private OnItemClickListener onItemClickListener;

        interface OnItemClickListener {
            void onItemClickListener(View view, int position);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
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

    public void showTotalPrice() {
        shopCart_totalMoney.setText(getTotalPrice() + "");
    }

    public void deleteData() {
        if (shoppingCartBeanList != null && shoppingCartBeanList.size() > 0) {
            for (Iterator iterator = shoppingCartBeanList.iterator(); iterator.hasNext(); ) {

                ShoppingCartBean shoppingCart = (ShoppingCartBean) iterator.next();

                if (shoppingCart.isSelected()) {

                    int position = shoppingCartBeanList.indexOf(shoppingCart);

                    //删除数据库数据
                    deleteInternetData(shoppingCart);

                    //更新本地个人数据
                    updateLocalUserData(newIds);

                    //删除内存中的数据
                    iterator.remove();

                    //3.刷新数据
                    notifyItemRemoved(position);
                }
            }
            //更新本地购物车缓存数据
            updateLocalShoppingCartData(shoppingCartBeanList);

        }
    }


    private void deleteInternetData(ShoppingCartBean shoppingCart) {
        new OkHttpUtils(Config.DELETE_SHOPPINGCART+"?userId="+ UserInfo.getUserInfo(mContext,"id")+"&shoppingCartId="+shoppingCart.getShoppingCartId(),
                HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(!result.equals("false")){
                    newIds = result;
                }
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                ToastUtils.getShortToastByString(mContext,"删除失败");
            }
        },null);
    }

    private void updateLocalUserData(String newIds) {
        UserInfo.saveUserInfo(mContext,"idOfCartShopping",newIds);
    }

    private void updateLocalShoppingCartData(List<ShoppingCartBean> shoppingCartBeanList) {
        String jsonData = JSON.toJSONString(shoppingCartBeanList);
        SharedPreferences sp = mContext.getSharedPreferences("shopping_cart",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("shopping_cart_"+UserInfo.getUserInfo(mContext,"id")+"",jsonData);
        editor.commit();
    }
}
