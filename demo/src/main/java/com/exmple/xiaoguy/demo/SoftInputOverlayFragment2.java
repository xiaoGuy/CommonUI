package com.exmple.xiaoguy.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoguy.commonui.util.KeyboardDetector;
import com.xiaoguy.commonui.util.KeyboardDetector.OnKeyboardStateChangedListener;

import me.yokeyword.fragmentation.SupportFragment;

public class SoftInputOverlayFragment2 extends SupportFragment {

    public static SoftInputOverlayFragment2 newInstance() {
        
        Bundle args = new Bundle();
        
        SoftInputOverlayFragment2 fragment = new SoftInputOverlayFragment2();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft_input_2, container, false);
        KeyboardDetector.bind(this, (ViewGroup) view, new OnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardShow() {
                Log.d("size", "onKeyboardShow");
            }

            @Override
            public void onKeyboardHide() {
                Log.d("size", "onKeyboardHide");
            }
        });
        return view;
    }
}
