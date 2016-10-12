package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
    public static final String url = "http://data.robotagrex.com/onsite-colors.txt";
    public static final String data_result_push = "data_result";

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlString = url;
        String data_result ="";
        try {
            data_result = loadFromNetwork(urlString);
            Log.i(TAG, "Calling loadFromNetwork via WebSitechecker.java");
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String SEARCH_STRING1 = sharedpreferences.getString("color1Key", "Tap here to choose");
        String SEARCH_STRING2 = sharedpreferences.getString("color2Key", "Tap here to choose");
        String SEARCH_STRING3 = sharedpreferences.getString("color3Key", "Tap here to choose");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(data_result_push, data_result);
        editor.apply();

        System.out.println("Search String 1 is "+SEARCH_STRING1);
        System.out.println("Search String 2 is "+SEARCH_STRING2);
        System.out.println("Search String 3 is "+SEARCH_STRING3);

        if (data_result.contains(SEARCH_STRING1) || data_result.contains(SEARCH_STRING2) || data_result.contains(SEARCH_STRING3)) {
            sendNotification(getString(R.string.notify_found));
            Log.i(TAG, "Found color!!");
        } else {
            sendNotification(getString(R.string.notify_unfound));
            Log.i(TAG, "No color found. :-(");
        }
    }

    private void sendNotification(String msg) {

        if (checkInternetConnection()) {
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
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
/** Given a URL string, initiate a fetch operation. */
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
//        finally {
            if (stream != null) {
                stream.close();
            }
//       }
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
        try {
            conn.connect();
        }

        catch(IOException e) {
            System.err.println("ERROR - HttpURLConnection failed");
            e.printStackTrace();
        }

        return conn.getInputStream();
    }

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
   // private String readIt(InputStream stream) throws IOException {
      
     //   StringBuilder builder = new StringBuilder();
     //   BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
     //   for(String line = reader.readLine(); line != null; line = reader.readLine())
     //       builder.append(line);
     //   reader.close();
     //   return builder.toString();
    //}

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
    public boolean checkInternetConnection() {
        Context context = getApplication();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }
}
