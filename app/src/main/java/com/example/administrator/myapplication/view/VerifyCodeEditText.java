package com.example.administrator.myapplication.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.lang.reflect.Field;

/**
 * Created by hua on 2017/1/11.
 *
 * 1. 改成用代码生成 Shape
 * 2. 改成用代码生成 ColorStateList
 */

public class VerifyCodeEditText extends FrameLayout {

    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;
    private static final String DEFAULT_HINT = "验证码";
    private static final int DEFAULT_RESEND_TIME = 60;
    private static final String DEFAULT_BUTTON_TEXT = "免费获取";
    private static final int DEFAULT_UNDERLINE_COLOR = Color.BLACK;

    private static final int DEFAULT_PRESSED_COLOR =  Color.parseColor("#bedda8");
    private static final int DEFAULT_DISABLED_COLOR = Color.parseColor("#bbbbbb");
    private static final int DEFAULT_NORMAL_COLOR =   Color.parseColor("#7dba50");

    private static final int[] STATE_PRESSED  = {android.R.attr.state_pressed};
    private static final int[] STATE_DISABLED = {-android.R.attr.state_enabled};
    private static final int[] STATE_NORMAL   = {};


    /**
     * 用在背景跟文字上的颜色
     */
    private static final ColorStateList DEFAULT_BUTTON_COLOR;
//    private static final Drawable DEFAULT_BUTTON_BACKGROUND;

    private static final int DEFAULT_SHAPE_CORNER = 4;
    private static final int DEFAULT_SHAPE_STROKE_WIDTH = 1;

    private EditText mEditText;
    private TextView mTextSendVerify;
    private View mViewUnderline;

    private int mResendTime;

    private int mLastCursorColor = -1;

    static {

        int[][] states = new int[3][];
        states[0] = STATE_PRESSED;
        states[1] = STATE_DISABLED;
        states[2] = STATE_NORMAL;

        int[] colors = {
            DEFAULT_PRESSED_COLOR,
            DEFAULT_DISABLED_COLOR,
            DEFAULT_NORMAL_COLOR
        };

        DEFAULT_BUTTON_COLOR = new ColorStateList(states, colors);
    }

    /**
     * 获取验证码按钮的背景
     */
    private GradientDrawable mGradientDrawable;

    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.verify_edit_layout, this);
        mEditText = (EditText) findViewById(R.id.edit_input);
        mTextSendVerify = (TextView) findViewById(R.id.text_send_verifyCode);
        mViewUnderline = findViewById(R.id.view_underline);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeEditText);

        int cursorDrawableResId = a.getResourceId(R.styleable.VerifyCodeEditText_cursorDrawable, -1);
        String hint = a.getString(R.styleable.VerifyCodeEditText_hint);
        int hintColor = a.getColor(R.styleable.VerifyCodeEditText_hintColor, -1);
        int textSize = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_textSize, -1);
        int textColor = a.getColor(R.styleable.VerifyCodeEditText_textColor, -1);
        int verifyCodeLength = a.getInt(R.styleable.VerifyCodeEditText_verifyCodeLength, -1);
        int textMarginBottom = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_editMarginBottom, -1);

        ColorStateList buttonTextColor = a.getColorStateList(R.styleable.VerifyCodeEditText_buttonTextColor);
        int buttonTextSize = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_buttonTextSize, -1);
        int buttonBackground = a.getResourceId(R.styleable.VerifyCodeEditText_buttonBackground, -1);
        int buttonMarginBottom = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_buttonMarginBottom, -1);
        String buttonText = a.getString(R.styleable.VerifyCodeEditText_buttonText);

        int underLineColor = a.getColor(R.styleable.VerifyCodeEditText_underlineColor, -1);
        int underLineHeight = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_underlineHeight, -1);

        boolean gravityCenterVertical = a.getBoolean(R.styleable.VerifyCodeEditText_gravityCenterVertical, true);

        mResendTime = a.getInt(R.styleable.VerifyCodeEditText_resendTime, -1);
        a.recycle();

        setCursorDrawable(cursorDrawableResId);
        setHint(hint);
        setHintColor(hintColor);
        setTextSize(textSize);
        setTextColor(textColor);
        setVerifyCodeLength(verifyCodeLength);
        setTextBottomMargin(textMarginBottom);

        setButtonTextColor(buttonTextColor);
        setButtonTextSize(buttonTextSize);
        setButtonBackground(buttonBackground);
        setButtonBottomMargin(buttonMarginBottom);
        setButtonText(buttonText);

        setUnderLineColor(underLineColor);
        setUnderLineHeight(underLineHeight);

        setGravityCenterVertical(gravityCenterVertical);
        setResendTime(mResendTime);
    }

    /**
     * @param hint null 则设置为默认的 hint
     */
    public void setHint(String hint) {
        if (hint == null) {
            hint = DEFAULT_HINT;
        }
        mEditText.setHint(hint);
    }

    public void setHintColor(int color) {
        if (color != -1) {
            mEditText.setHintTextColor(color);
        }
    }

    public void setTextSize(int size) {
        if (size != -1) {
            mEditText.setTextSize(size);
        }
    }

    public void setTextColor(int color) {
        if (color != -1) {
            mEditText.setTextColor(color);
        }
    }

    public void setVerifyCodeLength(int length) {
        if (length <= 0) {
            length = DEFAULT_VERIFY_CODE_LENGTH;
        }
        mEditText.setFilters(new InputFilter[]{new LengthFilter(length)});
    }

    public void setTextBottomMargin(int margin) {
        if (margin >= 0) {
            ((MarginLayoutParams) mEditText.getLayoutParams()).bottomMargin = margin;
        }
    }

    public void setButtonTextSize(int size) {
        if (size >= 0) {
            mTextSendVerify.setTextSize(size);
        }
    }

    public void setButtonTextColor(ColorStateList color) {
        if (color == null) {
//            color = ContextCompat.getColorStateList(getContext(), DEFAULT_BUTTON_TEXT_COLOR);
        }
        mTextSendVerify.setTextColor(color);
    }

    public void setButtonBackground(int drawable) {
        if (drawable == -1) {
//            mTextSendVerify.setBackground(DEFAULT_BUTTON_BACKGROUND);
        }
        mTextSendVerify.setBackgroundResource(drawable);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    private GradientDrawable generateDefaultShape() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp2px(DEFAULT_SHAPE_CORNER));
        gradientDrawable.setStroke(dp2px(DEFAULT_SHAPE_STROKE_WIDTH), DEFAULT_BUTTON_COLOR);
        return gradientDrawable;
    }

    private GradientDrawable generateShape(int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp2px(DEFAULT_SHAPE_CORNER));
        gradientDrawable.setStroke(dp2px(DEFAULT_SHAPE_STROKE_WIDTH), strokeColor);
        return gradientDrawable;
    }

    /**
     * Api 21 以下不能在 shape 中设置 ColorStateList ，所以只能创建 selector
     */
    private StateListDrawable generateDefaultSelector() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(STATE_PRESSED, generateShape(DEFAULT_PRESSED_COLOR));
        stateListDrawable.addState(STATE_DISABLED, generateShape(DEFAULT_DISABLED_COLOR));
        stateListDrawable.addState(STATE_NORMAL, generateShape(DEFAULT_NORMAL_COLOR));
        return stateListDrawable;
    }

    public void setResendTime(int time) {
        if (time <= 0) {
            time = DEFAULT_RESEND_TIME;
        }
        mResendTime = time;
    }

    public void setButtonBottomMargin(int margin) {
        if (margin >= 0) {
            ((MarginLayoutParams) mTextSendVerify.getLayoutParams()).bottomMargin = margin;
        }
    }

    public void setButtonText(String text) {
        if (TextUtils.isEmpty(text)) {
            text = DEFAULT_BUTTON_TEXT;
        }
        mTextSendVerify.setText(text);
    }

    public void setUnderLineColor(int color) {
        if (color == -1) {
            color = DEFAULT_UNDERLINE_COLOR;
        }
        mViewUnderline.setBackgroundColor(color);
    }

    public void setUnderLineHeight(int height) {
        if (height > 0) {
            mViewUnderline.getLayoutParams().height = height;
        }
    }

    public void setGravityCenterVertical(boolean centerVertical) {
        LayoutParams lpEdit = (LayoutParams) mEditText.getLayoutParams();
        LayoutParams lpText = (LayoutParams) mTextSendVerify.getLayoutParams();
        if (centerVertical) {
            lpEdit.gravity = Gravity.CENTER_VERTICAL;
            lpText.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        } else {
            lpEdit.gravity = Gravity.BOTTOM;
            lpText.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        }
    }

    /**
     * 设置获取验证码按钮的背景跟按钮文字颜色
     * @param pressedColor 按下时背景跟文字的颜色值
     * @param disabledColor 可不点击时背景跟文字的颜色值
     * @param normalColor 正常状态下背景跟文字的颜色值
     */
    public void setButtonColorByColorValue(@ColorInt int pressedColor,
                                           @ColorInt int disabledColor,
                                           @ColorInt int normalColor) {

    }

    /**
     * 设置获取验证码按钮的背景跟按钮文字颜色
     * @param pressedColorRes 按下时背景跟文字的颜色资源 id
     * @param disabledColorRes 可不点击时背景跟文字的颜色资源 id
     * @param normalColorRes 正常状态下背景跟文字的颜色资源 id
     */
    public void setButtonColorByColorResource(@ColorRes int pressedColorRes,
                                              @ColorRes int disabledColorRes,
                                              @ColorRes int normalColorRes) {

    }

    /**
     * @see #setCursorColor(int)
     * @see #setCursorColorResource(int)
     */
    public void setCursorDrawable(@DrawableRes int resId) {
        if (resId != -1) {
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(mEditText, resId);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 设置光标的宽度
     * @param width 光标的宽度，单位为 dp
     */
    public void setCursorWidth(int width, int color) {
        if (mLastCursorColor == color) {
            return;
        }
        mLastCursorColor = color;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(dp2px(width), 0);
        gradientDrawable.setColor(color);

        Drawable[] drawables = getCursorDrawable();
        for (Drawable d : drawables) {
            DrawableCompat.setTint(d, color);
        }
    }

    /**
     * 设置光标的颜色
     * {@see #setCursorDrawable(int)}
     *
     * @param color 光标的颜色值
     */
    public void setCursorColor(@ColorInt int color) {
        if (mLastCursorColor == color) {
            return;
        }
        mLastCursorColor = color;

        Drawable[] drawables = getCursorDrawable();
        for (Drawable d : drawables) {
            DrawableCompat.setTint(d, color);
        }
    }

    /**
     * 设置光标的颜色
     * {@see #setCursorDrawable(int)}
     *
     * @param colorRes 光标的颜色资源 id
     */
    public void setCursorColorResource(@ColorRes int colorRes) {
        setCursorColor(ContextCompat.getColor(getContext(), colorRes));
    }

    private Drawable[] getCursorDrawable() {
        try {
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(mEditText);

            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            return drawables;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private int dp2px(int dp) {
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    private int sp2px(int sp) {
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    private int applyDimension(int unit, int value) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(unit, value, dm);
    }
}
