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
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c.getTime());
        String storedDate = sharedpreferences.getString("todays_date_alarm", "07-21-2020");
        boolean checkedtodayalarm = false;
        if (formattedDate.equals(storedDate)) {
            checkedtodayalarm = true;
        }

        final Button titlebutton = (Button) findViewById(R.id.titlebutton);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttontest = (Button) findViewById(R.id.buttontest);
        assert buttontest != null;

        final TextView titletxt=(TextView)findViewById(R.id.titletxt);

        if (checkedtodayalarm) {
            Intent qoneintent = new Intent(Confirm.this, UI.class);
            startActivity(qoneintent);
        } else {
            playalarm();
        }

        Runnable confirmation_msg = new Runnable() {
            @Override
            public void run() {
                String confirmation_result = sharedpreferences.getString("confirmation_result", "Click above to confirm");
                //String loaded_ok_string = "Alarm Confirmed";
                if((confirmation_result.length() != 0)) {
                    titletxt.setText(confirmation_result);
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

        assert titlebutton != null;
        titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                stopaudio();
                try {
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    String formattedDate = df.format(c.getTime());
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("todays_date_alarm", formattedDate);
                    editor.apply();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                if (checkInternetConnection()) {
                    new confirmation_task().execute();
                } else {
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
                Intent qoneintent = new Intent(Confirm.this, UI.class);
                startActivity(qoneintent);
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
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check your internet connection.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    private void playalarm() {
        // code block below for heartbeat 'beep'
        final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        final MediaPlayer mPlayer = MediaPlayer.create(context, getResources().getIdentifier("alarm", "raw", getPackageName()));

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer player) {
                        mPlayer.start();
                    }
                });


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            int n = 0;
            @Override
            public void onCompletion(MediaPlayer player) {
                if (n < 10) {
                    mPlayer.start();
                    n++;
                }
            }
        });
        // code block above for heartbeat 'beep'
    }

    void stopaudio() {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(mPlayer!=null) {
            if(mPlayer.isPlaying())
                mPlayer.pause();
            mPlayer.reset();
            mPlayer.release();
            mPlayer=null;
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        releaseAudioFocusForMyApp();
    }

    class confirmation_task extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
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
            String confirmation_result = sharedpreferences.getString("confirmation_result", "Click above to confirm.");
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
