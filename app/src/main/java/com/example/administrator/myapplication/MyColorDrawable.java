package com.example.administrator.myapplication;

import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by Administrator on 2017/2/6.
 */

public class MyColorDrawable extends ColorDrawable {

    private static final String TAG = MyColorDrawable.class.getSimpleName();

    public MyColorDrawable() {
        super();
    }

    public MyColorDrawable(int color) {
        super(color);
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        final boolean result = super.onStateChange(stateSet);
        if (result) {
            invalidateSelf();
        }
        return result;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        setColorFilter(null);
        super.setTintList(tint);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
        invalidateSelf();
    }
}
