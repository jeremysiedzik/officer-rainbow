package com.robotagrex.or.officerrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.jsoup.Jsoup;

public class Main5Activity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    // URL Address
    String url = "http://www.robotagrex.com";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_5);

        Button titlebutton = (Button) findViewById(R.id.titlebutton);

        TextView notification_msg = (TextView) findViewById(R.id.textView3);
        assert notification_msg != null;
        notification_msg.setText(R.string.app_name);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttontest = (Button) findViewById(R.id.buttontest);
        assert buttontest != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent qoneintent = new Intent(Main5Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });
    }
}
