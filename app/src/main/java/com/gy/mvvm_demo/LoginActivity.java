package com.gy.mvvm_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gy.mvvm_demo.databinding.ActivityLoginBinding;
import com.gy.mvvm_demo.databinding.ActivityMainBinding;
import com.gy.mvvm_demo.utils.MVUtils;
import com.gy.mvvm_demo.viewmodels.LoginViewModel;
import com.gy.mvvm_demo.viewmodels.MainViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //数据绑定视图
        ActivityLoginBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        User user = new User("admin", "123456");
        loginViewModel.getUser().setValue(user);

        //获取观察对象
        MutableLiveData<User> user1 = loginViewModel.getUser();
        user1.observe(this, user2 -> dataBinding.setViewModel(loginViewModel));

        dataBinding.btnLogin.setOnClickListener(v -> {
            if (loginViewModel.user.getValue().getAccount().isEmpty()){
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (loginViewModel.user.getValue().getPwd().isEmpty()){
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        });

        //存
        Log.d("TAG", "onCreate: 存");
        MVUtils.put("age",24);
        //取
        int age = MVUtils.getInt("age",0);
        Log.d("TAG", "onCreate: 取 ：" + age);

    }
}