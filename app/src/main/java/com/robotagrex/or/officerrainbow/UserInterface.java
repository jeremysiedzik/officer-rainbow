package com.robotagrex.or.officerrainbow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
//import android.widget.Toast;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

//import static android.R.id.progress;

public class UserInterface extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinterface);

        Button buttonnext = (Button)findViewById(R.id.buttonlast);
        assert buttonnext != null;

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(UserInterface.this, OfficerRainbow.class);
                startActivity(qoneintent);
            }
        });

        RingProgressBar mRingProgressBar = (RingProgressBar) findViewById(R.id.progress_bar_2);

        // Set the progress bar's progress
        int progress = 40;
        mRingProgressBar.setProgress(progress);
        //mRingProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

          //  @Override
           // public void progressToComplete() {
                // Progress reaches the maximum callback default Max value is 100
             //   Toast.makeText(UserInterface.this, "complete", Toast.LENGTH_SHORT).show();
            //}
        //});
    }
}
