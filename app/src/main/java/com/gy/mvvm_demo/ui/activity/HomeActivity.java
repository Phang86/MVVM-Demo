package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityHomeBinding;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.MVUtils;

public class HomeActivity extends BaseActivity  {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //获取navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.news_fragment:
                    binding.tvTitle.setText("头条新闻");
                    navController.navigate(R.id.news_fragment);
                    break;
                case R.id.video_fragment:
                    binding.tvTitle.setText("热门视频");
                    navController.navigate(R.id.video_fragment);
                    break;
                default:
            }
            return true;
        });
        binding.ivAvatar.setOnClickListener(v-> binding.drawerLayout.open());
        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item_setting:

                    break;
                case R.id.item_logout:
                    logout();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 退出登录
     */
    private void logout() {
        showMsg("退出登录");
        MVUtils.put(Constant.IS_LOGIN,false);
        jumpActivityFinish(LoginActivity.class);
    }

}