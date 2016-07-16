package com.sociallearn.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sociallearn.app.utils.SessionManager;
import com.sociallearn.app.utils.StartupObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScrollingActivity extends AppCompatActivity {

    ProgressDialog progressdiag;
    TextView desc;
    String startupid;
    Button button,survey;
    String packageName;
    SessionManager sessionManager;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setTitle("WaterWala");

        progressdiag = new ProgressDialog(this);
        progressdiag.setIndeterminate(false);
        progressdiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdiag.setMessage("Loading...");
        Bundle b = getIntent().getExtras();
        startupid = b.getString("id");
        button = (Button)findViewById(R.id.button);
        survey = (Button)findViewById(R.id.survey);
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),SurveyActivity.class);
                in.putExtra("id",startupid);
                startActivity(in);
            }
        });

        setTitle(b.getString("name"));
        sessionManager = new SessionManager(this);
        String status = b.getString("status");
        if(status.equals("Installed")){

            button.setText("Open App");
            survey.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                    if (launchIntent != null) {
                        startActivity(launchIntent);//null pointer check in case package name was not found
                    }
                }
            });
        }
        else{
            button.setText("Try Now");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionManager.addToVisited(packageName);
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+packageName)));
                    }
                    finish();

                }
            });
        }
        desc = (TextView)findViewById(R.id.description);

        getStartupDetails(startupid);


    }

    void setTitleName(String name){
        setTitle(name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getStartupDetails(final String id){
        progressdiag.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sociallearn-1310.appspot.com/_ah/api/startupApi/v1/getStartupDetails?startupId="+id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Waterwala", "Response is: " + response);
                        try {
                            JSONObject output = new JSONObject(response);
                            desc.setText(Html.fromHtml(output.getString("startupDescription")));
                            packageName = output.getString("androidPackageId");
                            //setTitle(output.getString("startupName"));





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

        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
}
