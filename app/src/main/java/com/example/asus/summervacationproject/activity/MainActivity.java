package com.example.asus.summervacationproject.activity;

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.ListViewAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.fragment.ClassificationFragment;
import com.example.asus.summervacationproject.fragment.DiscoverFragment;
import com.example.asus.summervacationproject.fragment.HomePageFragment;
import com.example.asus.summervacationproject.fragment.ShoppingCartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 作用：主页面
 * Created by ASUS on 2018/7/17. 开始准备工作，配置Git和Github，添加ButterKnife插件
 * Updated by ASUS on 2018/7/18  组合toolbar和drawerLayout成功
 * Updated by ASUS on 2018/7/19  成功搭建底部模块选项，完善UI图标
 */


public class MainActivity extends AppCompatActivity implements android.widget.PopupMenu.OnMenuItemClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.left_drawer_list)
    ListView mDrawerListView;
    @BindView(R.id.rg_main)
    RadioGroup radioGroup;


    private ArrayList<String> menuLists = new ArrayList<String>();
    private ActionBarDrawerToggle mDrawerToggle;
    private ListViewAdapter adapter;
    private ImageView button;
    private List<BaseFragment> mBaseFragmentList;
    private int selectPostion;   //选中的Fragment对应的位置
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initDrawerLayout();
        initFragment();
        setFragment();

        //监听点击事件实现Popmenu效果
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.toolbar_menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();
            }
        });
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.drawable.user_image);//设置toolbar导航栏图标
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        button = new ImageView(this);
        button.setImageResource(R.drawable.add_image);
        toolbar.addView(button);
        //为ImageView设置参数
        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = GravityCompat.END;
        layoutParams.rightMargin = 20;

        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {

        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {           //抽屉打开
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {           //抽屉关闭
                super.onDrawerClosed(drawerView);

            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        for(int i=0;i<6;i++)
            menuLists.add("DrawerLayout"+i);
        adapter = new ListViewAdapter(this,menuLists,R.layout.adapter_list_item);
        mDrawerListView.setAdapter(adapter);

    }

    @OnItemClick(R.id.left_drawer_list)
    void onItemClick(int potion) {
        Toast.makeText(this, "点击了:" + potion, Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(mDrawerListView );
    }

    private void initFragment() {
        mBaseFragmentList = new ArrayList<>();
        mBaseFragmentList.add(new HomePageFragment());
        mBaseFragmentList.add(new ClassificationFragment());
        mBaseFragmentList.add(new DiscoverFragment());
        mBaseFragmentList.add(new ShoppingCartFragment());
    }


    private void setFragment() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.bottom_homePage:
                        selectPostion = 0;
                        break;
                    case R.id.bottom_classification:
                        selectPostion = 1;
                        break;
                    case R.id.bottom_discover:
                        selectPostion = 2;
                        break;
                    case R.id.bottom_shoppingCart:
                        selectPostion = 3;
                        break;
                    default:
                        selectPostion = 0;
                        break;
                }
                Fragment nextFragment = mBaseFragmentList.get(selectPostion);
                //替换
                switchFrament(mContent,nextFragment);

            }
        });

        //设置默认选中常用框架
        radioGroup.check(R.id.bottom_homePage);
    }

    private void switchFrament(Fragment from, Fragment to) {
        if(from!=to){
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(!to.isAdded()){
                if(from!=null){
                    ft.hide(from);
                }
                if(to!=null){
                    ft.add(R.id.content,to).commit();
                }
            }else{
                if(from!=null){
                    ft.hide(from);
                }
                if(to!=null){
                    ft.show(to).commit();
                }
            }
        }

    }

    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sync:
                Toast.makeText(this, "同步", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    /**
     * 为Toolbar设置Menu
     */

    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
