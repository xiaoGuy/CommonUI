package com.example.administrator.myapplication.activity;

import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.view.VerifyCodeEditText;
import com.example.administrator.myapplication.view.VerifyCodeEditText.OnVerifyButtonClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnVerifyButtonClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.verifyEdit)
    VerifyCodeEditText mVerifyEdit;
    @BindView(R.id.btn)
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.color);

        int colorPressed = colorStateList.getColorForState(new int[]{-android.R.attr.state_pressed}, 0);
        int colorDisabled = colorStateList.getColorForState(new int[]{-android.R.attr.state_enabled}, 0);

        Log.d(TAG, "pressed [" + android.R.attr.state_pressed + "] " + "#" + Integer.toHexString(colorPressed) + " " + colorPressed);
        Log.d(TAG, "disabled [" + -android.R.attr.state_enabled + "] " + "#" + Integer.toHexString(colorDisabled) + " " + colorDisabled);


    }

    @Override
    public void onVerifyButtonClick() {
        mVerifyEdit.startCountdown();
    }

    @OnClick(R.id.btn)
    public void onClick() {

        StateListDrawable drawable = (StateListDrawable) mBtn.getBackground();
        DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable, ContextCompat.getColorStateList(this, R.color.color2));

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
