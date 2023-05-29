package com.circle.paquan.view;

import android.app.Dialog;
import android.content.Context;

import com.circle.paquan.R;


public class LoadingDialog {
    private Context context;
    private Dialog dialog;

    public LoadingDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.Comm_LoadingDialogStyle);
        dialog.setContentView(new Loading2View(this.context));
        dialog.setCanceledOnTouchOutside(false);
    }

    public LoadingDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void showLoading() {
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disMissLoading() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dialog getDialog() {
        return dialog;
    }
}
