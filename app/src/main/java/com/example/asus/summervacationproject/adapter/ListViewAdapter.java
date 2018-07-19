package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/18.
 */

public class ListViewAdapter extends BaseAdapter {

    public  Context mcontext;
    public  ArrayList<String> listData;
    public int listItemId;
//    private ImageView imageView;
//    private TextView textView;

    public ListViewAdapter(Context context, ArrayList<String> listData,int listItemId) {
        this.mcontext = context;
        this.listData = listData;
        this.listItemId = listItemId;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(mcontext).inflate(listItemId,parent,false);
//            imageView = ButterKnife.findById(convertView,R.id.adapter_drawerListItem_imageView);
//            textView = ButterKnife.findById(convertView,R.id.adapter_drawerListItem_textView);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        //holder.imageView.setImageBitmap();
        holder.textView.setText(listData.get(position));
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.adapter_drawerListItem_textView) TextView textView;
        @BindView(R.id.adapter_drawerListItem_imageView) ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
