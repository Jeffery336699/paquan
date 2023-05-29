package com.circle.retrofithttp;

public class RetrofitHttpInit {

    private static boolean Is_Debug = false;

    public static void setDebug(boolean isDebug) {
        Is_Debug = isDebug;
    }

    public static boolean isDebug() {
        return Is_Debug;
    }
}
