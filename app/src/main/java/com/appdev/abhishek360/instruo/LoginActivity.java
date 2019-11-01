package com.appdev.abhishek360.instruo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.appdev.abhishek360.instruo.ApiModels.LoginResponse;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;
import com.appdev.abhishek360.instruo.Services.AlertService;
import com.appdev.abhishek360.instruo.Services.ApiClientInstance;
import com.appdev.abhishek360.instruo.Services.ApiServices;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {
    private TextView username, pass, forgotPass;
    private Button signIn, regi, register;
    private static final int REQUEST_CODE = 9001;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleApiClient;
    private ProgressBar progressBar;
    private String name = "alpha", email = "alpha@base", imgURL = "alpha.com";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private Set<String> eventsName;
    private ApiServices apiService;
    private CompositeDisposable compositeDisposable;
    private AlertService alertService;

    public static String spAccessTokenKey = "ACCESS_TOKEN", spFullNameKey = "FULL_NAME",
            spInstanceIdKey = "instanceId", spEmailKey = "EMAIL", spKey = "instruoPref",
            spEventsKey = "REG_EVENTS", spSessionId = "COOKIE_SESSION_ID";

    Dialog myDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alertService = new AlertService(this);
        compositeDisposable = new CompositeDisposable();
        apiService = ApiClientInstance
                .getRetrofitInstance(this)
                .create(ApiServices.class);

        myDailog = new Dialog(this);
        register = (Button) findViewById(R.id.register);
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = GoogleSignIn.getClient(this, gso);

        sharedPreferences = getSharedPreferences("instruoPref", MODE_PRIVATE);

        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener(this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    Log.e("newToken", newToken);
                    getSharedPreferences(LoginActivity.spKey, MODE_PRIVATE)
                            .edit()
                            .putString(LoginActivity.spInstanceIdKey, newToken)
                            .apply();
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
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });
    }

    public void showPopUp(View V) {
        myDailog.setContentView(R.layout.custompopup);
        username = (TextView) myDailog.findViewById(R.id.username);
        pass = (TextView) myDailog.findViewById(R.id.Password);
        signIn = (Button) myDailog.findViewById(R.id.signIn);
        regi = (Button) myDailog.findViewById(R.id.regi);
        forgotPass = (TextView) myDailog.findViewById(R.id.forgotPass_textview);
        progressBar = (ProgressBar) myDailog.findViewById(R.id.popup_progress_bar);

        FloatingActionButton closeBtn = (FloatingActionButton) myDailog.findViewById(R.id.closeDialog);

        myDailog.show();

        forgotPass.setOnClickListener(v -> {
            String email_str = username.getText().toString();
            resetConfirm(email_str);
        });


        signIn.setOnClickListener(v -> {
            String u_name = username.getText().toString();
            String pswd = pass.getText().toString();

            if (u_name.isEmpty()) {
                username.setFocusable(true);
                username.setError("Enter Username!");
                return;

            } else if (pswd.isEmpty()) {
                pass.setFocusable(true);
                pass.setError("Enter Password!");
                return;

            } else if (pswd.length() < 4) {
                pass.setFocusable(true);
                pass.setError("Incorrect Password!");
                return;

            } else {
                progressBar.setVisibility(View.VISIBLE);
                signIn.setEnabled(false);

                logIn(u_name, pswd);

                //UserLoginTask mauth = new UserLoginTask(u_name,pswd);
                //Boolean b=false;
            }
        });

        closeBtn.setOnClickListener(v -> myDailog.dismiss());

        regi.setOnClickListener(v -> myDailog.dismiss());
    }

    public void resetConfirm(final String email) {
        if (isEmailValid(email)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert!");
            builder.setMessage("You'll receive an email on :" + email + " to reset your Account password.");
            builder.setPositiveButton("OK", (dialog, which) -> {
                resetPassword(email);
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        } else
            username.setError("Enter Your Registered email here and click forgot Password");
    }

    public void resetPassword(final String email) {
        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("email", email);

        Single<LoginResponse> res = apiService
                .postForgotPassword(registerRequest);

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        if (loginResponse.getSuccess()) {
                            tosty(getApplicationContext(), loginResponse.getMsg() + "\nPlease, Check your email!");
                        } else {
                            tosty(getApplicationContext(), "Register Failed: " + loginResponse.getMsg());
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RESET_EMAIL_API_ERROR:", "Failed", e);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    private boolean isEmailValid(String email) {
        if (email.contains("@") && email.contains(".")) return true;

        return false;
    }

    public void logIn(final String email, final String pswd) {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", pswd);

        Single<LoginResponse> res = apiService
                .postLoginCred(loginRequest);

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        if (loginResponse.getSuccess()) {
                            tosty(getApplicationContext(), loginResponse.getMsg());

                            readUserData();
                        } else {
                            tosty(getApplicationContext(), "LogIn Failed: " + loginResponse.getMsg());
                            progressBar.setVisibility(View.GONE);
                            signIn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LOGIN_API_ERROR", "Failed", e);
                        if (e instanceof HttpException) {
                            int errorCode = ((HttpException) e).code();
                            if (errorCode == 400) {
                                alertService.showAlert("Login Error", "Incorrect Login Email/Password!");
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }
                });
    }


    public void readUserData() {
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
                        spEditor = sharedPreferences.edit();
                        spEditor.putString(spFullNameKey, res.getName());
                        spEditor.putString(spEmailKey, res.getEmail());
                        spEditor.apply();

                        readRegEvents();

//                        progressBar.setVisibility(View.GONE);
//                        myDailog.cancel();
//
//                        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
//                        startActivity(in);
//                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("PROFILE_API_ERROR", "Failed", e);
                        progressBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }
                });
    }

    public void readRegEvents() {
        Single<ArrayList<HashMap<String, String>>> res = apiService
                .getRegEvents();

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<HashMap<String, String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<HashMap<String, String>> res) {
                        Log.d("REG_EVENTS_RES", "" + res.getClass());

                        progressBar.setVisibility(View.GONE);
                        myDailog.cancel();

                        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(in);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("REG_EVENTS_ERROR", "Failed", e);
                        progressBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);
                    }
                });
    }

    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.gSignIn:
                signIN();
                break;
        }
    }

    private void signIN() {
        Intent i = googleApiClient.getSignInIntent();
        startActivityForResult(i, REQUEST_CODE);
    }

    private void onRequest(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String name = account.getDisplayName();
            String email = account.getEmail();
            Log.d("GOOGGLE_SIGNIN_SUCCESS", "Name:" + name);

            Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
            in.putExtra("name", name);
            in.putExtra("email", email);
            startActivity(in);
        } catch (ApiException e) {
            Log.d("GOOGGLE_SIGNIN_FAILED", "code=" + e.getStatusCode() + " Msg:" + GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()));
        }
    }

    public void skipLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("google_signin_request", "request code:" + requestCode);
        if (requestCode == REQUEST_CODE) {
            Log.d("google_signin", "success request");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            onRequest(task);
        }
    }

    public static void tosty(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        if (myDailog != null) {
            myDailog.cancel();
        }
        super.onDestroy();
    }

}
