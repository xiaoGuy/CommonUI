package com.xiaoguy.commonui.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by Xiaoguy on 2017/7/14.
 */

public class KeyboardDetector {

    private static final String TAG = KeyboardDetector.class.getSimpleName();
    private static int mScreenHeight;

    public interface OnKeyboardStateChangedListener {
        void onKeyboardShow();
        void onKeyboardHide();
    }

    private KeyboardDetector() {
    }

    public static void bind(@NonNull Fragment fragment, @NonNull ViewGroup container, OnKeyboardStateChangedListener listener) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new IllegalStateException("fragment.getActivity() return null");
        }
        bind(activity, container, listener);
    }

    public static void bind(@NonNull Activity activity, @NonNull ViewGroup container, final OnKeyboardStateChangedListener listener) {
        if (mScreenHeight == 0) {
            mScreenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        }

        ActivityInfo activityInfo;
        String pkgName = activity.getPackageName();
        String clsName = activity.getClass().getCanonicalName();
        try {
            activityInfo = activity.getPackageManager().getActivityInfo(
                    new ComponentName(pkgName, clsName), PackageManager.MATCH_DEFAULT_ONLY);
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "get activity info failed: " + activity.getPackageName() + activity.getClass().getCanonicalName());
            return;
        }

        if (!isAdjustResizeMode(activityInfo.softInputMode)) {
            throw new IllegalArgumentException("activity " + clsName + "'s windowSoftInputMode only allowed adjustResize in all of adjustXXX");
        }
        if (isFullScreen(activity)) {
            AndroidBug5497Workaround.assistActivity(activity);
        }

        if (listener != null) {
            container.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > mScreenHeight / 3)) {
                        // 软键盘弹出
                        listener.onKeyboardShow();
                    } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > mScreenHeight / 3)) {
                        // 软键盘隐藏
                        listener.onKeyboardHide();
                    }
                }
            });
        }
    }

    public static boolean isAdjustResizeMode(int softInputMode) {
        return softInputMode == LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }

    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        }else {
            return false;
        }
    }
}
