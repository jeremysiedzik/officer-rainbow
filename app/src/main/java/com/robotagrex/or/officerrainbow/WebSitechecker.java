package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebSitechecker extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public WebSitechecker() {
        super("SchedulingService");
    }
    public static final String TAG = "Color Check Service";
    public static final int NOTIFICATION_ID = 1;
    public static final String data_result_push = "data_result";

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String urlString = sharedpreferences.getString("colors_url", "nothing yet");
        String data_result ="";

        try {
            Log.i(TAG, "Calling loadFromNetwork via WebSitechecker.java");
            data_result = loadFromNetwork(urlString);
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
            System.out.println("no url set yet. urlString set to " +urlString);
        }


        String SEARCH_STRING1 = sharedpreferences.getString("color1Key", "Tap here to choose");
        String SEARCH_STRING2 = sharedpreferences.getString("color2Key", "Tap here to choose");
        String SEARCH_STRING3 = sharedpreferences.getString("color3Key", "Tap here to choose");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(data_result_push, data_result);
        editor.apply();

        System.out.println("Search String 1 is "+SEARCH_STRING1);
        System.out.println("Search String 2 is "+SEARCH_STRING2);
        System.out.println("Search String 3 is "+SEARCH_STRING3);

        if (data_result.contains(SEARCH_STRING1)
                || data_result.contains(SEARCH_STRING2)
                || data_result.contains(SEARCH_STRING3)) {
            sendNotification(getString(R.string.notify_found));
            Log.i(TAG, "Found color!!");
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

    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
        try {
            Log.i(TAG, "About to run downoadUrl");
            stream = downloadUrl(urlString);
            str = readIt_again(stream);
        }
        catch(IOException e) {
            System.err.println("ERROR");
            e.printStackTrace();
        }
            if (stream != null) {
                stream.close();
            }
        return str;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
    
        URL url = new URL(urlString);
        Log.i(TAG, "About to run HttpURLConnection");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        Log.i(TAG, "About to run HttpURLConnection query via conn.connect");
        try {
            conn.connect();
        }

        catch(IOException e) {
            System.err.println("ERROR - HttpURLConnection failed");
            e.printStackTrace();
        }

        return conn.getInputStream();
    }

    private String readIt_again(InputStream stream) throws IOException {
        String newliner = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
          //  System.out.println(line);
            builder.append(line);
            builder.append(newliner);
        }
        reader.close();
        return builder.toString();
    }
}
