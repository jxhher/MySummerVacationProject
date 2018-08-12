package com.example.asus.summervacationproject.bean;

/**
 * Created by ASUS on 2018/8/12.
 */

public class OrderFormBean {
    private int id;
    private int siteOfReceiveId;
    private String shopName;
    private String imageUrl;
    private String goodName;
    private int amount;
    private String price;
    private String date;
    private int userId;
    private int shopId;
    private int isEvaluate;

    public OrderFormBean() {

    }

    public OrderFormBean(int siteOfReceiveId, String shopName, String imageUrl, String goodName, int amount, String price,int shopId) {
        this.siteOfReceiveId = siteOfReceiveId;
        this.shopName = shopName;
        this.imageUrl = imageUrl;
        this.goodName = goodName;
        this.amount = amount;
        this.price = price;
        this.shopId = shopId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSiteOfReceiveId() {
        return siteOfReceiveId;
    }

    public void setSiteOfReceiveId(int siteOfReceiveId) {
        this.siteOfReceiveId = siteOfReceiveId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(int isEvaluate) {
        this.isEvaluate = isEvaluate;
    }
}
