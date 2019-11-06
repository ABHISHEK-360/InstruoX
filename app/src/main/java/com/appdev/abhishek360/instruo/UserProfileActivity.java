package com.appdev.abhishek360.instruo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.appdev.abhishek360.instruo.Adapters.EventPagerAdapter;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;
import com.appdev.abhishek360.instruo.Services.AlertService;
import com.appdev.abhishek360.instruo.Services.ApiClientInstance;
import com.appdev.abhishek360.instruo.Services.ApiServices;
import com.appdev.abhishek360.instruo.Services.NetworkConnectionListener;
import com.appdev.abhishek360.instruo.UserProfileFragments.RegisteredEventsFragment;
import com.appdev.abhishek360.instruo.UserProfileFragments.UserDetailsFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.appdev.abhishek360.instruo.LoginActivity.spEmailKey;
import static com.appdev.abhishek360.instruo.LoginActivity.spFullNameKey;
import static com.appdev.abhishek360.instruo.LoginActivity.tosty;

public class UserProfileActivity extends AppCompatActivity implements NetworkConnectionListener {
    private TabLayout tabs;
    private int tabCode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private OnAboutDataReceivedListener mAboutDataListener;
    private ViewPager vp;
    private ProgressBar progressBar;

    private TextView paymentStatus_textview, paymentId_textview, paymentAmt_textview, orderId_textview, email_textview, eventId_textview;

    private ArrayList<String> regEventId = new ArrayList<>();
    private ArrayList<String> regEvents =  new ArrayList<>();
    private ArrayList<String> regEventsFee =  new ArrayList<>();
    private ArrayList<Integer> paymentStatus = new ArrayList<>();
    private Set<String> eventsNameSet = new HashSet<>();
    private InstapayListener listener;
    private AlertService alertService;
    private final String WEBHOOK_KEY = "http://webhook.instruo.in/api/v1/payment/instamojohook";

    private Dialog dialog;
    private InstamojoPay instamojoPay;
    private String[] paymentDetail;
    private int eventIndex;
    private static Set<String> gamingEvents = new HashSet<>();

    private ApiServices apiService;
    private CompositeDisposable compositeDisposable;

    static {
        gamingEvents.add("pubg");
        gamingEvents.add("chess");
        gamingEvents.add("csgo");
        gamingEvents.add("fifa");
    }

    private static Set<String> myCollege = new HashSet<>();

    static {
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

    final public static Map<String, Integer> KEY_EVENTS_FEE;

    static {
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
        KEY_EVENTS_FEE.put("electronicazz", 100);
        KEY_EVENTS_FEE.put("wiredin", 100);

        KEY_EVENTS_FEE.put("colosseum_15", 2000);
        KEY_EVENTS_FEE.put("colosseum_60", 3000);
        KEY_EVENTS_FEE.put("death_race", 800);
        KEY_EVENTS_FEE.put("robovengers", 800);
        KEY_EVENTS_FEE.put("relative_velocity_challenge", 800);
        KEY_EVENTS_FEE.put("soccred_games", 800);
        KEY_EVENTS_FEE.put("tri_wizard", 800);
    }

    private ArrayList<String> accountDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabCode = getIntent().getIntExtra("tabCode", 0);
        setContentView(R.layout.activity_my_profile);
        dialog = new Dialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        compositeDisposable = new CompositeDisposable();
        alertService = new AlertService(this);
        apiService = ApiClientInstance
                .getRetrofitInstance(getApplicationContext())
                .create(ApiServices.class);
        sharedPreferences = getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE);
        progressBar = (ProgressBar) findViewById(R.id.myprofile_progressbar);

        tabs = (TabLayout) findViewById(R.id.tab_layout);
        //tabCode= this.getArguments().getInt("tCode");

        vp = (ViewPager) findViewById(R.id.myprofile_pager);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        // vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));

        readUserData();
    }

    public void showPopUp(String[] payDetails) {
        //View dialog = getLayoutInflater().inflate(R.layout.payment_popup, null);
        //PopupWindow popupWindow = new PopupWindow(dialog,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setContentView(R.layout.payment_popup);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        paymentStatus_textview = (TextView) dialog.findViewById(R.id.payment_popup_status);
        paymentAmt_textview = (TextView) dialog.findViewById(R.id.payment_popup_amt);
        orderId_textview = (TextView) dialog.findViewById(R.id.payment_popup_orderId);
        paymentId_textview = (TextView) dialog.findViewById(R.id.payment_popup_paymentId);
        email_textview = (TextView) dialog.findViewById(R.id.payment_popup_email);
        eventId_textview = (TextView) dialog.findViewById(R.id.payment_popup_eventId);

        paymentStatus_textview.setText(payDetails[0]);
        paymentAmt_textview.setText("amount= ");
        orderId_textview.setText(payDetails[1]);
        paymentId_textview.setText(payDetails[3]);
        email_textview.setText("email= " + accountDetails.get(1));
        eventId_textview.setText("event= ");

        ImageButton closeBtn = (ImageButton) dialog.findViewById(R.id.payment_close_dialog);

        closeBtn.setOnClickListener(v -> {
            dialog.dismiss();
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(in);
            finish();
        });
    }

    public void SetUpViewPager(ViewPager viewPager) {
        EventPagerAdapter adapter = new EventPagerAdapter(getSupportFragmentManager(), 2);
        adapter.AddFragmentPage(UserDetailsFragment.newInstance(accountDetails), "Account Details");
        adapter.AddFragmentPage(RegisteredEventsFragment.newInstance(regEvents, regEventsFee, paymentStatus), "Registered Events");
        adapter.notifyDataSetChanged();

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(tabCode);
    }

    public void loadRegEvents(){
        Single<ArrayList<HashMap<String, String>>> res = apiService
                .getRegEvents();

        res.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<ArrayList<HashMap<String, String>>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(ArrayList<HashMap<String, String>> res) {
                    for (HashMap<String, String> object : res){
                        regEvents.add(object.get("name"));
                        regEventId.add(object.get("event_key"));
                        if(object.get("registration_fee")!=null) regEventsFee.add(object.get("registration_fee"));
                        else regEventsFee.add("800");
                        paymentStatus.add(0);
                    }

                    regEvents.add("Accommodation Fee");
                    regEventsFee.add("200");
                    regEventId.add("accommodation_fee");
                    paymentStatus.add(0);

                    regEvents.add("Buy Coupon*");
                    regEventsFee.add("600");
                    regEventId.add("coupon_600");
                    paymentStatus.add(0);

                    regEvents.add("Buy Coupon**");
                    regEventsFee.add("1000");
                    regEventId.add("coupon_1000");
                    paymentStatus.add(0);

                    progressBar.setVisibility(View.GONE);
                    SetUpViewPager(vp);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("USER_EVENTS_API_ERROR", "Failed", e);
                    progressBar.setVisibility(View.GONE);
                }
            });
    }

    public void readUserData() {
        Single<UserProfileModel> res = apiService
                .getUserProfile();

        res.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<UserProfileModel>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(UserProfileModel res) {
                    accountDetails.add(res.getName());
                    accountDetails.add(res.getEmail());
                    accountDetails.add(res.getCollege());
                    accountDetails.add(res.getContact());

                    spEditor = sharedPreferences.edit();
                    spEditor.putString(spFullNameKey, res.getName());
                    spEditor.putString(spEmailKey, res.getEmail());
                    spEditor.apply();

                    loadRegEvents();
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("USER_PROFILE_API_ERROR", "Failed", e);
                    progressBar.setVisibility(View.GONE);
                }
            });

//        HurlStack hurlStack = new HurlStack()
//        {
//            @Override
//            protected HttpURLConnection createConnection(URL url) throws IOException
//            {
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
//                try
//                {
//                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
//                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                return httpsURLConnection;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this,hurlStack);
//
//        String URL = "https://instruo.in/api/v1/user";
//
//        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();
//
//        jsonRequestAdapter.setRequestAction("READ");
//        jsonRequestAdapter.setRequestData("username","leo4");
//        jsonRequestAdapter.setRequestData("password","null");
//        jsonRequestAdapter.setRequestParameteres("filter",null);
//
//
//
//        final Gson json = new GsonBuilder().serializeNulls().create();
//
//
//
//        final String jsonRequest = json.toJson(jsonRequestAdapter);
//
//        //tosty(this,jsonRequest);
//        //Log.d("JSON: ",jsonRequest);
//
//
//
//        JsonObjectRequest objectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL,
//                jsonRequest,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response.get("responseStatus").equals("FAILED")&&response.get("responseMessage").equals("Not Authorized2!!!")) {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                                builder.setTitle("Session Expired!");
//                                builder.setCancelable(false);
//                                builder.setMessage("Please Login Again to Continue!");
//                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                        Intent  in = new Intent(getApplicationContext(),LoginActivity.class);
//                                        spEditor=sharedPreferences.edit();
//                                        spEditor.clear();
//                                        spEditor.apply();
//                                        startActivity(in);
//                                        finish();
//                                    }
//                                });
//
//                                AlertDialog alert = builder.create();
//                                alert.show();
//                                //tosty(getApplicationContext(),"User Data Access Failed: "+response.get("responseMessage"));
//
//                                //progresBar.setVisibility(View.GONE);
//                            }
//                            if (response.get("responseStatus").equals("OK")&& (response.get("responseMessage")).equals("USER RETRIEVED SUCCESSFULLY")) {
//                                JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");
//
//                                accountDetails.add(jsonData.get("userName").toString());
//                                accountDetails.add(jsonData.get("userEmail").toString());
//                                accountDetails.add(jsonData.get("college").toString());
//
//                                JSONArray obj = jsonData.getJSONArray("contact");
//
//                                accountDetails.add(obj.getString(0));
//
//                                JSONArray object=jsonData.getJSONArray("events");
//                                JSONArray objectPayments=jsonData.getJSONArray("payments");
//
//                                int noOfRegisterEvents =object.length();
//                                int noOfPayments=objectPayments.length();
//                                eventsName = new ArrayList<>();
//                                regEventId= new ArrayList<>();
//                                regEventsFee= new ArrayList<>();
//                                paymentStatus= new ArrayList<>();
//
//                                eventsName.add("Accommodation Fee");
//                                regEventsFee.add("200");
//                                regEventId.add("accommodation_fee");
//                                eventsNameSet.add("accommodation_fee");
//
//                                eventsName.add("Buy Coupon*");
//                                regEventsFee.add("500");
//                                regEventId.add("coupon_500");
//                                eventsNameSet.add("coupon_500");
//
//                                eventsName.add("Buy Coupon**");
//                                regEventsFee.add("1000");
//                                regEventId.add("coupon_1000");
//                                eventsNameSet.add("coupon_1000");
//
//                                for(int i=0;i<noOfRegisterEvents;i++) {
//                                    String events=object.getString(i);
//                                    JSONObject jsonEvents = new JSONObject(""+events);
//
//                                    eventsName.add(jsonEvents.get("name").toString());
//                                    regEventsFee.add(""+KEY_EVENTS_FEE.get(jsonEvents.get("description").toString()));
//                                    regEventId.add(jsonEvents.get("description").toString());
//
//                                    eventsNameSet.add(jsonEvents.get("description").toString());
//                                }
//
//                                for (int i=0;i<noOfPayments;i++) {
//                                    String payments=objectPayments.getString(i);
//
//                                    JSONObject jsonPayments = new JSONObject(""+payments);
//                                    paymentStatus.add( jsonPayments.get("description").toString());
//                                }
//
//                                //List<Object> events= new ArrayList<Object>();
//
//                                spEditor=sharedPreferences.edit();
//                                spEditor.putStringSet(spEventsKey,eventsNameSet).apply();
//
//                                progressBar.setVisibility(View.GONE);
//                                SetUpViewPager(vp);
//
//                                //tosty(getApplicationContext(),"Events "+eventsName);
//                                //Log.d("Event Details:",""+eventsName);
//
//
//
//
//
//                            }
//                        }
//                        catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//
//                        //Log.d("Response",""+response);
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Log.d("Error:",""+error);
//                        tosty(getApplicationContext(),"Trying Again: Network Error!");
//                       finish();
//
//
//
//                    }
//                }
//        )
//        {
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("authorization", token);
//
//
//                return params;
//            }
//        };
//
//        requestQueue.add(objectRequest);
    }

    public void payAmt(final int index, final String regFee_str) {
        //String purpose = "Paying "+regFee_str+"for "+eventsName.get(index)+","+regEventId.get(index);
        //tosty(getApplicationContext(),purpose);

        eventIndex = index;

        if (validateCollege(accountDetails.get(2), regEventId.get(index))) {
            alertService.showAlert("Free Registration!", "Participation for IIEST Shibpur students is free except Gaming Events.");
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Free Registration!");
//            builder.setMessage("Participation for IIEST Shibpur students is free except Gaming Events.");
//            builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
//
//            AlertDialog alert = builder.create();
//            alert.show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Alert!");
        builder.setMessage("Pay Registration Fee Rs." + regFee_str);
        builder.setPositiveButton("Pay", (dialog, which) -> {
            callInstamojoPay(accountDetails.get(1), accountDetails.get(3), regFee_str, regEventId.get(index), accountDetails.get(0));
            dialog.cancel();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean validateCollege(String college, String eventId) {
        if (myCollege.contains(college.toLowerCase())) {
            return !gamingEvents.contains(eventId);
        }

        return false;
    }

    public ArrayList<String> getRegEventId() {
        return regEventId;
    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        tosty(getApplicationContext(), purpose);
        final Activity activity = this;
        instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        activity.registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("amount", amount);
            pay.put("purpose", purpose);
            pay.put("name", buyername);
            pay.put("webhook", WEBHOOK_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
        // showPopUp(paymentDetail);
    }

    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String s) {
                tosty(getApplicationContext(), "Payment Successful: " + s);
                paymentDetail = s.split(":");

                showAlert(paymentDetail);

                //showPopUp(paymentDetail);
            }

            @Override
            public void onFailure(int i, String s) {
                tosty(getApplicationContext(), "Failed: " + s);
                s = "status=failed:orderId=null:txnId=null:paymentId=null:token=null";
                paymentDetail = s.split(":");

                showAlert(paymentDetail);

                //unregisterReceiver(instamojoPay);
            }
        };
    }

    public void showAlert(final String[] payDetail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + payDetail[0]);
        builder.setCancelable(false);
        builder.setMessage("" + payDetail[1] + "\n" + payDetail[3] + "\nAmount= " + regEventsFee.get(eventIndex) + "\nEventName= " + regEvents.get(eventIndex) + "\nEmail= " + accountDetails.get(1) + "\n\n   *Receipt will be sent to registered Email.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.cancel();
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            in.putExtra("name", accountDetails.get(0));
            in.putExtra("email", accountDetails.get(1));
            startActivity(in);
            finish();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onInternetUnavailable() {
        tosty(getApplicationContext(), "Internet Unavailable!");
    }

    public interface OnAboutDataReceivedListener {
        void onDataReceived(Map model);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

}
