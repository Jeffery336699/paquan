package com.circle.paquan.utils;

import android.content.Context;

import com.circle.paquan.view.LoadingDialog;


public class DialogUtils {

    private static DialogUtils instance = new DialogUtils();
    private static LoadingDialog loadingDialog;

    public static synchronized DialogUtils getInstance() {

        return instance;
    }

    public void showLoading(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.showLoading();
    }

    public void disMissLoading() {
        if (loadingDialog != null) {
            loadingDialog.disMissLoading();
            loadingDialog = null;
        }
    }
}
