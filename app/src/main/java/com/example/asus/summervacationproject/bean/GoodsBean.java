package com.example.asus.summervacationproject.bean;

import java.io.Serializable;

/**
 * Created by ASUS on 2018/7/23.
 * Updated by ASUS on 2018/8/8
 */

public class GoodsBean implements Serializable {
    //价格
    private int cover_price;
    //图片
    private String imageUrl;
    //名称
    private String name;
    //产品id
    private int goodId;
    //快递费
    private int expressage_price;
    //发货地
    private String site;
    //月销量
    private int salesVolume;
    //店铺id
    private int shopId;
    //商品品牌
    private String brand;
    //商品型号
    private String version;
    //商品保修期/保质期
    private String expirationDate;
    //商品生产企业/产地
    private String manufacturing_enterprise;

    /**
     * 是否被选中
     */
    private boolean isSelected = true;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCover_price() {
        return cover_price;
    }

    public void setCover_price(int cover_price) {
        this.cover_price = cover_price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getExpressage_price() {
        return expressage_price;
    }

    public void setExpressage_price(int expressage_price) {
        this.expressage_price = expressage_price;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getManufacturing_enterprise() {
        return manufacturing_enterprise;
    }

    public void setManufacturing_enterprise(String manufacturing_enterprise) {
        this.manufacturing_enterprise = manufacturing_enterprise;
    }
}

