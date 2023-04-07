package com.gy.mvvm_demo.ui.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.FragmentVideoBinding;
import com.gy.mvvm_demo.ui.adapter.VideoAdapter;
import com.gy.mvvm_demo.viewmodels.VideoViewModel;

public class VideoFragment extends BaseFragment {

    private FragmentVideoBinding binding;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VideoViewModel mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        //获取视频数据
        mViewModel.getVideo();
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        //数据刷新
        mViewModel.video.observe(context, videoResponse ->
                binding.rv.setAdapter(new VideoAdapter(videoResponse.getResult())));
        mViewModel.failed.observe(context, this::showMsg);
    }

}
