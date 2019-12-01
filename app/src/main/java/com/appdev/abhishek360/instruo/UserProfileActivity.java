package com.appdev.abhishek360.instruo;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.appdev.abhishek360.instruo.ApiModels.SimpleResponse;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;
import com.appdev.abhishek360.instruo.Services.AlertService;
import com.appdev.abhishek360.instruo.Services.ApiClientInstance;
import com.appdev.abhishek360.instruo.Services.ApiServices;
import com.appdev.abhishek360.instruo.Services.NetworkConnectionListener;
import com.appdev.abhishek360.instruo.UserProfileFragments.RegisteredEventsFragment;
import com.appdev.abhishek360.instruo.UserProfileFragments.UserDetailsFragment;
import com.google.android.material.tabs.TabLayout;
import com.instamojo.android.Instamojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.appdev.abhishek360.instruo.LoginActivity.spEmailKey;
import static com.appdev.abhishek360.instruo.LoginActivity.spFullNameKey;
import static com.appdev.abhishek360.instruo.LoginActivity.tosty;

public class UserProfileActivity extends AppCompatActivity implements NetworkConnectionListener, Instamojo.InstamojoPaymentCallback {
    private TabLayout tabs;
    private int tabCode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private ViewPager vp;
    private ProgressBar progressBar;

    private TextView paymentStatus_textview, paymentId_textview, paymentAmt_textview, orderId_textview, email_textview, eventId_textview;

    private ArrayList<String> regEventId = new ArrayList<>();
    private ArrayList<String> regEvents =  new ArrayList<>();
    private ArrayList<String> regEventsFee =  new ArrayList<>();
    private ArrayList<String> transId =  new ArrayList<>();
    private ArrayList<String> paymentTime =  new ArrayList<>();
    private ArrayList<Integer> paymentStatus = new ArrayList<>();
    private AlertService alertService;

    private Dialog dialog;
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

        KEY_EVENTS_FEE.put("mathemania", 150);
        KEY_EVENTS_FEE.put("war_of_titans", 150);
        KEY_EVENTS_FEE.put("battle_of_bards", 150);
        KEY_EVENTS_FEE.put("innovation_challenge", 150);
        KEY_EVENTS_FEE.put("wonder_girls", 150);
        KEY_EVENTS_FEE.put("prashnavali", 150);
        KEY_EVENTS_FEE.put("face_off", 150);
        KEY_EVENTS_FEE.put("business_prototype", 150);
        KEY_EVENTS_FEE.put("mayday_mystery", 150);

        KEY_EVENTS_FEE.put("junkyard", 150);
        KEY_EVENTS_FEE.put("papyrus", 150);
        KEY_EVENTS_FEE.put("web_d", 150);
        KEY_EVENTS_FEE.put("access_denied", 150);
        KEY_EVENTS_FEE.put("ode_to_code", 150);
        KEY_EVENTS_FEE.put("boomerang", 150);
        KEY_EVENTS_FEE.put("electronicazz", 150);
        KEY_EVENTS_FEE.put("wiredin", 150);

        KEY_EVENTS_FEE.put("death_race", 800);
        KEY_EVENTS_FEE.put("natterjack", 800);
        KEY_EVENTS_FEE.put("metal_gear", 800);
        KEY_EVENTS_FEE.put("soccred_games", 800);
        KEY_EVENTS_FEE.put("illuminati", 800);
    }

    private ArrayList<String> accountDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instamojo.getInstance().initialize(this, Instamojo.Environment.PRODUCTION);

        tabCode = getIntent().getIntExtra("tabCode", 0);
        setContentView(R.layout.activity_my_profile);
        dialog = new Dialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
        adapter.AddFragmentPage(RegisteredEventsFragment.newInstance(regEvents,
                regEventsFee,
                paymentStatus,
                transId,
                paymentTime
        ), "Registered Events");
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

                    boolean accoFlag = false;
                    boolean coupon600Flag = false;
                    boolean coupon1000Flag = false;

                    for (HashMap<String, String> object : res){
                        regEvents.add(object.get("name"));
                        String eventKey = object.get("event_key");
                        regEventId.add(eventKey);
                        if(object.get("registration_fee")!=null) regEventsFee.add(object.get("registration_fee"));
                        else regEventsFee.add("800");
                        paymentStatus.add(0);
                        transId.add(null);
                        paymentTime.add(null);

                        if(eventKey.equals("accommodation_fee"))
                            accoFlag = true;

                        if(eventKey.equals("coupon_600"))
                            coupon600Flag = true;

                        if(eventKey.equals("coupon_1000"))
                            coupon1000Flag = true;
                    }

                    if(!accoFlag) {
                        regEvents.add("Accommodation Fee");
                        regEventsFee.add("300");
                        regEventId.add("accommodation_fee");
                        paymentStatus.add(0);
                        transId.add(null);
                        paymentTime.add(null);
                    }

                     if(!coupon600Flag){
                         regEvents.add("Buy Coupon*");
                         regEventsFee.add("600");
                         regEventId.add("coupon_600");
                         paymentStatus.add(0);
                         transId.add(null);
                         paymentTime.add(null);
                     }

                     if(!coupon1000Flag) {
                         regEvents.add("Buy Coupon**");
                         regEventsFee.add("1000");
                         regEventId.add("coupon_1000");
                         paymentStatus.add(0);
                         transId.add(null);
                         paymentTime.add(null);
                     }

                    loadUserPayments();
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("USER_EVENTS_API_ERROR", "Failed", e);
                    progressBar.setVisibility(View.GONE);
                }
            });
    }

    public void loadUserPayments(){
        Single<ArrayList<HashMap<String, String>>> res = apiService
                .getUserPayments();

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<HashMap<String, String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<HashMap<String, String>> res) {
                        Log.d("USER_PAYMENT_API_RES", res.toString());
                        for (HashMap<String, String> object : res){
                            int index = regEventId.indexOf(object.get("event_key"));
                            paymentStatus.set(index, 1);
                            transId.set(index, object.get("transaction_id"));
                            paymentTime.set(index, object.get("created"));
                        }

                        progressBar.setVisibility(View.GONE);
                        SetUpViewPager(vp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("USER_PAYMENTS_API_ERROR", "Failed", e);
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
    }

    private void getPaymentUrl(String event_id){
        Map<String, String> requestModel = new HashMap<>();
        requestModel.put("event_key", event_id);
        Single<SimpleResponse> res = apiService
                .postPaymentEvnt(requestModel);

        res.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<SimpleResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(SimpleResponse res) {
                    String paymentUrl = res.getPaymentUrl();
                    Log.e("PAYMENT_API_RES", paymentUrl);

                    Log.d("Order Id:", res.getOrderId());

                    Instamojo.getInstance().initiatePayment(
                            UserProfileActivity.this,
                            res.getOrderId(),
                            UserProfileActivity.this
                    );

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onError(Throwable e) {
                    Log.e("PAYMENT_API_ERROR", "Failed", e);
                    progressBar.setVisibility(View.GONE);

                }
            });
    }

    public void payAmt(final int index, final String regFee_str) {

        eventIndex = index;

        if (validateCollege(accountDetails.get(2), regEventId.get(index))) {
            alertService.showAlert("Free Registration!", "Participation for IIEST Shibpur students is free except Gaming Events.");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Alert!");
        builder.setMessage("Pay Registration Fee Rs." + regFee_str+"\n*transaction charges of 1.9% will be added on payment screen");
        builder.setPositiveButton("Pay", (dialog, which) -> {
            getPaymentUrl(regEventId.get(index));
            progressBar.setVisibility(View.VISIBLE);
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

    @Override
    public void onInstamojoPaymentComplete(String orderId, String tranId, String paymentId, String status) {
        //showPopUp(new String[]);
        alertService.showAlert("Payment Response", "Status"+status+"\nOrder ID "+
                orderId+"\nTransaction: "+
                tranId+"\nPayment Id"+paymentId+
                "\nReceipt sent to Registered email."+
                "\nIt may take up to 6hrs to reflect payment here.");
    }

    @Override
    public void onPaymentCancelled() {
        tosty(UserProfileActivity.this, "Payment cancelled by user");

    }

    @Override
    public void onInitiatePaymentFailure(String s) {
        Log.d("PAYMENT_FAILED", s);
        alertService.showAlert("Payment Failed", s);
        tosty(UserProfileActivity.this, "Payment Failed: "+s);
    }
}
