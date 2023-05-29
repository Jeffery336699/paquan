package com.circle.paquan.utils;

import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;

public class KvCache {

    private static KvCache cache = new KvCache();

    public static KvCache get() {
        return cache;
    }

    public void put(String key, @Nullable String value) {
        MMKV kv = MMKV.defaultMMKV();
        kv.putString(key, value);
    }
}
