package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivitySplashBinding;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.EasyAnimation;
import com.gy.mvvm_demo.utils.MVUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity {

    @Inject
    MVUtils mvUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        setContentView(ActivitySplashBinding.inflate(getLayoutInflater()).getRoot());
        new Handler().postDelayed(() -> jumpActivityFinish(mvUtils.getBoolean(Constant.IS_LOGIN) ? MainActivity.class : LoginActivity.class),400);
    }
}