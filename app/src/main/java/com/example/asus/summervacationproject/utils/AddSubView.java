package com.example.asus.summervacationproject.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/9.
 */

public class AddSubView extends LinearLayout {
    private Context mContext;
    private View view;
    @BindView(R.id.shopCart_add_imageView)
    ImageView shopCart_add_imageView;
    @BindView(R.id.shopCart_sub_imageView)
    ImageView shopCart_sub_imageView;
    @BindView(R.id.shopCart_value_textView)
    TextView shopCart_value_textView;
    private int value = 1;
    private int minValue = 1;
    private int maxValue = 5;
    private OnNumberChangeListener  onNumberChangeListener;

    public AddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        view = View.inflate(context, R.layout.add_sub_view,this);
        ButterKnife.bind(this,view);

    }

    @OnClick(R.id.shopCart_add_imageView)
    void OnAddButtonClick(){
        if(value < maxValue){
            value ++;
        }
        setValue(value);

        if(onNumberChangeListener !=null){
            onNumberChangeListener.onNumberChange(value);
        }
    }

    @OnClick(R.id.shopCart_sub_imageView)
    void OnSubButtonClick(){
        if(value > minValue){
            value --;
        }
        setValue(value);

        if(onNumberChangeListener !=null){
            onNumberChangeListener.onNumberChange(value);
        }
    }


    public int getValue() {
        String valueStr =  shopCart_value_textView.getText().toString().trim();
        if(!TextUtils.isEmpty(valueStr)){
            value = Integer.parseInt(valueStr) ;
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        shopCart_value_textView.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    public interface OnNumberChangeListener{
        public void onNumberChange(int value);
    }


    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }

}
