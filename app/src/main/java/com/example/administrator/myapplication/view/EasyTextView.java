package com.example.administrator.myapplication.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.drawable.FixedColorDrawable;


/**
 * Created by Administrator on 2017/2/9.
 */

public class EasyTextView extends TextView {

    private static final boolean AFTER_LOLLIPOP;
    private static final int[][] STATES;
    private static final int DEFAULT_DISABLED_COLOR = 0xFFBBBBBB;
    private static final int INVALID_COLOR = -2;

    private boolean mIgnoreTextColor;

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

    @SuppressWarnings("deprecation")
    public EasyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyTextView);

        ColorStateList textPressedColor = a.getColorStateList(R.styleable.EasyTextView_textPressedColor);
        ColorStateList textDisabledColor = a.getColorStateList(R.styleable.EasyTextView_textDisabledColor);
        ColorStateList textNormalColor = a.getColorStateList(R.styleable.EasyTextView_textNormalColor);
        int textPressedColorInt = getColor(textPressedColor);
        int textDisabledColorInt = getColor(textDisabledColor);
        int textNormalColorInt = getColor(textNormalColor);

        ColorStateList backgroundPressedColor = a.getColorStateList(R.styleable.EasyTextView_backgroundPressedColor);
        ColorStateList backgroundDisabledColor = a.getColorStateList(R.styleable.EasyTextView_backgroundDisabledColor);
        ColorStateList backgroundNormalColor = a.getColorStateList(R.styleable.EasyTextView_backgroundNormalColor);
        int backgroundPressedColorInt = getColor(backgroundPressedColor);
        int backgroundDisabledColorInt = getColor(backgroundDisabledColor);
        int backgroundNormalColorInt = getColor(backgroundNormalColor);

        int backgroundRadius = a.getDimensionPixelSize(R.styleable.EasyTextView_backgroundRadius, -1);
        int backgroundStrokeWidth = a.getDimensionPixelSize(R.styleable.EasyTextView_backgroundStrokeWidth, -1);

        if (backgroundPressedColorInt != INVALID_COLOR && backgroundNormalColorInt != INVALID_COLOR) {
            mIgnoreTextColor = true;
            setClickable(true);

            if (backgroundDisabledColorInt == INVALID_COLOR) {
                backgroundDisabledColorInt = DEFAULT_DISABLED_COLOR;
            }

            final ColorStateList colorStateList = createColorStateList
                    (backgroundPressedColorInt, backgroundDisabledColorInt, backgroundNormalColorInt);
            final Drawable drawable;

            // 如果没有设置 radius 和 stroke ，则使用 ColorDrawable 作为背景
            if (backgroundRadius <= 0 && backgroundStrokeWidth <= 0) {
                setTextColor(Color.WHITE);

                // 5.0 之后就可以使用 ColorDrawable 来实现带状态的颜色，5.0 之前只能通过 StateListDrawable
                if (AFTER_LOLLIPOP) {
                    FixedColorDrawable colorDrawable = new FixedColorDrawable(backgroundNormalColorInt);
                    colorDrawable.setTintList(createColorStateList(
                            backgroundPressedColorInt, backgroundDisabledColorInt, backgroundNormalColorInt));
                    drawable = colorDrawable;
                } else {
                    drawable = createSelector(STATES,
                            new ColorDrawable(backgroundPressedColorInt),
                            new ColorDrawable(backgroundDisabledColorInt),
                            new ColorDrawable(backgroundNormalColorInt));
                }
            } else {
                if (backgroundRadius > 0 && backgroundStrokeWidth <= 0) {
                    setTextColor(Color.WHITE);
                } else {
                    setTextColor(colorStateList);
                }

                // 5.0 之后 GradientDrawable 可以实现带状态的颜色
                if (AFTER_LOLLIPOP) {
                    GradientDrawable gradientDrawable = createShape
                            (backgroundStrokeWidth, backgroundRadius, colorStateList, false);
                    drawable = gradientDrawable;
                } else {
                    drawable = createSelector(STATES,
                            createShape(backgroundStrokeWidth, backgroundRadius, ColorStateList.valueOf(backgroundPressedColorInt), true),
                            createShape(backgroundStrokeWidth, backgroundRadius, ColorStateList.valueOf(backgroundDisabledColorInt), true),
                            createShape(backgroundStrokeWidth, backgroundRadius, ColorStateList.valueOf(backgroundNormalColorInt), true));
                }
            }
            setBackgroundDrawable(drawable);
        }

        if (textPressedColorInt != INVALID_COLOR && textNormalColorInt != INVALID_COLOR && ! mIgnoreTextColor) {
            // 如果没有定义 disabled 时按钮的颜色，则
            if (textDisabledColorInt == INVALID_COLOR) {
                textDisabledColorInt = DEFAULT_DISABLED_COLOR;
            }
            final ColorStateList csl = createColorStateList(textPressedColorInt, textDisabledColorInt, textNormalColorInt);
            setTextColor(csl);
            setClickable(true);
        }
        a.recycle();
    }

    private int getColor(ColorStateList colorStateList) {
        // -2 表示没有设置该颜色，不能用 -1 来表示，因为 -1 为白色（Color.WHITH）
        return colorStateList == null ? -2 : colorStateList.getDefaultColor();
    }

    private ColorStateList createColorStateList(int... colors) {
        if (colors == null || colors.length == 0) {
            return null;
        }
        return new ColorStateList(STATES, colors);
    }

    private GradientDrawable createShape(int strokeWidth, int radius, ColorStateList color, boolean singleColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (strokeWidth > 0) {
            if (singleColor) {
                gradientDrawable.setStroke(strokeWidth, color.getDefaultColor());
            } else {
                gradientDrawable.setStroke(strokeWidth, color);
            }
        }
        if (radius > 0) {
            gradientDrawable.setCornerRadius(radius);
            if (strokeWidth <= 0) {
                gradientDrawable.setColor(color);
            }
        }
        return gradientDrawable;
    }

    private StateListDrawable createSelector(int[][] states, Drawable... drawables) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], drawables[0]);
        stateListDrawable.addState(states[1], drawables[1]);
        stateListDrawable.addState(states[2], drawables[2]);
        return stateListDrawable;
    }
}
