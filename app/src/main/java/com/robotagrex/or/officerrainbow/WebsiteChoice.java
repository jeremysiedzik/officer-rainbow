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

public class WebsiteChoice extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    ToggleButton toggleButton, toggleButton2, toggleButton3, toggleButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.websitechoice);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);
        toggleButton4 = (ToggleButton) findViewById(R.id.toggleButton4);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttonnext = (Button) findViewById(R.id.buttonnext);
        assert buttonnext != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        boolean toggle1state = sharedpreferences.getBoolean("jams_state", false);
            toggleButton.setChecked(toggle1state);

        boolean toggle2state = sharedpreferences.getBoolean("nova_state", false);
            toggleButton2.setChecked(toggle2state);

        boolean toggle3state = sharedpreferences.getBoolean("onsite_state", false);
            toggleButton3.setChecked(toggle3state);

        boolean toggle4state = sharedpreferences.getBoolean("bgreen_state", false);
        toggleButton4.setChecked(toggle4state);

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent qoneintent = new Intent(WebsiteChoice.this, AlarmSettings.class);
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
            }
        });

        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
            }
        });

        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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

        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (toggleButton4.isChecked())
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("bgreen_state", true);
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit();
                    editor.putBoolean("bgreen_state", false);
                    editor.apply();
                }
            }
        });
    }
}
