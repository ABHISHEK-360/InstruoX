package com.appdev.abhishek360.instruox;

import android.content.Intent;
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


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((AnimatedVectorDrawable) getWindow().getDecorView().getBackground()).start();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent loginIntent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();

            }
        },SPLASH_TIME_OUT);




    }




}





