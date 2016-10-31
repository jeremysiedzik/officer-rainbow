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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SendEmail extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public SendEmail() {
        super("SchedulingService");
    }
    public static final String TAG = "Email Alert";

    @Override
    protected void onHandleIntent(Intent intent) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String emailmsg1 = sharedpreferences.getString("email_MSG1", "");
        String emailmsg2 = sharedpreferences.getString("email_MSG2", "");
        String emailmsg3 = sharedpreferences.getString("email_MSG3", "");
        String contact1 = sharedpreferences.getString("email1Key", "");
        String contact2 = sharedpreferences.getString("email2Key", "");
        String contact3 = sharedpreferences.getString("email3Key", "");

        String SEARCH_STRING1 = sharedpreferences.getString("color1Key", "");
        String SEARCH_STRING2 = sharedpreferences.getString("color2Key", "");
        String SEARCH_STRING3 = sharedpreferences.getString("color3Key", "");
        String data_result = sharedpreferences.getString("data_result", "");
        String loaded_ok_string = "<----->";

        emailmsg1 = emailmsg1.replace(" ", "%20");
        emailmsg2 = emailmsg2.replace(" ", "%20");
        emailmsg3 = emailmsg3.replace(" ", "%20");

        String web_url_start = "http://data.robotagrex.com/sendemail.php?emailaddress=";
        String web_url_end = "&emailmessage=";

        String emailtrigger1 = web_url_start + contact1 + web_url_end + emailmsg1;
        String emailtrigger2 = web_url_start + contact2 + web_url_end + emailmsg2;
        String emailtrigger3 = web_url_start + contact3 + web_url_end + emailmsg3;

        if(
                (data_result.length() != 0)
                        && (data_result.contains(loaded_ok_string))
                        && data_result.contains(SEARCH_STRING1)
                        || data_result.contains(SEARCH_STRING2)
                        || data_result.contains(SEARCH_STRING3)
                ){
                try {
                    if ((emailtrigger1.contains("@")) && (emailmsg1.length() != 0)) {
                        loadFromNetwork(emailtrigger1);
                    }

                    if ((emailtrigger2.contains("@")) && (emailmsg2.length() != 0)) {
                        loadFromNetwork(emailtrigger2);
                    }

                    if ((emailtrigger3.contains("@")) && (emailmsg3.length() != 0)) {
                        loadFromNetwork(emailtrigger3);
                    }

                    Log.i(TAG, "Calling loadFromNetwork via SendEmail.java");
                    System.out.println("SendEmail 1 url is "+emailtrigger1);
                    System.out.println("SendEmail 2 url is "+emailtrigger2);
                    System.out.println("SendEmail 3 url is "+emailtrigger3);
                } catch (IOException e) {
                    Log.i(TAG, getString(R.string.connection_error));
                }
            }
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
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("todays_date_email", formattedDate);
            editor.apply();
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
