package com.robotagrex.or.officerrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Confirm extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Handler mHandler = new Handler();
    TextView titletxt;
    public static final String confirmation_result_push = "confirmation_result";
    Context context = Confirm.this;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);

        mediaPlayer = MediaPlayer.create(context, getResources().getIdentifier("alarm", "raw", getPackageName()));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c.getTime());
        String storedDate = sharedpreferences.getString("todays_date_alarm", "08-01-2000");
        boolean checkedtodayalarm = false;
        if (formattedDate.equals(storedDate)) {
            checkedtodayalarm = true;
        }

        System.out.println("from confirm - checked today = "+checkedtodayalarm);
        System.out.println("from confirm - formatted date = "+storedDate);
        System.out.println("from confirm - stored date = "+storedDate);

        final Button titlebutton = (Button) findViewById(R.id.titlebutton);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final Button buttontest = (Button) findViewById(R.id.buttontest);
        assert buttontest != null;
        buttontest.setVisibility(View.INVISIBLE);

        boolean alarm_enabled = sharedpreferences.getBoolean("droptest_alarm_state", false);

        if ((!checkedtodayalarm) && (alarm_enabled)){
            playalarm();
        } else {
            Intent unconfirmation = new Intent(Confirm.this, UI.class);
            unconfirmation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            unconfirmation.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            unconfirmation.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            System.out.println("about to run UI.class because the alarm was checked");
            startActivity(unconfirmation);
        }

        titletxt = (TextView)findViewById(R.id.titletxt);

        Runnable confirmation_msg = new Runnable() {
            @Override
            public void run() {
                String confirmation_result = sharedpreferences.getString("confirmation_result", "");
                if(confirmation_result.length() != 0) {
                    titletxt.setText(confirmation_result);
                } else {
                    titletxt.setText(R.string.clickabove);
                }
                mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.post(confirmation_msg);

        String app_title = sharedpreferences.getString("app_title", "Officer Rainbow");
        if(getSupportActionBar() != null){
            //if (debug.length() != 0 && debug.contains("on")) {System.out.println(app_title);}
            getSupportActionBar().setTitle(app_title);
        }

        titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    stopaudio(getApplication());
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    String formattedDate = df.format(c.getTime());
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("todays_date_alarm", formattedDate);
                    editor.apply();
                    buttontest.setVisibility(View.VISIBLE);
                } catch (IllegalStateException e) {
                    stopaudio(getApplication());
                    e.printStackTrace();
                }
                if (checkInternetConnection()) {
                    stopaudio(getApplication());
                    new confirmation_task().execute();
                } else {
                    stopaudio(getApplication());
                    toast_internet_down();
                }
                }
        });

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titletxt.setText("");
                String confirmation_result = "Click above to confirm";

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(confirmation_result_push, confirmation_result);
                editor.apply();
                Intent unconfirmation = new Intent(Confirm.this, UI.class);
                unconfirmation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                unconfirmation.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                unconfirmation.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(unconfirmation);
            }
        });
    }

    void releaseAudioFocusForMyApp() {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
    }

    void sendconfirmation() {
       if (checkInternetConnection()) {
            //if (debug.contains("on")) {System.out.println("About to run Confirmation.class within sendconfirmation method");}
            Intent confirmationservice = new Intent(context, Confirmation.class);
            context.startService(confirmationservice);
        }
        else {
            toast_internet_down();
       }
   }

    void toast_internet_down() {
        Toast toast = Toast.makeText(context,
                "Check your internet connection.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    private void playalarm() {

        try {
            if(mediaPlayer != null){
                mediaPlayer.pause();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        final AudioManager mAudioManager_alarm = (AudioManager) getSystemService(AUDIO_SERVICE);
        final int originalVolume = mAudioManager_alarm.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager_alarm.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager_alarm.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer player) {
                        mediaPlayer.start();
                    }
                });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            int n = 0;
            @Override
            public void onCompletion(MediaPlayer player) {
                if (n < 3) {
                    mediaPlayer.start();
                    n++;
                }
                mAudioManager_alarm.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
            }

        });
    }


    void stopaudio(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        System.out.println("about to run mplayer kill");
        try {
            if(mediaPlayer != null){
                System.out.println("stopping audio - mediaPlayer is NOT null ---------------------------");
                mediaPlayer.pause();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        releaseAudioFocusForMyApp();
    }

    class confirmation_task extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            stopaudio(getApplication());
            mProgressDialog = new ProgressDialog(Confirm.this);
            mProgressDialog.setTitle("Confirming Notification");
            mProgressDialog.setMessage("Resetting Alarm");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            sendconfirmation();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String confirmation_result = sharedpreferences.getString("confirmation_result", "Click above to confirm");
            //String loaded_ok_string = "Alarm Confirmed";
                try {
                    titletxt.setText(confirmation_result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //String loaded_ok_string = "Alarm Confirmed";
                //if((confirmation_result.length() != 0) && (confirmation_result.contains(loaded_ok_string))) {
                //    try {
                //        titletxt.setText(confirmation_result);
                //    } catch (Exception e) {
                 //       e.printStackTrace();
                 //   }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            }, 5000);
        }
    }
}
