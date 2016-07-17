package com.sociallearn.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sociallearn.app.utils.SessionManager;
import com.sociallearn.app.utils.StartupObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends BaseActivity {

    ProgressDialog progressdiag;
    TextView desc;
    String startupid;
    Button button,survey,button2;
    String packageName;
    SessionManager sessionManager;
    ImageView iv;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv = (ImageView) findViewById(R.id.prof_img_picBackground);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setTitle("WaterWala");

        progressdiag = new ProgressDialog(this);
        progressdiag.setIndeterminate(false);
        progressdiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdiag.setMessage("Loading...");
        Bundle b = getIntent().getExtras();
        startupid = b.getString("id");

        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button2.setVisibility(View.VISIBLE);
        survey = (Button)findViewById(R.id.survey);
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(),SurveyActivity.class);
                in.putExtra("id",startupid);
                startActivityForResult(in,1);
                //survey.setVisibility(View.GONE);
                //button2.setText("Help Others");

            }
        });

        setTitle(b.getString("name"));
        sessionManager = new SessionManager(this);
        /*String status = b.getString("status");
        if(status.equals("Installed")){

            button.setText("Open App");
            button2.setText("Need Help?");
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
        }*/
        desc = (TextView)findViewById(R.id.description);

        getStartupDetails(startupid);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            sessionManager.updateStatus(packageName,"mentor");
            button2.setText("Help Others");
            survey.setVisibility(View.GONE);
        }
    }

    public void reward(View v){

        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setTitle("Reward")
                .setMessage("Please install to get a reward of X amount")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
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
                            Picasso.with(getApplicationContext()).load(output.getString("bannerUrl")).into(iv);
                            //setTitle(output.getString("startupName"));
                            updateButtons(packageName);




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
    void updateButtons(String key){
        String val = sessionManager.getStatus(key);
        if(val.equals("Fresh")){
            button.setText("Try Now");
            button2.setVisibility(View.GONE);
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
        else if(val.equals("Installed")){
            button.setText("Open App");
            button2.setText("Need Help?");
            survey.setVisibility(View.VISIBLE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    learnerForMentor(startupid,sessionManager.getPhno());
                }
            });


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
        else if(val.equals("mentor")){
            button.setText("Open App");
            button2.setText("Help Others");
            survey.setVisibility(View.GONE);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mentorForLearner(startupid,sessionManager.getPhno());
                }
            });
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
    }
    void learnerForMentor(final String id,final String userid){
        progressdiag.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sociallearn-1310.appspot.com/_ah/api/userActivityApi/v1/userLookingForMentor";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Waterwala", "Response is: " + response);
                        try {





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
                params.put("startupId", id);
                params.put("userId", userid);
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
    void mentorForLearner(final String id,final String userid){
        progressdiag.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sociallearn-1310.appspot.com/_ah/api/userActivityApi/v1/userRequestedToEnrollAsMentor";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Waterwala", "Response is: " + response);
                        try {





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
                params.put("startupId", id);
                params.put("userId", userid);
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

}
