package com.circle.retrofithttp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

import com.hjq.toast.style.BlackToastStyle;
import com.circle.retrofithttp.utils.PhoneUtil;

public class ToastStyle extends BlackToastStyle {

    @Override
    protected int getHorizontalPadding(Context context) {
        return PhoneUtil.dip2px(context,16);
    }

    @Override
    protected int getVerticalPadding(Context context) {
        return PhoneUtil.dip2px(context,12);
    }

    protected Drawable getBackgroundDrawable(Context context) {
        GradientDrawable drawable = new GradientDrawable();
        // 设置颜色
        drawable.setColor(0XAA000000);
        // 设置圆角
        drawable.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        return drawable;
    }
}
