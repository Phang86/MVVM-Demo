package com.gy.mvvm_demo.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ItemVideoBinding;
import com.gy.mvvm_demo.model.VideoResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<VideoResponse.ResultBean, BaseDataBindingHolder<ItemVideoBinding>> {
    public VideoAdapter(List<VideoResponse.ResultBean> data) {
        super(R.layout.item_video,data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemVideoBinding> bindingHolder, VideoResponse.ResultBean resultBean) {
        ItemVideoBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setVideo(resultBean);
            binding.setOnClick(new ClickBinding());
            binding.executePendingBindings();
        }
    }

    public static class ClickBinding {
        public void itemClick(@NotNull VideoResponse.ResultBean resultBean, View view) {
            if (resultBean.getShare_url() != null) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resultBean.getShare_url())));
            } else {
                Toast.makeText(view.getContext(), "视频地址为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
