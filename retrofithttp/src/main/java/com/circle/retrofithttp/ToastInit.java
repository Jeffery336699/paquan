package com.circle.retrofithttp;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.hjq.toast.ToastUtils;import com.circle.retrofithttp.ToastStyle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      @author : luqilong
 *      e-mail : luqilong@lixiang.com
 *      time : 2021/6/27 12:05 上午
 *      desc :
 *      version : 1.0
 * </pre>
 */
public class ToastInit implements Initializer<ToastInit> {

    @NonNull
    @NotNull
    @Override
    public ToastInit create(@NonNull @NotNull Context context) {
        init(context);
        return new ToastInit();
    }

    private void init(Context context) {
        // 设置 Toast 拦截器
        //ToastUtils.setInterceptor(new ToastInterceptor());
        // 初始化 Toast 框架
        ToastUtils.init((Application) context);
        ToastUtils.setStyle(new ToastStyle());
    }

    @NonNull
    @NotNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
