package com.example.administrator.myapplication;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by hua on 2017/1/11.
 */

public class VerifyCodeEditText extends FrameLayout {

    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;
    private static final String DEFAULT_HINT = "验证码";
    private static final int DEFAULT_RESEND_TIME = 60;
    private static final String DEFAULT_BUTTON_TEXT = "免费获取";
    private static final int DEFAULT_UNDERLINE_COLOR = Color.BLACK;
    private static final int DEFAULT_BUTTON_TEXT_COLOR = R.color.selector_text_verify_code_green;
    private static final int DEFAULT_BUTTON_BACKGROUND = R.drawable.selector_btn_verify_code_green;

    private EditText mEditText;
    private TextView mTextSendVerify;
    private View mViewUnderline;

    private int mResendTime;

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
            color = ContextCompat.getColorStateList(getContext(), DEFAULT_BUTTON_TEXT_COLOR);
        }
        mTextSendVerify.setTextColor(color);
    }

    public void setButtonBackground(int drawable) {
        if (drawable == -1) {
            drawable = DEFAULT_BUTTON_BACKGROUND;
        }
        mTextSendVerify.setBackgroundResource(drawable);
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
}
