package com.sociallearn.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.sociallearn.app.utils.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TWITTER_KEY = "ynnj7aTTpdadv6x5fSSI7dQtL";
    private static final String TWITTER_SECRET = "khbdJeIZK0t6tUTKENRRVNeMCSttiUJnvx4hsA4rKJgpuzWd8t";
    AuthCallback nc;
    Spinner spinner1,gender;
    String gend,area,nam,phn,ag;
    EditText name,phno,age;
    ProgressDialog progressdiag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText)findViewById(R.id.name);
        phno = (EditText)findViewById(R.id.phno);
        age = (EditText)findViewById(R.id.age);

        progressdiag = new ProgressDialog(this);
        progressdiag.setIndeterminate(false);
        progressdiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdiag.setMessage("Loading...");

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        nc = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                /*Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();*/
                register(nam,phn,ag,gend,area);

            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        };
        gender = (Spinner) findViewById(R.id.gender);

        List<String> glist = new ArrayList<String>();
        glist.add("Male");
        glist.add("Female");

        ArrayAdapter<String> gdataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,glist);

        gdataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(gdataAdapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gend = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gend = (String)parent.getItemAtPosition(0);

            }
        });


        spinner1 = (Spinner) findViewById(R.id.area);

        List<String> list = new ArrayList<String>();
        list.add("Area1");
        list.add("Area2");
        list.add("Area3");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  area = (String)parent.getItemAtPosition(position);
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {
                  area = (String)parent.getItemAtPosition(0);

              }
        });
    }

    public void submit(View v){
        nam = name.getText().toString();
        phn = phno.getText().toString();
        ag = age.getText().toString();
        Digits.authenticate(nc,"+91"+phn);


    }

    void register(final String name,final String phn,final String age,final String gender,final String area){
        progressdiag.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://sociallearn-1310.appspot.com/_ah/api/userApi/v1/registerUser";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Waterwala", "Response is: " + response);
                        try {
                            SessionManager session = new SessionManager(getApplicationContext());
                            session.createLoginSession(phn,nam);
                            Intent in = new Intent(getApplicationContext(),CatagoryActivity.class);
                            startActivity(in);
                            finish();

                        }catch(Exception e){
                            Log.i("Waterwala", "error here: ");
                        }
                        //session.setMachineID("WPBR00001");

                        progressdiag.dismiss();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Waterwala", "That didn't work!");
            }

        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("age", age);
                params.put("gender", gender);
                params.put("userArea", area);
                params.put("userId", phn);
                params.put("userName", name);


                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

}
