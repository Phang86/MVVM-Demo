package com.gy.mvvm_demo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gy.mvvm_demo.User;
import com.gy.mvvm_demo.repository.UserRepository;

/**
 * 登录页面ViewModel
 * @author llw
 */
public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<User> user;

    public MutableLiveData<User> getUser(){
        if(user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }

    public LiveData<com.gy.mvvm_demo.db.bean.User> localUser;

    public void getLocalUser(){
        UserRepository userRepository = new UserRepository();
        localUser = userRepository.getUser();
        failed = userRepository.failed;
    }
}



