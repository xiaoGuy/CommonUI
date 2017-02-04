package com.example.administrator.myapplication.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.lang.reflect.Field;

import retrofit2.http.GET;

/**
 * Created by hua on 2017/1/11.
 * <p>
 * 1. 改成用代码生成 Shape
 * 2. 改成用代码生成 ColorStateList
 */

public class VerifyCodeEditText extends FrameLayout {

    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;
    private static final String DEFAULT_HINT = "验证码";
    private static final int DEFAULT_RESEND_TIME = 60;
    private static final String DEFAULT_BUTTON_TEXT = "免费获取";
    private static final String BUTTON_TEXT_COUNTDOWN = "剩余%ss";
    private static final int DEFAULT_UNDERLINE_COLOR = Color.BLACK;
    private static final int DEFAULT_UNDERLINE_HEIGHT = 1;

    private static final int DEFAULT_PRESSED_COLOR = Color.parseColor("#bedda8");
    private static final int DEFAULT_DISABLED_COLOR = Color.parseColor("#bbbbbb");
    private static final int DEFAULT_NORMAL_COLOR = Color.parseColor("#7dba50");

    private static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
    private static final int[] STATE_DISABLED = {-android.R.attr.state_enabled};
    private static final int[] STATE_NORMAL = {};

    /**
     * 用在背景跟文字上的颜色
     */
    private static final ColorStateList DEFAULT_BUTTON_TEXT_COLOR;

    private static final int DEFAULT_SHAPE_CORNER = 4;
    private static final int DEFAULT_SHAPE_STROKE_WIDTH = 1;
    private static final int DEFAULT_BUTTON_PADDING_LEFT = 10;
    private static final int DEFAULT_BUTTON_PADDING_RIGHT = 10;
    private static final int DEFAULT_BUTTON_PADDING_TOP = 5;
    private static final int DEFAULT_BUTTON_PADDING_BOTTOM = 5;

    private EditText mEditText;
    private TextView mTextSendVerify;
    private View mViewUnderline;

    private int mResendTime;
    private int mLastCursorColor = -1;
    private boolean mShowButtonBorder;
    private boolean mIsCountdown;
    private int mLeftTime;
    private String mButtonText;
    private Drawable mDefaultButtonBackground;

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

        DEFAULT_BUTTON_TEXT_COLOR = new ColorStateList(states, colors);
    }

    private Handler mHandler = new Handler();

    private Runnable updateButtonTextTask = new Runnable() {
        @Override
        public void run() {
            updateButtonTextWhenCountdown();
        }
    };

    public void startCountdown() {
        mIsCountdown = true;
        setButtonEnabled(false);
        updateButtonTextWhenCountdown();
    }

    public void cancelCountdown() {
        if (! mIsCountdown) {
            return;
        }
        mIsCountdown = false;
        mHandler.removeCallbacks(updateButtonTextTask);
        setButtonEnabled(true);
        mLeftTime = mResendTime;
        mTextSendVerify.setText(mButtonText);
    }

    private void updateButtonTextWhenCountdown() {
        mLeftTime --;
        if (mLeftTime <= 0) {
            cancelCountdown();
            return;
        }
        mTextSendVerify.setText(String.format(BUTTON_TEXT_COUNTDOWN, mLeftTime));
        mHandler.postDelayed(updateButtonTextTask, 1000);
    }

    public interface OnVerifyButtonClickListener {
        void onVerifyButtonClick();
    }

    private OnVerifyButtonClickListener mOnButtonClickListener;

    public void setOnVerifyButtonClickListener(OnVerifyButtonClickListener l) {
        mOnButtonClickListener = l;
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.verify_edit_layout, this);

        mEditText = (EditText) findViewById(R.id.edit_input);
        mTextSendVerify = (TextView) findViewById(R.id.text_send_verifyCode);
        mViewUnderline = findViewById(R.id.view_underline);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeEditText);

        int cursorDrawableResId = a.getResourceId(R.styleable.VerifyCodeEditText_cursorDrawable, -1);
        int cursorColor = a.getColor(R.styleable.VerifyCodeEditText_cursorColor, -1);
        String hint = a.getString(R.styleable.VerifyCodeEditText_hint);
        int hintColor = a.getColor(R.styleable.VerifyCodeEditText_hintColor, -1);
        int textSize = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_textSize, -1);
        int textColor = a.getColor(R.styleable.VerifyCodeEditText_textColor, -1);
        int verifyCodeLength = a.getInt(R.styleable.VerifyCodeEditText_verifyCodeLength, -1);
        int textMarginBottom = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_editMarginBottom, -1);

        ColorStateList buttonTextColor = a.getColorStateList(R.styleable.VerifyCodeEditText_buttonTextColor);
        int buttonTextSize = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_buttonTextSize, -1);
        Drawable buttonBackground = a.getDrawable(R.styleable.VerifyCodeEditText_buttonBackground);
        int buttonMarginBottom = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_buttonMarginBottom, -1);
        String buttonText = a.getString(R.styleable.VerifyCodeEditText_buttonText);

        int underLineColor = a.getColor(R.styleable.VerifyCodeEditText_underlineColor, -1);
        int underLineHeight = a.getDimensionPixelSize(R.styleable.VerifyCodeEditText_underlineHeight, -1);

        boolean gravityCenterVertical = a.getBoolean(R.styleable.VerifyCodeEditText_gravityCenterVertical, true);
        mShowButtonBorder = a.getBoolean(R.styleable.VerifyCodeEditText_showButtonBorder, true);

        mResendTime = a.getInt(R.styleable.VerifyCodeEditText_resendTime, -1);
        ColorStateList tint = a.getColorStateList(R.styleable.VerifyCodeEditText_tint);
        a.recycle();

        setCursorDrawable(cursorDrawableResId);
        setCursorColor(cursorColor);
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

        setTint(tint);

        mTextSendVerify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonClickListener != null) {
                    mOnButtonClickListener.onVerifyButtonClick();
                }
            }
        });
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
            color = DEFAULT_BUTTON_TEXT_COLOR;
        }
        mTextSendVerify.setTextColor(color);
    }

    public void setButtonBackground(Drawable drawable) {
        if (drawable == null) {
            if (! mShowButtonBorder) {
                return;
            }
            drawable = generateDefaultButtonBackground();
            mDefaultButtonBackground = drawable;
        }
        mTextSendVerify.setBackgroundDrawable(drawable);
    }

    /**
     * 设置获取验证码按钮的文字跟背景的颜色
     */
    public void setTint(ColorStateList colorStateList) {
       if (colorStateList != null) {
           mTextSendVerify.setTextColor(colorStateList);

           if (mShowButtonBorder && mDefaultButtonBackground != null) {
               if (mDefaultButtonBackground instanceof GradientDrawable) {
                   GradientDrawable gradientDrawable = (GradientDrawable) mDefaultButtonBackground;
                   gradientDrawable.setStroke(dp2px(DEFAULT_SHAPE_STROKE_WIDTH), colorStateList);
               } else {
//                   DrawableCompat.setTintList(mDefaultButtonBackground, colorStateList);
//                   mDefaultButtonBackground.setTintList(colorStateList);
                   // TODO 改成 ColorStateList
                   int colorPressed = colorStateList.getColorForState(STATE_PRESSED, 0);
                   int colorDisabled = colorStateList.getColorForState(STATE_DISABLED, 0);
                   int colorNormal = colorStateList.getColorForState(STATE_NORMAL, 0);
                   StateListDrawable stateListDrawable = new StateListDrawable();
                   stateListDrawable.addState(STATE_PRESSED, generateShape(colorPressed, true));
                   stateListDrawable.addState(STATE_DISABLED, generateShape(colorDisabled, true));
                   stateListDrawable.addState(STATE_NORMAL, generateShape(colorNormal, true));
               }
           }
       }
    }

    public void setButtonEnabled(boolean enabled) {
        mTextSendVerify.setEnabled(enabled);
    }

    private Drawable generateDefaultButtonBackground() {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//            GradientDrawable gradientDrawable = new GradientDrawable();
//            gradientDrawable.setCornerRadius(dp2px(DEFAULT_SHAPE_CORNER));
//            gradientDrawable.setStroke(dp2px(DEFAULT_SHAPE_STROKE_WIDTH), DEFAULT_BUTTON_TEXT_COLOR);
//            setGradientDrawablePadding(gradientDrawable,
//                    dp2px(DEFAULT_BUTTON_PADDING_LEFT),
//                    dp2px(DEFAULT_BUTTON_PADDING_TOP),
//                    dp2px(DEFAULT_BUTTON_PADDING_RIGHT),
//                    dp2px(DEFAULT_BUTTON_PADDING_BOTTOM));
//            return gradientDrawable;
            generateShape(DEFAULT_BUTTON_TEXT_COLOR, false);
        }
        return generateSelector(DEFAULT_PRESSED_COLOR, DEFAULT_DISABLED_COLOR, DEFAULT_NORMAL_COLOR);
    }

    private GradientDrawable generateShape(ColorStateList strokeColor, boolean singleColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp2px(DEFAULT_SHAPE_CORNER));
        if (singleColor) {
            gradientDrawable.setStroke(dp2px(DEFAULT_SHAPE_STROKE_WIDTH), strokeColor.getDefaultColor());
        } else {
            gradientDrawable.setStroke(dp2px(DEFAULT_SHAPE_STROKE_WIDTH), strokeColor);
        }
        setGradientDrawablePadding(gradientDrawable,
                dp2px(DEFAULT_BUTTON_PADDING_LEFT),
                dp2px(DEFAULT_BUTTON_PADDING_TOP),
                dp2px(DEFAULT_BUTTON_PADDING_RIGHT),
                dp2px(DEFAULT_BUTTON_PADDING_BOTTOM));
        return gradientDrawable;
    }

    private StateListDrawable generateSelector(int pressedColor, int disabledColor, int normalColor) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(STATE_PRESSED, generateShape(ColorStateList.valueOf(pressedColor), true));
        stateListDrawable.addState(STATE_DISABLED, generateShape(ColorStateList.valueOf(disabledColor), true));
        stateListDrawable.addState(STATE_NORMAL, generateShape(ColorStateList.valueOf(normalColor), true));
        return stateListDrawable;
    }

    private void setGradientDrawablePadding(GradientDrawable gd, int left, int top, int right, int bottom) {
        try {
            Field f = GradientDrawable.class.getDeclaredField("mPadding");
            f.setAccessible(true);
            Rect rect = new Rect(left, top, right, bottom);
            f.set(gd, rect);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setResendTime(int time) {
        if (time <= 0) {
            time = DEFAULT_RESEND_TIME;
        }
        mResendTime = time;
        mLeftTime = time;
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
        mButtonText = text;
        mTextSendVerify.setText(text);
    }

    public void setUnderLineColor(int color) {
        if (color == -1) {
            color = DEFAULT_UNDERLINE_COLOR;
        }
        mViewUnderline.setBackgroundColor(color);
    }

    public void setUnderLineHeight(int height) {
        if (height < 0) {
            height = dp2px(DEFAULT_UNDERLINE_HEIGHT);
        }
        mViewUnderline.getLayoutParams().height = height;
    }

    public void setGravityCenterVertical(boolean centerVertical) {
        LayoutParams lpEdit = (LayoutParams) mEditText.getLayoutParams();
        LayoutParams lpText = (LayoutParams) mTextSendVerify.getLayoutParams();
        if (centerVertical) {
            lpEdit.gravity = Gravity.CENTER_VERTICAL;
            lpText.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        }
        else {
            lpEdit.gravity = Gravity.BOTTOM;
            lpText.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        }
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

        int res = 0;
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            res = f.getInt(mEditText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Drawable drawable = ContextCompat.getDrawable(mEditText.getContext(), res);
        DrawableCompat.setTint(drawable, color);

        Drawable[] drawables = new Drawable[2];
        drawables[0] = drawables[1] = drawable;
        setCursorDrawable(drawables);
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

    private void setCursorDrawable(Drawable[] drawables) {
        try {
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(mEditText);

            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            fCursorDrawable.set(mEditText, drawables);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int dp2px(int dp) {
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    private int sp2px(int sp) {
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    private int applyDimension(int unit, int value) {
        return (int) TypedValue.applyDimension(unit, value, getContext().getResources().getDisplayMetrics());
    }
}
