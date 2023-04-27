package com.gy.mvvm_demo.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ItemNotebookBinding;
import com.gy.mvvm_demo.db.bean.Notebook;
import com.gy.mvvm_demo.ui.activity.EditActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotebookAdapter extends BaseQuickAdapter<Notebook, BaseDataBindingHolder<ItemNotebookBinding>> {
    //是否批量删除
    private boolean isBatchDeletion;

    /**
     * 设置批量删除
     */
    public void setBatchDeletion(boolean batchDeletion) {
        isBatchDeletion = batchDeletion;
    }

    public NotebookAdapter(@Nullable List<Notebook> data) {
        super(R.layout.item_notebook, data);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemNotebookBinding> bindingHolder, Notebook notebook) {
        ItemNotebookBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setNotebook(notebook);
            binding.setIsBatchDeletion(isBatchDeletion);
//            binding.setOnClick(new NotebookAdapter.ClickBinding());
            binding.executePendingBindings();
        }
    }

    public static class ClickBinding {
        public void itemClick(Notebook notebook, View view) {
            Log.e("TAG", "itemClick: "+notebook.toString() );
            Intent intent = new Intent(view.getContext(), EditActivity.class);
            intent.putExtra("uid",notebook.getUid());
            view.getContext().startActivity(intent);
        }
    }

}

