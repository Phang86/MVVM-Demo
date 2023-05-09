package com.gy.mvvm_demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.room.Room;

import com.amap.api.location.UmidtokenInfo;
import com.gy.mvvm_demo.db.AppDatabase;
import com.gy.mvvm_demo.network.NetworkApi;
import com.gy.mvvm_demo.ui.activity.ActivityManager;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.MVUtils;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.uc.crashsdk.export.CrashApi;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.umcrash.UMCrash;

import java.util.HashMap;

import dagger.hilt.android.HiltAndroidApp;

/**
 * 自定义 Application
 * @author llw
 */
@HiltAndroidApp
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
        //创建本地数据库
        db = AppDatabase.getInstance(this);
        //初始化腾讯x5内核 WebView
        initX5WebView();
        //友盟SDK初始化
//        UMConfigure.init(this, Constant.U_MENG_APPKEY,"Umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
//
//        //针对于Native崩溃信息采集
//        final Bundle customInfo = new Bundle();
//        customInfo.putBoolean("mCallNativeDefaultHandler",true);
//        CrashApi.getInstance().updateCustomInfo(customInfo);
    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDb(){
        return db;
    }

    private void initX5WebView() {
        HashMap map = new HashMap(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }

}

