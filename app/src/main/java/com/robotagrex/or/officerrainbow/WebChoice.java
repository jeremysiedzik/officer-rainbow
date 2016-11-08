package com.robotagrex.or.officerrainbow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WebChoice extends AppCompatActivity implements Spinner.OnItemSelectedListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webchoice);

        Button buttonnext = (Button) findViewById(R.id.buttonnext);
        assert buttonnext != null;

        //Initializing the ArrayList
        sites = new ArrayList<>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        //Initializing TextViews
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCourse = (TextView) findViewById(R.id.textViewCourse);
        textViewSession = (TextView) findViewById(R.id.textViewSession);

        //This method will fetch the data from the URL
        getData();

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qoneintent = new Intent(WebChoice.this, UI.class);
                startActivity(qoneintent);
            }
        });
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
                            getStudents(result);
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

    private void getStudents(JSONArray j){
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
        spinner.setAdapter(new ArrayAdapter<>(WebChoice.this, android.R.layout.simple_spinner_dropdown_item, sites));
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


    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        textViewName.setText(getName(position));
        textViewCourse.setText(getCourse(position));
        textViewSession.setText(getSession(position));
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewName.setText("");
        textViewCourse.setText("");
        textViewSession.setText("");
    }
}