package com.gy.mvvm_demo.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.gy.mvvm_demo.api.ApiService;
import com.gy.mvvm_demo.model.BiYingResponse;
import com.gy.mvvm_demo.network.BaseObserver;
import com.gy.mvvm_demo.network.NetworkApi;
import com.gy.mvvm_demo.network.utils.KLog;

/**
 * Main存储库 用于对数据进行处理
 * @author llw
 */
public class MainRepository {

    @SuppressLint("CheckResult")
    public MutableLiveData<BiYingResponse> getBiYing() {
        final MutableLiveData<BiYingResponse> biyingImage = new MutableLiveData<>();
        ApiService apiService = NetworkApi.createService(ApiService.class);
        apiService.biying().compose(NetworkApi.applySchedulers(new BaseObserver<BiYingResponse>() {
            @Override
            public void onSuccess(BiYingResponse biYingImgResponse) {
                KLog.d(new Gson().toJson(biYingImgResponse));
                biyingImage.setValue(biYingImgResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
        return biyingImage;
    }
}

