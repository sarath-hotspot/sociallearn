package com.sociallearn.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sociallearn.app.utils.SessionManager;
import com.sociallearn.app.utils.StartupObject;
import com.sociallearn.app.utils.StartupRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StartupListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    ProgressDialog progressdiag;
    ArrayList results;
    List<ApplicationInfo> list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "StartupListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        results = new ArrayList<StartupObject>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StartupRecyclerViewAdapter(getApplicationContext(),results);
        mRecyclerView.setAdapter(mAdapter);

        progressdiag = new ProgressDialog(this);
        progressdiag.setIndeterminate(false);
        progressdiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdiag.setMessage("Loading...");

        PackageManager packageManager = getPackageManager();
        list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);


        Bundle b = getIntent().getExtras();
        String id = b.getString("interestid");
        getStartups(id);

        // Code to Add an item with default animation
        //((StartupRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((StartupRecyclerViewAdapter) mAdapter).deleteItem(index);
    }
    boolean isVisited(String packagename){

        SessionManager sessionManager =new SessionManager(this);
        ArrayList<String> arry = sessionManager.getApps();

        if(arry.contains(packagename)){

            return true;
        }
        return false;
    }
    boolean checkApp(String packagename){
        for(int i = 0;i<list.size();i++) {
            ApplicationInfo ai = list.get(i);
            if(ai.packageName.equals(packagename)) {


                return true;
            }
            //Log.i("tezt", ai.packageName);
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((StartupRecyclerViewAdapter) mAdapter).setOnItemClickListener(new StartupRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Log.i(LOG_TAG, " Clicked on Item " + position);
                Log.i(LOG_TAG, " Clicked on Item2 " + ((StartupObject)results.get(position)).getId());
                Intent in = new Intent(getApplicationContext(),ScrollingActivity.class);
                in.putExtra("id",((StartupObject)results.get(position)).getId());
                in.putExtra("status",((StartupObject)results.get(position)).getStatus());
                in.putExtra("name",((StartupObject)results.get(position)).getmText1());
                startActivity(in);
            }
        });
    }

    private ArrayList<StartupObject> getDataSet() {
        //ArrayList results = new ArrayList<StartupObject>();
        /*for (int index = 0; index < 5; index++) {
            StartupObject obj = new StartupObject("12","url","India daily gold prices",
                    "awesome app");
            results.add(index, obj);
        }*/
        return results;
    }
    void getStartups(final String id){
        progressdiag.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sociallearn-1310.appspot.com/_ah/api/startupApi/v1/getStartupsByInterest?interestId="+id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Waterwala", "Response is: " + response);
                        try {
                            JSONObject output = new JSONObject(response);

                                JSONArray startups = output.getJSONArray("startupSummaryList");
                                for(int i = 0;i< startups.length();i++) {

                                    JSONObject startup = startups.getJSONObject(i);
                                    StartupObject so = new StartupObject(startup.getString("startupId"),startup.getString("iconUrl"),startup.getString("startupName"),startup.getString("startupSmallDesc"),startup.getString("androidPackage"));
                                    if(checkApp(startup.getString("androidPackage"))) {
                                        Log.i("Waterwala", "inside ");
                                         if(isVisited(startup.getString("androidPackage"))){

                                            so.setStatus("Installed");
                                            results.add(so);
                                        }
                                    } else {
                                        so.setStatus("Try Now");
                                        results.add(so);
                                    }
                                 mAdapter.notifyDataSetChanged();


                            }

                        }catch(Exception e){
                            Log.i("Waterwala", "exception ");
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