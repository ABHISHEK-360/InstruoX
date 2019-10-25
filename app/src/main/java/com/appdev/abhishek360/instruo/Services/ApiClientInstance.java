package com.appdev.abhishek360.instruo.Services;

import android.content.Context;
import android.content.SharedPreferences;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
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

public class ApiClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.instruo.in/v1/";

    public static Retrofit getRetrofitInstance(Context ctx) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient(ctx).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static class SessionCookieJar implements CookieJar {
        //private Context context;
        //private List<Cookie> cookies;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor spEditor;

        private SessionCookieJar(Context context) {
            //this.context = context;
            sharedPreferences = context.getSharedPreferences("instruoPref", MODE_PRIVATE);
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (url.encodedPath().endsWith("login")) {
                //this.cookies = new ArrayList<>(cookies);
                spEditor=sharedPreferences.edit();
                String sessionId = cookies.toString().split(";")[0].split("=")[1];
                Log.d("LOGIN_COOKIES",cookies.toString());
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
                Log.d("Cookie Created",cookiesList.toString());
                return cookiesList;
            }
            return Collections.emptyList();
        }
    }

    private static OkHttpClient.Builder getUnsafeOkHttpClient(Context ctx) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new SessionCookieJar(ctx));
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
