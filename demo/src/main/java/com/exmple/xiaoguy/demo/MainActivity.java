package com.exmple.xiaoguy.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoguy.commonui.text.DecimalNumberFilter;
import com.xiaoguy.commonui.text.DecimalNumberFilter.OnExceedMaxValueListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DecimalNumberFilter filter = new DecimalNumberFilter(200, 1);
        filter.setOnExceedMaxValueListener(new OnExceedMaxValueListener() {
            @Override
            public void onExceedMaxValue(int maxValue) {
                Toast.makeText(MainActivity.this, "Max value is " + maxValue, Toast.LENGTH_SHORT).show();
            }
        });

        final EditText editText = (EditText) findViewById(R.id.edit);
        editText.setFilters(new InputFilter[] {filter});
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

}
