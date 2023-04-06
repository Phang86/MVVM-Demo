package com.gy.mvvm_demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.gy.mvvm_demo.db.AppDatabase;
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
    //数据库
    public static AppDatabase db;


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
        //创建本地数据库
        db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "mvvm_demo").build();

    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDb(){
        return db;
    }

}

