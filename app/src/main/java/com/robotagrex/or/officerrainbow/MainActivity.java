package com.robotagrex.or.officerrainbow;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        Button buttonfirstlast = (Button) findViewById(R.id.buttonlast);
        assert buttonfirstlast != null;
        buttonfirstlast.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent qoneintent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(qoneintent);
            }
        });
    }
}
