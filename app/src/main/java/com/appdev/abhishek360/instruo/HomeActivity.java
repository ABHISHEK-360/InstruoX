package com.appdev.abhishek360.instruo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appdev.abhishek360.instruo.EventTabFragments.AutomatonTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.EventTechnicalTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.WorkshopTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.ExhibitionsTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.GamingTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.NonGenericTabFragment;
import com.appdev.abhishek360.instruo.HomeFragments.AboutFragment;
import com.appdev.abhishek360.instruo.HomeFragments.ContactUsFragment;
import com.appdev.abhishek360.instruo.HomeFragments.DirectionsFragment;
import com.appdev.abhishek360.instruo.HomeFragments.EventsFragment;
import com.appdev.abhishek360.instruo.HomeFragments.HomeFragment;
import com.appdev.abhishek360.instruo.HomeFragments.ScheduleFragment;
import com.appdev.abhishek360.instruo.HomeFragments.SponsorsFragment;
import com.appdev.abhishek360.instruo.HomeFragments.TeamFragment;
import com.appdev.abhishek360.instruo.SchedulesTabFragments.ScheduleDayOneFragment;
import com.appdev.abhishek360.instruo.SchedulesTabFragments.ScheduleDayThreeFragment;
import com.appdev.abhishek360.instruo.SchedulesTabFragments.ScheduleDayTwoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import net.glxn.qrgen.android.QRCode;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DirectionsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener,
                                                            WorkshopTabFragment.OnFragmentInteractionListener,
                                                                    EventsFragment.OnFragmentInteractionListener, EventTechnicalTabFragment.OnFragmentInteractionListener, AutomatonTabFragment.OnFragmentInteractionListener,
                                                                            TeamFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener, SponsorsFragment.OnFragmentInteractionListener
                                                                                , GamingTabFragment.OnFragmentInteractionListener, ExhibitionsTabFragment.OnFragmentInteractionListener
                                                                                        , ContactUsFragment.OnFragmentInteractionListener, NonGenericTabFragment.OnFragmentInteractionListener
                                                                                            , ScheduleFragment.OnFragmentInteractionListener, ScheduleDayOneFragment.OnFragmentInteractionListener
                                                                                                , ScheduleDayTwoFragment.OnFragmentInteractionListener, ScheduleDayThreeFragment.OnFragmentInteractionListener



{
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private TextView nameView,emailView;
    private TextView username,pass;
    private Button signIn,regi;
    private FloatingActionButton floatingActionButton;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CircleImageView proPic;
    private ImageView logoView;
    private String name,email;
    private Dialog myDailog;
    private Fragment fragment=null;
    private MenuItem mItem;
    private FragmentTransaction ft;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sharedPreferences=getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
        fragment = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_frame,fragment);
        myDailog= new Dialog(this);

        ft.commit();



        toolbar = (Toolbar) findViewById(R.id.action_bar);
        //toolbar.setSubtitle("2nd-4th November");
        //toolbar.setLogo(R.drawable.ic_new_instruo_logo);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();

                fragment =new ContactUsFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        name = getIntent().getExtras().getString("name","no_name");
        email = getIntent().getExtras().getString("email");




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



        logoView =(ImageView)header.findViewById(R.id.logoView);

        if(name.equals("alpha")&&email.equals("alpha@base")) {
            nameView.setText(R.string.instruo_name);
            emailView.setText(R.string.instruo_edition);

            findViewById(R.id.home_fab_payments).setVisibility(View.GONE);
            findViewById(R.id.text_payment_textview).setVisibility(View.GONE);
            findViewById(R.id.home_fab_qrcode).setVisibility(View.GONE);
            findViewById(R.id.home_textview_qrtext).setVisibility(View.GONE);
        }
        else
        {
            nameView.setText(name);
            emailView.setText(email);
            //Glide.with(this).load(imgUrl).into(proPic);



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
                Intent myprofileIntent = new Intent(HomeActivity.this, UserProfileActivity.class);
                myprofileIntent.putExtra("tabCode",0);

                startActivity(myprofileIntent);
                break;

            case R.id.logout__id:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure, you want to Logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        spEditor=sharedPreferences.edit();
                        spEditor.clear();
                        spEditor.apply();
                        Intent logoutIntent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(logoutIntent);

                        dialog.cancel();
                        finish();
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


                break;

            case R.id.reg_events__id:
                Intent eventsRegIntent = new Intent(HomeActivity.this, UserProfileActivity.class);
                eventsRegIntent.putExtra("tabCode",1);
                startActivity(eventsRegIntent);
                break;

            case R.id.signIn_home:
                Intent loginIntent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(loginIntent);


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
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
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.team_id:

                fragment = new TeamFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft.addToBackStack(null);

                ft.commit();
                break;

            case R.id.home_id:
                fragment =new HomeFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;


            case R.id.contacts_id:
                fragment =new ContactUsFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.sponsors_id:
                fragment =new SponsorsFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);

                ft.commit();

                break;


            case R.id.schedule_id:
                fragment =new ScheduleFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;


            case R.id.about__id:
                fragment =new AboutFragment();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();

                break;

            case R.id.events_id:
                fragment= new EventsFragment();

                tabCode = 0;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.automaton_id:

                fragment= new EventsFragment();

                tabCode = 1;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);

                ft.commit();
                break;

            case R.id.workshops_id:
                fragment= new EventsFragment();

                tabCode = 4;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStackImmediate();

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);

                ft.commit();
                break;


        }

        item.setChecked(true);
        drawerLayout.closeDrawers();




        return false;
    }




    public void gotoEvents(View v)
    {
        int tabCode=0;
        Bundle bundle=new Bundle();

        switch (v.getId())
        {
            case R.id.home_button_technical:
                fragment= new EventsFragment();

                tabCode = 0;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);
                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.home_button_automaton:

                fragment= new EventsFragment();

                tabCode = 1;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);

                ft.commit();
                break;

            case R.id.home_button_nontechnical:
                fragment= new EventsFragment();

                tabCode = 2;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);

                ft.commit();
                break;


            case R.id.home_button_gaming:
                fragment= new EventsFragment();

                tabCode = 3;
                bundle.putInt("tCode",tabCode);
                fragment.setArguments(bundle);

                ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.home_frame,fragment);
                ft.addToBackStack(null);

                ft.commit();
                break;
        }

    }



    public void callHelp(View v)
    {
        int id=v.getId();
        if(id==R.id.shubham_call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8343040193"));
            /*if (ActivityCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }*/
            startActivity(callIntent);
        }
        else if(id==R.id.abhishek_call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:7979775976"));

            startActivity(callIntent);
        }
        else if(id==R.id.ravi_call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:7903635648"));

            startActivity(callIntent);
        }
        else if(id==R.id.prudhavi_call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8555884665"));

            startActivity(callIntent);
        }
        else if(id==R.id.vishnu_call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8210849023"));

            startActivity(callIntent);
        }
        else if(id==R.id.kush_call)
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:8707427223"));


            startActivity(callIntent);
        }
        //tosty(this,""+v);
    }

    public void emailHelp(View v)
    {
        int id=v.getId();
        if(id==R.id.shubham_email)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"shubhamakajack@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO: Query for events!");


            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        else if(id==R.id.abhishek_eamil)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"abhi.kumar310@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO:Query for  android APP!");


            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        else if(id==R.id.ravi_email)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"ravi.kr27iiest@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO:Query for  events!");


            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        else if(id==R.id.prudhavi_email)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"prudhvi@instruo.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO:Query for  Accomodation!");


            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        else if(id==R.id.vishnu_email)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vishnu44d@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO:Query for  Website!");


            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        else if(id==R.id.kush_email)
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kushagrawave525@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO:Query for  Website!");


            startActivity(Intent.createChooser(intent, "Send Email"));
        }
    }

    public static void tosty(Context ctx, String msg)
    {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();

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

    public void navigatePayments(View view)
    {

            Intent eventsRegIntent = new Intent(HomeActivity.this, UserProfileActivity.class);
            eventsRegIntent.putExtra("tabCode", 1);
            startActivity(eventsRegIntent);



    }

    public void navigateFB(View view)
    {
        String fbPage = "https://www.facebook.com/instruo.iiests/";
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(fbPage));
        //intent.setType("message/rfc822");
       // intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kushagrawave525@gmail.com"});
        //intent.putExtra(Intent.EXTRA_SUBJECT, "INSTRUO:Query for  Website!");


        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragmentManager= getSupportFragmentManager();
        navigationView.setCheckedItem(R.id.home_id);


        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStackImmediate();
        else super.onBackPressed();



    }

    public void showPopUp(View V,Bitmap bm)
    {
        myDailog.setContentView(R.layout.qrcode_popup);
        myDailog.show();

        ImageView qrCode = (ImageView) myDailog.findViewById(R.id.popup_qr_code_image);
        qrCode.setImageBitmap(bm);
        myDailog.setCancelable(false);


        FloatingActionButton closeBtn = (FloatingActionButton) myDailog.findViewById(R.id.close_popup_qr_code);

        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDailog.dismiss();
            }
        });




    }

    public void generateQR(View view)
    {
        final Bitmap myBitmap = QRCode.from("instruoX:"+email).withSize(720,720).bitmap();

        showPopUp(view,myBitmap);
    }
}
