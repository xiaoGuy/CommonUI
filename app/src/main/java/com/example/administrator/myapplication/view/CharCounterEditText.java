package com.example.administrator.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/2/10.
 */

public class CharCounterEditText extends FrameLayout implements TextWatcher {

    private static final String TAG = CharCounterEditText.class.getSimpleName();

    private static final int DEFAULT_EDIT_HEIGHT = 100;
    private static final int DEFAULT_MARGIN = 10;
    private static final int DEFAULT_MAX_COUNT = 175;

    private static final String DEFAULT_COUNTER_TEXT = "还可以输入 %s 个字";

    private EditText mEditText;
    private TextView mTextCounter;

    private int mMaxInput;
    private String mCounterText;

    public CharCounterEditText(Context context) {
        super(context);
    }

    public CharCounterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.char_counter_edit_layout, this);
        mEditText = (EditText) findViewById(R.id.edit);
        mTextCounter = (TextView) findViewById(R.id.text_counter);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CharCounterEditText);

        int editHeight = a.getDimensionPixelSize(R.styleable.CharCounterEditText_editHeight, dp2px(DEFAULT_EDIT_HEIGHT));
        int editMarginLeft = a.getDimensionPixelSize(R.styleable.CharCounterEditText_editMarginLeft, -1);
        int editMarginRight = a.getDimensionPixelSize(R.styleable.CharCounterEditText_editMarginRight, -1);
        int editMarginTop = a.getDimensionPixelSize(R.styleable.CharCounterEditText_editMarginTop, -1);
        int editMargin = a.getDimensionPixelSize(R.styleable.CharCounterEditText_editMargin, -1);

        Drawable editBackground = a.getDrawable(R.styleable.CharCounterEditText_editBackground);
        boolean singleLine = a.getBoolean(R.styleable.CharCounterEditText_singleLine, false);
        mMaxInput = a.getInt(R.styleable.CharCounterEditText_maxLength, DEFAULT_MAX_COUNT);
        int cursorColor = a.getColor(R.styleable.CharCounterEditText_textCursorColor, -1);

        int gap = a.getDimensionPixelOffset(R.styleable.CharCounterEditText_gap, dp2px(DEFAULT_MARGIN));

        mCounterText = a.getString(R.styleable.CharCounterEditText_counterText);
        int counterTextColor = a.getColor(R.styleable.CharCounterEditText_counterTextColor, -1);
        int counterMarginRight = a.getDimensionPixelSize(R.styleable.CharCounterEditText_counterMarginRight, -1);
        int counterMarginBottom = a.getDimensionPixelSize(R.styleable.CharCounterEditText_counterMarginBottom, -1);

        a.recycle();

        MarginLayoutParams lp = (MarginLayoutParams) mEditText.getLayoutParams();
        lp.height = editHeight;
        lp.leftMargin = editMarginLeft == -1 ? editMargin == -1 ? 0 : editMargin : editMarginLeft;
        lp.rightMargin = editMarginRight == -1 ? editMargin == -1 ? 0 : editMargin : editMarginRight;
        lp.topMargin = editMarginTop == -1 ? editMargin == -1 ? 0 : editMargin : editMarginTop;
        lp.bottomMargin = gap;

        if (editBackground != null) {
            mEditText.setBackgroundDrawable(editBackground);
        }
        if (singleLine) {
            mEditText.setSingleLine();
            lp.height = LayoutParams.WRAP_CONTENT;
        }
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxInput)});
        setCursorColor(mEditText, cursorColor);

        if (TextUtils.isEmpty(mCounterText) || mCounterText.indexOf("%s") == -1) {
            mCounterText = DEFAULT_COUNTER_TEXT;
        }
        mTextCounter.setText(String.format(mCounterText, mMaxInput));
        if (counterTextColor != -1) {
            mTextCounter.setTextColor(counterTextColor);
        }

        lp = (MarginLayoutParams) mTextCounter.getLayoutParams();
        lp.rightMargin = counterMarginRight == -1 ? 0 : counterMarginRight;
        lp.bottomMargin = counterMarginBottom == -1 ? 0 : counterMarginBottom;

        // 放在 setSingleLine() 之前会报异常
        mEditText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        int inputChars = s.length();
        int remainChars = mMaxInput - inputChars;
        mTextCounter.setText(String.format(mCounterText, remainChars));
    }

    private void setCursorColor(TextView tv, @ColorInt int color) {
        if (color == -1) {
            return;
        }
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
        setCursorDrawable(tv, drawables);
    }

    private void setCursorDrawable(TextView tv, Drawable[] drawables) {
        try {
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(tv);

            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            fCursorDrawable.set(editor, drawables);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTint(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
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
}
