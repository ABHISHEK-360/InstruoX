package com.appdev.abhishek360.instruox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity implements EventDescriptionFragment.OnFragmentInteractionListener,EventRulesFragment.OnFragmentInteractionListener
                                                                            ,EventCoordinatorFragment.OnFragmentInteractionListener
{
    private TabLayout tabs;
    private int tabCode=0;
    private String eventName_str="Instruo Event",eventTime_str,eventVenue_str,eventDesc_str,eventRules_str,eventCoordinators_str,eventPrize_str,eventFee_str;;
    private String eventId;
    private TextView title_textview;
    private SharedPreferences sharedPreferences;
    final public static String KEY_EVENT_OBJECT="eventDetails";
    private EventAdapter eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        title_textview=(TextView)findViewById(R.id.event_details_title);

        sharedPreferences=getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        tabs = (TabLayout)findViewById(R.id.event_details_tab_layout);

        Intent i = getIntent();
        eventDetails = (EventAdapter) i.getParcelableExtra(KEY_EVENT_OBJECT);
        //Bundle bundle= getIntent().getExtras();

        eventName_str=eventDetails.getTITLE();
        eventTime_str=eventDetails.getTIME();

        eventCoordinators_str=eventDetails.getCOORDINATORS();
        eventPrize_str=eventDetails.getPRIZE_MONEY();
        eventFee_str=eventDetails.getREG_FEE();

        eventId=i.getExtras().getString("eventId");

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

        else  sslConfigurationManager.updateUserData(eventId, token,getApplicationContext());

    }


    public void SetUpViewPager(ViewPager viewPager)
    {
        EventPagerAdapter adapter = new EventPagerAdapter(getSupportFragmentManager(),3);

        adapter.AddFragmentPage(EventDescriptionFragment.newInstance(eventDetails),"Description");
        adapter.AddFragmentPage(EventRulesFragment.newInstance(eventDetails),"Rules");
        adapter.AddFragmentPage(EventCoordinatorFragment.newInstance(eventDetails),"Event Coordinators");


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
