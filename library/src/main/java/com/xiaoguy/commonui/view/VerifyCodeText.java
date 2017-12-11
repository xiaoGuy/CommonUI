package com.xiaoguy.commonui.view;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoguy.commonui.R;

import java.lang.reflect.Field;

/**
 * Created by Xiaoguy on 2017/1/11.
 */

public class VerifyCodeText extends FrameLayout {

    private static final String TAG = VerifyCodeText.class.getSimpleName();

    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;
    private static final String DEFAULT_HINT = "验证码";
    private static final int DEFAULT_RESEND_TIME = 60;
    private static final String DEFAULT_BUTTON_TEXT = "获取验证码";
    private static final String DEFAULT_WAITING_TEXT = "%s秒后重新获取";
    private static final int DEFAULT_UNDERLINE_COLOR = Color.BLACK;
    private static final int DEFAULT_UNDERLINE_HEIGHT = 1;
    private static final int DEFAULT_LABEL_PADDING = 10;

    private static final int DEFAULT_PRESSED_COLOR = Color.parseColor("#bedda8");
    private static final int DEFAULT_DISABLED_COLOR = Color.parseColor("#bbbbbb");
    private static final int DEFAULT_NORMAL_COLOR = Color.parseColor("#7dba50");

    private static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
    private static final int[] STATE_DISABLED = {-android.R.attr.state_enabled};
    private static final int[] STATE_NORMAL = {};

    /**
     * 用在背景跟文字上的颜色
     */
    private static final ColorStateList DEFAULT_BUTTON_COLOR;

    private EditText mEditText;
    private TextView mTextSendVerify;
    private View mViewUnderline;
    private TextView mTextLabel;
    private ImageView mImageIcon;

    private int mResendTime;
    private int mLastCursorColor = -1;
    private int mLabelPadding;
    private boolean mUnderlineAlginEditText;
    private String mButtonText;
    private String mWaitingText;

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

    private class ResendCountDownTimer extends CountDownTimer {

        public ResendCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTextSendVerify.setText(String.format(mWaitingText, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            setButtonEnabled(true);
            mTextSendVerify.setText(mButtonText);
        }
    }

    private ResendCountDownTimer mResendCountDownTimer;

    public void startCountdown() {
        mResendCountDownTimer.start();
        setButtonEnabled(false);
    }

    public void cancelCountdown() {
        setButtonEnabled(true);
        mTextSendVerify.setText(mButtonText);
        mResendCountDownTimer.cancel();
    }

    public interface OnVerifyButtonClickListener {
        void onVerifyButtonClick();
    }

    private OnVerifyButtonClickListener mOnButtonClickListener;

    public void setOnVerifyButtonClickListener(OnVerifyButtonClickListener l) {
        mOnButtonClickListener = l;
    }

    public VerifyCodeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.verify_edit_layout, this);

        mEditText = (EditText) findViewById(R.id.edit_input);
        mTextSendVerify = (TextView) findViewById(R.id.text_send_verifyCode);
        mViewUnderline = findViewById(R.id.view_underline);
        mTextLabel = (TextView) findViewById(R.id.text_label);
        mImageIcon = (ImageView) findViewById(R.id.image_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeText);

        int cursorDrawableResId = a.getResourceId(R.styleable.VerifyCodeText_cursorDrawable, -1);
        int cursorColor = a.getColor(R.styleable.VerifyCodeText_cursorColor, -1);
        String hint = a.getString(R.styleable.VerifyCodeText_verify_hint);
        int hintColor = a.getColor(R.styleable.VerifyCodeText_verify_hintColor, -1);
        int textSize = a.getDimensionPixelSize(R.styleable.VerifyCodeText_verify_textSize, -1);
        int textColor = a.getColor(R.styleable.VerifyCodeText_verify_textColor, -1);
        int verifyCodeLength = a.getInt(R.styleable.VerifyCodeText_verifyCodeLength, -1);
        int textMarginBottom = a.getDimensionPixelSize(R.styleable.VerifyCodeText_editMarginBottom, -1);

        String label = a.getString(R.styleable.VerifyCodeText_leftLabel);
        int labelSize = a.getDimensionPixelSize(R.styleable.VerifyCodeText_leftLabelSize, -1);
        mLabelPadding = a.getDimensionPixelSize(R.styleable.VerifyCodeText_labelPadding, dp2px(DEFAULT_LABEL_PADDING));
        Drawable icon = a.getDrawable(R.styleable.VerifyCodeText_leftIcon);
        ColorStateList buttonTextColor = a.getColorStateList(R.styleable.VerifyCodeText_buttonTextColor);
        int buttonTextSize = a.getDimensionPixelSize(R.styleable.VerifyCodeText_buttonTextSize, -1);
        Drawable buttonBackground = a.getDrawable(R.styleable.VerifyCodeText_buttonBackground);
        int buttonMarginBottom = a.getDimensionPixelSize(R.styleable.VerifyCodeText_buttonMarginBottom, -1);
        String buttonText = a.getString(R.styleable.VerifyCodeText_buttonText);
        String waitingText = a.getString(R.styleable.VerifyCodeText_waitingText);

        int underLineColor = a.getColor(R.styleable.VerifyCodeText_underlineColor, -1);
        int underLineHeight = a.getDimensionPixelSize(R.styleable.VerifyCodeText_underlineHeight, -1);
        mUnderlineAlginEditText = a.getBoolean(R.styleable.VerifyCodeText_underlineAlignLeftEditText, true);

        boolean gravityCenterVertical = a.getBoolean(R.styleable.VerifyCodeText_gravityCenterVertical, true);

        mResendTime = a.getInt(R.styleable.VerifyCodeText_resendTime, DEFAULT_RESEND_TIME);
        a.recycle();

        setLeftLabel(label);
        setLeftLabelSize(labelSize);
        setLeftIcon(icon);

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
        setWaitingText(waitingText);

        setUnderLineColor(underLineColor);
        setUnderLineHeight(underLineHeight);

        setGravityCenterVertical(gravityCenterVertical);
        setResendTime(mResendTime);

        mTextSendVerify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonClickListener != null) {
                    mOnButtonClickListener.onVerifyButtonClick();
                }
            }
        });
    }

    public void setLeftLabel(String label) {
        if (TextUtils.isEmpty(label)) {
            return;
        }
        mTextLabel.setVisibility(VISIBLE);
        mImageIcon.setVisibility(GONE);
        mTextLabel.setText(label);
    }

    public void setLeftLabelSize(int size) {
        if (size != -1) {
            // TextView 的 setTextSize() 方法接收的是 sp ，所以要将 size 先转换成 sp
            size = px2sp(size);
            mTextLabel.setTextSize(size);
        }
    }

    public void setLeftIcon(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        mImageIcon.setVisibility(VISIBLE);
        mTextLabel.setVisibility(GONE);
        mImageIcon.setImageDrawable(drawable);
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
            color = DEFAULT_BUTTON_COLOR;
        }
        // 如果只设置了一种状态的颜色，则会添加默认的点击颜色
        if (!color.isStateful()) {
            int normalColor = color.getDefaultColor();
            int pressedColor = Color.LTGRAY;
            color = new ColorStateList(
                    new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.enabled}, new int[0]},
                    new int[]{pressedColor, pressedColor, normalColor});
        }
        mTextSendVerify.setTextColor(color);
    }

    public void setButtonBackground(Drawable drawable) {
        if (drawable != null) {
            mTextSendVerify.setBackgroundDrawable(drawable);
        }
    }

    public void setButtonEnabled(boolean enabled) {
        mTextSendVerify.setEnabled(enabled);
    }

    public void setResendTime(int time) {
        if (time <= 0) {
            time = DEFAULT_RESEND_TIME;
        }
        mResendTime = time;
        mResendCountDownTimer = new ResendCountDownTimer(time * 1000, 1000);
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

    public void setWaitingText(String text) {
        if (TextUtils.isEmpty(text)) {
            mWaitingText = DEFAULT_WAITING_TEXT;
        } else if (text.indexOf("%s") == -1) {
            Log.e(TAG, "setWaitingText() must contain %s ");
            mWaitingText = DEFAULT_WAITING_TEXT;
        } else {
            mWaitingText = text;
        }
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
     * @param resId 光标颜色的资源文件
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
        setTint(drawable, color);

        Drawable[] drawables = new Drawable[2];
        drawables[0] = drawables[1] = drawable;
        setCursorDrawable(drawables);
    }

    /**
     * 设置光标的颜色
     * @see #setCursorDrawable(int)
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
            fCursorDrawable.set(editor, drawables);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public EditText getEditText() {
        return mEditText;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        if (mTextLabel.getVisibility() == VISIBLE) {
            width = mTextLabel.getMeasuredWidth();
        } else if (mImageIcon.getVisibility() == VISIBLE) {
            width = mImageIcon.getMeasuredWidth();
        }
        if (width != 0) {
            ((MarginLayoutParams) mEditText.getLayoutParams()).leftMargin = width + mLabelPadding;
            ((MarginLayoutParams) mViewUnderline.getLayoutParams()).leftMargin = width + mLabelPadding;
        }
    }

    private int dp2px(int dp) {
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    private int px2sp(int px) {
        final float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity);
    }

    private int applyDimension(int unit, int value) {
        return (int) TypedValue.applyDimension(unit, value, getContext().getResources().getDisplayMetrics());
    }

    private void setTint(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
    }
}
