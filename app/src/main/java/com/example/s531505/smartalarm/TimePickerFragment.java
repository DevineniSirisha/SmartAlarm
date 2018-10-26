package com.example.s531505.smartalarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener)getActivity(),
                hour,minutes, DateFormat.is24HourFormat(getActivity()));
    }
}
