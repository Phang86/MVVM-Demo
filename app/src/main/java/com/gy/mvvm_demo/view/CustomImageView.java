package com.gy.mvvm_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.gy.mvvm_demo.BaseApplication;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.network.utils.KLog;

/**
 * 自定义View
 * @author llw
 * @description CustomImageVIew
 */
public class CustomImageView extends ShapeableImageView {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(R.drawable.wallpaper_bg)//图片加载出来前，显示的图片
            .fallback(R.drawable.wallpaper_bg) //url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed);//图片加载失败后，显示的图片

    private static final RequestOptions OPTIONS_LOCAL = new RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)//图片加载出来前，显示的图片
            .fallback(R.drawable.ic_launcher_background) //url为空的时候,显示的图片
            .error(R.mipmap.ic_loading_failed)//图片加载失败后，显示的图片
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);

    @BindingAdapter(value = {"localUrl"}, requireAll = false)
    public static void setLocalUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getContext()).load(url).apply(OPTIONS_LOCAL).into(imageView);
    }


    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 必应壁纸  因为拿到的url不完整，因此需要做一次地址拼接
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = {"biyingUrl"}, requireAll = false)
    public static void setBiyingUrl(ImageView imageView, String url) {
        String assembleUrl = "http://cn.bing.com" + url;
        KLog.d(assembleUrl);
        Glide.with(BaseApplication.getContext()).load(assembleUrl).into(imageView);
    }

    /**
     * 普通网络地址图片
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = {"networkUrl"}, requireAll = false)
    public static void setNetworkUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getContext()).load(url).apply(OPTIONS).into(imageView);
    }
}

