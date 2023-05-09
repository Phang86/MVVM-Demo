package com.gy.mvvm_demo.view;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ImageView;

import com.gy.mvvm_demo.R;

/**
 * WIFI状态监测显示工具类
 * author llw
 */
public class WifiStateUtils {

    /**
     * 检查wifi是否处开连接状态
     * @return
     */
    public static boolean isWifiConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    /**
     * 检查wifi强弱并更改图标显示  按wifi的监测来说，总共五级，这里只做四级，
     */
    public static void checkWifiStateImg(Context context,ImageView imageView) {
        if (isWifiConnect(context)) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            int wifi = mWifiInfo.getRssi();//获取wifi信号强度
            if (wifi > -50 && wifi < 0) {//最强  满格信号 4格
                imageView.setImageResource(R.drawable.icon_wifi_4);
            } else if (wifi > -70 && wifi < -50) {//较强  3格
                imageView.setImageResource(R.drawable.icon_wifi_3);
            } else if (wifi > -80 && wifi < -70) {//较弱  2格
                imageView.setImageResource(R.drawable.icon_wifi_2);
            } else if (wifi > -100 && wifi < -80) {//微弱 1格
                imageView.setImageResource(R.drawable.icon_wifi_1);
            }
        } else {
            //无连接
            imageView.setImageResource(R.drawable.icon_wifi_none);// 在这里  0格  表示没有网络
        }
    }



    /**
     * 检查wifi强弱 文字
     */
    public static String checkWifiStateTxt(Context context){
        String data = null;
        if (isWifiConnect(context)) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            int wifi = mWifiInfo.getRssi();//获取wifi信号强度
            if (wifi > -50 && wifi < 0) {//最强  满格信号 4格
                data = "最强";
            } else if (wifi > -70 && wifi < -50) {//较强  3格
                data = "较强";
            } else if (wifi > -80 && wifi < -70) {//较弱  2格
                data = "较弱";
            } else if (wifi > -100 && wifi < -80) {//微弱 1格
                data = "微弱";
            }
        } else {
            //无连接
            data = "无信号";// 在这里  0格  表示没有网络
        }
        return data;
    }

    /**
     * 检查wifi强弱 文字
     */
    public static int checkWifiStateNum(Context context){
        int data = 0;
        if (isWifiConnect(context)) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            int wifi = mWifiInfo.getRssi();//获取wifi信号强度
            if (wifi > -50 && wifi < 0) {//最强  满格信号 4格
                data = 4;
            } else if (wifi > -70 && wifi < -50) {//较强  3格
                data = 3;
            } else if (wifi > -80 && wifi < -70) {//较弱  2格
                data = 2;
            } else if (wifi > -100 && wifi < -80) {//微弱 1格
                data = 1;
            }
        } else {
            //无连接
            data = 0;// 在这里  0格  表示没有网络
        }
        return data;
    }
}


