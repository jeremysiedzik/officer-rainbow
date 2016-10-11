package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ColorChoice extends AppCompatActivity {

    EditText ed1,ed2,ed3;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Color1 = "color1Key";
    public static final String Color2 = "color2Key";
    public static final String Color3 = "color3Key";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorchoice);

        ed1=(EditText)findViewById(R.id.email1);
        ed2=(EditText)findViewById(R.id.editText7);
        ed3=(EditText)findViewById(R.id.editText8);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttoncolor = (Button) findViewById(R.id.buttoncolor);
        assert buttoncolor != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        String fillcolor2 = sharedpreferences.getString("color2Key", "");
        String fillcolor3 = sharedpreferences.getString("color3Key", "");

        if((fillcolor1.length() != 0)) {
            ed1.setText(fillcolor1);
        } else {
            ed1.setText(R.string.tap_here_to_choose);
        }

        if((fillcolor2.length() != 0)) {
            ed2.setText(fillcolor2);
        } else {
            ed2.setText(R.string.tap_here_to_choose);
        }

        if((fillcolor3.length() != 0)) {
            ed3.setText(fillcolor3);
        } else {
            ed3.setText(R.string.tap_here_to_choose);
        }

        ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed1.setSelectAllOnFocus(true);
            }
        });

        ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed2.setSelectAllOnFocus(true);
            }
        });

        ed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed3.setSelectAllOnFocus(true);
            }
        });

        buttoncolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color1 = ed1.getText().toString();
                String color2 = ed2.getText().toString();
                String color3 = ed3.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Color1, color1);
                editor.putString(Color2, color2);
                editor.putString(Color3, color3);
                editor.apply();

                Intent qoneintent = new Intent(ColorChoice.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }
}
