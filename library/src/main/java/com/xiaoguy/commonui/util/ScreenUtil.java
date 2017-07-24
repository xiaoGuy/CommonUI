package com.xiaoguy.commonui.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by hua on 2017/7/24.
 */

public class ScreenUtil {

    public static int dp2px(int dp) {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private ScreenUtil() {
    }
}
