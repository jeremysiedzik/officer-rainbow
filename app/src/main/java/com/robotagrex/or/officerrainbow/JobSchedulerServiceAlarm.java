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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class JobSchedulerServiceAlarm extends JobService {

    public static final String TAG = "Officer Rainbow";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            Toast.makeText(getApplicationContext(), "JobServiceAlarm task running", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "JobServiceAlarm idling");

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            String storedDate = sharedpreferences.getString("todays_date_alarm", "07-21-2020");
            boolean checkedtodayalarm = false;
            if (formattedDate.equals(storedDate)) {
                checkedtodayalarm = true;
            }
            System.out.println("checked today = " +checkedtodayalarm);
            System.out.println("formatted date = " +formattedDate);
            System.out.println("stored date = " +storedDate);

            int alarm_int = 5;
            int alarm_time = c.get(Calendar.HOUR_OF_DAY);

            if (alarm_time == alarm_int && (!checkedtodayalarm)) {

                Log.i(TAG, "JobServiceAlarm running - alarm_time variable matches hardcoded int - " + alarm_int);

                Context context = getApplication();

                boolean alarm_enabled = sharedpreferences.getBoolean("droptest_alarm_state", false);

                System.out.println("alarm enabled? = "+alarm_enabled);

                    Intent websitechecker = new Intent(context, WebSitechecker.class);
                    context.startService(websitechecker);

                if (alarm_enabled) {
                    Intent alarm = new Intent(context, Alarm.class);
                    context.startService(alarm);
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
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 33, params ) );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 33 );
        return false;
    }

}