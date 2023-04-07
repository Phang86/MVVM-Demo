package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.adapter.ImageAdapter;
import com.gy.mvvm_demo.databinding.ActivityPictureViewBinding;
import com.gy.mvvm_demo.viewmodels.PictureViewModel;

public class PictureViewActivity extends AppCompatActivity {

    private PictureViewModel viewModel;
    private ActivityPictureViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        initView();
    }

    private void initView(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_view);
        viewModel = new ViewModelProvider(this).get(PictureViewModel.class);
        String img = getIntent().getStringExtra("img");
        //获取热门壁纸数据
        viewModel.getWallPaper();
        viewModel.wallPaper.observe(this, wallPapers -> {
            binding.vp.setAdapter(new ImageAdapter(wallPapers));
            for (int i = 0; i < wallPapers.size(); i++) {
                if (img == null) {
                    return;
                }
                if (wallPapers.get(i).getImg().equals(img)) {
                    binding.vp.setCurrentItem(i,false);
                    break;
                }
            }

        });
    }
}