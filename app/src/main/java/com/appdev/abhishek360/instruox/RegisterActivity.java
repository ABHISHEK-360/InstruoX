package com.appdev.abhishek360.instruox;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.appdev.abhishek360.instruox.LoginActivity.tosty;

public class RegisterActivity extends AppCompatActivity
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText username_edittext,email_edittext,password_edittext,fullname_edittext;
    private EditText confpass_edittext,college_edittext,contact_edittext;
    private String fullname_str,email_str,password_str,confpass_str,
            contact_str,username_str,college_str;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    private static final int CHOOSE_IMAGE_CODE = 101 ;

    jsonResponseAdapter responseAdapter;

    private ProgressBar reg_progressBar;
    private Button register_button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle=getIntent().getExtras();


        fullname_edittext= (EditText)findViewById(R.id.reg_fullName_edittext);
        email_edittext= (EditText)findViewById(R.id.reg_email_edittext);
        contact_edittext=(EditText)findViewById(R.id.reg_phone_edittext);
        college_edittext=(EditText)findViewById(R.id.reg_college_edittext);
        username_edittext=(EditText)findViewById(R.id.reg_userName_edittext);
        confpass_edittext=(EditText)findViewById(R.id.reg_confpassword_edittext);
        password_edittext= (EditText)findViewById(R.id.reg_password_edittext);
        register_button= (Button) findViewById(R.id.reg_submit_button);
        reg_progressBar=(ProgressBar)findViewById(R.id.reg_progressbar);
        sharedPreferences=getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);

        try
        {
            fullname_str=bundle.getString("name");
            email_str=bundle.getString("email");

            fullname_edittext.setText(fullname_str);
            email_edittext.setText(email_str);
        }
        catch (Exception e)
        {
            Log.d("Register Page:","Normal Login-"+e);
        }

        register_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                validateDetails();

            }
        });
    }

    private void validateDetails()
    {
        fullname_str=fullname_edittext.getText().toString();
        email_str=email_edittext.getText().toString();
        contact_str=contact_edittext.getText().toString();
        college_str=college_edittext.getText().toString();
        username_str=username_edittext.getText().toString();
        password_str=password_edittext.getText().toString();
        confpass_str=confpass_edittext.getText().toString();

        if(fullname_str.isEmpty())
        {
            fullname_edittext.setError("Please,Enter User Name!");
            fullname_edittext.requestFocus();
            return;
        }
        else if(email_str.isEmpty())
        {
            email_edittext.setError("Email is Required");
            email_edittext.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
        {
            email_edittext.setError("Enter a valid Email id!");
            email_edittext.requestFocus();
            return;
        }




        if(contact_str.isEmpty())
        {
            contact_edittext.setError("Phone No. is Required");
            contact_edittext.requestFocus();
            return;
        }
        else if(!Patterns.PHONE.matcher(contact_str).matches()||contact_str.length()!=10)
        {
            contact_edittext.setError("Please,Enter a vaild  Phone No. without +91");
            contact_edittext.requestFocus();
            return;
        }
        else
        {
            //phone=Integer.parseInt(phone_string);
        }

        if(password_str.isEmpty())
        {
            password_edittext.setError("Password is Required");
            password_edittext.requestFocus();
            return;
        }
        else if(password_str.length()<6)
        {
            password_edittext.setError("Min. password length is 6");
            password_edittext.requestFocus();
            return;
        }

        if(confpass_str.isEmpty())
        {
            confpass_edittext.setError("Password not matched");
            confpass_edittext.requestFocus();
            return;
        }
        else if(!password_str.equals(confpass_str))
        {
            confpass_edittext.setError("Password not matched");
            confpass_edittext.requestFocus();
            return;
        }
        else
        {

            //imageProgressBar.setVisibility(View.VISIBLE);
            RegisterUser(email_str,password_str,fullname_str,username_str,contact_str,college_str);
        }



        return;

    }

    public void RegisterUser(final String email, final String pswd,final String name,final String username,final String contact,final String college)
    {


        //SSLContext sslContext= SslUtils.getSslContextForCertificateFile(this,"certificate.crt");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait!");
        builder.setMessage("Registering...");
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();

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



        //OkHttpClient client = new OkHttpClient.Builder()
        //        .sslSocketFactory(sslContext.getSocketFactory(),new Tru);


        String URL = "https://instruo.in/api/v1/user";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestAction("CREATE");
        jsonRequestAdapter.setRequestData("username",username);
        jsonRequestAdapter.setRequestData("name",name);
        jsonRequestAdapter.setRequestData("college",college);
        jsonRequestAdapter.setRequestData("contact",contact);

        jsonRequestAdapter.setRequestData("email",email);
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
                        reg_progressBar.setVisibility(View.GONE);
                        //tosty(getApplicationContext(),""+response);
                        Gson responseGson = new Gson();
                        responseAdapter= responseGson.fromJson(""+response,jsonResponseAdapter.class);
                        try
                        {
                            if (response.get("responseStatus").equals("FAILED"))
                            {
                                tosty(getApplicationContext(),"Registration Failed: "+response.get("responseMessage"));
                                alert.cancel();
                            }
                            if (response.get("responseStatus").equals("OK"))
                            {
                                tosty(getApplicationContext(),"Registered Successfully!");
                                alert.cancel();
                                logIn(username, pswd);

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        //Log.d("Response:",""+response);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        reg_progressBar.setVisibility(View.GONE);
                        tosty(getApplicationContext(),"Try Again: Network Error!");


                        Log.d("Error:",""+error);

                    }
                }


        );

        requestQueue.add(objectRequest);

    }



    public void logIn(final String email, final String pswd)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait!");
        builder.setMessage("Logging In...");
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);

        alert.show();



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
                                alert.cancel();

                            }
                            if (response.get("responseStatus").equals("OK"))
                            {
                                //Map<String,String> responseData = new HashMap<>();

                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");

                                String accessToken= jsonData.get("accessToken").toString();

                                spEditor=sharedPreferences.edit();
                                spEditor.putString(LoginActivity.spAccessTokenKey,accessToken);
                                spEditor.apply();

                                if(!accessToken.isEmpty())  readUserData(accessToken);
                                alert.cancel();





                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                        //Log.d("Response:",""+response);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("Error:",""+error);

                    }
                }


        );

        requestQueue.add(objectRequest);

    }




    public void readUserData(final String token)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait!");
        builder.setMessage("Fetching Details...");
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();

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
                                tosty(getApplicationContext(),"Network Issue: Try Again! ");
                                Log.d("Network Issue",""+response.get("responseMessage"));
                                alert.cancel();
                                //reg_progressBar.setVisibility(View.GONE);



                            }
                            if (response.get("responseStatus").equals("OK")&& (response.get("responseMessage")).equals("USER RETRIEVED SUCCESSFULLY"))
                            {


                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");


                                fullname_str= jsonData.get("userName").toString();
                                email_str= jsonData.get("userEmail").toString();
                                alert.cancel();


                                tosty(getApplicationContext(),"Hello! "+fullname_str);


                                spEditor=sharedPreferences.edit();
                                spEditor.putString(LoginActivity.spFullNameKey,fullname_str);
                                spEditor.putString(LoginActivity.spEmailKey,email_str);
                                spEditor.apply();

                                finish();

                                Intent in = new Intent(getApplicationContext(),HomeActivity.class);
                                in.putExtra("name",fullname_str);
                                in.putExtra("email",email_str);
                                in.putExtra("Url","test.com");
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
                        reg_progressBar.setVisibility(View.GONE);

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

    private void showPhotoChooser()
    {
        Intent in= new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(in,"Select Profile Image"),CHOOSE_IMAGE_CODE);

    }


}
