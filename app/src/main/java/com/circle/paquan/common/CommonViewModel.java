package com.circle.paquan.common;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.circle.paquan.utils.SPUtils;
import com.google.gson.Gson;


public class CommonViewModel extends ViewModel {
    Gson gson = new Gson();


    private MutableLiveData<String> token;


    {
        //用户数据
        String string = SPUtils.getInstance().getString(Const.USERINFO);
    }



    public MutableLiveData<String> getToken() {
        if (token == null) {
            token = new MutableLiveData<>();
        }
        return token;
    }
}