package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.model.BiYingResponse;
import com.gy.mvvm_demo.repository.MainRepository;

public class MainViewModel extends ViewModel {
    public LiveData<BiYingResponse> biying;

    public void getBiying(){
        biying = new MainRepository().getBiYing();
    }
}
