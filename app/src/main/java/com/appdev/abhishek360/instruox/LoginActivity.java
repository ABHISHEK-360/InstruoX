package com.appdev.abhishek360.instruox;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mklimek.sslutilsandroid.SslUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
{
    private TextView username,pass;
    private Button signIn,regi,register;
    private static final int REQUEST_CODE=9001;
    private GoogleApiClient googleApiClient;
    private ProgressBar progresBar;
    private String name="alpha",email="alpha@base",imgURL="alpha.com";



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private Set<String> eventsName;

    public static String spAccessTokenKey="ACCESS_TOKEN", spFullNameKey="FULL_NAME",spEmailKey="EMAIL",spKey="instruoPref",spEventsKey="REG_EVENTS";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Dialog myDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDailog = new Dialog(this);
        register = (Button)findViewById(R.id.register) ;
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        sharedPreferences= getSharedPreferences("instruoPref",MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });



    }
    public void showPopUp(View V)
    {
        myDailog.setContentView(R.layout.custompopup);
        username = (TextView)myDailog.findViewById(R.id.username);
        pass = (TextView)myDailog.findViewById(R.id.Password);
        signIn = (Button)myDailog.findViewById(R.id.signIn);
        regi = (Button)myDailog.findViewById(R.id.regi);
        progresBar=(ProgressBar)myDailog.findViewById(R.id.popup_progress_bar);

        FloatingActionButton closeBtn = (FloatingActionButton) myDailog.findViewById(R.id.closeDialog);

        myDailog.show();

        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String u_name= username.getText().toString();
                String pswd= pass.getText().toString();

                if(u_name.isEmpty())
                {
                    username.setFocusable(true);
                    username.setError("Enter Username!");
                    return;

                }
                else if(pswd.isEmpty())
                {
                    pass.setFocusable(true);
                    pass.setError("Enter Password!");
                    return;

                }
                else if(pswd.length()<4)
                {
                    pass.setFocusable(true);
                    pass.setError("Incorrect Password!");
                    return;

                }
                else
                {
                    progresBar.setVisibility(View.VISIBLE);
                    logIn(u_name,pswd);


                    //UserLoginTask mauth = new UserLoginTask(u_name,pswd);
                    //Boolean b=false;


                }

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDailog.dismiss();
            }
        });



        regi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDailog.dismiss();
            }
        });


    }

    public boolean logIn(final String email, final String pswd)
    {

        Boolean status=false;

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

        RequestQueue requestQueue = Volley.newRequestQueue(this,hurlStack);






        String URL = "https://instruo.in/api/v1/auth";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestAction("AUTH");
        jsonRequestAdapter.setRequestData("username",email);
        jsonRequestAdapter.setRequestData("password",pswd);

        final Gson json = new GsonBuilder().serializeNulls().create();



        final String jsonRequest = json.toJson(jsonRequestAdapter);

        //tosty(this,jsonRequest);
        //Log.d("JSON: ",jsonRequest);



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
                                tosty(getApplicationContext(),"LogIn Failed: "+response.get("responseMessage"));

                            }
                            if (response.get("responseStatus").equals("OK"))
                            {
                                //Map<String,String> responseData = new HashMap<>();

                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");


                                String accessToken= jsonData.get("accessToken").toString();
                                tosty(getApplicationContext(),"Logged In Successfully! ");

                                spEditor=sharedPreferences.edit();
                                spEditor.putString(spAccessTokenKey,accessToken);
                                spEditor.apply();
                                Boolean b=false;

                                if(!accessToken.isEmpty()) b= readUserData(accessToken);





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
                        progresBar.setVisibility(View.GONE);

                        tosty(getApplicationContext(),"Try Again: Wrong Credentials!");

                    }
                }


        );

        requestQueue.add(objectRequest);

        return true;

    }


    public boolean readUserData(final String token)
    {

         boolean status=false;

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

        RequestQueue requestQueue = Volley.newRequestQueue(this,hurlStack);






        String URL = "https://instruo.in/api/v1/user";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestAction("READ");
        jsonRequestAdapter.setRequestData("username","leo4");
        jsonRequestAdapter.setRequestData("password","null");
        jsonRequestAdapter.setRequestParameteres("filter",null);



        final Gson json = new GsonBuilder().serializeNulls().create();



        final String jsonRequest = json.toJson(jsonRequestAdapter);

        //tosty(this,jsonRequest);
        //Log.d("JSON: ",jsonRequest);



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
                                tosty(getApplicationContext(),"User Data Access Failed: "+response.get("responseMessage"));
                                progresBar.setVisibility(View.GONE);



                            }
                            if (response.get("responseStatus").equals("OK")&& (response.get("responseMessage")).equals("USER RETRIEVED SUCCESSFULLY"))
                            {


                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");


                                name= jsonData.get("userName").toString();
                                email= jsonData.get("userEmail").toString();

                                tosty(getApplicationContext(),"Hello! "+name);

                                JSONArray object=jsonData.getJSONArray("events");


                                int noOfRegisterEvents =object.length();
                                eventsName = new HashSet<>();
                                for(int i=0;i<noOfRegisterEvents;i++)
                                {
                                    String events=object.getString(i);
                                    JSONObject jsonEvents = new JSONObject(""+events);

                                    eventsName.add(jsonEvents.get("description").toString());



                                }


                                spEditor=sharedPreferences.edit();
                                spEditor.putString(spFullNameKey,name);
                                spEditor.putString(spEmailKey,email);
                                spEditor.putStringSet(spEventsKey,eventsName);
                                spEditor.apply();

                                finish();


                                myDailog.dismiss();
                                progresBar.setVisibility(View.GONE);

                                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                                in.putExtra("name",name);
                                in.putExtra("email",email);
                                in.putExtra("Url",imgURL);
                                startActivity(in);






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
                        progresBar.setVisibility(View.GONE);

                        tosty(getApplicationContext(),"Try Again: Network Error!");


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



    public void onClick(View V)
    {
        switch( V.getId())
        {
            case R.id.gSignIn:
                signIN();
                break;



        }
    }

    private void signIN()
    {
        Intent i =  Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i,REQUEST_CODE);


    }



    private void onRequest(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
             name = account.getDisplayName();
             email = account.getEmail();
             imgURL = account.getPhotoUrl().toString();
            Intent in = new Intent(LoginActivity.this,HomeActivity.class);
            in.putExtra("name",name);
            in.putExtra("email",email);
            in.putExtra("Url",imgURL);
            startActivity(in);


        }
    }

    public void skipLogin(View view)
    {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        intent.putExtra("Url",imgURL);

        startActivity(intent);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            onRequest(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    public static void tosty(Context ctx, String msg)
    {
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            //myDailog.dismiss();
            if(aBoolean)
            {
                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                in.putExtra("name",name);
                in.putExtra("email",email);
                in.putExtra("Url",imgURL);
                startActivity(in);
            }



        }

        @Override
        protected Boolean doInBackground(Void... params)
        {


            return logIn(mEmail,mPassword);
        }



        @Override
        protected void onCancelled()
        {

        }



    }


    private HostnameVerifier getHostnameVerifier()
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


    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers)
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
        InputStream caInput = getResources().openRawResource(R.raw.certificate); // this cert file stored in \app\src\main\res\raw folder path

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

}
