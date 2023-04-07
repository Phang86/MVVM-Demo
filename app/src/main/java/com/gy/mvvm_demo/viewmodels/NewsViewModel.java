package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.model.NewsResponse;
import com.gy.mvvm_demo.repository.NewsRepository;

public class NewsViewModel extends BaseViewModel {

    public LiveData<NewsResponse> news;

    public void getNews() {
        NewsRepository newsRepository = new NewsRepository();
        failed = newsRepository.failed;
        news = newsRepository.getNews();
    }
}
