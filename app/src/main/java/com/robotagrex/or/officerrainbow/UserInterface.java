package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
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
    Handler mHandler = new Handler();
    TextView color_choice_heading,color_choice,daily_colors_string_heading;
    TextView alarm_state_notify,alarm_state_email,alarm_state_sms,daily_colors_string;
    TextView alarmprompt,probation_end_date_heading,probation_meeting_date_heading;
    TextView probation_end_counter,probation_meeting_counter,raw_end_probation_date,raw_meeting_probation_date;
    private MediaPlayer mediaPlayer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinterface);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Button buttonnext = (Button)findViewById(R.id.buttonlast);
        assert buttonnext != null;

        final ImageButton listen_star = (ImageButton)findViewById(R.id.listen_star);
        assert listen_star != null;

        final ImageButton stop_star = (ImageButton)findViewById(R.id.stop_star);
        assert stop_star != null;

        RingProgressBar progress_bar_2 = (RingProgressBar)findViewById(R.id.progress_bar_2);
        assert progress_bar_2 != null;

        daily_colors_string_heading = (TextView)findViewById(R.id.daily_colors_heading);
        daily_colors_string = (TextView)findViewById(R.id.daily_colors_string);
        color_choice_heading = (TextView)findViewById(R.id.color_choice_heading);
        color_choice = (TextView)findViewById(R.id.textcolor1);
        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        if((fillcolor1.length() != 0)) {
            color_choice.setText(fillcolor1);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                daily_colors_string = (TextView)findViewById(R.id.daily_colors_string);
                String daily_colors_string_data = sharedpreferences.getString("data_result", "");
//                String runnable_log = "About to run daily_colors_string.setText";
//                System.out.println(runnable_log);
                String loaded_ok_string = "<----->";
                    if((daily_colors_string_data.length() != 0) && (daily_colors_string_data.contains(loaded_ok_string))) {
                        daily_colors_string.setText(daily_colors_string_data);
                    }
                        mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.post(runnable);

        alarmprompt = (TextView)findViewById(R.id.alarmprompt);

        alarm_state_notify = (TextView)findViewById(R.id.alarm_state_notify);
        boolean alarm_set_nofity = sharedpreferences.getBoolean("droptest_alarm_state", false);
        if (alarm_set_nofity) {
            alarm_state_notify.setText(getString(R.string.alarm_enabled_text));}
            else {
                alarm_state_notify.setText(getString(R.string.alarm_disabled_text));
            }

        alarm_state_sms = (TextView)findViewById(R.id.alarm_state_sms);
        boolean alarm_set_sms = sharedpreferences.getBoolean("droptest_sms_state", false);
        if (alarm_set_sms) {
            alarm_state_sms.setText(getString(R.string.sms_enabled_text));}
        else {
            alarm_state_sms.setText(getString(R.string.sms_disabled_text));
        }

        alarm_state_email = (TextView)findViewById(R.id.alarm_state_email);
        boolean alarm_set_email = sharedpreferences.getBoolean("droptest_email_state", false);
        if (alarm_set_email) {
            alarm_state_email.setText(getString(R.string.email_enabled_text));}
        else {
            alarm_state_email.setText(getString(R.string.email_disabled_text));
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

        class asyncAudioURLstop extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                if(mediaPlayer!=null) {
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer=null;
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                //final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                //final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                //mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    //@Override
                    //public void onCompletion(MediaPlayer mp) {
                      //  mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                      //      if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                //Toast toast= Toast.makeText(getApplicationContext(),
                                //        "mediaPlayer not null and is playing - stopping", Toast.LENGTH_LONG);
                                //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                                //toast.show();
                       //         mediaPlayer.release();
                       //     }
                  //  }
                //});
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                stop_star.setImageResource(android.R.drawable.btn_star_big_off);
                listen_star.setImageResource(android.R.drawable.star_off);
            }
        }

        class asyncAudioURL extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                    //final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    //final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                    Uri myUri = Uri.parse("http://pots.robotagrex.com/onsite.flac");
                    //final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), myUri);
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(getApplicationContext(), myUri);
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    mediaPlayer.setOnPreparedListener(
                            new MediaPlayer.OnPreparedListener() {
                                public void onPrepared(MediaPlayer player) {
                                    mediaPlayer.start();
                                }
                            });


                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                listen_star.setImageResource(android.R.drawable.star_off);
                            }
                        }
                    });
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Set title into TextView
                //TextView txttitle = (TextView) findViewById(R.id.titletxt);
                //assert txttitle != null;
                //txttitle.setText(title);
                //mProgressDialog.dismiss();
            }
        }

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

        alarm_state_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_email.setOnClickListener(new View.OnClickListener() {
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

        daily_colors_string_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AudioManager mAudioManager_beep = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager_beep.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager_beep.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //debug option - System.out.println("beep should play");
                mPlayer.setOnPreparedListener(
                        new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer player) {
                                mPlayer.start();
                            }
                        });

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mp != null) {
                            mp.release();
                        }
                    }
                });

                Toast toast= Toast.makeText(getApplicationContext(),
                        "The daily colors have been checked!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                Context context = getApplication();
                Intent service = new Intent(context, WebSitechecker.class);
                context.startService(service);
            }
        });

        daily_colors_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AudioManager mAudioManager_beep = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager_beep.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager_beep.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                // debug option - System.out.println("beep should play");
                mPlayer.setOnPreparedListener(
                        new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer player) {
                                mPlayer.start();
                            }
                        });

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mp != null) {
                            mp.release();
                        }
                    }
                });

                Toast toast= Toast.makeText(getApplicationContext(),
                        "The daily colors have been checked!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                Context context = getApplication();
                Intent service = new Intent(context, WebSitechecker.class);
                context.startService(service);
            }
        });

        listen_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen_star.setImageResource(android.R.drawable.star_on);
                //Toast toast= Toast.makeText(getApplicationContext(),
                //        "Retrieving and Playing Daily Recording", Toast.LENGTH_LONG);
                //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                //toast.show();
                System.out.println("start button pressed - running asyncaudioURL");
                new asyncAudioURL().execute();
            }
        });

        stop_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop_star.setImageResource(android.R.drawable.btn_star_big_on);
                System.out.println("stop button pressed - running asyncaudioURLstop");
                //Toast toast= Toast.makeText(getApplicationContext(),
                //        "Stopping Daily Recording", Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                //toast.show();
                new asyncAudioURLstop().execute();

            }
        });

        int progress = 60;
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
