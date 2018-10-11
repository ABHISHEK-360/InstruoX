package com.appdev.abhishek360.instruox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.appdev.abhishek360.instruox.LoginActivity.spFullNameKey;
import static com.appdev.abhishek360.instruox.LoginActivity.tosty;

public class MyProfileActivity extends AppCompatActivity implements PersonalDetailsFragment.OnFragmentInteractionListener,RegisteredEventsFragment.OnFragmentInteractionListener
{
    private TabLayout tabs;
    private int tabCode;
    private SharedPreferences sharedPreferences;
    private OnAboutDataReceivedListener mAboutDataListener;
    private ViewPager vp;
    private ProgressBar progressBar;
    private ArrayList<String> eventsName;
    private ArrayList<String> eventsEntryFee;
    private ArrayList<String> paymentStatus;


    private ArrayList<String> accountDetails=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        tabCode=bundle.getInt("tabCode");
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        sharedPreferences= getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        progressBar=(ProgressBar)findViewById(R.id.myprofile_progressbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tabs = (TabLayout)findViewById(R.id.tab_layout);
        //tabCode= this.getArguments().getInt("tCode");

        /**   tabs.addTab(tabs.newTab().setText("Technical"));
         tabs.addTab(tabs.newTab().setText("Automaton"));
         tabs.addTab(tabs.newTab().setText("Workshops"));
         tabs.addTab(tabs.newTab().setText("Gaming"));
         tabs.addTab(tabs.newTab().setText("Exibitions"));
         tabs.addTab(tabs.newTab().setText("Shows"));


         tabs.setTabGravity(TabLayout.GRAVITY_FILL);*/


         vp = (ViewPager) findViewById(R.id.myprofile_pager);
        //final EventPagerAdapter epAdapter = new EventPagerAdapter(getFragmentManager(),tabs.getTabCount());
        // vp.setAdapter(epAdapter);
        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        // vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        readUserData(sharedPreferences.getString(LoginActivity.spAccessTokenKey,null));


    }

    public void SetUpViewPager(ViewPager viewPager)
    {
        EventPagerAdapter adapter = new EventPagerAdapter(getSupportFragmentManager(),2);
        adapter.AddFragmentPage(PersonalDetailsFragment.newInstance(accountDetails),"Account Details");
        adapter.AddFragmentPage(RegisteredEventsFragment.newInstance(eventsName,eventsEntryFee),"Registered Events");



        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(tabCode);

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
                                //progresBar.setVisibility(View.GONE);



                            }
                            if (response.get("responseStatus").equals("OK")&& (response.get("responseMessage")).equals("USER RETRIEVED SUCCESSFULLY"))
                            {


                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");

                                accountDetails.add(jsonData.get("userName").toString());
                                accountDetails.add(jsonData.get("userEmail").toString());

                                JSONArray obj = jsonData.getJSONArray("contact");

                                accountDetails.add(obj.getString(0));


                                JSONArray object=jsonData.getJSONArray("events");


                                int noOfRegisterEvents =object.length();
                                eventsName = new ArrayList<>();
                                eventsEntryFee= new ArrayList<>();
                                for(int i=0;i<noOfRegisterEvents;i++)
                                {
                                    String events=object.getString(i);
                                    JSONObject jsonEvents = new JSONObject(""+events);

                                    eventsName.add(jsonEvents.get("name").toString());
                                    eventsEntryFee.add(jsonEvents.get("entryFee").toString());



                                }
                                //List<Object> events= new ArrayList<Object>();

                                progressBar.setVisibility(View.GONE);
                                SetUpViewPager(vp);

                                //tosty(getApplicationContext(),"Events "+eventsName);
                                Log.d("Event Details:",""+eventsName);


                                //finish();












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
                        tosty(getApplicationContext(),"Trying Again: Network Error!");
                        readUserData(token);



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


    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    public interface  OnAboutDataReceivedListener
    {
        void onDataReceived(Map model);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener)
    {
        this.mAboutDataListener = listener;
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




    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
