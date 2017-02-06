package com.example.administrator.myapplication;

import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;

/**
 * Created by Administrator on 2017/2/6.
 */

public class MyStateListDrawable extends StateListDrawable {

    private static final String TAG = MyStateListDrawable.class.getSimpleName();

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, TAG + " draw");
        super.draw(canvas);
    }
}
