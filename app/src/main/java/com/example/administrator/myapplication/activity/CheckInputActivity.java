package com.example.administrator.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */

public abstract class CheckInputActivity extends AppCompatActivity {

    private List<EditText> mEditTextList;

    protected void collectEditText(EditText... editTexts) {
        if (editTexts != null && editTexts.length != 0) {
            mEditTextList = new ArrayList<>(editTexts.length);
            for (EditText editText : editTexts) {
                mEditTextList.add(editText);
            }
        }

        init();
    }

    private void init() {
        if (mEditTextList != null) {
            for (EditText editText : mEditTextList) {
                editText.addTextChangedListener(mTextWatcher);
            }
        }
    }

    protected abstract void onAllEditTextFillStateChanged(boolean allFilled);

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                 onAllEditTextFillStateChanged(false);
            } else {
                // 布局中只有一个 EditText
                if (mEditTextList.size() == 1) {
                    onAllEditTextFillStateChanged(true);
                } else {
                    for (EditText editText : mEditTextList) {
                        if (editText.getText().length() == 0) {
                            onAllEditTextFillStateChanged(false);
                            break;
                        }
                        onAllEditTextFillStateChanged(true);
                    }
                }
            }
        }
    };
}
