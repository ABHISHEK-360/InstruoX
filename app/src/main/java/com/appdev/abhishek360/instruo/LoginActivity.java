package com.appdev.abhishek360.instruo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.appdev.abhishek360.instruo.ApiModels.CredModel;
import com.appdev.abhishek360.instruo.ApiModels.LoginResponse;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;
import com.appdev.abhishek360.instruo.Services.ApiClientInstance;
import com.appdev.abhishek360.instruo.Services.ApiServices;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView username,pass,forgotPass;
    private Button signIn,regi,register;
    private static final int REQUEST_CODE=9001;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleApiClient;
    private ProgressBar progresBar;
    private String name="alpha",email="alpha@base",imgURL="alpha.com";
    private CompositeDisposable compositeDisposable;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private Set<String> eventsName;
    private ApiServices apiService;

    public static String spAccessTokenKey="ACCESS_TOKEN", spFullNameKey="FULL_NAME",
            spInstanceIdKey="instanceId",spEmailKey="EMAIL",spKey="instruoPref",
            spEventsKey="REG_EVENTS", spSessionId = "COOKIE_SESSION_ID";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Dialog myDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        compositeDisposable = new CompositeDisposable();
        apiService = ApiClientInstance.getRetrofitInstance(getApplicationContext()).create(ApiServices.class);

        myDailog = new Dialog(this);
        register = (Button)findViewById(R.id.register) ;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = GoogleSignIn.getClient(this, gso);

        sharedPreferences = getSharedPreferences("instruoPref",MODE_PRIVATE);

        //tosty(this," Instance Id: "+FireBaseInstanceIDService.getToken(this));

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken",newToken);
            getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE).edit().putString(LoginActivity.spInstanceIdKey, newToken).apply();
            //tosty(this,"Instance Id: ");
        });

        /*-------------For Instant Run--------------------

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setMessage("Please install our app to Login/Register.\nYou Can 'Skip Login' now only.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                register.setEnabled(false);
                findViewById(R.id.gSignIn).setEnabled(false);
                findViewById(R.id.Login).setEnabled(false);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        -------------------------------------------------------------*/

        register.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(registerIntent);
        });
    }

    public void showPopUp(View V) {
        myDailog.setContentView(R.layout.custompopup);

        username = (TextView)myDailog.findViewById(R.id.username);
        pass = (TextView)myDailog.findViewById(R.id.Password);
        signIn = (Button)myDailog.findViewById(R.id.signIn);
        regi = (Button)myDailog.findViewById(R.id.regi);
        forgotPass=(TextView)myDailog.findViewById(R.id.forgotPass_textview);
        progresBar=(ProgressBar)myDailog.findViewById(R.id.popup_progress_bar);

        FloatingActionButton closeBtn = (FloatingActionButton) myDailog.findViewById(R.id.closeDialog);

        myDailog.show();

        forgotPass.setOnClickListener(v -> {
           String email_str=username.getText().toString();
           resetConfirm(email_str);
        });


        signIn.setOnClickListener(v -> {
            String u_name= username.getText().toString();
            String pswd= pass.getText().toString();

            if(u_name.isEmpty())
            {
                username.setFocusable(true);
                username.setError("Enter Username!");
                return;

            }
            else if(pswd.isEmpty())
            {
                pass.setFocusable(true);
                pass.setError("Enter Password!");
                return;

            }
            else if(pswd.length()<4)
            {
                pass.setFocusable(true);
                pass.setError("Incorrect Password!");
                return;

            }
            else
            {
                progresBar.setVisibility(View.VISIBLE);
                signIn.setEnabled(false);

                logIn(u_name,pswd);

                //UserLoginTask mauth = new UserLoginTask(u_name,pswd);
                //Boolean b=false;
            }
        });

        closeBtn.setOnClickListener(v -> myDailog.dismiss());

        regi.setOnClickListener(v -> myDailog.dismiss());
    }

    public void  resetConfirm(final String email){
        if(isEmailValid(email)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert!");
            builder.setMessage("You'll receive an email on :"+email+" to reset your Account password.");
            builder.setPositiveButton("OK", (dialog, which) -> {
                resetPassword(email);
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
            username.setError("Enter Your Registered email here and click forgot Password");
    }

    public void resetPassword(final String email) {
        HurlStack hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this,hurlStack);

        String URL = "https://instruo.in/api/v1/auth/forget";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestData("email",email);

        final Gson json = new GsonBuilder().serializeNulls().create();

        final String jsonRequest = json.toJson(jsonRequestAdapter);

        //tosty(this,jsonRequest);
        //Log.d("JSON: ",jsonRequest);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonRequest,
                response -> {
                    try
                    {
                        if (response.get("responseStatus").equals("FAILED")) {
                            tosty(getApplicationContext(),"Try Again,Failed: "+response.get("responseMessage"));

                        }
                        if (response.get("responseStatus").equals("OK")) {
                            //Map<String,String> responseData = new HashMap<>();

                            tosty(getApplicationContext(),"Please Check Your Email. ");
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.d("Response",""+response);
                },
                error -> {
                    Log.d("Error:",""+error);
                    progresBar.setVisibility(View.GONE);

                    tosty(getApplicationContext(),"Try Again: Wrong Credentials!");
                }
        );
        requestQueue.add(objectRequest);
    }


    private boolean isEmailValid(String email)
    {
        if(email.contains("@")&&email.contains(".")) return true;

        return false ;
    }

    public void logIn(final String email, final String pswd) {
        Single<LoginResponse> res = apiService
                .postLoginCred(new CredModel(email,pswd));

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        Log.d("Login Api Res:", loginResponse.getMsg());
                        if (loginResponse.getSuccess()) {
                            String accessToken = loginResponse.getAccessToken();
                            tosty(getApplicationContext(),loginResponse.getMsg());

                            if(!accessToken.isEmpty()) readUserData(accessToken);
                        }
                        else {
                            tosty(getApplicationContext(),"LogIn Failed: "+loginResponse.getMsg());
                        }

                        progresBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LOGIN_API_ERROR","Failed", e);
                        progresBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }
                });

//        HurlStack hurlStack = new HurlStack()
//        {
//            @Override
//            protected HttpURLConnection createConnection(URL url) throws IOException
//            {
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
//                try {
//                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
//                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return httpsURLConnection;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this,hurlStack);
//
//        String URL = "https://instruo.in/api/v1/auth";
//
//        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();
//
//        jsonRequestAdapter.setRequestAction("AUTH");
//
//        if(email.contains("@")&&email.contains("."))jsonRequestAdapter.setRequestData("email",email);
//        else jsonRequestAdapter.setRequestData("username",email);
//
//        jsonRequestAdapter.setRequestData("password",pswd);
//
//        final Gson json = new GsonBuilder().serializeNulls().create();
//
//        final String jsonRequest = json.toJson(jsonRequestAdapter);
//
//        //tosty(this,jsonRequest);
//        //Log.d("JSON: ",jsonRequest);
//
//        JsonObjectRequest objectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL,
//                jsonRequest,
//                response -> {
//                    try {
//                        if (response.get("responseStatus").equals("FAILED")) {
//                            tosty(getApplicationContext(),"LogIn Failed: "+response.get("responseMessage"));
//
//                        }
//                        if (response.get("responseStatus").equals("OK")) {
//                            //Map<String,String> responseData = new HashMap<>();
//
//                            JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");
//
//                            String accessToken= jsonData.get("accessToken").toString();
//                            tosty(getApplicationContext(),"Logged In Successfully! ");
//
//                            spEditor=sharedPreferences.edit();
//                            spEditor.putString(spAccessTokenKey,accessToken);
//                            spEditor.apply();
//
//
//                            if(!accessToken.isEmpty()) readUserData(accessToken);
//
//                        }
//                    }
//                    catch (JSONException e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    signIn.setEnabled(true);
//
//
//
//                },
//                error -> {
//                    Log.d("Error:",""+error);
//                    progresBar.setVisibility(View.GONE);
//
//                    tosty(getApplicationContext(),"Try Again: Wrong Credentials!");
//                    signIn.setEnabled(true);
//
//                }
//        );
//
//        requestQueue.add(objectRequest);
    }


    public void readUserData(final String token) {
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
                        //Log.d("User Profile Api Res:", res.getMsg());

                        progresBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("PROFILE_API_ERROR","Failed", e);
                        progresBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }
                });

//        HurlStack hurlStack = new HurlStack()
//        {
//            @Override
//            protected HttpURLConnection createConnection(URL url) throws IOException
//            {
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
//                try {
//                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
//                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return httpsURLConnection;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this,hurlStack);
//
//        String URL = "https://instruo.in/api/v1/user";
//
//        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();
//
//        jsonRequestAdapter.setRequestAction("READ");
//        jsonRequestAdapter.setRequestData("username","leo4");
//        jsonRequestAdapter.setRequestData("password","null");
//        jsonRequestAdapter.setRequestParameteres("filter",null);
//
//        final Gson json = new GsonBuilder().serializeNulls().create();
//
//        final String jsonRequest = json.toJson(jsonRequestAdapter);
//
//        JsonObjectRequest objectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL,
//                jsonRequest,
//                response -> {
//                    try
//                    {
//                        if (response.get("responseStatus").equals("FAILED"))
//                        {
//                            tosty(getApplicationContext(),"User Data Access Failed: "+response.get("responseMessage"));
//                            progresBar.setVisibility(View.GONE);
//                            signIn.setEnabled(true);
//                        }
//                        if (response.get("responseStatus").equals("OK")&& (response.get("responseMessage")).equals("USER RETRIEVED SUCCESSFULLY"))
//                        {
//                            JSONObject jsonData = new JSONObject(""+response).getJSONObject("responseData");
//
//
//                            name= jsonData.get("userName").toString();
//                            email= jsonData.get("userEmail").toString();
//
//                            tosty(getApplicationContext(),"Hello! "+name);
//
//                            JSONArray object=jsonData.getJSONArray("events");
//
//                            int noOfRegisterEvents =object.length();
//                            eventsName = new HashSet<>();
//                            for(int i=0;i<noOfRegisterEvents;i++) {
//                                String events=object.getString(i);
//                                JSONObject jsonEvents = new JSONObject(""+events);
//
//                                eventsName.add(jsonEvents.get("description").toString());
//                            }
//
//                            spEditor=sharedPreferences.edit();
//                            spEditor.putString(spFullNameKey,name);
//                            spEditor.putString(spEmailKey,email);
//                            spEditor.putStringSet(spEventsKey,eventsName);
//                            spEditor.apply();
//
//                            myDailog.dismiss();
//                            progresBar.setVisibility(View.GONE);
//
//                            Intent in = new Intent(getApplicationContext(),HomeActivity.class);
//                            in.putExtra("name",name);
//                            in.putExtra("email",email);
//                            in.putExtra("Url",imgURL);
//                            startActivity(in);
//                            finish();
//                        }
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    //Log.d("Response",""+response);
//                },
//                error -> {
//                    Log.d("Error:",""+error);
//                    progresBar.setVisibility(View.GONE);
//
//                    tosty(getApplicationContext(),"Try Again: Network Error!");
//                }
//        )
//        {
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("authorization", token);
//
//                return params;
//            }
//        };
//
//        requestQueue.add(objectRequest);

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

    private void signIN() {
        Intent i =  googleApiClient.getSignInIntent();
        startActivityForResult(i,REQUEST_CODE);
    }

    private void onRequest(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String name = account.getDisplayName();
            String email = account.getEmail();
            Log.d("GOOGGLE_SIGNIN_SUCCESS", "Name:" + name);

            Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
            in.putExtra("name",name);
            in.putExtra("email",email);
            startActivity(in);
        } catch (ApiException e) {
            Log.d("GOOGGLE_SIGNIN_FAILED", "code="+e.getStatusCode()+" Msg:" + GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()));
        }

    }

    public void skipLogin(View view) {
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
        Log.d("google_signin_request","request code:"+requestCode);
        if(requestCode==REQUEST_CODE) {
            Log.d("google_signin","success request");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            onRequest(task);
        }
    }

    public static void tosty(Context ctx, String msg)
    {
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }

    private HostnameVerifier getHostnameVerifier() {
        return (hostname, session) -> {
            return true;
            // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
            //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            //return hv.verify("localhost", session);
        };
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]
                {
                new X509TrustManager()
                {
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType)
                    {
                        try
                        {
                            if (certs != null && certs.length > 0)
                            {
                                certs[0].checkValidity();
                            } else
                                {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        }
                        catch (CertificateException e)
                        {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType)
                    {
                        try
                        {
                            if (certs != null && certs.length > 0)
                            {
                                certs[0].checkValidity();
                            } else
                                {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e)
                        {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }


    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException
    {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.certificate); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

}
