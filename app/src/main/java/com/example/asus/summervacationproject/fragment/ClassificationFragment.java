package com.example.asus.summervacationproject.fragment;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;

/**
 * Created by ASUS on 2018/7/19.
 */

public class ClassificationFragment extends BaseFragment {
    private static final String TAG = ClassificationFragment.class.getSimpleName();

    @Override
    protected View initView() {
        Log.e(TAG,"分类Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_classification,null);
        return view;
    }
}
