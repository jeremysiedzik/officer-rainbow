package com.robotagrex.or.officerrainbow;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.IOException;
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
        String contact1 = sharedpreferences.getString("sms1Key", "empty_string_contact");
        String contact2 = sharedpreferences.getString("sms2Key", "empty_string_contact");
        String contact3 = sharedpreferences.getString("sms3Key", "empty_string_contact");
        String msg1 = sharedpreferences.getString("sms_MSG1", "empty_string_msg");
        String msg2 = sharedpreferences.getString("sms_MSG2", "empty_string_msg");
        String msg3 = sharedpreferences.getString("sms_MSG3", "empty_string_msg");

        try {
            sendSMS(contact1,msg1);
            sendSMS(contact2,msg2);
            sendSMS(contact3,msg3);
            Log.i(TAG, "Calling loadFromNetwork via Confirmation.java");
        } catch (Exception e) {
            Log.i(TAG, getString(R.string.connection_error));
        }
    }

    private void sendSMS(String phoneNumber, String smsMSG) {

            Context mContext = getApplicationContext();

            if (!smsMSG.contains("empty_string_msg") && (!phoneNumber.contains("empty_string_contact"))) {
                try {
                    Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                    sms.putExtra("sms_body", smsMSG);
                    sms.putExtra(Intent.EXTRA_TEXT, smsMSG);
                    startActivity(sms);
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "SMS Sent via 4G", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } catch (Exception e) {
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "Telephone service not found. Trying to send SMS via network", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } else{

                try {
                        post(mContext.getString(R.string.backend_url), phoneNumber, smsMSG, new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "SMS NOT Sent - Check Network Connection", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "SMS Sent via Wi-Fi network", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }
                        });
                    } catch(Exception e){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Check Internet Connection", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
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
