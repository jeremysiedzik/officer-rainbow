package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProbationEndAlarmSettings extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probationendalarmsettings);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String txtdate = sharedpreferences.getString("probation_end_date", "No date chosen yet");
        TextView txtdate_view=(TextView)findViewById(R.id.probation_end_date_id);
        txtdate_view.setText(txtdate);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        String app_title = sharedpreferences.getString("app_title", "Officer Rainbow");
        if(getSupportActionBar() != null){
            System.out.println(app_title);
            getSupportActionBar().setTitle(app_title);
        }

        Button buttondate = (Button)findViewById(R.id.buttondate);
        assert buttondate != null;

        Button buttonnext = (Button)findViewById(R.id.buttonnext);
        assert buttonnext != null;

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(ProbationEndAlarmSettings.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new ProbationEndDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
