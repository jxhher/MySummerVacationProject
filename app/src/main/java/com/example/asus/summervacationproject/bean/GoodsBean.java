package com.example.asus.summervacationproject.bean;

import java.io.Serializable;

/**
 * Created by ASUS on 2018/7/23.
 */

public class GoodsBean implements Serializable {
    //价格
    private int cover_price;
    //图片
    private String image;
    //名称
    private String name;
    //产品id
    private int product_id;

    private int number = 1;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCover_price() {
        return cover_price;
    }

    public void setCover_price(int cover_price) {
        this.cover_price = cover_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}

