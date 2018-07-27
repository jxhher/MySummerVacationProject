package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.asus.summervacationproject.R;
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
 * Upfated by ASUS on 2018/7/22   完成banner与picasso与okhttp的配合，解决异步加载请求数据与页面适配器配合的大bug
 * Updated by ASUS on 2018/7/23   找到及完善theme的图标,完成基本布局
 * Updated by ASUS on 2018/7/26   解决theme的bug，成功显示图标
 */

public class HomePageAdapter extends RecyclerView.Adapter {

    private static final int BANNER = 0;
    private static final int THEME = 1;
    private static final int BRAND = 2;
    private static final int DISCOUNT = 3;
    private static final int RECOMMEND = 4;

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
        }/*/*//*else if (viewType == BANNER) {
            return new SeckillViewHolder(mContext, mLayoutInflater.inflate(R.layout.item_discount, null));
        }else if(viewType == DISCOUNT ){
            return new RecommendViewHolder(mContext, mLayoutInflater.inflate(R.layout.item_recommend, null));
        }else if(viewType == RECOMMEND){
            return new HotViewHolder(mContext, mLayoutInflater.inflate(R.layout.item_hot, null));
        }*/
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
                Log.e(TAG, resultBean.getBanner_info().toString());
            } else {
                Log.e(TAG, "resultBean为null:");
            }
        } else if (getItemViewType(position) == THEME) {
            ThemeViewHolder themeViewHolder = (ThemeViewHolder) holder;
            if (resultBean != null) {
                themeViewHolder.setData(resultBean.getThemeInfoBean());
                Log.e(TAG, resultBean.getThemeInfoBean().toString());
            } else {
                Log.e(TAG, "result为null");
            }
        } else if (getItemViewType(position) == BRAND) {
            BrandViewHolder brandViewHolder = (BrandViewHolder) holder;
            if (resultBean != null) {
                brandViewHolder.setData(resultBean.getBrandInfoBean());
            }else{
                Log.e(TAG, "result为null");
            }
        }/*else if(getItemViewType(position) == SECKILL){
            SeckillViewHolder seckillViewHolder = (SeckillViewHolder) holder;
            seckillViewHolder.setData(resultBean.getSeckill_info());
        }else if(getItemViewType(position) == RECOMMEND){
            RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
            recommendViewHolder.setData(resultBean.getRecommend_info());
        }else if(getItemViewType(position)==HOT){
            HotViewHolder hotViewHolder = (HotViewHolder) holder;
            hotViewHolder.setData(resultBean.getHot_info());
        }**/
    }




     class BannerViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private Banner banner;

        public BannerViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            this.banner = (Banner) itemView.findViewById(R.id.banner);
        }

        public void setData(List<ResultBeanData.ResultBean.BannerInfoBean> banner_info){
            ArrayList<String> imageUrlList = new ArrayList<>();
            for(int i=0;i<banner_info.size();i++){
                imageUrlList.add(banner_info.get(i).getImage());
                Log.e(TAG,"adapter:"+banner_info.get(i).getImage());
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
                    startBannerValueActivity();
                }
            });
        }

        private void startBannerValueActivity() {
//            Intent intent = new Intent(mContext, GoodsInfoActivuty.class);
//            intent.putExtra(GOODS_BEAN,goodsBean);
//            mContext.startActivity(intent);
        }
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
                Log.e(TAG,"themeList为null");
            }else{
                Log.e(TAG,themeList.toString());
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
        return 3;
    }
}
