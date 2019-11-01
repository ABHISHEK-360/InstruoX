package com.appdev.abhishek360.instruo.Services;

import android.content.SharedPreferences;

import com.appdev.abhishek360.instruo.SplashActivity;

import java.util.Set;

public class PreferencesManager {
    public static void saveStringPreferences(String key, String value) {
        SharedPreferences.Editor editor = SplashActivity.getAppPreferences().edit();
        editor.putString(key, value).apply();
    }

    public static void saveEventPreferences(String key, Set<String> value) {
        SharedPreferences.Editor editor = SplashActivity.getAppPreferences().edit();
        editor.putStringSet(key, value).apply();
    }

    public static String getPreferences(String keyValue) {
        return SplashActivity.getAppPreferences().getString(keyValue, null);
    }
}
