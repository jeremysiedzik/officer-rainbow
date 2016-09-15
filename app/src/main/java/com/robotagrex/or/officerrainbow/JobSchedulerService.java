package com.robotagrex.or.officerrainbow;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

public class JobSchedulerService extends JobService {

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
            int alarm_int = 10;

            if (alarm_time == alarm_int) {

                Log.i(TAG, "JobService running - alarm_time variable matches hardcoded int - " + alarm_int);

                Context context = getApplication();
                Intent service = new Intent(context, WebSitechecker.class);
                context.startService(service);

                final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mPlayer.setOnPreparedListener(
                        new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer player) {
                                mPlayer.start();
                            }
                        });


                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mp != null) {
                            mp.release();
                        }
                    }
                });
            }

                jobFinished((JobParameters) msg.obj, false);
                return true;
        }

    } );

    @Override
    public boolean onStartJob(JobParameters params ) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob( JobParameters params ) {
        mJobHandler.removeMessages( 1 );
        return false;
    }
}