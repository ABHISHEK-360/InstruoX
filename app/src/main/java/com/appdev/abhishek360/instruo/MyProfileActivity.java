package com.appdev.abhishek360.instruo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

import static com.appdev.abhishek360.instruo.LoginActivity.spEventsKey;
import static com.appdev.abhishek360.instruo.LoginActivity.tosty;

public class MyProfileActivity extends AppCompatActivity implements PersonalDetailsFragment.OnFragmentInteractionListener,RegisteredEventsFragment.OnFragmentInteractionListener
{
    private TabLayout tabs;
    private int tabCode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private OnAboutDataReceivedListener mAboutDataListener;
    private ViewPager vp;
    private ProgressBar progressBar;
    private ArrayList<String> eventsName;
    private ArrayList<String> eventId;
    private TextView paymentStatus_textview,paymentId_textview,paymentAmt_textview,orderId_textview,email_textview,eventId_textview;

    private ArrayList<String> eventsEntryFee;
    private ArrayList<String> paymentStatus;
    private InstapayListener listener;
    private final String WEBHOOK_KEY="http://webhook.instruo.in/api/v1/payment/instamojohook";

    private Set<String> eventsNameSet= new HashSet<>();
    private Dialog dialog;

    private InstamojoPay instamojoPay;
    private String[] paymentDetail;
    private int eventIndex;
    private static Set<String> gamingEvents=new HashSet<>();
    static
    {
        gamingEvents.add("pubg");
        gamingEvents.add("chess");
        gamingEvents.add("csgo");
        gamingEvents.add("fifa");
        gamingEvents.add("nfs");

    }
    private static Set<String> myCollege=new HashSet<>();
    static
    {
        myCollege.add("iiest shibpur");
        myCollege.add("indian institute of engineering science and technology");
        myCollege.add("indian institute of engineering science and technology shibpur");
        myCollege.add("indian institute of engineering science and technology,shibpur");
        myCollege.add("indian institute of engineering science and technology, shibpur");


        myCollege.add("iiest");
        myCollege.add("iiests");
        myCollege.add("iiest,shibpur");
        myCollege.add("iiest, shibpur");

        myCollege.add("i.i.e.s.t");
        myCollege.add("i.i.e.s.t shibpur");
        myCollege.add("i.i.e.s.t,shibpur");
        myCollege.add("i.i.e.s.t, shibpur");
    }


    final public static Map<String ,Integer> KEY_EVENTS_FEE;
    static
    {
        KEY_EVENTS_FEE = new HashMap<>();

        KEY_EVENTS_FEE.put("pubg", 200);
        KEY_EVENTS_FEE.put("nfs", 100);
        KEY_EVENTS_FEE.put("chess", 100);
        KEY_EVENTS_FEE.put("csgo", 500);
        KEY_EVENTS_FEE.put("fifa", 100);

        KEY_EVENTS_FEE.put("mathemania", 100);
        KEY_EVENTS_FEE.put("war_of_titans", 100);
        KEY_EVENTS_FEE.put("battle_of_bards", 100);
        KEY_EVENTS_FEE.put("innovation_challenge", 100);
        KEY_EVENTS_FEE.put("wonder_girls", 100);
        KEY_EVENTS_FEE.put("prashnavali", 100);
        KEY_EVENTS_FEE.put("face_off", 100);
        KEY_EVENTS_FEE.put("business_prototype", 100);
        KEY_EVENTS_FEE.put("mayday_mystery", 100);


        KEY_EVENTS_FEE.put("junkyard", 100);
        KEY_EVENTS_FEE.put("papyrus", 100);
        KEY_EVENTS_FEE.put("web_d", 100);
        KEY_EVENTS_FEE.put("access_denied", 100);
        KEY_EVENTS_FEE.put("ode_to_code", 100);
        KEY_EVENTS_FEE.put("boomerang", 100);
        KEY_EVENTS_FEE.put("electronicazz",100 );
        KEY_EVENTS_FEE.put("wiredin", 100);

        KEY_EVENTS_FEE.put("colosseum_15",2000 );
        KEY_EVENTS_FEE.put("colosseum_60",3000 );
        KEY_EVENTS_FEE.put("death_race", 800);
        KEY_EVENTS_FEE.put("robovengers", 800);
        KEY_EVENTS_FEE.put("relative_velocity_challenge", 800);
        KEY_EVENTS_FEE.put("soccred_games", 800);
        KEY_EVENTS_FEE.put("tri_wizard", 800);




    }



    private ArrayList<String> accountDetails=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        tabCode=bundle.getInt("tabCode");
        setContentView(R.layout.activity_my_profile);
        dialog= new Dialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        sharedPreferences= getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        progressBar=(ProgressBar)findViewById(R.id.myprofile_progressbar);





        tabs = (TabLayout)findViewById(R.id.tab_layout);
        //tabCode= this.getArguments().getInt("tCode");




         vp = (ViewPager) findViewById(R.id.myprofile_pager);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        // vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));



        readUserData(sharedPreferences.getString(LoginActivity.spAccessTokenKey,null));


    }



    public void showPopUp(String[] payDetails)
    {
        //View dialog = getLayoutInflater().inflate(R.layout.payment_popup, null);
        //PopupWindow popupWindow = new PopupWindow(dialog,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);


        dialog.setContentView(R.layout.payment_popup);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        paymentStatus_textview= (TextView)dialog.findViewById(R.id.payment_popup_status);
        paymentAmt_textview= (TextView)dialog.findViewById(R.id.payment_popup_amt);
        orderId_textview= (TextView)dialog.findViewById(R.id.payment_popup_orderId);
        paymentId_textview= (TextView)dialog.findViewById(R.id.payment_popup_paymentId);
        email_textview= (TextView)dialog.findViewById(R.id.payment_popup_email);
        eventId_textview= (TextView)dialog.findViewById(R.id.payment_popup_eventId);


        paymentStatus_textview.setText(payDetails[0]);
        paymentAmt_textview.setText("amount= ");
        orderId_textview.setText(payDetails[1]);
        paymentId_textview.setText(payDetails[3]);
        email_textview.setText("email= "+accountDetails.get(1));
        eventId_textview.setText("event= ");





        ImageButton closeBtn = (ImageButton) dialog.findViewById(R.id.payment_close_dialog);




        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                Intent  in = new Intent(getApplicationContext(),HomeActivity.class);
                in.putExtra("name",accountDetails.get(0));
                in.putExtra("email",accountDetails.get(1));
                startActivity(in);
                finish();

            }
        });



    }

    public void SetUpViewPager(ViewPager viewPager)
    {
        EventPagerAdapter adapter = new EventPagerAdapter(getSupportFragmentManager(),2);
        adapter.AddFragmentPage(PersonalDetailsFragment.newInstance(accountDetails),"Account Details");
        adapter.AddFragmentPage(RegisteredEventsFragment.newInstance(eventsName,eventsEntryFee,paymentStatus),"Registered Events");



        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(tabCode);

    }




    public boolean readUserData(final String token)
    {



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
                            if (response.get("responseStatus").equals("FAILED")&&response.get("responseMessage").equals("Not Authorized2!!!"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setTitle("Session Expired!");
                                builder.setCancelable(false);
                                builder.setMessage("Please Login Again to Continue!");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                        Intent  in = new Intent(getApplicationContext(),LoginActivity.class);
                                        spEditor=sharedPreferences.edit();
                                        spEditor.clear();
                                        spEditor.apply();
                                        startActivity(in);
                                        finish();

                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                                //tosty(getApplicationContext(),"User Data Access Failed: "+response.get("responseMessage"));


                                //progresBar.setVisibility(View.GONE);



                            }
                            if (response.get("responseStatus").equals("OK")&& (response.get("responseMessage")).equals("USER RETRIEVED SUCCESSFULLY"))
                            {


                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");

                                accountDetails.add(jsonData.get("userName").toString());
                                accountDetails.add(jsonData.get("userEmail").toString());
                                accountDetails.add(jsonData.get("college").toString());

                                JSONArray obj = jsonData.getJSONArray("contact");

                                accountDetails.add(obj.getString(0));


                                JSONArray object=jsonData.getJSONArray("events");
                                JSONArray objectPayments=jsonData.getJSONArray("payments");


                                int noOfRegisterEvents =object.length();
                                int noOfPayments=objectPayments.length();
                                eventsName = new ArrayList<>();
                                eventId= new ArrayList<>();
                                eventsEntryFee= new ArrayList<>();
                                paymentStatus= new ArrayList<>();

                                eventsName.add("Accommodation Fee");
                                eventsEntryFee.add("200");
                                eventId.add("accommodation_fee");
                                eventsNameSet.add("accommodation_fee");

                                eventsName.add("Buy Coupon*");
                                eventsEntryFee.add("500");
                                eventId.add("coupon_500");
                                eventsNameSet.add("coupon_500");

                                eventsName.add("Buy Coupon**");
                                eventsEntryFee.add("1000");
                                eventId.add("coupon_1000");
                                eventsNameSet.add("coupon_1000");

                                for(int i=0;i<noOfRegisterEvents;i++)
                                {
                                    String events=object.getString(i);
                                    JSONObject jsonEvents = new JSONObject(""+events);


                                    eventsName.add(jsonEvents.get("name").toString());
                                    eventsEntryFee.add(""+KEY_EVENTS_FEE.get(jsonEvents.get("description").toString()));
                                    eventId.add(jsonEvents.get("description").toString());

                                    eventsNameSet.add(jsonEvents.get("description").toString());




                                }

                                for (int i=0;i<noOfPayments;i++)
                                {
                                    String payments=objectPayments.getString(i);

                                    JSONObject jsonPayments = new JSONObject(""+payments);
                                    paymentStatus.add( jsonPayments.get("description").toString());

                                }

                                //List<Object> events= new ArrayList<Object>();

                                spEditor=sharedPreferences.edit();
                                spEditor.putStringSet(spEventsKey,eventsNameSet).apply();


                                progressBar.setVisibility(View.GONE);
                                SetUpViewPager(vp);

                                //tosty(getApplicationContext(),"Events "+eventsName);
                                //Log.d("Event Details:",""+eventsName);





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
                       finish();



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

    public void payAmt(final int index,final String regFee_str)
    {

        //String purpose = "Paying "+regFee_str+"for "+eventsName.get(index)+","+eventId.get(index);
        //tosty(getApplicationContext(),purpose);

        eventIndex=index;

        if(validateCollege(accountDetails.get(2),eventId.get(index)))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Free Registration!");
            builder.setMessage("Participation for IIEST Shibpur students is free except Gaming Events.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();

                }
            });

            AlertDialog alert = builder.create();
            alert.show();

            return;


        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Alert!");
        builder.setMessage("Pay Registration Fee Rs."+regFee_str);
        builder.setPositiveButton("Pay", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                callInstamojoPay(accountDetails.get(1),accountDetails.get(3),regFee_str,eventId.get(index),accountDetails.get(0));
                dialog.cancel();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


        //String arr[] ={"ghkjetdrfg","fcgvjhkjh","yfrtuygiuho","sertdryfuvgbhkjn","txdfchgvjbkjl"};
        //showPopUp(arr);
    }

    private boolean validateCollege(String college,String eventId)
    {
        if(myCollege.contains(college.toLowerCase()))
        {

            if(gamingEvents.contains(eventId)) return false;
            else return true;

        }



        return false;
    }

    public ArrayList<String> getEventId()
    {
        return eventId;
    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername)
    {
        tosty(getApplicationContext(),purpose);
        final Activity activity=  this;
        instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        activity.registerReceiver(instamojoPay,filter);
        JSONObject pay = new JSONObject();
        try
        {
            pay.put("email",email);
            pay.put("phone",phone);
            pay.put("amount",amount);
            pay.put("purpose",purpose);
            pay.put("name",buyername);
            pay.put("webhook",WEBHOOK_KEY);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity,pay,listener);
       // showPopUp(paymentDetail);

    }

    private void initListener()
    {

        listener = new InstapayListener()
        {


            @Override
            public void onSuccess(String s)

            {
                tosty(getApplicationContext(),"Payment Successful: "+s);
                 paymentDetail=s.split(":");

                showAlert(paymentDetail);

                //showPopUp(paymentDetail);



            }

            @Override
            public void onFailure(int i, String s)
            {
                tosty(getApplicationContext(),"Failed: "+s);
                s="status=failed:orderId=null:txnId=null:paymentId=null:token=null";
                paymentDetail=s.split(":");

                showAlert(paymentDetail);




                //unregisterReceiver(instamojoPay);

            }



        };
    }

    public void showAlert(final String[] payDetail)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(""+payDetail[0]);
        builder.setCancelable(false);
        builder.setMessage(""+payDetail[1]+"\n"+payDetail[3]+"\nAmount= "+eventsEntryFee.get(eventIndex)+"\nEventName= "+eventsName.get(eventIndex)+"\nEmail= "+accountDetails.get(1)+"\n\n   *Receipt will be sent to registered Email.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                Intent  in = new Intent(getApplicationContext(),HomeActivity.class);
                in.putExtra("name",accountDetails.get(0));
                in.putExtra("email",accountDetails.get(1));
                startActivity(in);
                finish();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onPause()
    {
        super.onPause();

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
