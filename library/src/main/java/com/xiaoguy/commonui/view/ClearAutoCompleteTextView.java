package com.xiaoguy.commonui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.xiaoguy.commonui.R;
import com.xiaoguy.commonui.util.ScreenUtil;

/**
 * Created by hua on 2017/7/24.
 */

public class ClearAutoCompleteTextView extends AppCompatAutoCompleteTextView implements
        OnFocusChangeListener, TextWatcher {

    /**
     * 清空图标的大小，单位 dp
     */
    private static final int ICON_CLEAR_SIZE;

    static {
        ICON_CLEAR_SIZE = ScreenUtil.dp2px(15);
    }

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;
    private OnFocusChangeListener listener;

    public ClearAutoCompleteTextView(Context context) {
        super(context);
    }

    public ClearAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.autoCompleteTextViewStyle);
    }

    public ClearAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_clear);
        }

        mClearDrawable.setBounds(0, 0, ICON_CLEAR_SIZE, ICON_CLEAR_SIZE);
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        super.setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
//			doClearTextWhenTouchCloseIcon(event);
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void doClearTextWhenTouchCloseIcon(MotionEvent event){
        if (getCompoundDrawables()[2] != null) {

            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                    && (event.getX() < ((getWidth() - getPaddingRight())));

            if (touchable) {
                this.setText("");
            }
        }
    }
    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (listener != null)
            listener.onFocusChange(v, hasFocus);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.listener = l;
    }

//	public interface OnFocusChange {
//		void onChange(boolean hasFocus);
//	}

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void hideClearIcon(){
        setClearIconVisible(false);
    }
}
