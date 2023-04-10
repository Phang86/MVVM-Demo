package com.gy.mvvm_demo.ui.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.FragmentInfoBinding;
import com.gy.mvvm_demo.ui.adapter.InfoFragmentAdapter;
import com.gy.mvvm_demo.viewmodels.InfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends BaseFragment {

    private FragmentInfoBinding binding;
    private String titles[] = {"新闻","视频"};
    private List<Fragment> fragmentList = new ArrayList<>();

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentList.add(new NewsFragment());
        fragmentList.add(new VideoFragment());
        binding.vp.setAdapter(new InfoFragmentAdapter(getChildFragmentManager(), fragmentList,titles));
        binding.tab.setupWithViewPager(binding.vp);
    }

}