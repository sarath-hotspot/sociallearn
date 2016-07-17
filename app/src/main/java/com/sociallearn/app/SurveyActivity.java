package com.sociallearn.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurveyActivity extends BaseActivity {

    ProgressDialog progressdiag;
    LinearLayout ll,freetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressdiag = new ProgressDialog(this);
        progressdiag.setIndeterminate(false);
        progressdiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdiag.setMessage("Loading...");
        ll = (LinearLayout)findViewById(R.id.questions);
        freetext = (LinearLayout)findViewById(R.id.freetext);
        Bundle b = getIntent().getExtras();
        getSurveyQuestions(b.getString("id"));

    }

    void getSurveyQuestions(final String id){
        progressdiag.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sociallearn-1310.appspot.com/_ah/api/startupSurveyApi/v1/getSurveyQuestionsByStartupId?startupId="+id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Waterwala", "Response is: " + response);
                        try {
                            JSONObject output = new JSONObject(response);
                            JSONArray arry = output.getJSONArray("surveyQuestionList");
                            for(int i=0;i<arry.length();i++){
                                JSONObject obj = arry.getJSONObject(i);
                                String ques = obj.getString("question");
                                int type = obj.getInt("questionType");
                                if(type == 1){

                                    TextView tv = new TextView(SurveyActivity.this);
                                    tv.setText(ques);
                                    tv.setTextSize(20);
                                    tv.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));

                                    ll.addView(tv);
                                    ArrayList<String> spinnerArray = new ArrayList<String>();
                                    spinnerArray.add(obj.getString("option1"));
                                    spinnerArray.add(obj.getString("option2"));
                                    spinnerArray.add(obj.getString("option3"));
                                    spinnerArray.add(obj.getString("option4"));

                                    Spinner spinner = new Spinner(SurveyActivity.this);
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SurveyActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                                    spinner.setAdapter(spinnerArrayAdapter);
                                    spinner.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));

                                    ll.addView(spinner);



                                }
                                if(type == 2){

                                    TextView tv = new TextView(SurveyActivity.this);
                                    tv.setText(ques);
                                    tv.setTextSize(20);
                                    tv.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));

                                    freetext.addView(tv);
                                    EditText et = new EditText(SurveyActivity.this);
                                    et.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));

                                    freetext.addView(et);

                                }
                            }
                            //desc.setText(Html.fromHtml(output.getString("startupDescription")));
                            //packageName = output.getString("androidPackageId");
                            //setTitle(output.getString("startupName"));
                            //act.setTitle(output.getString("startupName"));




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
    public void submit(View view) {

        Intent in = new Intent();
        setResult(1,in);
        finish();

    }

}
