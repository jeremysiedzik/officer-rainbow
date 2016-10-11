package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OfficerRainbow extends AppCompatActivity {

    EditText ed1,ed2;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String FirstName = "firstnameKey";
    public static final String LastName = "lastnameKey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officerrainbow);

        ed1=(EditText)findViewById(R.id.email1);
        ed2=(EditText)findViewById(R.id.sms_contact1);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Button buttonfirstlast = (Button) findViewById(R.id.buttonlast);
        assert buttonfirstlast != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String fillfirst = sharedpreferences.getString("firstnameKey", "");
        String filllast = sharedpreferences.getString("lastnameKey", "");

        if((fillfirst.length() != 0) && (filllast.length() != 0)) {
            ed1.setText(fillfirst);
            ed2.setText(filllast);
        }

        buttonfirstlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname  = ed1.getText().toString();
                String lastname  = ed2.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(FirstName, firstname);
                editor.putString(LastName, lastname);
                editor.apply();
                Intent qoneintent = new Intent(OfficerRainbow.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }
}
