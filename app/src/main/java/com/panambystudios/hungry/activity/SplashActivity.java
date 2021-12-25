package com.panambystudios.hungry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.panambystudios.hungry.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAuthentication();
            }
        }, 3000);
    }

    private void abrirAuthentication(){
        Intent i = new Intent(SplashActivity.this,AuthenticationActivity.class);
        startActivity(i);
        finish();
    }
}