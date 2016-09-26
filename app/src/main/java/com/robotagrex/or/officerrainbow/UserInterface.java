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

    TextView counter, raw_probation_date, tv1;

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

        tv1 = (TextView)findViewById(R.id.textcolor1);
        String fillcolor1 = sharedpreferences.getString("color1Key", "blue");
        tv1.setText(fillcolor1);

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

        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, CountdownActivity.class);
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
