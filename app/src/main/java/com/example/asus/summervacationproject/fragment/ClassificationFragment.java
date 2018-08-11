package com.example.asus.summervacationproject.fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.baseclass.BaseFragment;

/**
 * Created by ASUS on 2018/7/19.
 */

public class ClassificationFragment extends BaseFragment {
    private static final String TAG = ClassificationFragment.class.getSimpleName();
    private Toolbar mToolBar;
    private TextView titleTextView;

    @Override
    protected View initView() {
        Log.e(TAG,"分类Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_classification,null);
        return view;
    }

        @Override
        public void onResume() {
            if (mToolBar == null) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                mToolBar = (Toolbar) activity.findViewById(R.id.toolbar);
                if (titleTextView == null) {
                    titleTextView = (TextView) mToolBar.findViewWithTag("title");
                    titleTextView.setTextSize(21);
                }
            }
            titleTextView.setText("分类");
            super.onResume();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden) {


        }else{
            titleTextView = (TextView) mToolBar.findViewWithTag("title");
            titleTextView.setText("分类");
        }

    }
}
