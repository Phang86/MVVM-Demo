package com.gy.mvvm_demo.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ItemNewsBinding;
import com.gy.mvvm_demo.model.NewsResponse;
import com.gy.mvvm_demo.ui.activity.WebActivity;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<NewsResponse.ResultBean.DataBean, BaseDataBindingHolder<ItemNewsBinding>> {

    public NewsAdapter(List<NewsResponse.ResultBean.DataBean> data) {
        super(R.layout.item_news,data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemNewsBinding> bindingHolder, NewsResponse.ResultBean.DataBean dataBean) {
        ItemNewsBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setNews(dataBean);
            binding.setOnClick(new ClickBinding());
            binding.executePendingBindings();
        }
    }

    public static class ClickBinding {
        public void itemClick(NewsResponse.ResultBean.DataBean dataBean, View view) {
            if("1".equals(dataBean.getIs_content())){
                Intent intent = new Intent(view.getContext(), WebActivity.class);
                intent.putExtra("uniquekey", dataBean.getUniquekey());
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "没有详情信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
