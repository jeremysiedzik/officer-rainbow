package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;



public class Alarm extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public Alarm() {
        super("SchedulingService");
    }
    public static final String TAG = "Alarm Service";
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onHandleIntent(Intent intent) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String SEARCH_STRING1 = sharedpreferences.getString("color1Key", "huggermugger");
        String SEARCH_STRING2 = sharedpreferences.getString("color2Key", "huggermugger");
        String SEARCH_STRING3 = sharedpreferences.getString("color3Key", "huggermugger");
        String data_result = sharedpreferences.getString("data_result", "");
        String loaded_ok_string = "<----->";

        System.out.println("Search String 1 is "+SEARCH_STRING1);
        System.out.println("Search String 2 is "+SEARCH_STRING2);
        System.out.println("Search String 3 is "+SEARCH_STRING3);

        if(
                (data_result.length() != 0)
                        && (data_result.contains(loaded_ok_string))
                        && data_result.contains(SEARCH_STRING1)
                        || data_result.contains(SEARCH_STRING2)
                        || data_result.contains(SEARCH_STRING3)
                ){
            sendNotification(getString(R.string.notify_found));
            Log.i(TAG, "Found color!!");

            //Context context = getApplicationContext();
            new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                    .setTitle("Your Test Group/Color Was Selected")
                    .setMessage("Silence Alarm?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            sendNotification(getString(R.string.notify_unfound));
            Log.i(TAG, "No color found. :-(");
        }
    }

    private void sendNotification(String msg) {

            NotificationManager mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, Confirm.class), 0);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(getString(R.string.notify_alert))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setContentText(msg);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
