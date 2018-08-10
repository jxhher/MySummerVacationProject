package com.example.asus.summervacationproject.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.asus.summervacationproject.R;
import com.example.asus.summervacationproject.bean.User;
import com.example.asus.summervacationproject.utils.BitmapUtils;
import com.example.asus.summervacationproject.utils.Config;
import com.example.asus.summervacationproject.utils.HttpMethod;
import com.example.asus.summervacationproject.utils.OkHttpUtils;
import com.example.asus.summervacationproject.utils.PostType;
import com.example.asus.summervacationproject.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ASUS on 2018/8/6.
 * Updated by ASUS on 2018/8/6 完成界面的基本布局及对应的数据显示
 * Updated by ASUS on 2018/8/7 完成个人头像的修改，图片的上传，头像数据的更新
 */

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.userinfo_linearLayout_back)
    LinearLayout userinfo_button_back;
    @BindView(R.id.userInfo_linearLayout_name)
    RelativeLayout userInfo_linearLayout_name;
    @BindView(R.id.userInfo_linearLayout_sex)
    RelativeLayout userInfo_linearLayout_sex;
    @BindView(R.id.userInfo_linearLayout_imageView)
    RelativeLayout userInfo_linearLayout_imageView;
    @BindView(R.id.userInfo_linearLayout_siteOfReceive)
    RelativeLayout userInfo_linearLayout_siteOfReceive;
    @BindView(R.id.userInfo_name)
    TextView userInfo_name;
    @BindView(R.id.userInfo_sex)
    TextView userInfo_sex;
    @BindView(R.id.userInfo_imageView)
    ImageView userInfo_imageView;
    @BindView(R.id.userInfo_progressBar)
    ProgressBar userInfo_progressBar;
    @BindView(R.id.userInfo_age)
    TextView userInfo_age;

    private String updateFlag = null;
    private final int IMAGEURL = 0;
    private final int NAME = 1;
    private final int SEX = 2;
    private final int SITEOFRECEIVE = 3;
    private boolean flag = false;
    public int id = -1;
    private String type = null;
    private int updateType = -1;
    private int PICTURE = 0;
    private int CAMERA = 1;
    private Intent updateIntent = new Intent("com.example.asus.summervacationproject.activity.LoginActivity");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id").toString());
        userInfo_name.setText(intent.getStringExtra("name"));
        userInfo_sex.setText(intent.getStringExtra("sex"));
        userInfo_age.setText(intent.getStringExtra("age"));
    }
    @OnClick(R.id.userinfo_linearLayout_back)
    void OnBackButtonClick(){
        finish();
    }
    @OnClick(R.id.userInfo_linearLayout_imageView)
    void OnImageViewClick(){
        String[] items = new String[]{"图库","相机"};
        new AlertDialog.Builder(this)
                .setTitle("请选择来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:  //图库
                                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(picture, PICTURE);
                                break;
                            case 1:  //相机
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera, CAMERA);
                                break;
                        }
                    }
                }) .setCancelable(false)
                   .setNegativeButton("取消",null).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA && resultCode == RESULT_OK && data != null){//相机
            userInfo_progressBar.setVisibility(View.VISIBLE);
            //获取intent中的图片对象
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            //对获取到的bitmap进行压缩、圆形处理
            bitmap = BitmapUtils.zoom(bitmap,200,200);
            bitmap = BitmapUtils.circleBitmap(bitmap);

            //上传到服务器
            sendImage(bitmap);
            //保存到本地
            //saveImage(bitmap);


        }else if(requestCode == PICTURE && resultCode == RESULT_OK && data != null){//图库
            //图库
            Uri selectedImage = data.getData();
            //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
            // 所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
            //这里返回的uri情况就有点多了
            //在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
            // 在4.4.2返回的是content://com.android.providers.media.documents/document/image

            String pathResult = getPath(selectedImage);
            //存储--->内存
            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, 200,200);
            //bitmap圆形裁剪
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

            //上传到服务器
            sendImage(circleImage);
            //保存到本地
            //saveImage(circleImage);

        }


    }

    private void sendImage(Bitmap bitmap) {
        userInfo_progressBar.setVisibility(View.VISIBLE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();
        String image = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        String[] datas = {image,id+""};

        new OkHttpUtils(Config.SEND_HEADPORTRAIT, PostType.SEND, new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                userInfo_progressBar.setVisibility(View.GONE);
                updateUserInfo(IMAGEURL,"/send_head_portrait"+id+".png");
                ToastUtils.getShortToastByString(UserInfoActivity.this,"设置成功");
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                userInfo_progressBar.setVisibility(View.GONE);
                ToastUtils.getShortToastByString(UserInfoActivity.this,"设置失败");
            }
        },datas);
    }


    @OnClick(R.id.userInfo_linearLayout_name)
    void OnNameClick(){
        final EditText et = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("设置你的名称")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        updateUserInfo(NAME,input);

                    }
                }).setNegativeButton("取消",null);
        topInputMethod(dialog,et);
    }




    private void updateUserInfo(int i,final String data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        switch (i){
            case IMAGEURL:
                type = "imageUrl";
                updateType = IMAGEURL;
                jsonObject.put(type,data);
                break;
            case NAME:
                type = "name";
                updateType = NAME;
                jsonObject.put(type,data);
                break;
            case SEX:
                type = "sex";
                updateType = SEX;
                jsonObject.put(type,data);
                break;
            case SITEOFRECEIVE:
                break;
        }

        new OkHttpUtils(Config.UPDATE_USER_URL, HttpMethod.POST,new OkHttpUtils.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                SharedPreferences sp = UserInfoActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(type,data);
                editor.commit();
                Message msg = new Message();
                msg.arg1 = 1;
                msg.arg2 = updateType;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        }, new OkHttpUtils.FailCallback() {
            @Override
            public void onFail() {
                Message msg = new Message();
                msg.arg1 = 0;
                handler.sendMessage(msg);
            }
        },jsonObject.toString());
    }

    Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1){
                    case 0:
                        Snackbar.make(userInfo_linearLayout_siteOfReceive, "修改失败", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        switch (msg.arg2){
                            case NAME:
                                userInfo_name.setText((String) msg.obj);
                                LocalBroadcastManager.getInstance(UserInfoActivity.this).sendBroadcast(updateIntent);
                                break;
                            case SEX:
                                userInfo_sex.setText((String)msg.obj);
                                break;
                            case IMAGEURL:
                                LocalBroadcastManager.getInstance(UserInfoActivity.this).sendBroadcast(updateIntent);
                                break;
                        }
                        break;
                }
            }
        };

    @OnClick(R.id.userInfo_linearLayout_sex)
    void OnSexClick(){
        final EditText et2 = new EditText(this);

        AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("设置你的性别")
                .setView(et2)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input2 = et2.getText().toString();
                        updateUserInfo(SEX,input2);
                    }
                }).setNegativeButton("取消",null);
        topInputMethod(dialog2,et2);
    }
    @OnClick(R.id.userInfo_linearLayout_siteOfReceive)
    void OnSiteOfReceiveClick(){
            Intent intent = new Intent(UserInfoActivity.this,ManageSiteOfReceiveActivity.class);
            startActivity(intent);
    }

    private void topInputMethod(AlertDialog.Builder dialog, final EditText et3) {

        //下面是弹出键盘的关键处
        AlertDialog tempDialog = dialog.create();
        tempDialog.setView(et3, 0, 0, 0, 0);

        tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et3, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        tempDialog.show();
    }

    private Uri saveImage(Bitmap bitmap) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getFilesDir();

        }
        FileOutputStream fos = null;
        try {
            File file = new File(filesDir,"user_head_portrait"+id+".png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            System.out.println("file:"+Uri.fromFile(file));
            return Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }


        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @OnClick(R.id.userInfo_loginOutButton)
    void OnLoginOutButtonClick(){              //退出登录删除本地数据
        SharedPreferences sp = this.getSharedPreferences("user_info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//删除个人数据
        editor.clear();
        editor.commit();

        SharedPreferences sp2 = this.getSharedPreferences("shopping_cart_"+id+"",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sp.edit();//删除购物车数据
        editor.clear();
        editor.commit();
        File filesDir;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getFilesDir();

        }
        File file = new File(filesDir,"send_head_portrait"+id+".png");
        if(file.exists()){
            System.out.println("delete");
            file.delete();//删除存储中的文件
        }
        Intent intent = new Intent(this,MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        /*FLAG_ACTIVITY_CLEAR_TASK这个标志,那么这个标志将会清除之前所有已经打开的activity.
        然后将会变成另外一个空栈的root,然后其他的Activitys就都被关闭了.
        这个方法必须跟着{@link #FLAG_ACTIVITY_NEW_TASK}一起使用.
        * */
    }
}
