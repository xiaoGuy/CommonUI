package com.example.administrator.myapplication.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
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

//        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.selector_text_verify_code_green);
//        Log.d("size", colorStateList.toString());
        int[][] states = new int[3][1];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};

        int[] colors = {Color.parseColor("#bedda8"), Color.parseColor("#bbbbbb"), Color.parseColor("#7dba50")};

        ColorStateList c = new ColorStateList(states, colors);
//        mText.setTextColor(c);

        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.BLUE);
        g.setCornerRadius(20);

        try {
            Field f = GradientDrawable.class.getDeclaredField("mGradientState");
            f.setAccessible(true);

            Object obj = f.get(g);
            f = obj.getClass().getDeclaredField("mPadding");
            f.setAccessible(true);
            Rect rect = new Rect(100, 200, 100, 200);
            f.set(obj, rect);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed}, g);
        sld.addState(new int[]{}, g);

        mText.setBackgroundDrawable(g);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Rect rect = new Rect();
            mText.getBackground().getPadding(rect);
            Log.d("size", rect.toString());
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
