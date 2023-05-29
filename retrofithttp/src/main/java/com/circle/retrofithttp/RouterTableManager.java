package com.circle.retrofithttp;

public class RouterTableManager {

    private static RouterTableManager mManager;



    private RouterTableManager() {

    }

    public static RouterTableManager getInstance() {
        if (mManager == null) {
            synchronized (RouterTableManager.class) {
                mManager = new RouterTableManager();
            }
        }
        return mManager;
    }

    private String currentApiDomain = null;

    public void setApiDomain(String domain) {
        this.currentApiDomain = domain;
    }

    public String getApiDomain() {
        return this.currentApiDomain;
    }

}
