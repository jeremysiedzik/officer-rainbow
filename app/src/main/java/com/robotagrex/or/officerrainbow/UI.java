package com.robotagrex.or.officerrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

public class UI extends AppCompatActivity {

    CountDownTimer probation_end;
    CountDownTimer probation_meet;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Handler mHandler = new Handler();
    TextView color_choice_heading,color_choice,daily_colors_string_heading,image_store_heading,marquee;
    TextView alarm_state_notify,alarm_state_email,alarm_state_sms,daily_colors_string,probation_officer_name;
    TextView alarmprompt,probation_end_date_heading,probation_meeting_date_heading,call_probation_heading;
    TextView probation_end_counter,probation_meeting_counter,raw_end_probation_date,raw_meeting_probation_date;
    private MediaPlayer mediaPlayer;
    ProgressDialog mProgressDialog;
    Context context = getApplication();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        marquee = (TextView)findViewById(R.id.mywidget);

        Button buttonnext = (Button)findViewById(R.id.buttonlast);
        assert buttonnext != null;

        Button button_call_probation = (Button)findViewById(R.id.button_call_probation);
        assert button_call_probation != null;

        final ImageButton listen_star = (ImageButton)findViewById(R.id.listen_star);
        assert listen_star != null;

        RingProgressBar progress_bar_ring = (RingProgressBar)findViewById(R.id.progress_bar_ring);
        assert progress_bar_ring != null;

        probation_officer_name = (TextView)findViewById(R.id.probation_officer_name);
        final String probation_officer_name_stored = sharedpreferences.getString("officerName", "");
        if((probation_officer_name_stored.length() != 0)) {
            probation_officer_name.setText(probation_officer_name_stored);
        }

        call_probation_heading = (TextView)findViewById(R.id.call_probation_heading);
        daily_colors_string_heading = (TextView)findViewById(R.id.daily_colors_heading);
        daily_colors_string = (TextView)findViewById(R.id.daily_colors_string);
        color_choice_heading = (TextView)findViewById(R.id.color_choice_heading);

        color_choice = (TextView)findViewById(R.id.color_choice);
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

        image_store_heading = (TextView)findViewById(R.id.image_store_heading);

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

        call_probation_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationContact.class);
                startActivity(qoneintent);
            }
        });

        probation_officer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationContact.class);
                startActivity(qoneintent);
            }
        });

        button_call_probation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());

                String probation_officer_number = sharedpreferences.getString("officerNumber","");

                if (probation_officer_number.length() != 0) {
                    dialContactPhone(probation_officer_number);
                } else { Toast toast= Toast.makeText(getApplicationContext(),
                        "Please set probation officer contact info.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show(); }
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50); //You can manage the time of the blink with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(20);
                call_probation_heading.startAnimation(anim);
                probation_officer_name.startAnimation(anim);
            }
        });

        color_choice_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        color_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarmprompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        raw_end_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_date_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_meeting_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        raw_meeting_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_meeting_date_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        image_store_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, ImageView.class);
                startActivity(qoneintent);
            }
        });

        progress_bar_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
                Intent qoneintent = new Intent(UI.this, DataTest.class);
                startActivity(qoneintent);
            }
        });

        daily_colors_string_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
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

                checkdailycolors(getApplicationContext());
            }
        });

        daily_colors_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudio(getApplication());
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

                Toast toast = Toast.makeText(getApplicationContext(),
                        "The daily colors have been checked!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                //Context context = getApplication();
                //Intent service = new Intent(context, WebSitechecker.class);
                //context.startService(service);
                checkdailycolors(getApplicationContext());
            }
        });

        listen_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen_star.setImageResource(android.R.drawable.star_on);
                Context context = getApplication();
                AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                if(mediaPlayer!=null) {
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer=null;
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        listen_star.setImageResource(android.R.drawable.star_off);
                }

                else {
                    System.out.println("audio start button pressed - getting focus");
                    boolean gotFocus = requestAudioFocusForMyApp(getApplication());
                    if (gotFocus) {
                        new asyncURLaudio().execute();
                    }
                }
            }
        });


        int progress = 78;
        progress_bar_ring.setProgress(progress);
        //mRingProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

          //  @Override
           // public void progressToComplete() {
                // Progress reaches the maximum callback default Max value is 100
             //   Toast.makeText(UserInterface.this, "complete", Toast.LENGTH_SHORT).show();
            //}
        //});
    }
    private boolean requestAudioFocusForMyApp(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        // Request audio focus for playback
        int result = am.requestAudioFocus(null,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d("AudioFocus", "Audio focus received");
            return true;
        } else {
            Log.d("AudioFocus", "Audio focus NOT received");
            return false;
        }
    }

    void releaseAudioFocusForMyApp(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
        ImageButton listen_star = (ImageButton)findViewById(R.id.listen_star);
        assert listen_star != null;
        listen_star.setImageResource(android.R.drawable.star_off);
    }

    void checkdailycolors(final Context uicontext) {
        Intent service = new Intent(uicontext, WebSitechecker.class);
        uicontext.startService(service);
    }

    void stopaudio(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying())
                mediaPlayer.pause();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        releaseAudioFocusForMyApp(getApplication());
    }

    private void dialContactPhone(final String phoneNumber) {
        try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        } catch (Exception e) {
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Telephone service not found. Please make sure this device can make calls.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    class asyncURLaudio extends AsyncTask<Void, Void, Void> {
    Context context = getApplication();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(UI.this);
            mProgressDialog.setTitle("Onsite Voice Message");
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
                final AudioManager am = (AudioManager)context.getSystemService(AUDIO_SERVICE);
                final int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                System.out.println("audio start button pressed - GOT FOCUS - ");
                Uri myUri = Uri.parse("http://pots.robotagrex.com/onsite.flac");
                //final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));

                try {
                    if(mediaPlayer != null){
                        mediaPlayer.pause();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    mediaPlayer = MediaPlayer.create(getApplicationContext(), myUri);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                }

                catch (IOException e) {
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
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mediaPlayer != null) {
                            mediaPlayer.release();
                        }
                        ImageButton listen_star = (ImageButton)findViewById(R.id.listen_star);
                        assert listen_star != null;
                        listen_star.setImageResource(android.R.drawable.star_off);
                    }
                });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();

        }
    }
}
