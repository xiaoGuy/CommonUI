package com.exmple.xiaoguy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.xiaoguy.commonui.view.ClearAutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClearAutoCompleteTextView ctv = (ClearAutoCompleteTextView) findViewById(R.id.clear_edit);
        ctv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"111", "1111", "11111"}));
    }
}
