package com.circle.retrofithttp.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.circle.retrofithttp.R;


/**
 * 作者：junj
 * 时间：2016/8/12
 * 描述：TOTO
 */
public class NonNetworkDialog extends AlertDialog {
    private Context context;
    private DialogClick2 dialogClick;
    private boolean mShowed;

    public NonNetworkDialog(Context context, DialogClick2 dialogClick) {
        super(context, R.style.NetDialog);
        this.context = context;
        this.dialogClick = dialogClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_non_network);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick.dialogCancel();
                dismiss();
            }
        });
        findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClick.dialogOk();
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        if (!mShowed) {
            mShowed = true;
            super.show();
        }
    }

    @Override
    public void dismiss() {
        mShowed = false;
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialogClick.dialogCancel();
            dismiss();
        }
        return false;
    }
}
