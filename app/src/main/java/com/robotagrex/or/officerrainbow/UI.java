package com.robotagrex.or.officerrainbow;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class UI extends AppCompatActivity {

    CountDownTimer probation_end;
    CountDownTimer probation_meet;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    Handler mHandler = new Handler();
    TextView color_choice_heading, color_choice_1, daily_colors_string_heading, marquee;
    TextView alarm_state_notify, alarm_state_email, alarm_state_sms, daily_colors_string, probation_officer_name;
    TextView alarmprompt, probation_end_date_heading, probation_meeting_date_heading, call_probation_heading;
    TextView probation_end_counter, probation_meeting_counter, raw_end_probation_date, raw_meeting_probation_date;
    TextView color_choice_2, color_choice_3, confidence_header, notification_message_heading, debug_heading;
    TextView sms_notification1, sms_notification2, sms_notification3, email_msg_header, sms_msg_header;
    TextView email_notification1, email_notification2, email_notification3, listen_colors_heading;
    String debug = "off";
    private MediaPlayer mPlayerBeep;
    private MediaPlayer mPlayerURL;
    ProgressDialog mProgressDialog;

    Context context = getApplication();
    public static final String marquee_link_push = "marquee_link";
    public static final String marquee_key_push = "marquee_key";
    public static final String marquee_description_push = "marquee_description";
    public static final String app_title_push = "app_title";
    public static final String device_ID = "unique_id";
    public static final String device_email = "email";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.ui);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        marquee = (TextView) findViewById(R.id.mywidget);

        Button buttonnext = (Button) findViewById(R.id.buttonlast);
        assert buttonnext != null;

        Button button_call_probation = (Button) findViewById(R.id.button_call_probation);
        assert button_call_probation != null;

        final RingProgressBar progress_bar_ring = (RingProgressBar) findViewById(R.id.progress_bar_ring);
        assert progress_bar_ring != null;
        confidence_header = (TextView) findViewById(R.id.confidence_header);

        if (checkInternetConnection()) {
            checkdailycolors(getApplicationContext());
        }

        String device_id_number = getDeviceId(getApplicationContext());
        System.out.println("--------------------" + device_id_number + "-----------------------");
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context = getApplicationContext();
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                editor.putString(device_email, possibleEmail);
                editor.apply();
                String storedEmail = sharedpreferences.getString("email", "");
                System.out.println("stored email = " +storedEmail);
            }
        }

        editor.putString(device_ID, device_id_number);
        editor.apply();

        probation_officer_name = (TextView)findViewById(R.id.probation_officer_name);
        final String probation_officer_name_stored = sharedpreferences.getString("officerName", "");
        if((probation_officer_name_stored.length() != 0)) {
            probation_officer_name.setText(probation_officer_name_stored);
        }

        email_notification1 = (TextView)findViewById(R.id.email_notification1);
        email_notification2 = (TextView)findViewById(R.id.email_notification2);
        email_notification3 = (TextView)findViewById(R.id.email_notification3);
        sms_notification1 = (TextView)findViewById(R.id.sms_notification1);
        sms_notification2 = (TextView)findViewById(R.id.sms_notification2);
        sms_notification3 = (TextView)findViewById(R.id.sms_notification3);
        email_msg_header = (TextView)findViewById(R.id.email_msg_header);
        sms_msg_header = (TextView)findViewById(R.id.sms_msg_header);

        String fillnotify1 = sharedpreferences.getString("email_MSG1", "Tap here to add");
        String fillnotify2 = sharedpreferences.getString("email_MSG2", "Tap here to add");
        String fillnotify3 = sharedpreferences.getString("email_MSG3", "Tap here to add");
        String fillnotify4 = sharedpreferences.getString("sms_MSG1", "Tap here to add");
        String fillnotify5 = sharedpreferences.getString("sms_MSG2", "Tap here to add");
        String fillnotify6 = sharedpreferences.getString("sms_MSG3", "Tap here to add");

        String fillname1 = sharedpreferences.getString("contact_name_1", "");
        String fillname2 = sharedpreferences.getString("contact_name_2", "");
        String fillname3 = sharedpreferences.getString("contact_name_3", "");
        String fillemail1 = sharedpreferences.getString("email1Key", "");
        String fillemail2 = sharedpreferences.getString("email2Key", "");
        String fillemail3 = sharedpreferences.getString("email3Key", "");

        if (fillemail1.length()!=0 && fillnotify1.length()!=0) {
            email_notification1.setText(fillemail1 + " - " + fillnotify1);
        }

        if (fillemail2.length()!=0 && fillnotify2.length()!=0) {
            email_notification2.setText(fillemail2 + " - " + fillnotify2);
        }

        if (fillemail3.length()!=0 && fillnotify3.length()!=0) {
            email_notification3.setText(fillemail3 + " - " + fillnotify3);
        }

        if (fillname1.length()!=0 && fillnotify4.length()!=0) {
            sms_notification1.setText(fillname1 + " - " + fillnotify4);
        }

        if (fillname2.length()!=0 && fillnotify5.length()!=0) {
            sms_notification2.setText(fillname2 + " - " + fillnotify5);
        }

        if (fillname3.length()!=0 && fillnotify6.length()!= 0) {
            sms_notification3.setText(fillname3 + " - " + fillnotify6);
        }

        listen_colors_heading = (TextView)findViewById(R.id.listen_colors_heading);
        call_probation_heading = (TextView)findViewById(R.id.call_probation_heading);
        notification_message_heading = (TextView)findViewById(R.id.notification_message_heading);
        daily_colors_string_heading = (TextView)findViewById(R.id.daily_colors_heading);
        daily_colors_string = (TextView)findViewById(R.id.daily_colors_string);
        color_choice_heading = (TextView)findViewById(R.id.color_choice_heading);
        debug_heading = (TextView)findViewById(R.id.debug_heading);

        color_choice_1 = (TextView)findViewById(R.id.color_choice_1);
        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        if((fillcolor1.length() != 0)) {
            color_choice_1.setText(fillcolor1);
        }

        color_choice_2 = (TextView)findViewById(R.id.color_choice_2);
        String fillcolor2 = sharedpreferences.getString("color2Key", "");
        if((fillcolor2.length() != 0)) {
            color_choice_2.setText(fillcolor2);
        }

        color_choice_3 = (TextView)findViewById(R.id.color_choice_3);
        String fillcolor3 = sharedpreferences.getString("color3Key", "");
        if((fillcolor3.length() != 0)) {
            color_choice_3.setText(fillcolor3);
        }

        Runnable server_stat = new Runnable() {
            @Override
            public void run() {
                Intent serverup = new Intent(UI.this, ServerUp.class);
                startService(serverup);

                final boolean server_up = sharedpreferences.getBoolean("web_status_result", false);

                if (!server_up) {
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(20);
                    listen_colors_heading.startAnimation(anim);
                }
                mHandler.postDelayed(this, 60 * 1000);
            }
        };
        mHandler.post(server_stat);

        Runnable colors_from_prefs = new Runnable() {
            @Override
            public void run() {
                daily_colors_string = (TextView)findViewById(R.id.daily_colors_string);
                String daily_colors_string_data = sharedpreferences.getString("data_result", "");
                String loaded_ok_string = "<----->";
                    if((daily_colors_string_data.length() != 0) && (daily_colors_string_data.contains(loaded_ok_string))) {
                        daily_colors_string.setText(daily_colors_string_data);
                    }
                        mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.post(colors_from_prefs);

        Runnable confidence_from_prefs = new Runnable() {
            @Override
            public void run() {
                String daily_confidence_raw = sharedpreferences.getString("confidence_result", "0");
                String daily_confidence_string_to_int = daily_confidence_raw.replaceAll("\\s+","0");
                if (checkInternetConnection()) {
                    try {
                        int daily_confidence_string_data = Integer.parseInt(daily_confidence_string_to_int);
                        if(daily_confidence_string_data > 0) {
                            progress_bar_ring.setProgress(daily_confidence_string_data);
                        }
                    }

                    catch(NumberFormatException e) {
                        System.out.println("Crashed in runnable - confidence from prefs integer");
                        e.printStackTrace();
                    }
                }

                mHandler.postDelayed(this, 10000);
            }
        };
        mHandler.post(confidence_from_prefs);

       Runnable rss_setter = new Runnable() {
            @Override
            public void run() {
               if (checkInternetConnection()) {
                    new asyncxml().execute();
                }

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                String marquee_key = sharedpreferences.getString("marquee_key", "");
                String marquee_description = sharedpreferences.getString("marquee_description", "");
                String app_title = sharedpreferences.getString("app_title", "Officer Rainbow");

                if (marquee_key.contains("321654987")) {
                    marquee.setText(marquee_description);
                } else {
                   marquee.setText("");
                }

                if(getSupportActionBar() != null){
                    System.out.println(app_title);
                    getSupportActionBar().setTitle(app_title);
                }

                mHandler.postDelayed(this, 1000 * 30);
            }
        };
        mHandler.post(rss_setter);

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

        //image_store_heading = (TextView)findViewById(R.id.image_store_heading);

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

        final int[] clickcount = {0};
        debug_heading.setText("");

        rlayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                clickcount[0] = clickcount[0] +1;
                if(clickcount[0] == 7)
                {
                    //first time clicked to do this
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "About to set Debug Mode", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                if(clickcount[0] == 8)
                {
                   debug = "on";
                    System.out.println("Debug Enabled - debug string set to "+debug);
                    clickcount[0] = 0;
                    System.out.println("Debug Enabled - clickcount set back to "+clickcount[0]);
                    debug_heading.setText(R.string.debug_enabled);
                }

                try {
                    stopaudioURL(getApplication());
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }
        });

        marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String marquee_key = sharedpreferences.getString("marquee_key", "");
                String marquee_link = sharedpreferences.getString("marquee_link", "");


                if (marquee_link.length() != 0 && marquee_key.contains("321654987") && checkInternetConnection()) {
                    Uri uri = Uri.parse(marquee_link);
                    Intent browse_to_ad = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(browse_to_ad);
                }
                else {
                    toast_internet_down();
                }
            }
        });

        call_probation_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationContact.class);
                startActivity(qoneintent);
            }
        });

        notification_message_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        sms_notification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        sms_notification2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        sms_notification3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        email_notification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        email_notification2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        email_notification3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        email_msg_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });
        sms_msg_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, Notifications.class);
                startActivity(qoneintent);
            }
        });

        probation_officer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationContact.class);
                startActivity(qoneintent);
            }
        });

        button_call_probation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());

                String raw_officer_number = sharedpreferences.getString("officerNumber","");
                String probation_officer_number = raw_officer_number.replace("-", "");

                if (probation_officer_number.length() != 0) {
                    System.out.println(probation_officer_number);
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
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        color_choice_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        color_choice_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        color_choice_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ColorChoice.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarm_state_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        alarmprompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, AlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        raw_end_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_end_date_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationEndAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_meeting_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        raw_meeting_probation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

        probation_meeting_date_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                Intent qoneintent = new Intent(UI.this, ProbationMeetingAlarmSettings.class);
                startActivity(qoneintent);
            }
        });

       // image_store_heading.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
       //         stopaudioURL(getApplication());
       //         Intent qoneintent = new Intent(UI.this, ImageView.class);
       //         startActivity(qoneintent);
       //     }
       // });

        progress_bar_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                if (checkInternetConnection()) {
                    checkdailycolors(getApplicationContext());
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(20);
                    confidence_header.startAnimation(anim);
                } else {
                    toast_internet_down();
                }

            }
        });

        daily_colors_string_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                if (checkInternetConnection()) {
                final AudioManager mAudioManager_beep = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager_beep.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager_beep.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                mPlayerBeep = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                mPlayerBeep.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayerBeep.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mPlayer) {
                                mPlayer.start();
                            }
                        });

                mPlayerBeep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mPlayerBeep) {
                        mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mPlayerBeep != null) {
                            mPlayerBeep.release();
                        }
                    }
                });

                Toast toast = Toast.makeText(getApplicationContext(),
                        "The daily colors have been checked!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                checkdailycolors(getApplicationContext());
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(20);
                    daily_colors_string_heading.startAnimation(anim);
            } else {
                    toast_internet_down();
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(20);
                    daily_colors_string_heading.startAnimation(anim);
                }
            }
        });

        daily_colors_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopaudioURL(getApplication());
                if (checkInternetConnection()) {
                final AudioManager mAudioManager_beep = (AudioManager) getSystemService(AUDIO_SERVICE);
                final int originalVolume = mAudioManager_beep.getStreamVolume(AudioManager.STREAM_MUSIC);
                mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager_beep.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                mPlayerBeep = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier("beep", "raw", getPackageName()));
                mPlayerBeep.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayerBeep.setOnPreparedListener(
                        new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mPlayerBeep) {
                                mPlayerBeep.start();
                            }
                        });

                mPlayerBeep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mPlayerBeep) {
                        mAudioManager_beep.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mPlayerBeep != null) {
                            mPlayerBeep.release();
                        }
                    }
                });

                Toast toast = Toast.makeText(getApplicationContext(),
                        "The daily colors have been checked!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                checkdailycolors(getApplicationContext());
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(20);
                    daily_colors_string.startAnimation(anim);

            } else {
                    toast_internet_down();
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(20);
                    daily_colors_string.startAnimation(anim);
            }
            }
        });

        listen_colors_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickcount[0] = 0;
                stopaudioURL(getApplication());
                boolean gotFocus = requestAudioFocusForMyApp(getApplication());
                if ((gotFocus) && checkInternetConnection()) {
                    System.out.println("audio start button pressed - getting focus");
                    new asyncURLaudio().execute();

                } else {
                    toast_internet_down();
                }
            }
        });
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

    void toast_internet_down() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check your internet connection.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    void releaseAudioFocusForMyApp(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
    }

    void checkdailycolors(final Context uicontext) {
        if (checkInternetConnection()) {
            System.out.println("About to run WebSitechecker.class within checkdailycolors method");
            Intent webservice = new Intent(uicontext, WebSitechecker.class);
            uicontext.startService(webservice);

            System.out.println("About to run Confidence.class within checkdailycolors method");
            Intent confservice = new Intent(uicontext, Confidence.class);
            uicontext.startService(confservice);
        }
        else {
            toast_internet_down();
        }
    }

    void stopaudioURL(final Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        try {
            if(mPlayerURL != null){
                System.out.println("stopping audio - mPlayerURL is NOT null ---------------------------");
                mPlayerURL.pause();
            }
        }

        catch (Exception e) {
            e.printStackTrace();
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

                try {
                    if(mPlayerURL != null){
                        mPlayerURL.pause();
                        mPlayerURL.release();
                        mPlayerURL = null;
                    }

                    mPlayerURL = MediaPlayer.create(getApplicationContext(), myUri);
                    mPlayerURL = new MediaPlayer();
                    mPlayerURL.setDataSource(getApplicationContext(), myUri);
                    mPlayerURL.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayerURL.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mPlayerURL.setOnPreparedListener(
                        new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mPlayerURL) {
                                mPlayerURL.start();
                            }
                        });


                mPlayerURL.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mPlayerURL) {
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        if (mPlayerURL != null) {
                            mPlayerURL.release();
                        }
                    }
                });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();

        }
    }

    public static String getDeviceId(Context context) {
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId != null) {
            return deviceId;
        } else {
            return android.os.Build.SERIAL;
        }
    }

    public boolean checkInternetConnection() {
        Context context = getApplication();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    class asyncxml extends AsyncTask<Void, Void, Void> {
        Context context = getApplication();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("Fetching XML");
            String finalUrl = "http://data.robotagrex.com/onsite-ads.xml";
            HandleXML obj = new HandleXML(finalUrl);
            obj.fetchXML();

            while(true) {
                if (!(obj.parsingComplete)) break;
            }

            String marquee_link = obj.getLink();
            String marquee_key = obj.getTitle();
            String marquee_description = obj.getDescription();
            String app_title = obj.getEditor();

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(marquee_description_push, marquee_description);
            editor.putString(marquee_link_push, marquee_link);
            editor.putString(marquee_key_push, marquee_key);
            editor.putString(app_title_push, app_title);
            editor.apply();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
