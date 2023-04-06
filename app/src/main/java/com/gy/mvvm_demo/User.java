package com.gy.mvvm_demo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class User extends BaseObservable {
    private String account;
    private String pwd;

    @Bindable
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        notifyPropertyChanged(BR.account);//只通知改变的参数
    }

    @Bindable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        notifyPropertyChanged(BR.pwd);//只通知改变的参数
    }

    public User(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;

    }
}
