package com.mcn.honeydew.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by gkumar on 9/3/18.
 */

public class TimePickerFragment extends DialogFragment {

   TimePickerDialog.OnTimeSetListener listener;


    public TimePickerFragment() {

    }

    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        listener = ontime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
       /* timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    //toggleState(true);
                }
            }
        });*/

        return timePickerDialog;

    }

}