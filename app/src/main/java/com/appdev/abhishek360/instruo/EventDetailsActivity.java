package com.appdev.abhishek360.instruo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.appdev.abhishek360.instruo.Adapters.EventAdapter;
import com.appdev.abhishek360.instruo.Adapters.EventPagerAdapter;
import com.appdev.abhishek360.instruo.EventDetailsFragments.CoordinatorFragment;
import com.appdev.abhishek360.instruo.EventDetailsFragments.DescriptionFragment;
import com.appdev.abhishek360.instruo.EventDetailsFragments.ResultFragment;
import com.appdev.abhishek360.instruo.EventDetailsFragments.RulesFragment;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventDetailsActivity extends AppCompatActivity implements DescriptionFragment.OnFragmentInteractionListener, RulesFragment.OnFragmentInteractionListener
                                                                            , CoordinatorFragment.OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener
{
    private TabLayout tabs;
    private int tabCode=0;
    private String eventName_str="Instruo Event",eventTime_str,eventVenue_str,eventDesc_str,eventRules_str,eventCoordinators_str,eventPrize_str,eventFee_str;;
    private String eventId,posterRef_str;
    private TextView title_textview;
    private ImageView poster_image;
    private SharedPreferences sharedPreferences;
    final public static String KEY_EVENT_OBJECT="eventDetails",KEY_POSTER_REF="posterRef",KEY_EVENT_ID="eventId";
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private EventAdapter eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        title_textview=(TextView)findViewById(R.id.event_details_title);

        sharedPreferences=getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        tabs = (TabLayout)findViewById(R.id.event_details_tab_layout);
        poster_image=(ImageView)findViewById(R.id.htab_header);

        Intent i = getIntent();
        eventDetails = (EventAdapter) i.getParcelableExtra(KEY_EVENT_OBJECT);
        //Bundle bundle= getIntent().getExtras();

        eventName_str=eventDetails.getTITLE();
        eventTime_str=eventDetails.getTIME();

        eventCoordinators_str=eventDetails.getCOORDINATORS();
        eventPrize_str=eventDetails.getPRIZE_MONEY();
        eventFee_str=eventDetails.getREG_FEE();

        eventId=i.getExtras().getString("eventId","myEvent");
        posterRef_str=i.getExtras().getString(KEY_POSTER_REF);

        StorageReference storageReference=  firebaseStorage.getReference().child(posterRef_str);

        Glide.with(this).using(new FirebaseImageLoader()).load(storageReference)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(poster_image);

        title_textview.setText(eventName_str);

        //tabCode= this.getArguments().getInt("tCode");

        Toolbar toolbar = (Toolbar) findViewById(R.id.event_details_action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ViewPager vp = (ViewPager)findViewById(R.id.event_details_pager);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        // vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));
        SetUpViewPager(vp);

        vp.setCurrentItem(tabCode);
    }

    public void registerEvent(View v)
    {
        final SslConfigurationManager sslConfigurationManager = new SslConfigurationManager();
        String token=sharedPreferences.getString(LoginActivity.spAccessTokenKey,"void");

        if(token.equals("void"))
            Toast.makeText(this,"Please SignIn to Participate in events!",Toast.LENGTH_LONG).show();

        else  sslConfigurationManager.updateUserData(eventId, token,getBaseContext());

    }


    public void SetUpViewPager(ViewPager viewPager)
    {
        EventPagerAdapter adapter = new EventPagerAdapter(getSupportFragmentManager(),4);

        adapter.AddFragmentPage(DescriptionFragment.newInstance(eventDetails,eventId),"Description");
        adapter.AddFragmentPage(RulesFragment.newInstance(eventDetails),"Rules");
        adapter.AddFragmentPage(CoordinatorFragment.newInstance(eventDetails),"Event Coordinators");
        adapter.AddFragmentPage(ResultFragment.newInstance(eventId),"Results");



        viewPager.setAdapter(adapter);
    }

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
