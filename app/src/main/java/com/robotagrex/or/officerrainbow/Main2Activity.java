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

public class Main2Activity extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4,ed5,ed6,current_ed;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String emailkey1 = "email1Key";
    public static final String emailkey2 = "email2Key";
    public static final String emailkey3 = "email3Key";
    public static final String smskey1 = "sms1Key";
    public static final String smskey2 = "sms2Key";
    public static final String smskey3 = "sms3Key";
    SharedPreferences sharedpreferences;

    static final int PICK_CONTACT_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

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

        Button contactbutton1 = (Button) findViewById(R.id.buttoncontact1);
        assert contactbutton1 != null;

        Button contactbutton2 = (Button) findViewById(R.id.buttoncontact2);
        assert contactbutton2 != null;

        Button contactbutton3 = (Button) findViewById(R.id.buttoncontact3);
        assert contactbutton3 != null;

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

        contactbutton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_ed=(EditText)findViewById(R.id.editText2);
                Intent pickContactIntent1 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent1, PICK_CONTACT_REQUEST);
            }
        });

        contactbutton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_ed=(EditText)findViewById(R.id.editText5);
                Intent pickContactIntent2 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent2.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent2, PICK_CONTACT_REQUEST);
            }
        });

        contactbutton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_ed=(EditText)findViewById(R.id.editText6);
                Intent pickContactIntent3 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent3.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent3, PICK_CONTACT_REQUEST);
            }
        });



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
                Intent qoneintent = new Intent(Main2Activity.this, Main3Activity.class);
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
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

            // Perform the query on the contact to get the NUMBER column
            // We don't need a selection or sort order (there's only one result for the given URI)
            // CAUTION: The query() method should be called from a separate thread to avoid blocking
            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
            // Consider using CursorLoader to perform the query.
            Cursor cursor = getContentResolver()
                    .query(contactUri, projection, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            // Retrieve the phone number from the NUMBER column
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(column);

            // Do something with the phone number...
            current_ed.setText(number);
        }
    }
}
