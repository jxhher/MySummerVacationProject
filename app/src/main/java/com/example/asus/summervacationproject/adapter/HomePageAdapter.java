package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.activity.GoodsInfoActivity;
import com.example.asus.summervacationproject.bean.GoodsBean;
import com.example.asus.summervacationproject.bean.ResultBeanData;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;

import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ASUS on 2018/7/21.
 * Upfated by ASUS on 2018/7/22   完成banner与picasso与okhttp的配合，解决异步加载请求数据与页面适配器响应配合时间的bug
 * Updated by ASUS on 2018/7/23   找到及完善theme的图标,完成基本布局
 * Updated by ASUS on 2018/7/26   解决theme因json数据数据名造成实例化不了集合的bug，成功显示图标
 * Upaated by ASUS on 2018/7/27   完成brand的显示，解决因set和get方法错误造成实例化不了对象的bug及gridView只显示一行的bug
 * Updated by ASUS on 2018/7/28   完成recommend的显示及页面返回顶部的按钮
 */

public class HomePageAdapter extends RecyclerView.Adapter {

    private static final int BANNER = 0;
    private static final int THEME = 1;
    private static final int BRAND = 2;
    private static final int DISCOUNT = 3;
    private static final int RECOMMEND = 4;
    private static final String GOOD_BEAN = "goodBean";

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示
    /**
     * 用来初始化布局
     */
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    /**
     * 数据
     */
    private ResultBeanData.ResultBean resultBean;

    /**
     * 当前类型
     */
    private int currentType = BANNER;

    public HomePageAdapter(Context mContext, ResultBeanData.ResultBean resultBean) {
        this.mContext = mContext;
        this.resultBean = resultBean;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    /**
     *
     * 相当于getView 创建ViewHolder部分代码
     * 创建ViewHolder
     * @param parent
     * @param viewType  当前的类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BANNER) {
            return new BannerViewHolder(mContext, mLayoutInflater.inflate(R.layout.type_banner, null));
        } else if (viewType == THEME) {
            return new ThemeViewHolder(mContext, mLayoutInflater.inflate(R.layout.type_theme, null));
        } else if (viewType == BRAND) {
            return new BrandViewHolder(mContext, mLayoutInflater.inflate(R.layout.type_brand, null,false));
        }else if (viewType == DISCOUNT) {
            return new DiscountViewHolder(mContext, mLayoutInflater.inflate(R.layout.type_discount, null));
        }else if(viewType == RECOMMEND){
            return new RecommendViewHolder(mContext, mLayoutInflater.inflate(R.layout.type_recommend, null,false));
        }
        return null;
    }

    /**
     * 相当于getView中的绑定模块
     *
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "position:" + position);
        if (getItemViewType(position) == BANNER) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            if (resultBean != null) {
                bannerViewHolder.setData(resultBean.getBanner_info());
                //   Log.e(TAG, resultBean.getBanner_info().toString());
            } else {
                //  Log.e(TAG, "resultBean为null:");
            }
        } else if (getItemViewType(position) == THEME) {
            ThemeViewHolder themeViewHolder = (ThemeViewHolder) holder;
            if (resultBean != null) {
                themeViewHolder.setData(resultBean.getThemeInfoBean());
                //  Log.e(TAG, resultBean.getThemeInfoBean().toString());
            } else {
                // Log.e(TAG, "result为null");
            }
        } else if (getItemViewType(position) == BRAND) {
            BrandViewHolder brandViewHolder = (BrandViewHolder) holder;
            if (resultBean != null) {
                brandViewHolder.setData(resultBean.getBrandInfoBean());
            } else {
                // Log.e(TAG, "result为null");
            }
        } else if (getItemViewType(position) == DISCOUNT) {
            DiscountViewHolder discountViewHolder = (DiscountViewHolder) holder;
            if (resultBean != null) {
                discountViewHolder.setData(resultBean.getDiscountInfoBean());
            } else {
                // Log.e(TAG, "result为null");
            }
        } else if (getItemViewType(position) == RECOMMEND) {
            RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
            if (resultBean != null) {
                recommendViewHolder.setData(resultBean.getRecommend_info());
            } else {
                // Log.e(TAG, "result为null");
            }
        } else {


        }


    }

     class BannerViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private Banner banner;

        public BannerViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            this.banner = (Banner) itemView.findViewById(R.id.banner);
        }

        public void setData(final List<ResultBeanData.ResultBean.BannerInfoBean> banner_info){
            ArrayList<String> imageUrlList = new ArrayList<>();
            for(int i=0;i<banner_info.size();i++){
                imageUrlList.add(banner_info.get(i).getImage());
               // Log.e(TAG,"adapter:"+banner_info.get(i).getImage());
            }
            banner.setImages(imageUrlList);
            //设置循环指示点
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setBannerAnimation(Transformer.Default);
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                   Picasso.with(context).load(Config.BASE_URL_IMAGE+path).into(imageView);
                }
            });
            //banner.setBannerTitles();        //设置标题集合
            banner.setDelayTime(5000);
            banner.setIndicatorGravity(BannerConfig.CENTER); //设置指示器位置
            banner.start();


            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    Toast.makeText(mContext, "position==" + position, Toast.LENGTH_SHORT).show();
                    GoodsBean goodsBean  = new GoodsBean();;
                    startBannerValueActivity(goodsBean);
                }
            });
        }

    }

    private void startBannerValueActivity(GoodsBean goodsBean) {

    }


    class ThemeViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private GridView gridView;

        private List<ResultBeanData.ResultBean.ThemeInfoBean> data;
        private ThemeAdapter adapter;

        public ThemeViewHolder(final Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            gridView = (GridView) itemView.findViewById(R.id.theme_gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "position" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(List<ResultBeanData.ResultBean.ThemeInfoBean> themeList) {
            if(themeList==null){
              //  Log.e(TAG,"themeList为null");
            }else{
              //  Log.e(TAG,themeList.toString());
            }
            adapter = new ThemeAdapter(mContext,themeList);
            gridView.setAdapter(adapter);
        }
    }




    class BrandViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private GridView gridView;
        private List<ResultBeanData.ResultBean.BrandInfoBean> brandList;
        private BrandAdapter brandAdapter;

        public BrandViewHolder(Context context,View itemView) {
            super(itemView);
            this.mContext = context;
            gridView = (GridView) itemView.findViewById(R.id.brand_gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "position" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(List<ResultBeanData.ResultBean.BrandInfoBean> brandList) {
            this.brandList = brandList;
            brandAdapter = new BrandAdapter(mContext,brandList);
            gridView.setAdapter(brandAdapter);
        }
    }


    class DiscountViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private GridView gridView;
        private List<ResultBeanData.ResultBean.DiscountInfoBean> discountList;
        private DiscountAdapter discountAdapter;

        public DiscountViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            gridView = (GridView) itemView.findViewById(R.id.discount_gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, "position" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setData(List<ResultBeanData.ResultBean.DiscountInfoBean> discountList) {
            this.discountList = discountList;
            discountAdapter = new DiscountAdapter(context, discountList);
            gridView.setAdapter(discountAdapter);
        }


    }

        class RecommendViewHolder extends RecyclerView.ViewHolder{
            private Context context;
            private GridView gridView;
            private List<ResultBeanData.ResultBean.RecommendInfoBean> recommendList;
            private RecommendAdapter recommendAdapter;

            public RecommendViewHolder(Context context,View itemView) {
                super(itemView);
                this.context = context;
                gridView = (GridView) itemView.findViewById(R.id.recommend_gridView);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        GoodsBean goodsBean  = new GoodsBean();
                        goodsBean.setCover_price(recommendList.get(position).getCover_price());
                        goodsBean.setName(recommendList.get(position).getName());
                        goodsBean.setImageUrl(recommendList.get(position).getImageUrl());
                        goodsBean.setGoodId(recommendList.get(position).getGoodId());
                        goodsBean.setShopId(recommendList.get(position).getShopId());
                        startGoodValueActivity(goodsBean);
                    }
                });
            }

            public void setData(List<ResultBeanData.ResultBean.RecommendInfoBean> recommendList) {
                this.recommendList = recommendList;
                recommendAdapter = new RecommendAdapter(context,recommendList);
                gridView.setAdapter(recommendAdapter);
            }
    }



        private void startGoodValueActivity(GoodsBean goodBean) {
            Intent intent = new Intent(mContext, GoodsInfoActivity.class);
            intent.putExtra(GOOD_BEAN,goodBean);
            mContext.startActivity(intent);
        }



    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case BANNER:
                currentType = BANNER;
                break;
            case THEME:
                currentType = THEME;
                break;
            case BRAND:
                currentType = BRAND;
                break;
            case DISCOUNT:
                currentType = DISCOUNT;
                break;
            case RECOMMEND:
                currentType = RECOMMEND;
                break;

        }
        return currentType;
    }



    @Override
    public int getItemCount() {
        return 5;
    }
}
