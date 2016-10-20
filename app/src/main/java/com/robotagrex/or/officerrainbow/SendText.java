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

        Context mContext = getApplicationContext();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String contact1 = sharedpreferences.getString("sms1Key", "empty_string_contact1");
        String contact2 = sharedpreferences.getString("sms2Key", "empty_string_contact2");
        String contact3 = sharedpreferences.getString("sms3Key", "empty_string_contact3");

        try {
            sendSMS(contact1);
            sendSMS(contact2);
            sendSMS(contact3);
            Log.i(TAG, "Calling loadFromNetwork via Confirmation.java");
        } catch (Exception e) {
            Log.i(TAG, getString(R.string.connection_error));
        }

        try {
            post(mContext.getString(R.string.backend_url), new  Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "SMS NOT Sent - Check Network Connection", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "SMS Sent", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Call post(String url, Callback callback) throws IOException{
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

        }
    }

    public boolean checkInternetConnection() {
        Context context = getApplication();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }


}
