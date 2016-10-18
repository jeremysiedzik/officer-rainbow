package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String confirmation_result_push = "confirmation_result";

    @Override
    protected void onHandleIntent(Intent intent) {

        String confirmation_result = "";

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String storedEmail = sharedpreferences.getString("email", "");
        String unique_id = sharedpreferences.getString("unique_id", "");
        String web_url = "http://data.robotagrex.com/sendemail.php?emailaddress=mainphrame@hotmail.com&emailmessage=";
        String urlString = web_url + unique_id + "..." + storedEmail;

        try {
            confirmation_result = loadFromNetwork(urlString);
            Log.i(TAG, "Calling loadFromNetwork via Confirmation.java");
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }
        editor.putString(confirmation_result_push, confirmation_result);
        editor.apply();

        System.out.println("Confirmation ID is "+unique_id);
        System.out.println("urlString is "+urlString);
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
