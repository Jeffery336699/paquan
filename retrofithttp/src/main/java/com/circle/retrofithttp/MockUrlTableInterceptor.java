package com.circle.retrofithttp;

import android.util.Log;

import com.circle.retrofithttp.RetrofitHttpInit;import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl.Builder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MockUrlTableInterceptor implements Interceptor {

    private final static boolean DEBUG = true;
    private final static String TAG = MockUrlTableInterceptor.class.getSimpleName();

    public MockUrlTableInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!RetrofitHttpInit.isDebug()) {
            return chain.proceed(chain.request());
        }
        if (!chain.request().url().host().equals("rap2api.taobao.org")) {
            return chain.proceed(chain.request());
        }
        //获取request
        Request oldRequest = chain.request();
        //app/mock/286385/api
        Builder urlBuilder = oldRequest.url().newBuilder();
        List<String> segments = oldRequest.url().pathSegments();
        for (int i = 0; i < segments.size(); i++) {
            urlBuilder.removePathSegment(0);
        }

        urlBuilder.scheme("http");
        urlBuilder.addPathSegment("app");
        urlBuilder.addPathSegment("mock");
        urlBuilder.addPathSegment("286385");
        for (int i = 0; i < segments.size(); i++) {
            urlBuilder.addPathSegment(segments.get(i));
        }
        Request newRequest = oldRequest.newBuilder().url(urlBuilder.build()).build();
        if (DEBUG) {
            Log.d(TAG, "oldHost:" + oldRequest.url());
            Log.d(TAG, "newHost:" + newRequest.url());
        }
        return chain.proceed(newRequest);
    }
}
