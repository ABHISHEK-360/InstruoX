package com.appdev.abhishek360.instruo;

import android.content.Context;
import android.util.Log;

//import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseInstanceIDService extends FirebaseMessagingService
{
    private static final String TAG = "Firebase Notification";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);

        getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE).edit().putString(LoginActivity.spInstanceIdKey, s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
    }

    public static String getToken(Context context)
    {
        return context.getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE).getString(LoginActivity.spInstanceIdKey, "empty");
    }
}
