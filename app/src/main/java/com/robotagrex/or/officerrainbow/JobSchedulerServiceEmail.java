package com.robotagrex.or.officerrainbow;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class JobSchedulerServiceEmail extends JobService {

    public static final String TAG = "Officer Rainbow";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            Calendar c = Calendar.getInstance();
            int alarm_time = c.get(Calendar.HOUR_OF_DAY);
            Toast.makeText(getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "JobService idling");
            int alarm_int = 5;

            if (alarm_time == alarm_int) {

                Log.i(TAG, "JobService running - alarm_time variable matches hardcoded int - " + alarm_int);

                Context context = getApplication();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                boolean sms_enabled = sharedpreferences.getBoolean("droptest_sms_state", false);
                boolean alarm_enabled = sharedpreferences.getBoolean("droptest_alarm_state", false);
                boolean email_enabled = sharedpreferences.getBoolean("droptest_email_state", false);

                System.out.println("sms enabled? = "+sms_enabled);
                System.out.println("alarm enabled? = "+alarm_enabled);
                System.out.println("email enabled? = "+email_enabled);

                    Intent websitechecker = new Intent(context, WebSitechecker.class);
                    context.startService(websitechecker);

                if (alarm_enabled) {
                    Intent alarm = new Intent(context, Alarm.class);
                    context.startService(alarm);
                }

                if (sms_enabled) {
                    Intent sendtext = new Intent(context, SendText.class);
                    context.startService(sendtext);
                }

                if (email_enabled) {
                    Intent sendemail = new Intent(context, SendEmail.class);
                    context.startService(sendemail);
                }

                // code block below for heartbeat 'beep'
                //final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                //final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                //final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                //mPlayer.setOnPreparedListener(
                        //new MediaPlayer.OnPreparedListener() {
                        //    public void onPrepared(MediaPlayer player) {
                        //        mPlayer.start();
                        //    }
                        //});


                //mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               //     @Override
                //    public void onCompletion(MediaPlayer mp) {
                //        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                //        if (mp != null) {
                //            mp.release();
                //        }
                //    }
                //});
                // code block above for heartbeat 'beep'
            }
                jobFinished((JobParameters) msg.obj, false);
                return true;
        }

    } );

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return false;
    }
}