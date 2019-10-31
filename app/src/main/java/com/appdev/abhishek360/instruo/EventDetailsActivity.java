package com.appdev.abhishek360.instruo;

import android.content.Intent;
import android.content.SharedPreferences;

import com.appdev.abhishek360.instruo.Adapters.EventAdapter;
import com.appdev.abhishek360.instruo.Adapters.EventPagerAdapter;
import com.appdev.abhishek360.instruo.EventDetailsFragments.CoordinatorFragment;
import com.appdev.abhishek360.instruo.EventDetailsFragments.DescriptionFragment;
import com.appdev.abhishek360.instruo.EventDetailsFragments.ResultFragment;
import com.appdev.abhishek360.instruo.EventDetailsFragments.RulesFragment;
import com.appdev.abhishek360.instruo.Services.ApiRequestManager;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.reactivex.disposables.CompositeDisposable;

public class EventDetailsActivity extends AppCompatActivity {
    private TabLayout tabs;
    private int tabCode=0;
    private String eventName_str="Instruo Event";
    private String eventId, posterRef_str, eventCat;
    private TextView title_textview;
    private ImageView poster_image;
    private SharedPreferences sharedPreferences;
    final public static String KEY_EVENT_OBJECT = "eventDetails", KEY_POSTER_REF = "posterRef", KEY_EVENT_ID = "eventId", KEY_EVENT_CAT = "eventCat";
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private EventAdapter eventDetails;
    private CompositeDisposable compositeDisposable;
    private FirebaseFirestore db;
    private ViewPager vp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        db = FirebaseFirestore.getInstance();
        compositeDisposable = new CompositeDisposable();
        title_textview = (TextView)findViewById(R.id.event_details_title);
        sharedPreferences = getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        tabs = (TabLayout)findViewById(R.id.event_details_tab_layout);
        poster_image = (ImageView)findViewById(R.id.htab_header);
        progressBar = findViewById(R.id.event_details_progressbar);

        Intent i = getIntent();
        eventDetails = new EventAdapter();
        //eventDetails = (EventAdapter) i.getParcelableExtra(KEY_EVENT_OBJECT);
        eventId = i.getStringExtra(KEY_EVENT_ID);
        eventCat = i.getStringExtra(KEY_EVENT_CAT);
        getEventDetails(eventId);

        eventId = i.getExtras().getString("eventId","myEvent");
        posterRef_str = i.getExtras().getString(KEY_POSTER_REF);

        StorageReference storageReference=  firebaseStorage.getReference().child(posterRef_str);

        Glide.with(this).using(new FirebaseImageLoader()).load(storageReference)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(poster_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_details_action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vp = (ViewPager)findViewById(R.id.event_details_pager);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        // vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));

        vp.setCurrentItem(tabCode);
    }

    private void renderEventHeaders(){
        eventName_str = eventDetails.getTITLE();
        title_textview.setText(eventName_str);
    }

    public void registerEvent(View v) {
        final ApiRequestManager apiRequestManager = new ApiRequestManager(getApplicationContext(), compositeDisposable);
        String sessionId = sharedPreferences.getString(LoginActivity.spSessionId,"void");

        if(sessionId.equals("void"))
            Toast.makeText(this,"Please, Sign up to Participate in Events!", Toast.LENGTH_LONG).show();

        else
            apiRequestManager.registerEvent(eventId);
    }

    private void getEventDetails(String eventId){
        db.collection("/EVENTS_INSTRUO/"+eventCat+"/EVENTS").document(eventId)
            .get()
            .addOnSuccessListener( document -> {
                    if(document.exists()) {
                        eventDetails = document.toObject(EventAdapter.class);
                        progressBar.setVisibility(View.GONE);
                        renderEventHeaders();
                        SetUpViewPager(vp);
                    }
                    else{
                        Log.d("EVENT_DETAILS_FAILED", "event not found!");
                        tosty("Try Again!, Failed to load details.");
                        finish();
                    }

            })
            .addOnFailureListener( error -> {
                Log.e("EVENT_DETAILS_ERROR",""+error);
                tosty("Try Again!, Failed to load details.");
                finish();
            });

    }


    public void SetUpViewPager(ViewPager viewPager) {
        EventPagerAdapter adapter = new EventPagerAdapter(getSupportFragmentManager(), 4);
        adapter.AddFragmentPage(DescriptionFragment.newInstance(eventDetails, eventId),"Description");
        adapter.AddFragmentPage(RulesFragment.newInstance(eventDetails), "Rules");
        adapter.AddFragmentPage(CoordinatorFragment.newInstance(eventDetails), "Event Coordinators");
        adapter.AddFragmentPage(ResultFragment.newInstance(eventId), "Results");

        viewPager.setAdapter(adapter);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void tosty(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
