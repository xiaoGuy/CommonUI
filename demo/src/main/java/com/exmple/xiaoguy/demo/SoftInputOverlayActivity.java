package com.exmple.xiaoguy.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;

import me.yokeyword.fragmentation.SupportActivity;

public class SoftInputOverlayActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input_overlay);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.container, SoftInputOverlayFragment1.newInstance());
        }
    }
}
