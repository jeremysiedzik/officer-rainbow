package com.robotagrex.or.officerrainbow;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Calendar;

public class MyActivity extends Activity {

    private PendingIntent pendingIntent;
    NotifyAlarmReceiver alarm = new NotifyAlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button buttonnext = (Button) findViewById(R.id.buttonnext);
        assert buttonnext != null;

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(MyActivity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MyActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MyActivity.this, 0, alarmIntent, 0);

        findViewById(R.id.stopAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.startAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUser();
            }
        });
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startUser() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval_set = 8000;

        /* Set the alarm to start at plus one minute from now*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int sethourofday = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int setminuteofday = Calendar.getInstance().get(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, sethourofday);
        calendar.set(Calendar.MINUTE, setminuteofday + 1);

        /* Repeating on every x minutes interval */
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval_set, pendingIntent);

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

}