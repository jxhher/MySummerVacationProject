package com.example.asus.summervacationproject.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
	 private static Context context = null;
	 private static Toast toast = null;
	 
	 public static void getShortToastByString(Context context,String hint){
	        if (toast == null) {
	            toast = Toast.makeText(context, hint, 0);
	        } else {
	            toast.setText(hint);
	            toast.setDuration(Toast.LENGTH_SHORT);
	        }


	        toast.show();
	    }

}
