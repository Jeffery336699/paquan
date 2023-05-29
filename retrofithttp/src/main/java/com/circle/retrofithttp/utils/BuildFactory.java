package com.circle.retrofithttp.utils;

import com.circle.retrofithttp.HttpUtils;

/**
 * @author jingbin
 * @data 2018/2/9
 * @Description
 */

public class BuildFactory {

    private static BuildFactory instance;
    private Object gankHttps;
    private String mBaseUrl;

    public static BuildFactory getInstance() {
        if (instance == null) {
            synchronized (BuildFactory.class) {
                if (instance == null) {
                    instance = new BuildFactory();
                }
            }
        }
        return instance;
    }

    public <T> T createCCFox(Class<T> serviceClazz, String baseUrl) {
        if (!baseUrl.equals(mBaseUrl)){
            gankHttps = null;
            mBaseUrl = baseUrl;
        }
        if (gankHttps == null) {
            synchronized (BuildFactory.class) {
                if (gankHttps == null) {
                    gankHttps = HttpUtils.getInstance().getCCFoxBuilder(baseUrl).build().create(serviceClazz);
                }
            }
        }
        return (T) gankHttps;
    }

    public <T> T createOther(Class<T> serviceClazz, String baseUrl) {
        if (!baseUrl.equals(mBaseUrl)){
            gankHttps = null;
            mBaseUrl = baseUrl;
        }
        if (gankHttps == null) {
            synchronized (BuildFactory.class) {
                if (gankHttps == null) {
                    gankHttps = HttpUtils.getInstance().getOtherBuilder(baseUrl).build().create(serviceClazz);
                }
            }
        }
        return (T) gankHttps;
    }

    public <T> T createOther(Class<T> serviceClazz, String baseUrl,int timeout) {
        if (!baseUrl.equals(mBaseUrl)){
            gankHttps = null;
            mBaseUrl = baseUrl;
        }
        if (gankHttps == null) {
            synchronized (BuildFactory.class) {
                if (gankHttps == null) {
                    gankHttps = HttpUtils.getInstance().getOtherBuilder(baseUrl,timeout).build().create(serviceClazz);
                }
            }
        }
        return (T) gankHttps;
    }

}
