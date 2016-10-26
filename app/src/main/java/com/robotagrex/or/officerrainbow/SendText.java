package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendText extends IntentService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public SendText() {
        super("SchedulingService");
    }
    public static final String TAG = "Text Alert";

    private OkHttpClient mClient = new OkHttpClient();

    @Override
    protected void onHandleIntent(Intent intent) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String contact1 = sharedpreferences.getString("sms1Key", "huggermugger");
        String contact2 = sharedpreferences.getString("sms2Key", "huggermugger");
        String contact3 = sharedpreferences.getString("sms3Key", "huggermugger");
        String msg1 = sharedpreferences.getString("sms_MSG1", "huggermugger");
        String msg2 = sharedpreferences.getString("sms_MSG2", "huggermugger");
        String msg3 = sharedpreferences.getString("sms_MSG3", "huggermugger");

        String SEARCH_STRING1 = sharedpreferences.getString("color1Key", "huggermugger");
        String SEARCH_STRING2 = sharedpreferences.getString("color2Key", "huggermugger");
        String SEARCH_STRING3 = sharedpreferences.getString("color3Key", "huggermugger");
        String data_result = sharedpreferences.getString("data_result", "");
        String loaded_ok_string = "<----->";

        if(
                (data_result.length() != 0)
                && (data_result.contains(loaded_ok_string))
                && data_result.contains(SEARCH_STRING1)
                || data_result.contains(SEARCH_STRING2)
                || data_result.contains(SEARCH_STRING3)
                ){
            try {
                sendSMS(contact1,msg1);
                sendSMS(contact2,msg2);
                sendSMS(contact3,msg3);
                Log.i(TAG, "Calling sendSMS via SendText.java");
            } catch (Exception e) {
                Log.i(TAG, getString(R.string.connection_error));
            }
        }
    }

    private void sendSMS(String phoneNumber, String smsMSG) {

        Context mContext = getApplicationContext();

        if (!smsMSG.contains("huggermugger") && (!phoneNumber.contains("huggermugger"))) {
            try {
                Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                sms.putExtra("sms_body", smsMSG);
                sms.putExtra(Intent.EXTRA_TEXT, smsMSG);
                sms.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sms);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                String formattedDate = df.format(c.getTime());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("todays_date_sms", formattedDate);
                editor.apply();
                System.out.println("SMS Sent Via 4G Network");
                return;
            } catch (Exception e) {
                System.out.println("SMS NOT Sent Attempting to send via post to Twilio API - stacktrace follows if set");
                //e.printStackTrace();
            }
        }

        if (!smsMSG.contains("huggermugger") && (!phoneNumber.contains("huggermugger"))) {

            try {
                post(mContext.getString(R.string.backend_url_sms), phoneNumber, smsMSG, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("SMS NOT sent via Twilio API - stacktrace follows if set");
                        //e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                        String formattedDate = df.format(c.getTime());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("todays_date_sms", formattedDate);
                        editor.apply();
                        System.out.println("SMS sent via Twilio");
                    }
                });
            } catch (Exception e) {
                System.out.println("SMS NOT sent via Twilio API - Check Internet Connection - stacktrace follows if set");
                //e.printStackTrace();
            }
        }
    }

    Call post(String url, String current_sms_number, String current_sms_msg, Callback callback) throws IOException{
        RequestBody formBody = new FormBody.Builder()
                .add("To", current_sms_number)
                .add("Body", current_sms_msg)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;

    }
}
