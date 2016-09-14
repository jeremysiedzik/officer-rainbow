package com.robotagrex.or.officerrainbow;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class WebDataCheck extends JobService {

    private Handler mJobHandler = new Handler( new Handler.Callback() {
        public static final String TAG = "Officer Rainbow";

        @Override
        public boolean handleMessage(Message msg) {

            Calendar c = Calendar.getInstance();
            int alarm_time = c.get(Calendar.HOUR_OF_DAY);
            Toast.makeText(getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "JobService idling");

            if (alarm_time == 16) {
                Log.i(TAG, "JobService running - it's 5am");

                final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                //final MediaPlayer mPlayer = new MediaPlayer();
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