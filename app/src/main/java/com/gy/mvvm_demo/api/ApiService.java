package com.gy.mvvm_demo.api;

import com.gy.mvvm_demo.model.BiYingResponse;
import com.gy.mvvm_demo.model.NewsDetailResponse;
import com.gy.mvvm_demo.model.NewsResponse;
import com.gy.mvvm_demo.model.VideoResponse;
import com.gy.mvvm_demo.model.WallPaperResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

    /**
     * 热门壁纸
     */
    @GET("/v1/vertical/vertical?limit=30&skip=180&adult=false&first=0&order=hot")
    Observable<WallPaperResponse> wallPaper();
    /**
     * 聚合新闻数据
     */
    @GET("/toutiao/index?type=&page=&page_size=&is_filter=&key=7e892c1f26c696a1153ee7e543ebae1d")
    Observable<NewsResponse> news();

    /**
     * 聚合热门视频数据
     */
    @GET("/fapig/douyin/billboard?type=hot_video&size=20&key=b31cc0c6e0594398d7292e9f41f183cb")
    Observable<VideoResponse> video();

    /**
     * 聚合新闻数据详情
     */
    @GET("/toutiao/content?key=99d3951ed32af2930afd9b38293a08a2")
    Observable<NewsDetailResponse> newsDetail(@Query("uniquekey") String uniquekey);


}

