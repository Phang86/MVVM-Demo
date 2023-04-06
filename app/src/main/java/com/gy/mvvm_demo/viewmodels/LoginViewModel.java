package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.User;

/**
 * 登录页面ViewModel
 * @author llw
 */
public class LoginViewModel extends ViewModel {

    public MutableLiveData<User> user;

    public MutableLiveData<User> getUser(){
        if(user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }
}
