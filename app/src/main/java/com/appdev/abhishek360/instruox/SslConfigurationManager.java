package com.appdev.abhishek360.instruox;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.appdev.abhishek360.instruox.LoginActivity.tosty;

public class SslConfigurationManager extends Fragment
{
    private Context  ctx;


    public boolean readUserData(final String eventId,final String token,final Context context)
    {


        ctx=context;


        HurlStack hurlStack = new HurlStack()
        {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException
            {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try
                {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext(),hurlStack);






        String URL = "https://instruo.in/api/v1/user";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestAction("UPDATE");
        jsonRequestAdapter.setRequestData("eventIdAdd",eventId);
        jsonRequestAdapter.setRequestParameteres("filter",null);



        final Gson json = new GsonBuilder().serializeNulls().create();



        final String jsonRequest = json.toJson(jsonRequestAdapter);





        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonRequest,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.get("responseStatus").equals("FAILED"))
                            {
                                tosty(context.getApplicationContext(),"Try Again! Failed To Register! ");
                                //progresBar.setVisibility(View.GONE);



                            }
                            if (response.get("responseStatus").equals("OK"))
                            {

                                tosty(context.getApplicationContext(),"Registered Successfully ! Please Check Registered Event Page for Payment status. ");

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        //Log.d("Response",""+response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("Error:",""+error);
                        //readUserData(token);

                        tosty(context.getApplicationContext(),"Trying Again: Network Error!");


                    }
                }


        )
        {
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", token);


                return params;
            }
        };

        requestQueue.add(objectRequest);

        return true;

    }


    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers)
    {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]
                {
                        new X509TrustManager()
                        {
                            public X509Certificate[] getAcceptedIssuers()
                            {
                                return originalTrustManager.getAcceptedIssuers();
                            }

                            public void checkClientTrusted(X509Certificate[] certs, String authType)
                            {
                                try
                                {
                                    if (certs != null && certs.length > 0)
                                    {
                                        certs[0].checkValidity();
                                    } else
                                    {
                                        originalTrustManager.checkClientTrusted(certs, authType);
                                    }
                                }
                                catch (CertificateException e)
                                {
                                    Log.w("checkClientTrusted", e.toString());
                                }
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType)
                            {
                                try
                                {
                                    if (certs != null && certs.length > 0)
                                    {
                                        certs[0].checkValidity();
                                    } else
                                    {
                                        originalTrustManager.checkServerTrusted(certs, authType);
                                    }
                                } catch (CertificateException e)
                                {
                                    Log.w("checkServerTrusted", e.toString());
                                }
                            }
                        }
                };
    }



    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException
    {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = ctx.getResources().openRawResource(R.raw.certificate); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();

    }


    public static HostnameVerifier getHostnameVerifier()
    {
        return new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
                // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                //return hv.verify("localhost", session);
            }
        };
    }





}
