package com.appdev.abhishek360.instruox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    TextView nameView,emailView;
    FloatingActionButton floatingActionButton;
    CircleImageView proPic;
    ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
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

        String name = getIntent().getExtras().getString("name");
        String email = getIntent().getExtras().getString("email");
        String imgUrl = getIntent().getExtras().getString("Url");




        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
         nameView = (TextView)header.findViewById(R.id.hname_user);

         emailView = (TextView)header.findViewById(R.id.hemail);

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
        inflater.inflate(R.menu.dot_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
