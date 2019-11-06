package com.appdev.abhishek360.instruo.Services;


import com.appdev.abhishek360.instruo.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class NetworkInterceptorService implements Interceptor {
    public abstract boolean isInternetAvailable();
    public abstract void onInternetUnavailable();
    public abstract void onServerError();
    public abstract void notAuthorized();

    private void saveRegEvents(ResponseBody body) {
        Gson gson = new Gson();
        Type userType = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();

        try {
            List<HashMap<String, String>> userList = gson.fromJson(body.string(), userType);
            Set<String> regEvents = new HashSet<>();
            for (HashMap<String, String> object : userList){
                regEvents.add(object.get("event_key"));
            }

            PreferencesManager.saveEventPreferences(LoginActivity.spEventsKey, regEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isInternetAvailable()) {
            onInternetUnavailable();
        }

        Response response = chain.proceed(request);

        HttpUrl url = response.request().url();

        if(url.encodedPath().endsWith("user/events") && response.code() == 200 && request.method().equals("GET")){

            Response res = response;
            if (res.body() != null) {
                saveRegEvents(res.body());
            }

            return chain.proceed(request);
        }

        if (response.code() == 500) {
            onServerError();
            return response;
        }

        if (response.code() == 401) {
            notAuthorized();
            return response;
        }

        return response;

        //return chain.proceed(request);
    }
}
