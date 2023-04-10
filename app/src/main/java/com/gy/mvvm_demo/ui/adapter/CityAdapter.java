package com.gy.mvvm_demo.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ItemCityBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CityAdapter extends BaseQuickAdapter<String, BaseDataBindingHolder<ItemCityBinding>> {

    public CityAdapter(@Nullable List<String> data) {
        super(R.layout.item_city, data);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemCityBinding> bindingHolder, String s) {
        ItemCityBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setCityName(s);
            binding.executePendingBindings();
        }
    }
}

