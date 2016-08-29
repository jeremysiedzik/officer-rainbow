package com.robotagrex.or.officerrainbow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main5Activity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    // URL Address
    String url = "http://ec2-52-42-215-71.us-west-2.compute.amazonaws.com/onsite-dropmessage.txt";
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_5);

        Button titlebutton = (Button) findViewById(R.id.titlebutton);

        // Title AsyncTask
        class Title extends AsyncTask<Void, Void, Void> {
            String title;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(Main5Activity.this);
                mProgressDialog.setTitle("Android Basic JSoup Tutorial");
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Connect to the web site
                    Document document = Jsoup.connect(url).get();
                    // Get the html document title
                    title = document.title();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // Set title into TextView
                TextView txttitle = (TextView) findViewById(R.id.titletxt);
                assert txttitle != null;
                txttitle.setText(title);
                mProgressDialog.dismiss();
            }
        }

        TextView notification_msg = (TextView) findViewById(R.id.textView3);
        assert notification_msg != null;
        notification_msg.setText(R.string.app_name);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttontest = (Button) findViewById(R.id.buttontest);
        assert buttontest != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Capture button click
        assert titlebutton != null;
        titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Execute Title AsyncTask
                new Title().execute();
            }
        });

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent qoneintent = new Intent(Main5Activity.this, MainActivity.class);
                startActivity(qoneintent);
            }
        });
    }
}
