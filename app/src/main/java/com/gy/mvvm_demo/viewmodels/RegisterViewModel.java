package com.gy.mvvm_demo.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.gy.mvvm_demo.db.bean.User;
import com.gy.mvvm_demo.repository.UserRepository;

public class RegisterViewModel extends BaseViewModel {

    public MutableLiveData<User> user;


    public MutableLiveData<User> getUser(){
        if(user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }

    /**
     * 注册
     */
    public void register() {
        UserRepository userRepository = new UserRepository();
        failed = userRepository.failed;
        user.getValue().setUid(1);
        Log.d("TAG", "register: "+new Gson().toJson(user.getValue()));
        userRepository.saveUser(user.getValue());
    }
}


