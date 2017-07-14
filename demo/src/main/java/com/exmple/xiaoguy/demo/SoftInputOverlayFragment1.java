package com.exmple.xiaoguy.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;

public class SoftInputOverlayFragment1 extends SupportFragment {

    public static SoftInputOverlayFragment1 newInstance() {
        
        Bundle args = new Bundle();
        
        SoftInputOverlayFragment1 fragment = new SoftInputOverlayFragment1();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft_input_1, container, false);
        view.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start(SoftInputOverlayFragment2.newInstance());
            }
        });
        return view;
    }
}
