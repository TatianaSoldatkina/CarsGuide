package com.carsguide.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.carsguide.R;


public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 2000; // 2 sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainCarsListActivity.class);
                startActivity(intent);
                finish();


            }
        }, SPLASH_DURATION);
    }
}