package com.exmple.xiaoguy.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.xiaoguy.commonui.view.ClearAutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    private Thread mThread1;
    private Thread mThread2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"111", "1111", "11111"});
        ClearAutoCompleteTextView ctv = (ClearAutoCompleteTextView) findViewById(R.id.clear_edit);
        ctv.setAdapter(new FilterAdapter());

        mThread1 = Thread.currentThread();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mThread2 = Thread.currentThread();

                Log.d("size", (mThread1 == mThread2) + "");
            }
        });


    }

}
