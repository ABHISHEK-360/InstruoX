package com.appdev.abhishek360.instruox;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.appdev.abhishek360.instruox.LoginActivity.tosty;

public class RegisterActivity extends AppCompatActivity
{

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText username_edittext,email_edittext,password_edittext;
    private Button register_button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username_edittext= (EditText)findViewById(R.id.reg_fullName_edittext);
        email_edittext= (EditText)findViewById(R.id.reg_email_edittext);
        password_edittext= (EditText)findViewById(R.id.reg_password_edittext);
        register_button= (Button) findViewById(R.id.reg_submit_button);

        register_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserRegTask regTask= new UserRegTask(email_edittext.getText().toString(),password_edittext.getText().toString(),username_edittext.getText().toString());
                regTask.execute((Void)null);
            }
        });
    }


    public class UserRegTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mEmail;
        private final String uName;
        private final String mPassword;

        UserRegTask(String email, String password,String username)
        {
            mEmail = email;
            mPassword = password;
            uName= username;
        }





        @Override
        protected Boolean doInBackground(Void... params)
        {
            // TODO: attempt authentication against a network service.


            OkHttpClient client = new OkHttpClient();

            String URL = "http://instruo.herokuapp.com/api/v1/user";

            jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

            jsonRequestAdapter.setRequestAction("CREATE");
            jsonRequestAdapter.setRequestData("email",mEmail);
            jsonRequestAdapter.setRequestData("username",uName);
            jsonRequestAdapter.setRequestData("password",mPassword);

            final Gson json = new GsonBuilder().serializeNulls().create();



            final String jsonRequest = json.toJson(jsonRequestAdapter);

            //tosty(getApplicationContext(),jsonRequest);
            Log.d("JSON: ",jsonRequest);

            //Map<String,String> head = new HashMap<>();
            //head.put("content-type","abcd");

           // Headers headers = Headers.of(head);



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
