package com.gy.mvvm_demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.gy.mvvm_demo.network.NetworkApi;
import com.gy.mvvm_demo.utils.MVUtils;
import com.tencent.mmkv.MMKV;

/**
 * 自定义 Application
 * @author llw
 */
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
        context = getApplicationContext();
        //MMKV初始化
        MMKV.initialize(this);
        //工具类初始化
        MVUtils.getInstance();
    }

    public static Context getContext() {
        return context;
    }
}

