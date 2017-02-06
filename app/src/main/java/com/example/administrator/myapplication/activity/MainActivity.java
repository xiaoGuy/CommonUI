package com.example.administrator.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.view.VerifyCodeEditText;
import com.example.administrator.myapplication.view.VerifyCodeEditText.OnVerifyButtonClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnVerifyButtonClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.verifyEdit)
    VerifyCodeEditText mVerifyEdit;
    @BindView(R.id.btn_red)
    Button mBtn;

    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.btn_blue)
    Button mBtnBlue;
    @BindView(R.id.btn_yellow)
    Button mBtnYellow;

    @BindView(R.id.btn_filter)
    Button mBtnFilter;
    @BindView(R.id.image_2)
    ImageView mImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onVerifyButtonClick() {
        mVerifyEdit.startCountdown();
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
