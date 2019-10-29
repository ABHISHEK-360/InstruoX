package com.appdev.abhishek360.instruo.Services;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class NetworkInterceptorService implements Interceptor {
    public abstract boolean isInternetAvailable();
    public abstract void onInternetUnavailable();
    public abstract void onServerError();
    public abstract void notAuthorized();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isInternetAvailable()) {
            onInternetUnavailable();
        }

        Response response = chain.proceed(request);

        if (response.code() == 500) {
            onServerError();
            return response;
        }

        if (response.code() == 401) {
            notAuthorized();
            return response;
        }

        return chain.proceed(request);
    }
}
