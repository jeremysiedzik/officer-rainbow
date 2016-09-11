package com.robotagrex.or.officerrainbow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String probation_date = "probation_meeting_date" ;
    public static final String raw_date = "raw_probation_date" ;
    SharedPreferences sharedpreferences;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date_string = String.valueOf(month + 1) + "/" +
                day + "/" + year;
        String raw_date_string = String.valueOf(month + 1) + "." +
                day + "." + year;
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(probation_date, date_string);
        editor.putString(raw_date, raw_date_string);
        editor.apply();
        String txtdate = sharedpreferences.getString("probation_meeting_date", "No date chosen yet");
        TextView txtdate_view=(TextView)getActivity().findViewById(R.id.textView1);
        txtdate_view.setText(txtdate);
    }
}

