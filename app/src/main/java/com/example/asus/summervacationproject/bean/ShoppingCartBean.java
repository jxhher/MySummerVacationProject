package com.example.asus.summervacationproject.bean;

import java.io.Serializable;

/**
 * Created by ASUS on 2018/8/10.  用来显示购物车条目信息的存储类
 *
 */

public class ShoppingCartBean implements Serializable {
    private int shoppingCartId;        //购物车数据id
    private String imageUrl;           //商品图片地址
    private String goodName;           //商品名称
    private String shopName;           //店铺名称
    private String goodPrice;          //商品价格
    private String amount;                //购买数量
    private int siteOfReceiveId;
    private boolean isSelected = false;
    private int userId;
    private int shopId;
    private int goodId;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getSiteOfReceiveId() {
        return siteOfReceiveId;
    }

    public void setSiteOfReceiveId(int siteOfReceiveId) {
        this.siteOfReceiveId = siteOfReceiveId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }
}
