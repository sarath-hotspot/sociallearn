package com.sociallearn.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CatagoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public void ecommerce(View view) {
        Intent in = new Intent(this,StartupListActivity.class);
        in.putExtra("interestid","5629499534213120");
        startActivity(in);

    }
    public void utilities(View view) {
        Intent in = new Intent(this,StartupListActivity.class);
        in.putExtra("interestid","5639445604728832");
        startActivity(in);

    }
    public void travel(View view) {
        Intent in = new Intent(this,StartupListActivity.class);
        in.putExtra("interestid","5649391675244544");
        startActivity(in);

    }
    public void services(View view) {
        Intent in = new Intent(this,StartupListActivity.class);
        in.putExtra("interestid","5707702298738688");
        startActivity(in);
    }
}
