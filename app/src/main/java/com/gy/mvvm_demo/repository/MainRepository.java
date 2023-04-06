package com.gy.mvvm_demo.repository;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.gy.mvvm_demo.BaseApplication;
import com.gy.mvvm_demo.api.ApiService;
import com.gy.mvvm_demo.db.bean.Image;
import com.gy.mvvm_demo.model.BiYingResponse;
import com.gy.mvvm_demo.network.BaseObserver;
import com.gy.mvvm_demo.network.NetworkApi;
import com.gy.mvvm_demo.network.utils.DateUtil;
import com.gy.mvvm_demo.network.utils.KLog;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.MVUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Main存储库 用于对数据进行处理
 * @author llw
 */
public class MainRepository {
    private static final String TAG = MainRepository.class.getSimpleName();
    final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();
    /**
     * 保存数据
     */
    private void saveImageData(BiYingResponse biYingImgResponse) {
        //记录今日已请求
        MVUtils.put(Constant.IS_TODAY_REQUEST,true);
        //记录此次请求的时最晚有效时间戳
        MVUtils.put(Constant.REQUEST_TIMESTAMP, DateUtil.getMilliSecond());
        BiYingResponse.ImagesBean bean = biYingImgResponse.getImages().get(0);
        //保存到数据库
        new Thread(() -> BaseApplication.getDb().imageDao().insertAll(
                new Image(1,bean.getUrl(),bean.getUrlbase(),bean.getCopyright(),
                        bean.getCopyrightlink(), bean.getTitle()))).start();
    }


    public MutableLiveData<BiYingResponse> getBiYing() {
        //今日此接口是否已请求
        if (MVUtils.getBoolean(Constant.IS_TODAY_REQUEST)) {
            if(DateUtil.getTimestamp() <= MVUtils.getLong(Constant.REQUEST_TIMESTAMP)){
                //当前时间未超过次日0点，从本地获取
                getLocalDB();
            } else {
                //大于则数据需要更新，从网络获取
                requestNetworkApi();
            }
        } else {
            //没有请求过接口 或 当前时间，从网络获取
            requestNetworkApi();
        }
        return biyingImage;
    }


    /**
     * 从网络上请求数据
     */
    @SuppressLint("CheckResult")
    private void requestNetworkApi() {
        Log.d(TAG, "requestNetworkApi: 从网络获取");
        ApiService apiService = NetworkApi.createService(ApiService.class);
        apiService.biying().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingImgResponse) {
                //存储到本地数据库中，并记录今日已请求了数据
                saveImageData(biYingImgResponse);
                biyingImage.setValue(biYingImgResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
    }

    /**
     * 从本地数据库获取
     */
    private void getLocalDB() {
        Log.d(TAG, "getLocalDB: 从本地数据库获取");
        BiYingResponse biYingImgResponse = new BiYingResponse();
        new Thread(() -> {
            //从数据库获取
            Image image = BaseApplication.getDb().imageDao().queryById(1);
            BiYingResponse.ImagesBean imagesBean = new BiYingResponse.ImagesBean();
            imagesBean.setUrl(image.getUrl());
            imagesBean.setUrlbase(image.getUrlbase());
            imagesBean.setCopyright(image.getCopyright());
            imagesBean.setCopyrightlink(image.getCopyrightlink());
            imagesBean.setTitle(image.getTitle());
            List<BiYingResponse.ImagesBean> imagesBeanList = new ArrayList<>();
            imagesBeanList.add(imagesBean);
            biYingImgResponse.setImages(imagesBeanList);
            biyingImage.postValue(biYingImgResponse);
        }).start();
    }

}

