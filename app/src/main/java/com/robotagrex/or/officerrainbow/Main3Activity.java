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

public class Main3Activity extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String emailkey1 = "email1Key";
    public static final String emailkey2 = "email2Key";
    public static final String emailkey3 = "email3Key";
    public static final String smskey1 = "sms1Key";
    public static final String smskey2 = "sms2Key";
    public static final String smskey3 = "sms3Key";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_3);

        ed1=(EditText)findViewById(R.id.editText);
        ed2=(EditText)findViewById(R.id.editText3);
        ed3=(EditText)findViewById(R.id.editText4);
        ed4=(EditText)findViewById(R.id.editText2);
        ed5=(EditText)findViewById(R.id.editText5);
        ed6=(EditText)findViewById(R.id.editText6);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Button buttonfirstlast = (Button) findViewById(R.id.buttonlast);
        assert buttonfirstlast != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String fillemail1 = sharedpreferences.getString("email1Key", "");
        String fillemail2 = sharedpreferences.getString("email2Key", "");
        String fillemail3 = sharedpreferences.getString("email3Key", "");
        String fillsms1 = sharedpreferences.getString("sms1Key", "");
        String fillsms2 = sharedpreferences.getString("sms2Key", "");
        String fillsms3 = sharedpreferences.getString("sms3Key", "");

        ed1.setText(fillemail1);
        ed2.setText(fillemail2);
        ed3.setText(fillemail3);
        ed4.setText(fillsms1);
        ed5.setText(fillsms2);
        ed6.setText(fillsms3);

        buttonfirstlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1  = ed1.getText().toString();
                String email2  = ed2.getText().toString();
                String email3  = ed3.getText().toString();
                String sms1  = ed4.getText().toString();
                String sms2  = ed5.getText().toString();
                String sms3  = ed6.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(emailkey1, email1);
                editor.putString(emailkey2, email2);
                editor.putString(emailkey3, email3);
                editor.putString(smskey1, sms1);
                editor.putString(smskey2, sms2);
                editor.putString(smskey3, sms3);
                editor.apply();
                Intent qoneintent = new Intent(Main3Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });
    }
}
