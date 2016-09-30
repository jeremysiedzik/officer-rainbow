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

        ed1=(EditText)findViewById(R.id.editText);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Button buttonmessage = (Button) findViewById(R.id.buttonmessage);
        assert buttonmessage != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
                String blanket_save = getResources().getString(R.string.blanket_save);
                //Intent qoneintent = new Intent(AlarmSettings.this, Class.forName(blanket_save));
                Intent qoneintent = null;
                try {
                    qoneintent = new Intent( NotificationMessage.this, Class.forName( blanket_save ) );
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                startActivity(qoneintent);
            }
        });
    }
}
