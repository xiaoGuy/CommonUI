package com.example.administrator.myapplication.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
//            Field f = GradientDrawable.class.getDeclaredField("mGradientState");
//            f.setAccessible(true);

//            Object obj = f.get(g);
//            f = obj.getClass().getDeclaredField("mPadding");
//            f.setAccessible(true);
//            Rect rect = new Rect(100, 200, 100, 200);
//            f.set(obj, rect);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(mText);
            Drawable drawable = ContextCompat.getDrawable(this, mCursorDrawableRes);
            Log.d("size", drawable.getClass().getCanonicalName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void f() {
//        MutableDateTime start = new MutableDateTime(2017, 1, 1, 0, 0, 0, 0);
//        MutableDateTime end   = new MutableDateTime(2017, 2, 10, 0, 0, 0, 0);
//        int days = Days.daysBetween(start, end).getDays();
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("M月d日");
//        String s = formatter.print(start);
//
//        for (int i = 0; i < days; i ++) {
//            Log.d("size", formatter.print(start));
//           start.addDays(1);
//        }
//        MutableDateTime start = new MutableDateTime(0,0,0,1,0,0,0);
//        MutableDateTime end = new MutableDateTime(0,0,0,0,20,0,0);
//        Hours.hoursBetween(start, end).getHours();
    }
}
