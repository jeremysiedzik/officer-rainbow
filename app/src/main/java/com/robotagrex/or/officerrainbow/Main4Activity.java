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

public class Main4Activity extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4,ed5;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Color1 = "color1Key";
    public static final String Color2 = "color2Key";
    public static final String Color3 = "color3Key";
    public static final String Color4 = "color4Key";
    public static final String Color5 = "color5Key";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_4);

        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText7);
        ed3=(EditText)findViewById(R.id.editText8);
        ed4=(EditText)findViewById(R.id.editText9);
        ed5=(EditText)findViewById(R.id.editText10);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Button buttoncolor = (Button) findViewById(R.id.buttoncolor);
        assert buttoncolor != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        String fillcolor2 = sharedpreferences.getString("color2Key", "");
        String fillcolor3 = sharedpreferences.getString("color3Key", "");
        String fillcolor4 = sharedpreferences.getString("color4Key", "");
        String fillcolor5 = sharedpreferences.getString("color5Key", "");

        ed1.setText(fillcolor1);
        ed2.setText(fillcolor2);
        ed3.setText(fillcolor3);
        ed4.setText(fillcolor4);
        ed5.setText(fillcolor5);

        buttoncolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color1 = ed1.getText().toString();
                String color2 = ed2.getText().toString();
                String color3 = ed3.getText().toString();
                String color4 = ed4.getText().toString();
                String color5 = ed5.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Color1, color1);
                editor.putString(Color2, color2);
                editor.putString(Color3, color3);
                editor.putString(Color4, color4);
                editor.putString(Color5, color5);

                editor.apply();
                Intent qoneintent = new Intent(Main4Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });
    }
}