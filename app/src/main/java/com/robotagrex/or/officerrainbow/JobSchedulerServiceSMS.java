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

public class JobSchedulerServiceSMS extends JobService {

    public static final String TAG = "Officer Rainbow";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private Handler mJobHandlerSMS = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            Toast.makeText(getApplicationContext(), "JobServiceSMS task running", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "JobServiceSMS idling");
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            String storedDate = sharedpreferences.getString("todays_date_sms", "07-21-2020");
            boolean checkedtodaysms = false;
            if (formattedDate.equals(storedDate)) {
                checkedtodaysms = true;
            }
            System.out.println("checked today = " +checkedtodaysms);
            System.out.println("formatted date = " +formattedDate);
            System.out.println("stored date = " +storedDate);

            int alarm_int = 5;
            int alarm_time = c.get(Calendar.HOUR_OF_DAY);
            if (alarm_time == alarm_int && (!checkedtodaysms)) {

                Log.i(TAG, "JobServiceSMS running - alarm_time variable matches hardcoded int - " + alarm_int);

                Context context = getApplication();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                boolean sms_enabled = sharedpreferences.getBoolean("droptest_sms_state", false);

                System.out.println("sms enabled? = "+sms_enabled);

                    Intent websitechecker = new Intent(context, WebSitechecker.class);
                    context.startService(websitechecker);


                if (sms_enabled) {
                    Intent sendtext = new Intent(context, SendText.class);
                    context.startService(sendtext);
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
        mJobHandlerSMS.sendMessage( Message.obtain( mJobHandlerSMS, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandlerSMS.removeMessages( 1 );
        return false;
    }
}