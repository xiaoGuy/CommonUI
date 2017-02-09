package com.example.administrator.myapplication.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2017/2/9.
 */

public class EasyTextView extends TextView {

    private static final boolean AFTER_LOLLIPOP;
    private static final int[][] STATES;

    static {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            AFTER_LOLLIPOP = true;
        } else {
            AFTER_LOLLIPOP = false;
        }

        STATES = new int[3][];
        // 顺序不能错，全为 false 的 item 要放在不全为 false 的 item 的后面
        STATES[0] = new int[]{android.R.attr.state_pressed};
        STATES[1] = new int[]{-android.R.attr.state_enabled};
        STATES[2] = new int[]{};
    }

    public EasyTextView(Context context) {
        super(context);
    }

    public EasyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyTextView);

        int textPressedColor = a.getColor(R.styleable.EasyTextView_textPressedColor, -1);
        int textDisabledColor = a.getColor(R.styleable.EasyTextView_textDisabledColor, -1);
        int textNormalColor = a.getColor(R.styleable.EasyTextView_textNormalColor, -1);

        int backgroundPressedColor = a.getColor(R.styleable.EasyTextView_backgroundPressedColor, -1);
        int backgroundDisabledColor = a.getColor(R.styleable.EasyTextView_backgroundDisabledColor, -1);
        int backgroundNormalColor = a.getColor(R.styleable.EasyTextView_backgroundNormalColor, -1);

        int backgroundRadius = a.getDimensionPixelSize(R.styleable.EasyTextView_backgroundRadius, -1);
        int backgroundStrokeWidth = a.getDimensionPixelSize(R.styleable.EasyTextView_backgroundStrokeWidth, -1);

        if (textPressedColor != -1 && textNormalColor != -1) {
            // 如果没有定义 disabled 时按钮的颜色，则
            if (textDisabledColor == -1) {
                textDisabledColor = lighterColor(textNormalColor);
            }
            final ColorStateList csl = createColorStateList(textPressedColor, textDisabledColor, textNormalColor);
            setTextColor(csl);
            setClickable(true);
        }

        if (backgroundPressedColor != -1 && backgroundNormalColor != -1) {
            if (backgroundDisabledColor == -1) {
                backgroundDisabledColor = lighterColor(backgroundNormalColor);
            }
            // 如果没有设置圆角或者 stroke ，则使用 ColorDrawable 作为背景
            if (backgroundRadius == -1 && backgroundStrokeWidth == -1) {
                // 5.0 之后就可以使用 ColorDrawable 来实现带状态的颜色，5.0 之前只能通过 StateListDrawable
                if (AFTER_LOLLIPOP) {
                    ColorDrawable colorDrawable = new ColorDrawable(backgroundNormalColor);
                    colorDrawable.setTintList(createColorStateList(
                            backgroundPressedColor, backgroundDisabledColor, backgroundNormalColor));
                    setBackgroundDrawable(colorDrawable);
                } else {

                }
            }

        }

        a.recycle();
    }

    private ColorStateList createColorStateList(int... colors) {
        if (colors == null || colors.length == 0) {
            return null;
        }

        return new ColorStateList(STATES, colors);
    }

    private StateListDrawable createSelector(int... colors) {
        if (colors == null || colors.length == 0) {
            return null;
        }

        
    }

    private int lighterColor(int color) {
        return ColorStateList.valueOf(color).withAlpha(0xf0).getDefaultColor();
    }
}
