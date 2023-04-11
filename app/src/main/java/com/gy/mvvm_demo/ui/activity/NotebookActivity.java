package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityNotebookBinding;

public class NotebookActivity extends BaseActivity {

    private ActivityNotebookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notebook);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notebook);
        initView();
    }

    private void initView() {
        setStatusBar(true);
        back(binding.toolbar);
        binding.fabAddNotebook.setOnClickListener(toEdit -> {
            jumpActivity(EditActivity.class);
        });
    }


}