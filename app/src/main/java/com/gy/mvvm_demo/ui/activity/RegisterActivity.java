package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityRegisterBinding;
import com.gy.mvvm_demo.db.bean.User;
import com.gy.mvvm_demo.viewmodels.RegisterViewModel;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.getUser().setValue(new User(0, "", "", "", "", ""));
        binding.setRegister(registerViewModel);
        initView();
    }

    private void initView() {
        back(binding.toolbar);
        binding.btnRegister.setOnClickListener(v -> {
            if (registerViewModel.user.getValue().getAccount().isEmpty()) {
                showMsg("请输入账号");
                return;
            }
            if (registerViewModel.user.getValue().getPwd().isEmpty()) {
                showMsg("请输入密码");
                return;
            }
            if (registerViewModel.user.getValue().getConfirmPwd().isEmpty()) {
                showMsg("请确认密码");
                return;
            }
            if (!registerViewModel.user.getValue().getPwd().equals(registerViewModel.user.getValue().getConfirmPwd())) {
                showMsg("两次输入密码不一致");
                return;
            }

            registerViewModel.register();
            registerViewModel.failed.observe(this, failed -> {
                showMsg("200".equals(failed) ? "注册成功" : failed);
                if ("200".equals(failed)) {
                    finish();
                }
            });
        });
    }
}
