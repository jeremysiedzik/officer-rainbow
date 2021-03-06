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

//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;


public class ProbationEndCountdown extends AppCompatActivity {
    CountDownTimer mCountDownTimer;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    TextView mTextView;
    TextView raw_probation_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        raw_probation_date = (TextView) findViewById(R.id.raw_end_probation_date);
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


        Button buttonnext = (Button)findViewById(R.id.buttonlast);
        assert buttonnext != null;

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(ProbationEndCountdown.this, UI.class);
                startActivity(qoneintent);
            }
        });


        mTextView = (TextView) findViewById(R.id.counter);
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
                mTextView.setText(DateUtils.formatElapsedTime(0));
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
                mTextView.setText(time.toString());

            }
        }.start();
    }
}