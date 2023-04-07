package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.User;
import com.gy.mvvm_demo.databinding.ActivityLoginBinding;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.MVUtils;
import com.gy.mvvm_demo.viewmodels.LoginViewModel;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding dataBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //数据绑定视图
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        //Model → View
        User user = new User("", "");
        loginViewModel.getUser().setValue(user);
        //获取观察对象
        MutableLiveData<User> user1 = loginViewModel.getUser();
        user1.observe(this, user2 -> {
            Log.d("LoginActivity", "onCreate: " + user2.getAccount());
            dataBinding.setViewModel(loginViewModel);
        });

        dataBinding.btnLogin.setOnClickListener(v -> {
            if (loginViewModel.user.getValue().getAccount().isEmpty()) {
                showMsg("请输入账号");
                return;
            }
            if (loginViewModel.user.getValue().getPwd().isEmpty()) {
                showMsg("请输入密码");
                return;
            }
            //检查输入的账户和密码是否是注册过的。
            checkUser();
        });


    }

    private void checkUser() {
        loginViewModel.getLocalUser();

        loginViewModel.localUser.observe(this, localUser -> {
            if (!loginViewModel.user.getValue().getAccount().equals(localUser.getAccount()) ||
                    !loginViewModel.user.getValue().getPwd().equals(localUser.getPwd())) {
                showMsg("账号或密码错误");
                return;
            }
            //记录已经登录过
            MVUtils.put(Constant.IS_LOGIN, true);
            showMsg("登录成功");
            jumpActivity(MainActivity.class);
            finish();
        });
        loginViewModel.failed.observe(this, this::showMsg);
    }

    private long timeMillis;

    /**
     * Add a prompt to exit the application
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - timeMillis) > 2000) {
                showMsg("再次按下退出应用程序");
                timeMillis = System.currentTimeMillis();
            } else {
                exitTheProgram();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void toRegister(View view) {
        jumpActivity(RegisterActivity.class);
    }

}
