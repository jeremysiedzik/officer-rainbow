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

public class NotificationMessage extends AppCompatActivity {

    EditText ed1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Message = "messageKey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationmessage);

        ed1=(EditText)findViewById(R.id.editText6);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String app_title = sharedpreferences.getString("app_title", "Officer Rainbow");
        if(getSupportActionBar() != null){
            System.out.println(app_title);
            getSupportActionBar().setTitle(app_title);
        }


        Button buttonmessage = (Button) findViewById(R.id.buttonmessage);
        assert buttonmessage != null;

        String fillmessage = sharedpreferences.getString("messageKey", "");

        if(fillmessage.length() != 0){
            ed1.setText(fillmessage);
        }

        buttonmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messagekey  = ed1.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Message, messagekey);
                editor.apply();
                Intent qoneintent = new Intent(NotificationMessage.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }
}
