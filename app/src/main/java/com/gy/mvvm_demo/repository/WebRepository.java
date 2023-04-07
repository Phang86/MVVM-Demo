package com.gy.mvvm_demo.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.gy.mvvm_demo.api.ApiService;
import com.gy.mvvm_demo.model.NewsDetailResponse;
import com.gy.mvvm_demo.network.BaseObserver;
import com.gy.mvvm_demo.network.NetworkApi;

@SuppressLint("CheckResult")
public class WebRepository {

    final MutableLiveData<NewsDetailResponse> newsDetail = new MutableLiveData<>();

    public final MutableLiveData<String> failed = new MutableLiveData<>();

    /**
     * 获取新闻详情数据
     * @param uniquekey 新闻ID
     * @return newsDetail
     */
    public MutableLiveData<NewsDetailResponse> getNewsDetail(String uniquekey) {
        NetworkApi.createService(ApiService.class, 2).
                newsDetail(uniquekey).compose(NetworkApi.applySchedulers(new BaseObserver<NewsDetailResponse>() {
                    @Override
                    public void onSuccess(NewsDetailResponse newsDetailResponse) {
                        if (newsDetailResponse.getError_code() == 0) {
                            newsDetail.setValue(newsDetailResponse);
                        } else {
                            failed.postValue(newsDetailResponse.getReason());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        failed.postValue("NewsDetail Error: " + e.toString());
                    }
                }));
        return newsDetail;
    }
}

