package com.gy.mvvm_demo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.gy.mvvm_demo.BaseApplication;

public class WindowUtil {
    private static volatile WindowUtil instance;

    public static WindowUtil getInstance() {
        if (instance == null) {
            synchronized (WindowUtil.class) {
                if (instance == null) {
                    instance = new WindowUtil();
                }
            }
        }
        return instance;
    }

    public WindowUtil() {
    }

    public void setWindowDecorView(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            decorView.setFitsSystemWindows(true);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setBarBgColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    public void showSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view,0);
        }
    }

    public void hideSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
