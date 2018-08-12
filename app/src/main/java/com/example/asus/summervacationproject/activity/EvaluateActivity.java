package com.example.asus.summervacationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.PostType;
import com.example.asus.summervacationproject.utils.ToastUtils;
import com.example.asus.summervacationproject.utils.UserInfo;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/12.
 */

public class EvaluateActivity extends AppCompatActivity {
    @BindView(R.id.evaluate_text)
    EditText evaluate_text;
    private int goodId = -1;
    private String observerName = null;
    private String userId = null;
    private int orderFormId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        goodId = intent.getIntExtra("goodId",-1);
        orderFormId = intent.getIntExtra("orderFormId",-1);
        observerName = UserInfo.getUserInfo(this,"name");
        userId = UserInfo.getUserInfo(this,"id");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.evaluate_linearLayout_back)
    void OnBackClick(){
        finish();
    }

    @OnClick(R.id.evaluate_button)
    void OnEvaluateButtonClick() {
        String text = evaluate_text.getText().toString();
        if (text==null||text.equals("")) {
            Snackbar.make(evaluate_text, "评价不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
       // if (shopId!=null&&observerName!=null) {
        Log.e(EvaluateActivity.class.getSimpleName(),"goodId:"+goodId+"observerName:"+observerName+"text:"+text+"userId:"+userId+"orderFormId:"+orderFormId);
            String[] data = {goodId+"",observerName,text,userId,orderFormId+""};

            new OkHttpUtils(Config.ADD_COMMENT, PostType.ADD, new OkHttpUtils.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    if(result.equals("true")) {
                        ToastUtils.getShortToastByString(EvaluateActivity.this,"提交成功");
                    }else{
                        ToastUtils.getShortToastByString(EvaluateActivity.this,"提交失败");
                    }
                    Intent intent = new Intent(EvaluateActivity.this,MyOrderFormsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, new OkHttpUtils.FailCallback() {
                @Override
                public void onFail() {
                    ToastUtils.getShortToastByString(EvaluateActivity.this,"提交失败");
                    finish();
                }
            },data);
        }
  //  }
}
