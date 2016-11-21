package com.robotagrex.or.officerrainbow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
//import android.view.ContextMenu;
import android.view.Gravity;
//import android.view.MotionEvent;
import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Settings extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    //Declaring an Spinner
    private Spinner spinner;

    //An ArrayList for Spinner Items
    private ArrayList<String> sites;

    //JSON Array
    private JSONArray result;

    //TextViews to display details
    private TextView textViewName;
    private TextView textViewCourse;
    private TextView textViewSession;
    public static final String Color1 = "color1Key";
    public static final String Color2 = "color2Key";
    public static final String Color3 = "color3Key";
    public static final String marquee_link_push = "marquee_link";
    public static final String marquee_key_push = "marquee_key";
    public static final String marquee_description_push = "marquee_description";
    public static final String app_title_push = "app_title";
    public static final String sound_file_push = "soundfile";
    public static final String colors_url_push = "colors_url";
    //public boolean xmlverified = false;

    public static final String MyPREFERENCES = "MyPrefs";
    public String longestString;
    public int longestStringCharacters;
    SharedPreferences sharedpreferences;

    EditText color1,color2,color3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        View current_focus = getCurrentFocus();
        if (current_focus != null) current_focus.clearFocus();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean preferences_set = sharedpreferences.getBoolean("preferences_set", false);

        color1=(EditText)findViewById(R.id.color1);
        color2=(EditText)findViewById(R.id.color2);
        color3=(EditText)findViewById(R.id.color3);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String app_title = sharedpreferences.getString("app_title", "Officer Rainbow - Settings");
        if (getSupportActionBar() != null){
            System.out.println(app_title);
            getSupportActionBar().setTitle(app_title);
        }

        if (preferences_set) {
            Intent intent = new Intent(getApplicationContext(), UI.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        String fillcolor1 = sharedpreferences.getString("color1Key", "");
        String fillcolor2 = sharedpreferences.getString("color2Key", "");
        String fillcolor3 = sharedpreferences.getString("color3Key", "");

        if((fillcolor1.length() != 0)) {
            color1.setText(fillcolor1);
        } else {
            color1.setText(R.string.tap_here_to_choose);
        }

        if((fillcolor2.length() != 0)) {
            color2.setText(fillcolor2);
        } else {
            color2.setText(R.string.tap_here_to_choose);
        }

        if((fillcolor3.length() != 0)) {
            color3.setText(fillcolor3);
        } else {
            color3.setText(R.string.tap_here_to_choose);
        }

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color1.setSelectAllOnFocus(true);
            }
        });

        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color2.setSelectAllOnFocus(true);
            }
        });

        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color3.setSelectAllOnFocus(true);
            }
        });

        Button buttonnext = (Button) findViewById(R.id.buttonnext);
        assert buttonnext != null;

        //Initializing the ArrayList
        sites = new ArrayList<>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class itself
        // we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        //Initializing TextViews
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCourse = (TextView) findViewById(R.id.textViewCourse);
        textViewSession = (TextView) findViewById(R.id.textViewSession);

        //This method will fetch the data from the URL
        //if (checkInternetConnection() && serverUp()) {
            getData();
        //}

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String config_url  = textViewName.getText().toString();
                String colorchoice1 = color1.getText().toString();
                String colorchoice2 = color2.getText().toString();
                String colorchoice3 = color3.getText().toString();

                if (colorchoice1.length() == 0) {
                    color1.setText(R.string.tap_here_to_choose);
                }
                if (colorchoice2.length() == 0) {
                    color2.setText(R.string.tap_here_to_choose);
                }
                if (colorchoice3.length() == 0) {
                    color3.setText(R.string.tap_here_to_choose);
                }

                colorchoice1 = color1.getText().toString();
                colorchoice2 = color2.getText().toString();
                colorchoice3 = color3.getText().toString();

                editor.putString(Color1, colorchoice1);
                editor.putString(Color2, colorchoice2);
                editor.putString(Color3, colorchoice3);

                editor.putString("config_url", config_url);
                editor.putBoolean("preferences_set", true);
                editor.apply();
                System.out.println("about to run fetchxml from Settings.java");

                if (checkInternetConnection() && serverUp()){
                    checkdailycolors(getApplicationContext());
                    new asyncxml().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                if (config_url.contains("xml")) {
                    Intent qoneintent = new Intent(Settings.this, UI.class);
                    startActivity(qoneintent);
                } else toast_select_xml();

            }
        });
    }

    void checkdailycolors(final Context uicontext) {
        if (checkInternetConnection()) {
            System.out.println("About to run WebSitechecker.class within Settings method");
            Intent webservice = new Intent(uicontext, WebSitechecker.class);
            uicontext.startService(webservice);
        }
        else {
            toast_internet_down();
        }
    }

    void toast_internet_down() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check your internet connection.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    void toast_select_xml() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Please choose a URL that contains .xml", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(WebConfig.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(WebConfig.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getSites(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSites(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                sites.add(json.getString(WebConfig.TAG_SITE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(Settings.this, R.layout.spinner_item, sites);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(sharedpreferences.getInt("PREF_SPINNER", 0));
        longest_string_java();
        System.out.println("this is the longest string "+longestString);
        System.out.println("it's character count is "+longestStringCharacters);


        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) spinner.getLayoutParams();
        //params.width = longestStringCharacters;
        //spinner.setLayoutParams(params);


        //spinner.setOnTouchListener(new View.OnTouchListener() {
        //    @Override
        //    public boolean onTouch(View v, MotionEvent event) {
        //        if(event.getAction() == MotionEvent.ACTION_DOWN && (!opened)) {
        //            System.out.println("greying out the background - ACTION_UP opened = "+opened);
        //            opened = true;
        //        } else if(event.getAction() == MotionEvent.ACTION_DOWN && (opened)) {
        //            System.out.println("returning background to regular - ACTION_UP opened = "+opened);
        //            opened = false;
        //        }
        //        return false;
        //            }
        //});

    }


    //public void greyoutbackground() {
    //    System.out.println("running greyoutbackground -------------------");
    //    final RelativeLayout settings_animate = (RelativeLayout) findViewById(R.id.settings);
    //    AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.5F);
    //    alpha.setDuration(2); // Make animation instant
    //    alpha.setFillAfter(true); // Tell it to persist after the animation ends
    //    settings_animate.startAnimation(alpha);
   // }

    //public void returnbackground() {
    //    System.out.println("running returnbackground -------------------");
    //    final RelativeLayout settings_animate = (RelativeLayout) findViewById(R.id.settings);
    //    AlphaAnimation alpha = new AlphaAnimation(0.5F, 1.0F);
    //    alpha.setDuration(2); // Make animation instant
    //    alpha.setFillAfter(true); // Tell it to persist after the animation ends
    //    settings_animate.startAnimation(alpha);
   // }

    public void longest_string_java() {
        longestString = sites.get(0);
        for (String element : sites) {
            if (element.length() > longestString.length()) {
                longestString = element;
            }
        }
        longestStringCharacters = longestString.length();
    }


    //Method to get student name of a particular position
    private String getName(int position){
        String url="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            url = json.getString(WebConfig.TAG_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return url;
    }

    //Doing the same with this method as we did with getName()
    private String getCourse(int position){
        String state="";
        try {
            JSONObject json = result.getJSONObject(position);
            state = json.getString(WebConfig.TAG_STATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }

    //Doing the same with this method as we did with getName()
    private String getSession(int position){
        String hotline="";
        try {
            JSONObject json = result.getJSONObject(position);
            hotline = json.getString(WebConfig.TAG_HOTLINE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hotline;
    }


    //this method will execute when we pick an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        textViewName.setText(getName(position));
        textViewCourse.setText(getCourse(position));
        textViewSession.setText(getSession(position));
        sharedpreferences.edit().putInt("PREF_SPINNER", position).apply();
        System.out.println("item was just selected");
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewName.setText("");
        textViewCourse.setText("");
        textViewSession.setText("");
    }

    public boolean checkInternetConnection() {
        Context context = getApplication();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean serverUp() {
        Intent serverup = new Intent(Settings.this, ServerUp.class);
        startService(serverup);
        return sharedpreferences.getBoolean("web_status_result", false);
    }

    //public boolean validXML() {
    //    return sharedpreferences.getString()
    //}

    class asyncxml extends AsyncTask<Void, Void, Void> {
        Context context = getApplication();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            System.out.println("Fetching XML");
            String configURL = sharedpreferences.getString("config_url", "nothing yet");
            System.out.println("configURL is " +configURL);

            HandleXML obj = new HandleXML(configURL);
            obj.fetchXML();

            while(true) {
                if (!(obj.parsingComplete)) break;
            }

            String marquee_link = obj.getLink();
            String marquee_key = obj.getTitle();
            String marquee_description = obj.getDescription();
            String app_title = obj.getEditor();
            String colors_list = obj.getColorslist();
            String soundfile = obj.getSoundfile();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(marquee_description_push, marquee_description);
            editor.putString(marquee_link_push, marquee_link);
            editor.putString(marquee_key_push, marquee_key);
            editor.putString(app_title_push, app_title);
            editor.putString(sound_file_push, soundfile);
            editor.putString(colors_url_push, colors_list);
            editor.apply();

            //xmlverified = marquee_key.contains("321654987");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


        }
    }
}