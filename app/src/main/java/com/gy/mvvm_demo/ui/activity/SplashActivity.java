package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivitySplashBinding;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.EasyAnimation;
import com.gy.mvvm_demo.utils.MVUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        setStatusBar(true);
        EasyAnimation.moveViewWidth(binding.tvTranslate, () -> {
            binding.tvMvvm.setVisibility(View.VISIBLE);
            jumpActivity(MVUtils.getBoolean(Constant.IS_LOGIN) ? MainActivity.class : LoginActivity.class);
            finish();
        });
    }
}