package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;

import com.gy.mvvm_demo.model.NewsDetailResponse;
import com.gy.mvvm_demo.repository.WebRepository;

public class WebViewModel extends BaseViewModel {

    public LiveData<NewsDetailResponse> newsDetail;

    public void getNewDetail(String uniquekey) {
        WebRepository webRepository = new WebRepository();
        failed = webRepository.failed;
        newsDetail = webRepository.getNewsDetail(uniquekey);
    }
}

