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

    CountDownTimer mCountDownTimer;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    TextView counter, raw_probation_date, probation_end_date, color_choice_text, color_choice;
    TextView alarmprompt,alarmstate;

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

        color_choice = (TextView)findViewById(R.id.textcolor1);
        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        color_choice.setText(fillcolor1);

        boolean alarm_state = sharedpreferences.getBoolean("alarm_state", false);

        if (alarm_state) {
            alarmstate.setText(R.string.alarm_enabled_text);
        }
        alarmstate.setText(R.string.alarm_disabled_text);


        probation_end_date = (TextView)findViewById(R.id.probation_end_date);
        color_choice_text = (TextView)findViewById(R.id.color_choice_prompt);
        alarmprompt = (TextView)findViewById(R.id.alarmprompt);
        alarmstate = (TextView)findViewById(R.id.alarmstate);

        raw_probation_date = (TextView) findViewById(R.id.raw_probation_date);
        String raw_probation_txt = sharedpreferences.getString("raw_probation_date", "07.21.2020");
        raw_probation_date.setText(raw_probation_txt);

        SimpleDateFormat f = new SimpleDateFormat("MM.dd.yyyy", Locale.US);
        Date d = null;
        try {
            d = f.parse(raw_probation_txt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert d != null;
        long mEndingTime = d.getTime();

        long millisStart = Calendar.getInstance().getTimeInMillis();

        long mInitialTime = mEndingTime - millisStart;

        // long mInitialTime = DateUtils.DAY_IN_MILLIS * 3 +
        //         DateUtils.HOUR_IN_MILLIS * 9 +
        //         DateUtils.MINUTE_IN_MILLIS * 3 +
        //         DateUtils.SECOND_IN_MILLIS * 42;


        counter = (TextView) findViewById(R.id.counter);
        //Animation anim = new AlphaAnimation(0.0f, 1.0f);
        //anim.setDuration(50); //You can manage the time of the blink with this parameter
        //anim.setStartOffset(20);
        //anim.setRepeatMode(Animation.REVERSE);
        //anim.setRepeatCount(Animation.INFINITE);
        //mTextView.startAnimation(anim);



        mCountDownTimer = new CountDownTimer(mInitialTime, 1000) {
            StringBuilder time = new StringBuilder();
            @Override
            public void onFinish() {
                counter.setText(DateUtils.formatElapsedTime(0));
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
                counter.setText(time.toString());

            }
        }.start();

        color_choice_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        alarmstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarmprompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
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

        raw_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });


        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
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
        int progress = 40;
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
