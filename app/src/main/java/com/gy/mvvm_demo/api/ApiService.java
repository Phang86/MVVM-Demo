package com.gy.mvvm_demo.api;

import com.gy.mvvm_demo.model.BiYingResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 所有的Api网络接口
 * @author llw
 */
public interface ApiService {

    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<BiYingResponse> biying();
}
