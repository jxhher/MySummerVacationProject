package com.example.asus.summervacationproject.utils;


import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ASUS on 2018/7/20. 完成OkHttp的GET和POST请求的简单封装
 *
 */

/**
 * Post两种传输：
 * Form表单数据的传递
 * Json格式数据的传递
 */
public class OkHttpUtils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public OkHttpUtils(final String url, final HttpMethod method, final SuccessCallback successCallback,
                       final FailCallback failCallback, final String json){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Response response = null;

                switch(method){
                    case GET:
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        try {
                            response = client.newCall(request).execute();  // 异步请求 okHttpClient.newCall(request).enqueue(new Callback() {}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case POST:
                        RequestBody body = RequestBody.create(JSON, json);
                        Request request2 = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();
                        try {
                            response = client.newCall(request2).execute();// 异步请求 okHttpClient.newCall(request).enqueue(new Callback(){}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                if(response!=null){
                    try {
                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result!=null){
                    if(successCallback!=null){
                        System.out.println(result);
                        successCallback.onSuccess(result);
                    }
                }else{
                    if(failCallback!=null){
                        failCallback.onFail();
                    }
                }
                super.onPostExecute(result);
            }
        }.execute();

    }



    public static interface SuccessCallback{
        void onSuccess(String result);
    }
    public static interface FailCallback{
        void onFail();
    }


}
