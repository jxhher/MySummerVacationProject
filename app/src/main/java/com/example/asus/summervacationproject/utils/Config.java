package com.example.asus.summervacationproject.utils;

import com.example.asus.summervacationproject.bean.Shop;

/**
 * Created by ASUS on 2018/7/20.
 */

public class Config {
    public static final String SERVEL_URL = "http://192.168.0.104:8080/shopping_app_server";
    public static final String HOMEPAGE_URL = SERVEL_URL+"/servlet/initHomePage";
    public static final String BASE_URL_IMAGE  = SERVEL_URL+"/img";
    public static final String Base_URl_HEAD_PORTRAIT =  SERVEL_URL+"/head_portrait";
    public static final String LOGINPAGE_URL = SERVEL_URL+"/servlet/loginServlet";
    public static final String UPDATE_USER_URL = SERVEL_URL+"/servlet/updateUserServlet";
    public static final String FIND_SITEOFRECEIVE_URL = SERVEL_URL+"/servlet/findSiteOfReceiveServlet";
    public static final String DELETE_SITEODRECEIVE = SERVEL_URL+"/servlet/deleteSiteOfReceiveServlet";
    public static final String ADD_SITEOFRECEIVE = SERVEL_URL+"/servlet/addSiteOfReceiveServlet";
    public static final String SEND_HEADPORTRAIT = SERVEL_URL+"/servlet/sendHeadPortraitServlet";
    public static final String REGISTER_UTL = SERVEL_URL+"/servlet/registerServlet";
    public static final String GET_GOOD_URL = SERVEL_URL+"/servlet/getGoodInfoServlet";
    public static final String GET_SHOP_URL = SERVEL_URL+"/servlet/getShopInfoServlet";
    public static final String GET_COMMENT_URL = SERVEL_URL+"/servlet/getCommentsServlet";
    public static final String ADD_SHOPPINGCART = SERVEL_URL+"/servlet/addShoppingCartServlet";
}
