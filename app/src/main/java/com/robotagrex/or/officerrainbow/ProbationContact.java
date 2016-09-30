package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProbationContact extends AppCompatActivity {

    EditText ed6,ed11,ed12,current_ed;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String officername = "officerName";
    public static final String officernumber = "officerNumber";
    public static final String officernotes = "officerNotes";
    SharedPreferences sharedpreferences;

    static final int PICK_CONTACT_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probationcontact);

        ed6=(EditText)findViewById(R.id.editText6);
        ed11=(EditText)findViewById(R.id.editText11);
        ed12=(EditText)findViewById(R.id.editText12);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttonfirstlast = (Button) findViewById(R.id.buttonlast);
        assert buttonfirstlast != null;

        Button contactbutton3 = (Button) findViewById(R.id.buttoncontact3);
        assert contactbutton3 != null;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String fillcontact_name = sharedpreferences.getString("officerName","");
        String fillcontact_number = sharedpreferences.getString("officerNumber","");
        String fillcontact_notes = sharedpreferences.getString("officerNotes","");

        ed6.setText(fillcontact_name);
        ed11.setText(fillcontact_number);
        ed12.setText(fillcontact_notes);


        contactbutton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_ed=(EditText)findViewById(R.id.editText11);
                Intent pickContactIntent1 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent1, PICK_CONTACT_REQUEST);
            }
        });

        buttonfirstlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String form_officer_name  = ed6.getText().toString();
                String form_officer_number  = ed11.getText().toString();
                String form_officer_notes  = ed12.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(officername, form_officer_name);
                editor.putString(officernumber, form_officer_number);
                editor.putString(officernotes, form_officer_notes);

                editor.apply();
                Intent qoneintent = new Intent(ProbationContact.this, UI.class);
                startActivity(qoneintent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        // Make sure the request was successful
        if (requestCode == PICK_CONTACT_REQUEST) if (resultCode == RESULT_OK) {
            // Get the URI that points to the selected contact
            Uri contactUri = data.getData();
            // We only need the NUMBER column, because there will be only one row in the result
            String[] contact_number = {ContactsContract.CommonDataKinds.Phone.NUMBER};
            String[] contact_name = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            // Perform the query on the contact to get the NUMBER column
            // We don't need a selection or sort order (there's only one result for the given URI)
            // CAUTION: The query() method should be called from a separate thread to avoid blocking
            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
            // Consider using CursorLoader to perform the query.
            Cursor cursor_number = getContentResolver()
                    .query(contactUri, contact_number, null, null, null);
            assert cursor_number != null;
            cursor_number.moveToFirst();

            Cursor cursor_name = getContentResolver()
                    .query(contactUri, contact_name, null, null, null);
            assert cursor_name != null;
            cursor_name.moveToFirst();

            // Retrieve the phone number from the NUMBER column
            int column_number = cursor_number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String final_number = cursor_number.getString(column_number);

            int column_name = cursor_name.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String final_name = cursor_name.getString(column_name);


            // Do something with the phone number...
            ed11.setText(final_number);
            ed6.setText(final_name);
        }
    }
}
