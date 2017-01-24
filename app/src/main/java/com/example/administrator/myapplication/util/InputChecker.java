package com.example.administrator.myapplication.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/1/22.
 */

public class InputChecker {

    public interface InputListener {
        void isAllFieldInput(boolean allInput);
    }

    private InputChecker() {
    }

    public static void checkInput(InputListener listener, EditText... editTexts) {
        if (editTexts != null && editTexts.length != 0 && listener != null) {
            final TextWatcher textWatcher = new MyTextWatcher(editTexts, listener);
            for (EditText editText : editTexts) {
                editText.addTextChangedListener(textWatcher);
            }
            preCheck(listener, editTexts);
        }
    }

    /**
     * 因为 EditText 有可能存在默认值，所以在用户输入前需要先行检查
     */
    private static void preCheck(InputListener listener, EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().length() == 0) {
                listener.isAllFieldInput(false);
                break;
            }
            listener.isAllFieldInput(true);
        }
    }

    private static class MyTextWatcher implements TextWatcher {

        private InputListener mListener;
        private EditText[] mEditTextList;

        private MyTextWatcher(EditText[] editTexts, InputListener l) {
            mEditTextList = editTexts;
            mListener = l;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                mListener.isAllFieldInput(false);
            } else {
                // 布局中只有一个 EditText
                if (mEditTextList.length == 1) {
                    mListener.isAllFieldInput(true);
                } else {
                    for (EditText editText : mEditTextList) {
                        if (editText.getText().length() == 0) {
                            mListener.isAllFieldInput(false);
                            break;
                        }
                        mListener.isAllFieldInput(true);
                    }
                }
            }
        }
    }
}
