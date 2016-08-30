package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class Main6Activity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    ToggleButton toggleButton, toggleButton2, toggleButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_6);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttonnext = (Button) findViewById(R.id.buttonnext);
        assert buttonnext != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        toggleButton.setChecked(sharedpreferences.getBoolean("jams_state", true));
        toggleButton2.setChecked(sharedpreferences.getBoolean("nova_state", true));
        toggleButton3.setChecked(sharedpreferences.getBoolean("onsite_state", true));



        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent qoneintent = new Intent(Main6Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (toggleButton.isChecked())
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("jams_state", true);
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("jams_state", false);
                    editor.apply();
                }

                if (toggleButton2.isChecked())
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("nova_state", true);
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("nova_state", false);
                    editor.apply();
                }

                if (toggleButton3.isChecked())
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("onsite_state", true);
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("onsite_state", false);
                    editor.apply();
                }
            }
        });
    }
}
