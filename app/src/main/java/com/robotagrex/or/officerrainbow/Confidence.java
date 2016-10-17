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

public class Confidence extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public Confidence() {
        super("SchedulingService");
    }
    public static final String TAG = "Confidence Check";
    public static final String confnumberurl = "http://data.robotagrex.com/onsite-confidence.txt";
    public static final String confidence_result_push = "confidence_result";

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlNumber = confnumberurl;
        String confidence_result = "";

        try {
            confidence_result = loadFromNetwork(urlNumber);
            Log.i(TAG, "Calling loadFromNetwork via Confidence.java with URL = " +urlNumber);
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(confidence_result_push, confidence_result);
        editor.apply();

        System.out.println("Confidence Number from web is " + confidence_result);
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
