package com.cvn.cmsgd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doSuccess();
            }
        }, 1600);
    }

    private void doSuccess() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }
}
