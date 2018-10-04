package com.appdev.abhishek360.instruox;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
{
    private TextView username,pass;
    private Button signIn,regi,register;
    private static final int REQUEST_CODE=9001;
    private GoogleApiClient googleApiClient;
    String name="alpha",email="alpha@base",imgURL="alpha.com";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Dialog myDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDailog = new Dialog(this);
        register = (Button)findViewById(R.id.register) ;
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });



    }
    public void showPopUp(View V)
    {
        myDailog.setContentView(R.layout.custompopup);
        username = (TextView)myDailog.findViewById(R.id.username);
        pass = (TextView)myDailog.findViewById(R.id.Password);
        signIn = (Button)myDailog.findViewById(R.id.signIn);
        regi = (Button)myDailog.findViewById(R.id.regi);

        FloatingActionButton closeBtn = (FloatingActionButton) myDailog.findViewById(R.id.closeDialog);

        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String u_name= username.getText().toString();
                String pswd= pass.getText().toString();

                if(u_name.isEmpty())
                {
                    username.setFocusable(true);
                    username.setError("Enter Username!");

                }
                else if(pswd.isEmpty())
                {
                    pass.setFocusable(true);
                    pass.setError("Enter Password!");

                }
                else
                {
                    UserLoginTask mauth = new UserLoginTask(u_name,pswd);
                    mauth.execute((Void) null);
                }

            }
        });

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
                myDailog.dismiss();
            }
        });


    }

    public void logIn(final String email, final String pswd)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //OkHttpClient client = new OkHttpClient();

        String URL = "http://instruo.herokuapp.com/api/v1/auth";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestAction("AUTH");
        jsonRequestAdapter.setRequestData("email",email);
        jsonRequestAdapter.setRequestData("password",pswd);

        final Gson json = new GsonBuilder().serializeNulls().create();



        final String jsonRequest = json.toJson(jsonRequestAdapter);

        tosty(this,jsonRequest);
        Log.d("JSON: ",jsonRequest);

        Map<String,String> head = new HashMap<>();
        head.put("content-type","abcd");

        Headers headers = Headers.of(head);




        /*RequestBody body = RequestBody.create(JSON, jsonRequest);

        okhttp3.Request request = new okhttp3.Request.Builder().url(URL).post(body).headers(headers).build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e)
            {
                //tosty(getApplicationContext(),""+e);
                Log.d("Error!:",""+e);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException
            {
                //tosty(getApplicationContext(),""+response);
                Log.d("Error:",""+response);

            }
        });*/








        JsonObjectRequest objectRequest = new JsonObjectRequest(

                Request.Method.POST,
                URL,
                jsonRequest,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }


        ){

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        requestQueue.add(objectRequest);

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

    public static void tosty(Context ctx, String msg)
    {
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }





        @Override
        protected Boolean doInBackground(Void... params)
        {
            // TODO: attempt authentication against a network service.


            OkHttpClient client = new OkHttpClient();

            String URL = "https://instruo.in/api/v1/auth";

            jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

            jsonRequestAdapter.setRequestAction("AUTH");
            jsonRequestAdapter.setRequestData("email",mEmail);
            jsonRequestAdapter.setRequestData("password",mPassword);

            final Gson json = new GsonBuilder().serializeNulls().create();



            final String jsonRequest = json.toJson(jsonRequestAdapter);

            //tosty(getApplicationContext(),jsonRequest);
            Log.d("JSON: ",jsonRequest);

            Map<String,String> head = new HashMap<>();
            head.put("content-type","abcd");

            Headers headers = Headers.of(head);



            RequestBody body = RequestBody.create(JSON, jsonRequest);

            okhttp3.Request request = new okhttp3.Request.Builder().url(URL).post(body).build();


            try
            {

                okhttp3.Response  response = client.newCall(request).execute();

                //tosty(getApplicationContext(),""+response);
                Log.d("Response:",""+response);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return true;
        }



        @Override
        protected void onCancelled()
        {

        }



    }

}
