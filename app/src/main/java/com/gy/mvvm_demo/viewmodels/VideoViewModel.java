package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.model.VideoResponse;
import com.gy.mvvm_demo.repository.VideoRepository;

public class VideoViewModel extends BaseViewModel {

    public LiveData<VideoResponse> video;

    public void getVideo() {
        VideoRepository videoRepository = new VideoRepository();
        failed = videoRepository.failed;
        video = videoRepository.getVideo();
    }
}
