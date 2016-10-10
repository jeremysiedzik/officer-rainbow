package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Confirm extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Handler mHandler = new Handler();

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

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Runnable confirmation_msg = new Runnable() {
            @Override
            public void run() {
                String confirmation_result = sharedpreferences.getString("confirmation_result", "No data yet");
                String loaded_ok_string = "Sent email test";
                if((confirmation_result.length() != 0) && (confirmation_result.contains(loaded_ok_string))) {
                    titletxt.setText(confirmation_result);
                }
                mHandler.postDelayed(this, 5000);
            }
        };
        mHandler.post(confirmation_msg);


        // Capture button click
        assert titlebutton != null;
        titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent qoneintent = new Intent(Confirm.this, Confirmation.class);
                startActivity(qoneintent);
                String confirmation_result = sharedpreferences.getString("confirmation_result", "No data yet");
                titletxt.setText(confirmation_result);
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
