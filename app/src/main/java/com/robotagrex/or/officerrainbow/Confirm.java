package com.robotagrex.or.officerrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Confirm extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Handler mHandler = new Handler();
    TextView titletxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final Button titlebutton = (Button) findViewById(R.id.titlebutton);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttontest = (Button) findViewById(R.id.buttontest);
        assert buttontest != null;

        final TextView titletxt=(TextView)findViewById(R.id.titletxt);
        titletxt.setMovementMethod(new ScrollingMovementMethod());


        Runnable confirmation_msg = new Runnable() {
            @Override
            public void run() {
                String confirmation_result = sharedpreferences.getString("confirmation_result", "Click above to confirm.");
                String loaded_ok_string = "Alarm Confirmed";
                if((confirmation_result.length() != 0) && (confirmation_result.contains(loaded_ok_string))) {
                    titletxt.setText(confirmation_result);
                }
                mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.post(confirmation_msg);

        String app_title = sharedpreferences.getString("app_title", "Officer Rainbow");
        if(getSupportActionBar() != null){
            System.out.println(app_title);
            getSupportActionBar().setTitle(app_title);
        }

        assert titlebutton != null;
        titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (checkInternetConnection()) {
                    new confirmation_task().execute();
                } else {
                    toast_internet_down();
                }
                }
        });

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titletxt.setText("");
                Intent qoneintent = new Intent(Confirm.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }

    void sendconfirmation(final Context uicontext) {
       if (checkInternetConnection()) {
            System.out.println("About to run Confirmation.class within sendconfirmation method");
            Intent confirmationservice = new Intent(uicontext, Confirmation.class);
            uicontext.startService(confirmationservice);
        }
        else {
            toast_internet_down();
       }
   }

    void toast_internet_down() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check your internet connection.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public boolean checkInternetConnection() {
        Context context = getApplication();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    class confirmation_task extends AsyncTask<Void, Void, Void> {
        Context context = getApplicationContext();
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(Confirm.this);
            mProgressDialog.setTitle("Confirming Notification");
            mProgressDialog.setMessage("Confirming...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            sendconfirmation(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String confirmation_result = sharedpreferences.getString("confirmation_result", "Click above to confirm.");
            String loaded_ok_string = "Alarm Confirmed";
            if((confirmation_result.length() != 0) && (confirmation_result.contains(loaded_ok_string))) {
                titletxt.setText(confirmation_result);
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            }, 5000);
        }
    }
}
