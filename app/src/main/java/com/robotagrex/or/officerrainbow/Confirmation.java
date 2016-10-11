package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Confirmation extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public Confirmation() {
        super("SchedulingService");
    }
    public static final String TAG = "Confirmation Check";
    public static final String pulled_user_number = "user_number";
    public static final String pulled_user_imei = "user_imei";
    public static final String pulled_user_simSerialNumber = "user_simSerialNumber";
    public static final String confirmation_result_push = "confirmation_result";

    @Override
    protected void onHandleIntent(Intent intent) {

        String confirmation_result = "";

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        final String user_number = tm.getLine1Number();
        final String user_imei = tm.getDeviceId();
        final String user_simSerialNumber = tm.getSimSerialNumber();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String divider = "##";
        String message = divider + user_imei + divider + user_number + divider + user_simSerialNumber;
        String web_url = "http://data.robotagrex.com/sendemail.php?emailaddress=mainphrame@hotmail.com&emailmessage=";
        String urlString = web_url + message;

        try {
            confirmation_result = loadFromNetwork(urlString);
            Log.i(TAG, "Calling loadFromNetwork via Confirmation.java");
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }
        editor.putString(pulled_user_number, user_number);
        editor.putString(pulled_user_imei, user_imei);
        editor.putString(pulled_user_simSerialNumber, user_simSerialNumber);
        editor.putString(confirmation_result_push, confirmation_result);
        editor.apply();

        System.out.println("Confirmation message is "+message);
        System.out.println("urlString is"+urlString);

    }

    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str = "";
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
}
