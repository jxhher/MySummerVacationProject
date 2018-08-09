package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.CommentsBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ASUS on 2018/7/30.
 */

public class GoodCommentListViewAdapter extends BaseAdapter {
    public Context mContext;
    public ArrayList<CommentsBean> commentsBeanList;
    public  int listItemId;

    public GoodCommentListViewAdapter(Context context, ArrayList<CommentsBean> commentsBeanList, int item_good_evaluate) {
        this.mContext = context;
        this.commentsBeanList = commentsBeanList;
        this.listItemId = item_good_evaluate;
    }

    @Override
    public int getCount() {
        return commentsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_good_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.comment_content_TextView.setText(commentsBeanList.get(position).getContent());
        viewHolder.comment_observerName_TextView.setText(commentsBeanList.get(position).getObserverName());
        viewHolder.comment_time_TextView.setText(commentsBeanList.get(position).getTime());
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.good_comment_content)TextView comment_content_TextView;
        @BindView(R.id.good_comment_time)TextView comment_time_TextView;
        @BindView(R.id.good_comment_observerName)TextView comment_observerName_TextView;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }


    }
}
