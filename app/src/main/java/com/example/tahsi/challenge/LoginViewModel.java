package com.example.tahsi.challenge;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class LoginViewModel extends BaseObservable {
    private String username;
    private String password;



    @Bindable
    public String getUsername() {
        return username;
    }

    @Bindable
    public String getPassword() {
        return password;
    }


    @Bindable
    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(com.example.tahsi.challenge.BR.username);
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(com.example.tahsi.challenge.BR.password);
    }

}




