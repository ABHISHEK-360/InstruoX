package com.appdev.abhishek360.instruox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashActivity extends AppCompatActivity
{

    private static int SPLASH_TIME_OUT=2000;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        final String tokenKey=sharedPreferences.getString(LoginActivity.spAccessTokenKey,null);
        final String fullName =sharedPreferences.getString(LoginActivity.spFullNameKey,null);
        String email =sharedPreferences.getString(LoginActivity.spEmailKey,null);


        ((AnimatedVectorDrawable) getWindow().getDecorView().getBackground()).start();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (tokenKey!=null)
                {
                    Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                    in.putExtra("name",fullName);
                    in.putExtra("email","test@test.com");
                    in.putExtra("Url","test.url");
                    startActivity(in);
                    finish();
                }
                else {

                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);




    }




}





