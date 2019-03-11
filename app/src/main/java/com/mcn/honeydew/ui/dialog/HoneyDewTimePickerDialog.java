package com.mcn.honeydew.ui.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;

public class HoneyDewTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker mTimePicker;

    public HoneyDewTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        super.updateTime(hourOfDay, minuteOfHour);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
