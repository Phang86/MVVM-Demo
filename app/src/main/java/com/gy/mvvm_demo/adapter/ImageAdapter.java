package com.gy.mvvm_demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ItemImageBinding;
import com.gy.mvvm_demo.db.bean.WallPaper;
import com.gy.mvvm_demo.model.WallPaperResponse;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<WallPaper, BaseDataBindingHolder<ItemImageBinding>> {

    public ImageAdapter(List<WallPaper> data) {
        super(R.layout.item_image,data);
    }

    @Override
    protected void convert(BaseDataBindingHolder<ItemImageBinding> bindingHolder, WallPaper wallPaper) {
        if (wallPaper == null) {
            return;
        }
        ItemImageBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setWallPaper(wallPaper);
            binding.executePendingBindings();
        }
    }

}
