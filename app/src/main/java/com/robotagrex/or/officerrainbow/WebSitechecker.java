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
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    // The string the app searches for in the Google home page content. If the app finds 
    // the string, it indicates the presence of a doodle.
    //String SEARCH_STRING = sharedpreferences.getString("color1Key", "blue");
    //public static final String SEARCH_STRING = fillcolor1;
    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    public static final String url = "http://feed.robotagrex.com/onsite-colors.txt";

    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // The URL from which to fetch content.
        String urlString = url;
    
        String result ="";
        
        // Try to connect to the Google homepage and download content.
        try {
            result = loadFromNetwork(urlString);
            Log.i(TAG, "Calling loadFromNetwork");
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }
    
        // If the app finds the string "doodle" in the Google home page content, it
        // indicates the presence of a doodle. Post a "Doodle Alert" notification.
        String SEARCH_STRING = sharedpreferences.getString("color1Key", "blue");
        if (result.contains(SEARCH_STRING)) {
            sendNotification(getString(R.string.doodle_found));
            Log.i(TAG, "Found color!!");
        } else {
            sendNotification(getString(R.string.no_doodle));
            Log.i(TAG, "No color found. :-(");
        }
    }
    
    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
    
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, OfficerRainbow.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(getString(R.string.doodle_alert))
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//
    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
      
        try {
            Log.i(TAG, "About to run downoadUrl");
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
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
        conn.connect();
        return conn.getInputStream();
    }

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
    private String readIt(InputStream stream) throws IOException {
      
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine()) 
            builder.append(line);
        reader.close();
        return builder.toString();
    }
}
