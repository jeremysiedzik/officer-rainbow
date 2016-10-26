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

public class JobSchedulerServiceEmail extends JobService {

    public static final String TAG = "Officer Rainbow";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private Handler mJobHandlerEmail = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            Toast.makeText(getApplicationContext(), "JobServiceEmail task running", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "JobServiceEmail idling");
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            String storedDate = sharedpreferences.getString("todays_date_email", "07-21-2020");
            boolean checkedtodayemail = false;
            if (formattedDate.equals(storedDate)) {
                checkedtodayemail = true;
            }
            System.out.println("checked today = " +checkedtodayemail);
            System.out.println("formatted date = " +formattedDate);
            System.out.println("stored date = " +storedDate);

            int alarm_int = 5;
            int alarm_time = c.get(Calendar.HOUR_OF_DAY);
            if (alarm_time == alarm_int && (!checkedtodayemail)) {

                Log.i(TAG, "JobServiceEmail running - alarm_time variable matches hardcoded int - " + alarm_int);

                Context context = getApplication();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                boolean email_enabled = sharedpreferences.getBoolean("droptest_email_state", false);

                System.out.println("email enabled? = "+email_enabled);

                    Intent websitechecker = new Intent(context, WebSitechecker.class);
                    context.startService(websitechecker);

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
        mJobHandlerEmail.sendMessage( Message.obtain( mJobHandlerEmail, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandlerEmail.removeMessages( 1 );
        return false;
    }
}