package com.gy.mvvm_demo.repository;

import androidx.lifecycle.MutableLiveData;

import com.gy.mvvm_demo.BaseApplication;
import com.gy.mvvm_demo.db.bean.WallPaper;

import java.util.List;

import io.reactivex.Flowable;

public class PictureRepository {

    private final MutableLiveData<List<WallPaper>> wallPaper = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();


    public MutableLiveData<List<WallPaper>> getWallPaper() {
        Flowable<List<WallPaper>> listFlowable = BaseApplication.getDb().wallPaperDao().getAll();
        CustomDisposable.addDisposable(listFlowable, wallPaper::postValue);
        return wallPaper;
    }


}

