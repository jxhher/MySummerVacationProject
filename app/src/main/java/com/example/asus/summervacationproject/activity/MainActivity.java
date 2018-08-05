package com.example.asus.summervacationproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.adapter.ListViewAdapter;
import com.example.asus.summervacationproject.baseclass.BaseFragment;
import com.example.asus.summervacationproject.fragment.ClassificationFragment;
import com.example.asus.summervacationproject.fragment.DiscoverFragment;
import com.example.asus.summervacationproject.fragment.HomePageFragment;
import com.example.asus.summervacationproject.fragment.ShoppingCartFragment;
import com.example.asus.summervacationproject.utils.BitmapUtils;
import com.example.asus.summervacationproject.utils.Config;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.drawer_layout_navigationView)
    NavigationView navigationView;
    @BindView(R.id.rg_main)
    RadioGroup radioGroup;
    private ArrayList<String> menuLists = new ArrayList<String>();


    private ActionBarDrawerToggle mDrawerToggle;
    private ListViewAdapter adapter;
    private ImageView button;
    private List<BaseFragment> mBaseFragmentList;
    private int selectPostion;   //选中的Fragment对应的位置
    private Fragment mContent;
    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    protected  SharedPreferences sp;
    private ImageView head_portrait_imageView;
    private TextView head_userName_textView;
    private String TAG = MainActivity.class.getSimpleName();
    private boolean login_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
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
        View headerView = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        head_portrait_imageView = (ImageView) headerView.findViewById(R.id.headerView_head_portrait);
        head_portrait_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login_flag){
                    Intent intent = new Intent(MainActivity.this,UserInfoActivity.class);
                    intent.putExtra("name",sp.getString("name",""));
                    intent.putExtra("sex",sp.getString("sex",""));
                    startActivity(intent);
                }
                else login();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        head_userName_textView = (TextView)headerView.findViewById(R.id.headView_userName);
        head_userName_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login_flag){
                    Intent intent = new Intent(MainActivity.this,UserInfoActivity.class);
                    intent.putExtra("name",sp.getString("name",""));
                    intent.putExtra("sex",sp.getString("sex",""));
                    startActivity(intent);
                }
                else login();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.asus.summervacationproject.activity.LoginActivity");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
        islogin();
    }

    private void islogin() {
        SharedPreferences sp2 = MainActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp2.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            return;
        }else{
            setUser();
        }
    }

    public void setUser(){
        sp = MainActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String imageUrl = sp.getString("imageUrl","");
        // toolbar.setNavigationIcon();//设置toolbar导航栏图标
        head_userName_textView.setText(sp.getString("name",""));
          /*  //判断本地是否已经保存头像的图片，如果有，则不再执行联网操作
            boolean isExist = readImage();
            if(isExist){
                return;
            }*/
        //使用Picasso联网获取图
        Log.e(TAG,Config.Base_URl_HEAD_PORTRAIT+sp.getString("imageUrl",""));
        Picasso.with(MainActivity.this).load(Config.Base_URl_HEAD_PORTRAIT+sp.getString("imageUrl","")).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {//下载以后的内存中的bitmap对象
                //压缩处理
                Bitmap bitmap = BitmapUtils.zoom(source,dp2px(62),dp2px(62));
                //圆形处理
                bitmap = BitmapUtils.circleBitmap(bitmap);
                //回收bitmap资源
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "";//需要保证返回值不能为null。否则报错
            }
        }).into( head_portrait_imageView);
        login_flag = true;
    }


    //将dp转化为px
    public  int dp2px(int dp){
        //获取手机密度
        float density = MainActivity.this.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);//实现四舍五入
    }


    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
           setUser();
        }
        private boolean readImage() {
            File filesDir;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
                //路径1：storage/sdcard/Android/data/包名/files
                filesDir = MainActivity.this.getExternalFilesDir("");

            }else{//手机内部存储
                //路径：data/data/包名/files
                filesDir = MainActivity.this.getFilesDir();

            }
            File file = new File(filesDir,"icon.png");
            if(file.exists()){
                //存储--->内存
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                head_portrait_imageView.setImageBitmap(bitmap);
                return true;
            }
            return false;

        }
    }

        private void login() {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
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

    private boolean flag = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RESET_BACK:
                    flag = true;//复原
                    break;
            }
        }
    };
    private static final int RESET_BACK = 1;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&flag){
            Toast.makeText(MainActivity.this,"再点击一次，退出当前应用",Toast.LENGTH_SHORT).show();
            flag = false;
            handler.sendEmptyMessageDelayed(RESET_BACK,2000);  //发送延迟消息，判断两秒内点击两次
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);  //为防止内存泄漏，移除所有的未被执行的消息
        localBroadcastManager.unregisterReceiver(localReceiver);
    }


}
