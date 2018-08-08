package com.example.asus.summervacationproject.bean;

/**
 * Created by ASUS on 2018/8/8.
 */

public class Shop {
    private int shopId;
    private String shopName;
    private String attentionAmount;
    private String goodsAmount;
    private String introduce;
    private String merchantService;
    private String expressageService;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getAttentionAmount() {
        return attentionAmount;
    }

    public void setAttentionAmount(String attentionAmount) {
        this.attentionAmount = attentionAmount;
    }

    public String getMerchantService() {
        return merchantService;
    }

    public void setMerchantService(String merchantService) {
        this.merchantService = merchantService;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getExpressageService() {
        return expressageService;
    }

    public void setExpressageService(String expressageService) {
        this.expressageService = expressageService;
    }
}
