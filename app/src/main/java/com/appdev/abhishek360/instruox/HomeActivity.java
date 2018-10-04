package com.appdev.abhishek360.instruox;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,DirectionsFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,
                                                            EventWorkshopFragment.OnFragmentInteractionListener,
                                                                    EventsFragment.OnFragmentInteractionListener,EventTechnicalTabFragment.OnFragmentInteractionListener,EventAutomatonTabFragment.OnFragmentInteractionListener,
                                                                            TeamFragment.OnFragmentInteractionListener,AboutFragment.OnFragmentInteractionListener,SponsorsFragment.OnFragmentInteractionListener
                                                                                ,EventGamingTabFragment.OnFragmentInteractionListener,EventExibitionsTabFragment.OnFragmentInteractionListener


{
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    TextView nameView,emailView;
    TextView username,pass;
    Button signIn,regi;
    FloatingActionButton floatingActionButton;
    NavigationView navigationView;
    Toolbar toolbar;
    CircleImageView proPic;
    ImageView logoView;
    String name,email;
    Dialog myDailog;
    Fragment fragment=null;
    MenuItem mItem;
    android.support.v4.app.FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragment = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.home_frame,fragment);

        ft.commit();



        toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setSubtitle("2nd-4th November");
        toolbar.setLogo(R.drawable.ic_new_instruo_logo);
        setSupportActionBar(toolbar);

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

        name = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        String imgUrl = getIntent().getExtras().getString("Url");




        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);





        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
         nameView = (TextView)header.findViewById(R.id.hname_user);

         emailView = (TextView)header.findViewById(R.id.hemail);
         navigationView.setCheckedItem(R.id.home_id);

         floatingActionButton = (FloatingActionButton)header.findViewById(R.id.SearchPic);

        proPic = (CircleImageView)header.findViewById(R.id.ProfilePic);
        logoView =(ImageView)header.findViewById(R.id.logoView);
        proPic.setVisibility(View.GONE);

        if(name.equals("alpha")&&email.equals("alpha@base"))
        {
            nameView.setText("INSTRUO-2018");
            emailView.setText("The 10th Edition!");
            proPic.setImageResource(R.drawable.instruo_logo);
            floatingActionButton.setVisibility(View.GONE);

        }
        else
        {
            nameView.setText(name);
            emailView.setText(email);
            Glide.with(this).load(imgUrl).into(proPic);

            logoView.setVisibility(View.GONE);
            proPic.setVisibility(View.VISIBLE);

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        if(name.equals("alpha")&&email.equals("alpha@base"))
        {
            inflater.inflate(R.menu.dot_menu_or, menu);
        }
        else inflater.inflate(R.menu.dot_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int intId = item.getItemId();
        switch (intId)
        {
            case R.id.myprofile_id:
                Intent myprofileIntent = new Intent(HomeActivity.this,MyProfileActivity.class);
                startActivity(myprofileIntent);
                break;

            case R.id.logout__id:
                Intent logoutIntent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(logoutIntent);
                break;

            case R.id.reg_events__id:
                Intent eventsRegIntent = new Intent(HomeActivity.this,MyProfileActivity.class);
                startActivity(eventsRegIntent);
                break;

            case R.id.signIn_home:
                showPopUP();


        }

        return super.onOptionsItemSelected(item);
    }

    private void showPopUP()
    {
        myDailog = new Dialog(this);

        myDailog.setContentView(R.layout.custompopup);
        username = (TextView)findViewById(R.id.username);
        pass = (TextView)findViewById(R.id.Password);
        signIn = (Button)findViewById(R.id.signIn);
        regi = (Button)myDailog.findViewById(R.id.regi);

        FloatingActionButton closeBtn = (FloatingActionButton) myDailog.findViewById(R.id.closeDialog);


        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDailog.dismiss();
            }
        });

        myDailog.show();

        regi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent logingIntent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(logingIntent);
            }
        });
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id= item.getItemId();
        Bundle bundle= new Bundle();
        int tabCode;

        switch (id)
        {
            case R.id.directions_id:
                fragment =new DirectionsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.commit();

                break;

            case R.id.team_id:

                fragment = new TeamFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.commit();
                break;

            case R.id.home_id:
                fragment =new HomeFragment();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.commit();

                break;

            case R.id.sponsors_id:
                fragment =new SponsorsFragment();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.commit();

                break;


            case R.id.about__id:
                fragment =new AboutFragment();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.commit();

                break;

            case R.id.events_id:
                fragment= new EventsFragment();
                fragment= new EventsFragment();

                tabCode = 0;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.commit();
                break;

            case R.id.automaton_id:
                fragment= new EventsFragment();

                tabCode = 1;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);

                ft.commit();
                break;

            case R.id.workshops_id:
                fragment= new EventsFragment();

                tabCode = 2;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);

                ft.commit();
                break;


        }

        item.setChecked(true);
        drawerLayout.closeDrawers();




        return false;
    }

    public void getDirection(View v)
    {
        Intent i=null,chooser=null;

        if(v.getId()==R.id.direction_i_hall)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.555483,88.306516()&iwloc=A&hl=es"));
            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=I-Hall,IIEST Shibpur&destination_place_id=ChIJ9QQEpMl5AjoRpIw0lXsUJps&travelmode=walking"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.555483,88.306516&fov=90&heading=235&pitch=10&sensor=false"));
            //i.setData(Uri.parse("geo:22.555897,88.3068597?q="+Uri.encode("I-Hall")));
            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);

        }
        else if(v.getId()==R.id.direction_a_seminar_hall)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.5563189,88.3083137()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
            //i.setData(Uri.parse("geo:22.5563189,88.3083137?q="+Uri.encode("Alumni Seminar Hall")));

            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=Alumni Seminar Hall&destination_place_id=ChIJ1cIAoot5AjoRITV7kL_WaTk&travelmode=walking"));

            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
        else if(v.getId()==R.id.direction_hospital)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.5570627,88.3052268()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
            //i.setData(Uri.parse("geo:22.5570627,88.3052268?q="+Uri.encode("22.5570627,88.3052268(Hospital)")));

            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=Hospital-IIEST Shibpur&destination_place_id=ChIJocmQ9sl5AjoRz3-PnT6jBgw&travelmode=walking"));

            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
        else if(v.getId()==R.id.direction_netaji_bhawan)
        {

            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.555965, 88.307685()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
            //i.setData(Uri.parse("geo:22.5563189,88.3083137?q="+Uri.encode("22.5563189,88.3083137(Netaji Bhavan BESUS)")));
            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=Netaji Bhavan&destination_place_id=ChIJ8e2zbMl5AjoR_byYI8vTK20&travelmode=walking"));


            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
        else if(v.getId()==R.id.direction_new_building)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.553944,88.306344()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
            //i.setData(Uri.parse("geo:22.553944, 88.306344?q="+Uri.encode("SCIENCE & TECHNOLOGY BUILDING")));

            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=SCIENCE & TECHNOLOGY BUILDING&destination_place_id=ChIJG7dpTsh5AjoRuGmhvoF5b0g&travelmode=walking"));

            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
        else if(v.getId()==R.id.direction_regis_desk)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.5555323,88.3075095&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
           // i.setData(Uri.parse("geo:22.5556014,88.3074323?q="+Uri.encode("Instruo")));
            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=instruo&destination_place_id=ChIJ1cIAoot5AjoRITV7kL_WaTk&travelmode=walking"));

            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
        else if(v.getId()==R.id.direction_sengupta_hall)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.5556748,88.3093986()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
            //i.setData(Uri.parse("geo:22.5556748,88.3093986?q="+Uri.encode("Sengupta Hall")));
            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=Sengupta Hall&destination_place_id=ChIJ7weQuM55AjoR2OB3qyWeaFE&travelmode=walking"));

            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);

        }
        else if(v.getId()==R.id.direction_sen_hall_button)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.5563189,88.3083137()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
           // i.setData(Uri.parse("geo:22.5551697,88.3074003?q="+Uri.encode("Sen Hall")));
            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=Sen Hall&destination_place_id=ChIJb43ntM55AjoRNSGwx6RMyOo&travelmode=walking"));
            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
        else if(v.getId()==R.id.direction_iiest_1gate)
        {
            i= new Intent(Intent.ACTION_VIEW);
            //i.setData(Uri.parse("http://maps.google.com/maps?q=22.5570938,88.3070076()&iwloc=A&hl=es"));
            //i.setData(Uri.parse("http://maps.googleapis.com/maps/api/streetview?size=500x500&location=22.553944, 88.306344&fov=90&heading=235&pitch=10&sensor=false"));
           // i.setData(Uri.parse("geo:22.5563189,88.3083137?q="+Uri.encode("IIEST , First Gate")));
            i.setData(Uri.parse("https://www.google.com/maps/dir/?api=1&destination=IIEST,First Gate&destination_place_id=ChIJ9Wi3Ksp5AjoR5a0J1knFLCk&travelmode=walking"));


            chooser=Intent.createChooser(i,"Launch Maps");

            startActivity(chooser);
        }
    }



    @Override
    public void onDirFragmentInteraction(Uri uri)
    {

    }

    @Override
    public void onHomeFragmentInteraction(Uri uri)
    {

    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
