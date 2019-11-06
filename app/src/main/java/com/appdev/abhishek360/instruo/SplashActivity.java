package com.appdev.abhishek360.instruo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 2000;
    private static SharedPreferences sharedPreferences;
    private AnimatedVectorDrawable animatedVectorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE);

        final String sessionId = sharedPreferences.getString(LoginActivity.spSessionId,null);

        animatedVectorDrawable = ((AnimatedVectorDrawable) getWindow().getDecorView().getBackground());
        animatedVectorDrawable.start();

        new Handler().postDelayed(() -> {
            if (sessionId!=null) {
                Intent in = new Intent(getApplicationContext(), HomeActivity.class);
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

    public static SharedPreferences getAppPreferences(){
        return sharedPreferences;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(animatedVectorDrawable!=null){
            animatedVectorDrawable.start();
            animatedVectorDrawable = null;
        }
    }
}





