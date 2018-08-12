package com.example.asus.summervacationproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.activity.MainActivity;
import com.example.asus.summervacationproject.bean.SiteOfReceive;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ASUS on 2018/7/18.
 */

public class ListViewAdapter extends BaseAdapter {

    public Context mcontext;
    public List<SiteOfReceive> listData;
    public int listItemId;
    public int id;
    private SharedPreferences sp;
    private String[] ids;
    private StringBuffer newIds = new StringBuffer();
    private boolean change;

    public ListViewAdapter(Context context, List<SiteOfReceive> listData,int listItemId,boolean change) {
        this.mcontext = context;
        this.listData = listData;
        this.listItemId = listItemId;
        this.change = change;
    }

    public ListViewAdapter() {

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
        ViewHolder holder ;
        if (convertView==null){
            convertView = LayoutInflater.from(mcontext).inflate(listItemId,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name.setText(listData.get(position).getName());
        holder.phoneNumber.setText(listData.get(position).getPhoneNumber());
        holder.site.setText(listData.get(position).getSite());
        if(change){
            holder.checkBox.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
        holder.deleteButton.setTag(R.id.btn,listData.get(position).getId());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                id = (Integer) v.getTag(R.id.btn);
                sp = mcontext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                ids = sp.getString("siteOfReceive","").split(",");
                new OkHttpUtils(Config.DELETE_SITEODRECEIVE+"?id="+v.getTag(R.id.btn)+"&userId="+sp.getString("id",""),HttpMethod.GET, new OkHttpUtils.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if(result.equals("true")){
                            SharedPreferences.Editor editor = sp.edit();
                            if(ids.length!=1) {
                                for (int i = 0; i < ids.length; i++) {       //重新生成本地数据
                                    if (Integer.parseInt(ids[i]) == id) {
                                        continue;
                                    }
                                    if (i == 0) {
                                        newIds.append(ids[i]);
                                    }else if(Integer.parseInt(ids[0])==id&&i==1){
                                        newIds.append(ids[i]);
                                    }
                                    else newIds.append("," + ids[i]);
                                }
                            }
                            editor.putString("siteOfReceive",newIds.toString());
                            editor.commit();
                            Message msg = Message.obtain();
                            msg.obj = "删除成功";
                            handler.sendMessage(msg);
                        }else{
                            Snackbar.make(v, "删除失败", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }, new OkHttpUtils.FailCallback() {
                    @Override
                    public void onFail() {
                        Snackbar.make(v, "联网失败", Snackbar.LENGTH_SHORT).show();
                    }
                },null);
            }
        });


        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.siteOfReceive_name_TextView)TextView name;
        @BindView(R.id.siteOfReceive_phoneNumber_TextView)TextView phoneNumber;
        @BindView(R.id.siteOfReceive_site_TextView)TextView site;
        @BindView(R.id.siteOfReceive_deleteButton)Button deleteButton;
        @BindView(R.id.siteOfReceive_setting)CheckBox checkBox;
        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    private Handler handler = new Handler(){
        @Override                                      //移除list集合中被删除条目，更新界面
        public void handleMessage(Message msg){
            // TODO Auto-generated method stub
            String result = (String)msg.obj;
            ToastUtils.getShortToastByString(mcontext, result);
            if (listData.size() > 0){
                for(int i=0;i<listData.size();i++){
                    if(listData.get(i).getId()==id){
                        System.out.println("listDataId:"+listData.get(i).getId()+"listDataName:"+listData.get(i).getName());
                        System.out.println("id:"+id+"   "+"remove"+i);
                        listData.remove(i);
                        break;
                    }
                }
                notifyDataSetChanged();
            }
        }

    };

}
