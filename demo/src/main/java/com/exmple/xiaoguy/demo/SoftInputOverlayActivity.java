package com.exmple.xiaoguy.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoguy.commonui.util.KeyboardDetector;
import com.xiaoguy.commonui.util.KeyboardDetector.OnKeyboardStateChangedListener;

import me.yokeyword.fragmentation.SupportActivity;

public class SoftInputOverlayActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input_overlay);

        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        KeyboardDetector.bind(this, layout, new OnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardShow() {
                findViewById(R.id.image).setVisibility(View.GONE);
            }

            @Override
            public void onKeyboardHide() {
                findViewById(R.id.image).setVisibility(View.VISIBLE);
            }
        });


    }
}
