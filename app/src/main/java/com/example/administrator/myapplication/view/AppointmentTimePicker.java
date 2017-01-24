package com.example.administrator.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by Administrator on 2017/1/19.
 */

public class AppointmentTimePicker extends LinearLayout {

    private NumberPickerView mDatePicker;
    private NumberPickerView mTimerPicker;


    public AppointmentTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_time_picker, this);

        mDatePicker = (NumberPickerView) view.findViewById(R.id.picker_date);
        mTimerPicker = (NumberPickerView) view.findViewById(R.id.picker_time);


    }
}
