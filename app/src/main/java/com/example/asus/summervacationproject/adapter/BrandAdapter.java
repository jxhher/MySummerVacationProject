package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.ResultBeanData;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by ASUS on 2018/7/26.
 */
public class BrandAdapter extends BaseAdapter{
    private Context context;
    private List<ResultBeanData.ResultBean.BrandInfoBean> brandList;


    public BrandAdapter(Context mContext, List<ResultBeanData.ResultBean.BrandInfoBean> brandList) {
        this.context = mContext;
        this.brandList = brandList;
    }

    @Override
    public int getCount() {
        Log.e(TAG,"gridView的大小："+brandList.size());
        return brandList.size();

    }

    @Override
    public Object getItem(int position) {
        return brandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.item_brand,null);
            viewHolder = new BrandAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ResultBeanData.ResultBean.BrandInfoBean brand = brandList.get(position);
        Log.e(TAG,"brandAdapter:"+brand.getImage());
        Picasso.with(context).load(Config.BASE_URL_IMAGE+brand.getImage()).into(viewHolder.imageView);
        return convertView;

    }
        static class ViewHolder{
            @BindView(R.id.brand_image) ImageView imageView;
           public ViewHolder(View convertView) {
                ButterKnife.bind(this,convertView);
            }

        }


}
