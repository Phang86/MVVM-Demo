package com.gy.mvvm_demo.ui.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.FragmentNewsBinding;
import com.gy.mvvm_demo.ui.adapter.NewsAdapter;
import com.gy.mvvm_demo.viewmodels.NewsViewModel;


public class NewsFragment extends BaseFragment {


    private FragmentNewsBinding binding;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NewsViewModel mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        //获取新闻数据
        mViewModel.getNews();
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        //数据刷新
        mViewModel.news.observe(context, newsResponse ->
                binding.rv.setAdapter(new NewsAdapter(newsResponse.getResult().getData())));
        mViewModel.failed.observe(context, this::showMsg);
    }

}
