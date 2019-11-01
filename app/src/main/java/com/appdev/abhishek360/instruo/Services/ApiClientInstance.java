package com.appdev.abhishek360.instruo.Services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.appdev.abhishek360.instruo.LoginActivity;
import com.appdev.abhishek360.instruo.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.appdev.abhishek360.instruo.LoginActivity.tosty;

public class ApiClientInstance{
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.instruo.in/v1/";
    private static NetworkConnectionListener mNetworkConnectionListener;
    private static AlertService alertService;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor spEditor;

    public static Retrofit getRetrofitInstance(Context ctx, NetworkConnectionListener networkConnectionListener) {
        mNetworkConnectionListener = networkConnectionListener;
        alertService = new AlertService(ctx);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getInstruoOkHttpClient(ctx).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance(Context ctx) {
        alertService = new AlertService(ctx);
        sharedPreferences = ctx.getSharedPreferences("instruoPref", MODE_PRIVATE);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getInstruoOkHttpClient(ctx).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void setNetworkConnectionListener(NetworkConnectionListener listener) {
        mNetworkConnectionListener = listener;
    }

    public static void removeNetworkConnectionListener() {
        mNetworkConnectionListener = null;
    }

    private static class SessionCookieJar implements CookieJar {
        //private Context context;
        //private List<Cookie> cookies;
//        private SharedPreferences sharedPreferences;
//        private SharedPreferences.Editor spEditor;

//        private SessionCookieJar(Context context) {
//            //this.context = context;
//            //sharedPreferences = context.getSharedPreferences("instruoPref", MODE_PRIVATE);
//        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (url.encodedPath().endsWith("login")) {
                spEditor = sharedPreferences.edit();
                String sessionId = cookies.toString().split(";")[0].split("=")[1];
                //Log.d("LOGIN_COOKIES",cookies.toString());
                spEditor.putString(LoginActivity.spSessionId,sessionId);
                spEditor.apply();
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            String instruoCookies =  sharedPreferences.getString(LoginActivity.spSessionId,null);
            if (!url.encodedPath().endsWith("login") && !url.encodedPath().endsWith("register") && instruoCookies != null) {
                Cookie newCookies = new Cookie
                        .Builder()
                        .name("session")
                        .value(instruoCookies)
                        .domain("api.instruo.in")
                        .httpOnly()
                        .build();

                List<Cookie> cookiesList = new ArrayList<>();
                cookiesList.add(newCookies);
                //Log.d("Cookie Created",cookiesList.toString());
                return cookiesList;
            }
            return Collections.emptyList();
        }
    }

//    private static OkHttpClient.Builder getUnsafeOkHttpClient(Context ctx) {
//
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.cookieJar(new SessionCookieJar(ctx));
//            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            builder.hostnameVerifier((hostname, session) -> true);
//            return builder;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static OkHttpClient.Builder getInstruoOkHttpClient(Context ctx) {
        try {
            // Load CAs from an InputStream
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            InputStream inputStream = ctx.getResources().openRawResource(R.raw.certificate); //(.crt)
            Certificate certificate = certificateFactory.generateCertificate(inputStream);
            inputStream.close();

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", certificate);

            // Create a TrustManager that trusts the CAs in our KeyStore.
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];

            // Create an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new SessionCookieJar());
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            builder.addInterceptor(new NetworkInterceptorService() {
                @Override
                public boolean isInternetAvailable() {
                    ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
                }

                @Override
                public void onInternetUnavailable() {
                    Log.e("NETWORK_UNAVAILABLE", "Not connected to Internet!");
                    alertService.showAlert("NETWORK_UNAVAILABLE", "Not connected to Internet!");
                    if (mNetworkConnectionListener != null) {
                        //mNetworkConnectionListener.onInternetUnavailable();
                    }
                }

                @Override
                public void onServerError() {
                    alertService.showToast("SERVER_ERROR: Please, Try Again!");
                    Log.e("SERVER_ERROR", "Please, Try Again!");
                }

                @Override
                public void notAuthorized() {
                    Log.e("SESSION_EXPIRED", "Please, Login Again!");
                    alertService.showToast("SESSION_EXPIRED: Please, Login Again!");
                    spEditor = sharedPreferences.edit();
                    spEditor.clear();
                    spEditor.apply();
                    Intent i = new Intent(ctx, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                }

            });
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
