package com.robotagrex.or.officerrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Confirm extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public static final String pulled_user_number = "user_number";
    public static final String pulled_user_imei = "user_imei";
    public static final String pulled_user_simSerialNumber = "user_simSerialNumber";

    TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);

        String user_number = tm.getLine1Number();
        String user_imei = tm.getDeviceId();
        String user_simSerialNumber = tm.getSimSerialNumber();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(pulled_user_number, user_number);
        editor.putString(pulled_user_imei, user_imei);
        editor.putString(pulled_user_simSerialNumber, user_simSerialNumber);
        editor.apply();

        Button titlebutton = (Button) findViewById(R.id.titlebutton);

        // aynctask
        class Title extends AsyncTask<Void, Void, Void> {
            private String title;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(Confirm.this);
                mProgressDialog.setTitle("Confirming receipt");
                mProgressDialog.setMessage("Sending...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Connect to the web site
                    String url = "http://data.robotagrex.com/sendemail.php?emailaddress=mainphrame@hotmail.com&emailmessage=predata-test";
                    Document document = Jsoup.connect(url).get();
                    // Get the html document title
                    title = document.data();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Set title into TextView
                TextView txttitle = (TextView) findViewById(R.id.titletxt);
                assert txttitle != null;
                txttitle.setText(title);
                mProgressDialog.dismiss();
            }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttontest = (Button) findViewById(R.id.buttontest);
        assert buttontest != null;

        TextView titletxt=(TextView)findViewById(R.id.titletxt);
        titletxt.setMovementMethod(new ScrollingMovementMethod());

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Capture button click
        assert titlebutton != null;
        titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Execute Title AsyncTask
                new Title().execute();
            }
        });

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(Confirm.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }
}
