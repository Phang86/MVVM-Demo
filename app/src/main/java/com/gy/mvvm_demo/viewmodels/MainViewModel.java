package com.gy.mvvm_demo.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.model.BiYingResponse;
import com.gy.mvvm_demo.model.WallPaperResponse;
import com.gy.mvvm_demo.repository.MainRepository;
import com.gy.mvvm_demo.repository.NewsRepository;

import java.util.function.ObjIntConsumer;

import dagger.hilt.android.AndroidEntryPoint;


public class MainViewModel extends BaseViewModel {
    public LiveData<BiYingResponse> biying;

    public LiveData<WallPaperResponse> wallPaper;

    private final MainRepository mainRepository;

    @ViewModelInject
    MainViewModel(MainRepository mainRepository){
        this.mainRepository = mainRepository;
    }

    public void getWallPaper() {
        //wallPaper = new MainRepository().getWallPaper();
        failed = mainRepository.failed;
        wallPaper = mainRepository.getWallPaper();
    }


    public void getBiying(){
//        biying = new MainRepository().getBiYing();
        failed = mainRepository.failed;
        biying = mainRepository.getBiYing();
    }
}
