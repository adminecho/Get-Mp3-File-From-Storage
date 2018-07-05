package com.getmp3filefromstorage;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {


    private int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


        new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_TIME_OUT);

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
