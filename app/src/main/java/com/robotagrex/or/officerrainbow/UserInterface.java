package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class UserInterface extends AppCompatActivity {

    CountDownTimer probation_end;
    CountDownTimer probation_meet;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    TextView color_choice_heading,color_choice;
    TextView alarmprompt,alarmstate,probation_end_date_heading,probation_meeting_date_heading;
    TextView probation_end_counter,probation_meeting_counter,raw_end_probation_date,raw_meeting_probation_date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinterface);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttonnext = (Button)findViewById(R.id.buttonlast);
        assert buttonnext != null;

        RingProgressBar progress_bar_2 = (RingProgressBar)findViewById(R.id.progress_bar_2);
        assert progress_bar_2 != null;

        color_choice_heading = (TextView)findViewById(R.id.color_choice_heading);
        color_choice = (TextView)findViewById(R.id.textcolor1);
        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        if((fillcolor1.length() != 0)) {
            color_choice.setText(fillcolor1);
        }

        alarmprompt = (TextView)findViewById(R.id.alarmprompt);
        alarmstate = (TextView)findViewById(R.id.alarmstate);
        boolean alarm_state = sharedpreferences.getBoolean("alarm_state", false);
        if (alarm_state) {
            alarmstate.setText(getString(R.string.alarm_enabled_text));}
            else {
                alarmstate.setText(getString(R.string.alarm_disabled_text));
            }

        probation_meeting_date_heading = (TextView)findViewById(R.id.probation_meeting_date_heading);
        probation_end_date_heading = (TextView)findViewById(R.id.probation_end_date_heading);

        probation_meeting_counter = (TextView)findViewById(R.id.probation_meeting_counter);
        probation_end_counter = (TextView)findViewById(R.id.probation_end_counter);

        raw_end_probation_date = (TextView) findViewById(R.id.raw_end_probation_date);
        String raw_end_probation_txt = sharedpreferences.getString("probation_end_date", "07.21.2020");
        raw_end_probation_date.setText(raw_end_probation_txt);

        raw_meeting_probation_date = (TextView) findViewById(R.id.raw_meeting_probation_date);
        String raw_meeting_probation_txt = sharedpreferences.getString("probation_meeting_date", "07.21.2020");
        raw_meeting_probation_date.setText(raw_meeting_probation_txt);

        long millisStart = Calendar.getInstance().getTimeInMillis();

        SimpleDateFormat end_date = new SimpleDateFormat("MM.dd.yyyy", Locale.US);
        Date new_end_date = null;
        try {
            new_end_date = end_date.parse(raw_end_probation_txt);
        } catch (ParseException end_date_error) {
            end_date_error.printStackTrace();
        }
        assert new_end_date != null;
        long probation_ending = new_end_date.getTime();
        long initial_ending = probation_ending - millisStart;

        SimpleDateFormat meeting_date = new SimpleDateFormat("MM.dd.yyyy", Locale.US);
        Date new_meeting_date = null;
        try {
            new_meeting_date = meeting_date.parse(raw_meeting_probation_txt);
        } catch (ParseException meeting_date_error) {
            meeting_date_error.printStackTrace();
        }
        assert new_meeting_date != null;
        long probation_meeting = new_meeting_date.getTime();
        long initial_meeting = probation_meeting - millisStart;

        probation_end = new CountDownTimer(initial_ending, 1000) {
            StringBuilder time = new StringBuilder();
            @Override
            public void onFinish() {
                probation_end_counter.setText(DateUtils.formatElapsedTime(0));
                //mTextView.setText("Times Up!");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                time.setLength(0);
                // Use days if appropriate
                if(millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                    long count = millisUntilFinished / DateUtils.DAY_IN_MILLIS;
                    if(count > 1)
                        time.append(count).append(" days ");
                    else
                        time.append(count).append(" day ");

                    millisUntilFinished %= DateUtils.DAY_IN_MILLIS;
                }

                time.append(DateUtils.formatElapsedTime(Math.round(millisUntilFinished / 1000d)));
                probation_end_counter.setText(time.toString());

            }
        }.start();

        probation_meet = new CountDownTimer(initial_meeting, 1000) {
            StringBuilder time = new StringBuilder();
            @Override
            public void onFinish() {
                probation_meeting_counter.setText(DateUtils.formatElapsedTime(0));
                //mTextView.setText("Times Up!");
            }

            @Override
            public void onTick(long millisUntilFinished) {
                time.setLength(0);
                // Use days if appropriate
                if(millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                    long count = millisUntilFinished / DateUtils.DAY_IN_MILLIS;
                    if(count > 1)
                        time.append(count).append(" days ");
                    else
                        time.append(count).append(" day ");

                    millisUntilFinished %= DateUtils.DAY_IN_MILLIS;
                }

                time.append(DateUtils.formatElapsedTime(Math.round(millisUntilFinished / 1000d)));
                probation_meeting_counter.setText(time.toString());

            }
        }.start();

        color_choice_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        color_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        alarmstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarmprompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        raw_end_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_date_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_meeting_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        raw_meeting_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_meeting_date_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        progress_bar_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, DataTest.class);
                startActivity(qoneintent);
            }
        });

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, OfficerRainbow.class);
                startActivity(qoneintent);
            }
        });

        // Set the progress bar's progress
        int progress = 50;
        progress_bar_2.setProgress(progress);
        //mRingProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

          //  @Override
           // public void progressToComplete() {
                // Progress reaches the maximum callback default Max value is 100
             //   Toast.makeText(UserInterface.this, "complete", Toast.LENGTH_SHORT).show();
            //}
        //});
    }
}
