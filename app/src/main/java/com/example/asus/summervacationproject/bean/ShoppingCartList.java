package com.example.asus.summervacationproject.bean;

import java.util.List;

/**
 * 作用：为了传输数据而建的类
 * Created by ASUS on 2018/8/12.
 */

public class ShoppingCartList {
    private List<ShoppingCartBean> shoppingCartBeen;

    public ShoppingCartList(List<ShoppingCartBean> shoppingCartBeen) {
        this.shoppingCartBeen = shoppingCartBeen;
    }

    public List<ShoppingCartBean> getShoppingCartBeen() {
        return shoppingCartBeen;
    }

    public void setShoppingCartBeen(List<ShoppingCartBean> shoppingCartBeen) {
        this.shoppingCartBeen = shoppingCartBeen;
    }
}
