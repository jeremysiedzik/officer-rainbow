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

public class Notifications extends AppCompatActivity {

    EditText current_ed,email_msg1,email_msg2,email_msg3;
    EditText sms_msg1,sms_msg2,sms_msg3;
    EditText email_edit1,email_edit2,email_edit3;
    EditText sms_contact_number_1,sms_contact_number_2,sms_contact_number_3;
    EditText current_name,contact_name_1,contact_name_2,contact_name_3;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String emailkey1 = "email1Key";
    public static final String emailkey2 = "email2Key";
    public static final String emailkey3 = "email3Key";
    public static final String smskey1 = "sms1Key";
    public static final String smskey2 = "sms2Key";
    public static final String smskey3 = "sms3Key";

    public static final String notifymsg1 = "email_MSG1";
    public static final String notifymsg2 = "email_MSG2";
    public static final String notifymsg3 = "email_MSG3";
    public static final String notifymsg4 = "sms_MSG1";
    public static final String notifymsg5 = "sms_MSG2";
    public static final String notifymsg6 = "sms_MSG3";

    public static final String contactname1 = "contact_name_1";
    public static final String contactname2 = "contact_name_2";
    public static final String contactname3 = "contact_name_3";

    SharedPreferences sharedpreferences;

    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        email_edit1=(EditText)findViewById(R.id.mail1);
        email_edit2=(EditText)findViewById(R.id.mail2);
        email_edit3=(EditText)findViewById(R.id.mail3);
        sms_contact_number_1=(EditText)findViewById(R.id.sms_contact1);
        sms_contact_number_2=(EditText)findViewById(R.id.sms_contact2);
        sms_contact_number_3=(EditText)findViewById(R.id.sms_contact3);

        email_msg1=(EditText)findViewById(R.id.notify_msg_1);
        email_msg2=(EditText)findViewById(R.id.notify_msg_2);
        email_msg3=(EditText)findViewById(R.id.notify_msg_3);
        sms_msg1=(EditText)findViewById(R.id.notify_msg_4);
        sms_msg2=(EditText)findViewById(R.id.notify_msg_5);
        sms_msg3=(EditText)findViewById(R.id.notify_msg_6);

        contact_name_1=(EditText)findViewById(R.id.contact_name_1);
        contact_name_2=(EditText)findViewById(R.id.contact_name_2);
        contact_name_3=(EditText)findViewById(R.id.contact_name_3);

        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String app_title = sharedpreferences.getString("app_title", "Officer Rainbow");
        if(getSupportActionBar() != null){
            System.out.println(app_title);
            getSupportActionBar().setTitle(app_title);
        }

        Button buttonfirstlast = (Button) findViewById(R.id.buttonlast);
        assert buttonfirstlast != null;

        Button contactbutton1 = (Button) findViewById(R.id.buttoncontact1);
        assert contactbutton1 != null;

        Button contactbutton2 = (Button) findViewById(R.id.buttoncontact2);
        assert contactbutton2 != null;

        Button contactbutton3 = (Button) findViewById(R.id.buttoncontact3);
        assert contactbutton3 != null;

        String fillemail1 = sharedpreferences.getString("email1Key", "");
        String fillemail2 = sharedpreferences.getString("email2Key", "");
        String fillemail3 = sharedpreferences.getString("email3Key", "");
        String fillsms1 = sharedpreferences.getString("sms1Key", "");
        String fillsms2 = sharedpreferences.getString("sms2Key", "");
        String fillsms3 = sharedpreferences.getString("sms3Key", "");

        String fillnotify1 = sharedpreferences.getString("email_MSG1", "");
        String fillnotify2 = sharedpreferences.getString("email_MSG2", "");
        String fillnotify3 = sharedpreferences.getString("email_MSG3", "");
        String fillnotify4 = sharedpreferences.getString("sms_MSG1", "");
        String fillnotify5 = sharedpreferences.getString("sms_MSG2", "");
        String fillnotify6 = sharedpreferences.getString("sms_MSG3", "");

        String fillname1 = sharedpreferences.getString("contact_name_1", "");
        String fillname2 = sharedpreferences.getString("contact_name_2", "");
        String fillname3 = sharedpreferences.getString("contact_name_3", "");

        email_edit1.setText(fillemail1);
        email_edit2.setText(fillemail2);
        email_edit3.setText(fillemail3);
        sms_contact_number_1.setText(fillsms1);
        sms_contact_number_2.setText(fillsms2);
        sms_contact_number_3.setText(fillsms3);

        email_msg1.setText(fillnotify1);
        email_msg2.setText(fillnotify2);
        email_msg3.setText(fillnotify3);
        sms_msg1.setText(fillnotify4);
        sms_msg2.setText(fillnotify5);
        sms_msg3.setText(fillnotify6);

        contact_name_1.setText(fillname1);
        contact_name_2.setText(fillname2);
        contact_name_3.setText(fillname3);

        contactbutton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_name=(EditText) findViewById(R.id.contact_name_1);
                current_ed=(EditText)findViewById(R.id.sms_contact1);
                Intent pickContactIntent1 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent1, PICK_CONTACT_REQUEST);
            }
        });

        contactbutton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_name=(EditText) findViewById(R.id.contact_name_2);
                current_ed=(EditText)findViewById(R.id.sms_contact2);
                Intent pickContactIntent2 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent2.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent2, PICK_CONTACT_REQUEST);
            }
        });

        contactbutton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view) {
                current_name=(EditText) findViewById(R.id.contact_name_3);
                current_ed=(EditText)findViewById(R.id.sms_contact3);
                Intent pickContactIntent3 = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent3.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent3, PICK_CONTACT_REQUEST);
            }
        });

        buttonfirstlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedpreferences.edit();

                String email1=email_edit1.getText().toString();
                String email2=email_edit2.getText().toString();
                String email3=email_edit3.getText().toString();
                String sms1=sms_contact_number_1.getText().toString();
                String sms2=sms_contact_number_2.getText().toString();
                String sms3=sms_contact_number_3.getText().toString();

                String notify1=email_msg1.getText().toString();
                String notify2=email_msg2.getText().toString();
                String notify3=email_msg3.getText().toString();
                String notify4=sms_msg1.getText().toString();
                String notify5=sms_msg2.getText().toString();
                String notify6=sms_msg3.getText().toString();
                String fillname1=contact_name_1.getText().toString();
                String fillname2=contact_name_2.getText().toString();
                String fillname3=contact_name_3.getText().toString();

                editor.putString(notifymsg1, notify1);
                editor.putString(notifymsg2, notify2);
                editor.putString(notifymsg3, notify3);
                editor.putString(notifymsg4, notify4);
                editor.putString(notifymsg5, notify5);
                editor.putString(notifymsg6, notify6);

                editor.putString(emailkey1, email1);
                editor.putString(emailkey2, email2);
                editor.putString(emailkey3, email3);
                editor.putString(smskey1, sms1);
                editor.putString(smskey2, sms2);
                editor.putString(smskey3, sms3);

                editor.putString(contactname1, fillname1);
                editor.putString(contactname2, fillname2);
                editor.putString(contactname3, fillname3);

                editor.apply();
                Intent qoneintent = new Intent(Notifications.this, UI.class);
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
            String[] contact_name = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            // Perform the query on the contact to get the NUMBER column
            // We don't need a selection or sort order (there's only one result for the given URI)
            // CAUTION: The query() method should be called from a separate thread to avoid blocking
            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
            // Consider using CursorLoader to perform the query.
            Cursor cursor = getContentResolver()
                    .query(contactUri, projection, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            Cursor cursor_name = getContentResolver()
                    .query(contactUri, contact_name, null, null, null);
            assert cursor_name != null;
            cursor_name.moveToFirst();

            // Retrieve the phone number from the NUMBER column
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(column);
            number = number.replace(" ", "");
            number = number.replace("(", "");
            number = number.replace(")", "");
            number = number.replace("-", "");
            number = number.replace("+", "");
            if (number.length() >= 10) {
                try {
                    number = number.substring(number.length() - 10);
                    // Do something with the phone number...
                    current_ed.setText(number);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int column_name = cursor_name.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String final_name = cursor_name.getString(column_name);
            current_name.setText(final_name);
        }
    }
}
