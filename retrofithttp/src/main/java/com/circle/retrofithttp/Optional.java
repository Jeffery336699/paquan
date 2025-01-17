package com.circle.retrofithttp;

import java.util.NoSuchElementException;

import androidx.annotation.Nullable;

public class Optional<M> {

    private final M optional; // 接收到的返回结果

    public Optional(@Nullable M optional) {
        this.optional = optional;
    }

    // 判断返回结果是否为null
    public boolean isEmpty() {
        return this.optional == null;
    }

    // 获取不能为null的返回结果，如果为null，直接抛异常，经过二次封装之后，这个异常最终可以在走向RxJava的onError()
    public M get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }

    // 获取可以为null的返回结果
    public M getIncludeNull() {
        return optional;
    }
}
