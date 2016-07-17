package com.sociallearn.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.view.Window;



public class StartActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        boolean flag = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        in = new Intent(this, CatagoryActivity.class);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        System.exit(0);
    }
}
