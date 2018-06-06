package com.appdev.abhishek360.instruox;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
{
    private TextView username,pass;
    private Button signIn,regi;
    private static final int REQUEST_CODE=9001;
    private GoogleApiClient googleApiClient;
    String name="alpha",email="alpha@base",imgURL="alpha.com";

    Dialog myDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDailog = new Dialog(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();



    }
    public void showPopUp(View V)
    {
        myDailog.setContentView(R.layout.custompopup);
        username = (TextView)findViewById(R.id.username);
        pass = (TextView)findViewById(R.id.Password);
        signIn = (Button)findViewById(R.id.signIn);
        regi = (Button)findViewById(R.id.regi);

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


    }

    public void onClick(View V)
    {
        switch( V.getId())
        {
            case R.id.gSignIn:
                signIN();
                break;



        }
    }

    private void signIN()
    {
        Intent i =  Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i,REQUEST_CODE);


    }

    private void signOut()
    {

    }

    private void onRequest(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
             name = account.getDisplayName();
             email = account.getEmail();
             imgURL = account.getPhotoUrl().toString();
            Intent in = new Intent(LoginActivity.this,HomeActivity.class);
            in.putExtra("name",name);
            in.putExtra("email",email);
            in.putExtra("Url",imgURL);
            startActivity(in);


        }
    }

    public void skipLogin(View view)
    {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        intent.putExtra("Url",imgURL);

        startActivity(intent);


    }

    private void changeUI(boolean isLogin)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            onRequest(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }
}
