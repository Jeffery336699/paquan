package com.circle.paquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.circle.paquan.R;


public class Loading2View extends FrameLayout {

    public Loading2View(@NonNull Context context) {
        this(context, null);
    }

    public Loading2View(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public Loading2View(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.comm_view_loading2, this);
    }
}
