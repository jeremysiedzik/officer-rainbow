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
    public static final String rawconfurl = "http://data.robotagrex.com/onsite-confidence-raw.txt";
    public static final String confnumberurl = "http://data.robotagrex.com/onsite-confidence.txt";
    public static final String confidence_result_push = "confidence_result";
    public static final String confidence_string_push = "confidence_string";
    public static final String confidence_final_push = "confidence_final";

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlString = rawconfurl;
        String urlNumber = confnumberurl;
        String confidence_result = "";
        String confidence_string = "";

        try {
            confidence_string = loadFromNetwork(urlString);
            Log.i(TAG, "Calling loadFromNetwork via Confidence.java with URL = " +urlString);
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }

        try {
            confidence_result = loadFromNetwork(urlNumber);
            Log.i(TAG, "Calling loadFromNetwork via Confidence.java with URL = " +urlNumber);
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //String CONFIDENCE_STRING = sharedpreferences.getString("confidence_string", "");
        //String CONFIDENCE_RESULT = sharedpreferences.getString("confidence_result", "0");
        String ONLINE = "ONLINE";
        String QUALIFIER = "QUALIFIER MATCH";
        String DATE = "DATE MATCH";
        String data_result = sharedpreferences.getString("data_result", "");
        int FINAL_CONFIDENCE = 0;

        if (confidence_string.contains(ONLINE)) {
            FINAL_CONFIDENCE = FINAL_CONFIDENCE + 100;
        }

        if (confidence_string.contains(QUALIFIER)) {
            FINAL_CONFIDENCE = FINAL_CONFIDENCE + 100;
        }

        if (confidence_string.contains(DATE)) {
            FINAL_CONFIDENCE = FINAL_CONFIDENCE + 100;
        }


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(confidence_result_push, confidence_result);
        editor.putString(confidence_string_push, confidence_string);
        editor.putInt(confidence_final_push, FINAL_CONFIDENCE);
        editor.apply();

        System.out.println("Confidence Number from web is " + confidence_result);
        System.out.println("Confidence Number calculated is " +FINAL_CONFIDENCE);
        System.out.print("Confidence String is " + confidence_string);
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
