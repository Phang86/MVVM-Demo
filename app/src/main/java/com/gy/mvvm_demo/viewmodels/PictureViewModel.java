package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.db.bean.WallPaper;
import com.gy.mvvm_demo.repository.PictureRepository;

import java.util.List;

public class PictureViewModel extends BaseViewModel {

    public LiveData<List<WallPaper>> wallPaper;

    public void getWallPaper() {
        PictureRepository pictureRepository = new PictureRepository();
        failed = pictureRepository.failed;
        wallPaper = pictureRepository.getWallPaper();
    }
}

