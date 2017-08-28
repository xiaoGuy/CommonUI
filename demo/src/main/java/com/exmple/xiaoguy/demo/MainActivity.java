package com.exmple.xiaoguy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaoguy.commonui.view.ClearAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClearAutoCompleteTextView ctv = (ClearAutoCompleteTextView) findViewById(R.id.clear_edit);
        FilterAdapter<String> adapter = new FilterAdapter<String>() {
            @Override
            public List<String> performFiltering(CharSequence input, List<String> values) {
                List<String> result = new ArrayList<>(values.size());
                for (String str : values) {
                    if (str.startsWith(input.toString())) {
                        result.add(str);
                    }
                }
                return result;
            }
        };

        for (int i = 0; i < 1000000; i ++) {
            adapter.add("ba");
        }

        ctv.setAdapter(adapter);
    }

}
