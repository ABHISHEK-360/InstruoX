package com.appdev.abhishek360.instruox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=2000;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        final String tokenKey=sharedPreferences.getString(LoginActivity.spAccessTokenKey,null);
        final String fullName =sharedPreferences.getString(LoginActivity.spFullNameKey,null);
        final String email =sharedPreferences.getString(LoginActivity.spEmailKey,null);

        ((AnimatedVectorDrawable) getWindow().getDecorView().getBackground()).start();
        new Handler().postDelayed(() -> {
            if (tokenKey!=null) {
                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                in.putExtra("name",fullName);
                in.putExtra("email",email);
                startActivity(in);
                finish();
            }
            else {
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}





