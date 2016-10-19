package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendText extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public SendText() {
        super("SchedulingService");
    }
    public static final String TAG = "Text Alert";

    @Override
    protected void onHandleIntent(Intent intent) {

        String text_result = "";

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String contact1 = sharedpreferences.getString("sms1Key", "empty_string_contact1");
        String contact2 = sharedpreferences.getString("sms2Key", "empty_string_contact2");
        String contact3 = sharedpreferences.getString("sms3Key", "empty_string_contact3");

        String urlString = "http://data.robotagrex.com/sendemail.php?emailaddress=mainphrame@hotmail.com&emailmessage=";

        try {
            sendSMS(contact1);
            sendSMS(contact2);
            sendSMS(contact3);
            text_result = loadFromNetwork(urlString);
            Log.i(TAG, "Calling loadFromNetwork via Confirmation.java");
        } catch (IOException e) {
            Log.i(TAG, getString(R.string.connection_error));
        }

        System.out.println("SMS string is "+text_result);
        System.out.println("urlString is "+urlString);
    }

    private void sendSMS(final String phoneNumber) {
        try {
            String msg1 = sharedpreferences.getString("sms_MSG1", "empty_string_msg1");
            String msg2 = sharedpreferences.getString("sms_MSG2", "empty_string_msg2");
            String msg3 = sharedpreferences.getString("sms_MSG3", "empty_string_msg3");

            if (!msg1.contains("empty_string_msg1")) {
                Intent sms1 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
                sms1.putExtra("sms_body", msg1);
                sms1.putExtra(Intent.EXTRA_TEXT, msg1);
                startActivity(sms1);
            }

            if (!msg2.contains("empty_string_msg2")) {
                Intent sms2 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
                sms2.putExtra("sms_body", msg2);
                sms2.putExtra(Intent.EXTRA_TEXT, msg2);
                startActivity(sms2);
            }

            if (!msg3.contains("empty_string_msg3")) {
                Intent sms3 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
                sms3.putExtra("sms_body", msg3);
                sms3.putExtra(Intent.EXTRA_TEXT, msg3);
                startActivity(sms3);
            }


        }

        catch (Exception e) {
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Telephone service not found. Trying to send SMS via network", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            if (checkInternetConnection()) {

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

    public boolean checkInternetConnection() {
        Context context = getApplication();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }
}
