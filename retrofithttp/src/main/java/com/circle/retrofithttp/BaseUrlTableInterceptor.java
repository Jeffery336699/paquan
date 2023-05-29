package com.circle.retrofithttp;

import android.text.TextUtils;
import android.util.Log;

import com.circle.retrofithttp.RouterTableManager;import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseUrlTableInterceptor implements Interceptor {

    private final static boolean DEBUG = false;
    private final static String TAG = BaseUrlTableInterceptor.class.getSimpleName();

    private RouterTableManager mTableManager;

    public BaseUrlTableInterceptor() {
        mTableManager = RouterTableManager.getInstance();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取request
        Request oldRequest = chain.request();
        Request newRequest = null;

        try {
            String newHost = getUrlTable();
            if (newHost != null && newHost.startsWith("http")){
                newHost = newHost.substring(newHost.indexOf("//") + 2);
            }
            if (newHost != null) {
                HttpUrl newHttpUrl = oldRequest.url().newBuilder().host(newHost).build();
                newRequest = oldRequest.newBuilder().url(newHttpUrl).build();
                if (DEBUG) {
                    Log.d(TAG, "oldHost:" + oldRequest.url());
                    Log.d(TAG, "newHost:" + newRequest.url());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newRequest == null) {
            newRequest = oldRequest;
        }
        return chain.proceed(newRequest);
    }

    private String getUrlTable() {
        String apiDomain = mTableManager.getApiDomain();
        if (!TextUtils.isEmpty(apiDomain)) {
            return apiDomain;
        }
        return null;
    }
}
