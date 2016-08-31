package com.robotagrex.or.officerrainbow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Main7Activity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String probation_date = "probation_meeting_date" ;
    SharedPreferences sharedpreferences;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_7);

        String txtdate = sharedpreferences.getString(probation_date, "");
        TextView txtdate_view=(TextView)findViewById(R.id.textView1);
        txtdate_view.setText(txtdate);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttonnext = (Button) findViewById(R.id.buttonnext);
        assert buttonnext != null;


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(Main7Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });
    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        String date_string = String.valueOf(month) + "/" +
                day + "/" + year;
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(probation_date, date_string);
        editor.apply();

        Toast.makeText(Main7Activity.this,new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year), Toast.LENGTH_SHORT).show();

    }
}
