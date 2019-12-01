package com.appdev.abhishek360.instruo;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_TIME_OUT = 2000;
    public static int UPDATE_REQUEST_CODE = 999;
    private static SharedPreferences sharedPreferences;
    private AnimatedVectorDrawable animatedVectorDrawable;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash);


        sharedPreferences = getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE);

        sessionId = sharedPreferences.getString(LoginActivity.spSessionId,null);

        animatedVectorDrawable = ((AnimatedVectorDrawable) getWindow().getDecorView().getBackground());
        animatedVectorDrawable.start();

        FirebaseMessaging.getInstance().subscribeToTopic("SCHEDULES_UPDATES")
            .addOnCompleteListener(task -> {
                String msg = "Subscribed: SCHEDULES_UPDATES";
                if (!task.isSuccessful()) {
                    msg = "Failed to subscribe!";
                }
                Log.d("TOPIC_SUBSCRIBE", msg);
            });

        if(sessionId!=null){
            FirebaseMessaging.getInstance().subscribeToTopic("EVENTS_UPDATES")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed: EVENTS_UPDATES";
                    if (!task.isSuccessful()) {
                        msg = "Failed to subscribe!";
                    }
                    Log.d("TOPIC_SUBSCRIBE", msg);
                });
        }

        new Handler().postDelayed(() -> {
            if (sessionId!=null) {
                Intent in = new Intent(SplashActivity.this, HomeActivity.class);
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

//    private boolean checkUpdateAvailable(){
//        // Creates instance of the manager.
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
//
//// Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//// Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // For a flexible update, use AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                            appUpdateInfo,
//                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                            AppUpdateType.FLEXIBLE,
//                            // The current activity making the update request.
//                            this,
//                            // Include a request code to later monitor this update request.
//                            UPDATE_REQUEST_CODE);
//
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return false;
//    }

    public static SharedPreferences getAppPreferences(){
        return sharedPreferences;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE) {

            Log.d("UPDATE_FLOW!"," Result code: " + resultCode);
//            if (resultCode == RESULT_CANCELED) {
//                Log.d("UPDATE FLOW FAILED!"," Result code: " + resultCode);
//                if (sessionId!=null) {
//                    Intent in = new Intent(SplashActivity.this, HomeActivity.class);
//                    startActivity(in);
//                    finish();
//                }
//                else {
//                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(loginIntent);
//                    finish();
//                }
//                //finish();
//                return;
//            }
//            if (resultCode != RESULT_OK) {
//                Log.d("UPDATE FLOW FAILED!"," Result code: " + resultCode);
//                checkUpdateAvailable();
//            }
//            else finish();
        }
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





