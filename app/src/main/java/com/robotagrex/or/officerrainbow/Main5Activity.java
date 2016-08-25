package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main5Activity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_5);

        //http://stackoverflow.com/questions/3275467/why-cant-i-set-text-to-an-android-textview
        TextView notification_msg = (TextView) findViewById(R.id.textView3);
        assert notification_msg != null;
        notification_msg.setText(R.string.app_name);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttoncolor = (Button) findViewById(R.id.buttoncolor);
        assert buttoncolor != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        buttoncolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent qoneintent = new Intent(Main5Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });
    }
}
