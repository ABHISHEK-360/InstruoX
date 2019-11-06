package com.appdev.abhishek360.instruo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.appdev.abhishek360.instruo.ApiModels.SimpleResponse;
import com.appdev.abhishek360.instruo.Services.ApiClientInstance;
import com.appdev.abhishek360.instruo.Services.ApiServices;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static com.appdev.abhishek360.instruo.LoginActivity.tosty;

public class RegisterActivity extends AppCompatActivity {
    private EditText username_edittext, email_edittext, password_edittext, fullname_edittext;
    private EditText confpass_edittext, college_edittext, contact_edittext;
    private String fullnameStr;
    private String emailStr;
    private SharedPreferences.Editor spEditor;
    private static final int CHOOSE_IMAGE_CODE = 101;
    private ProgressBar reg_progressBar;
    private Button register_button;
    private ApiServices apiService;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        compositeDisposable = new CompositeDisposable();
        apiService = ApiClientInstance.getRetrofitInstance(this).create(ApiServices.class);

        Bundle bundle = getIntent().getExtras();

        fullname_edittext = (EditText) findViewById(R.id.reg_fullName_edittext);
        email_edittext = (EditText) findViewById(R.id.reg_email_edittext);
        contact_edittext = (EditText) findViewById(R.id.reg_phone_edittext);
        college_edittext = (EditText) findViewById(R.id.reg_college_edittext);
//        username_edittext = (EditText) findViewById(R.id.reg_userName_edittext);
        confpass_edittext = (EditText) findViewById(R.id.reg_confpassword_edittext);
        password_edittext = (EditText) findViewById(R.id.reg_password_edittext);
        register_button = (Button) findViewById(R.id.reg_submit_button);
        reg_progressBar = (ProgressBar) findViewById(R.id.reg_progressbar);

        try {
            fullnameStr = bundle.getString("name");
            emailStr = bundle.getString("email");
            fullname_edittext.setText(fullnameStr);
            email_edittext.setText(emailStr);
        } catch (Exception e) {
            Log.d("Register Page:", "Normal Login-" + e);
        }

        register_button.setOnClickListener(v -> validateDetails());
    }

    private void validateDetails() {
        fullnameStr = fullname_edittext.getText().toString();
        emailStr = email_edittext.getText().toString();
        String contact = contact_edittext.getText().toString();
        String college = college_edittext.getText().toString();
        //String username = username_edittext.getText().toString();
        String password = password_edittext.getText().toString();
        String confpass = confpass_edittext.getText().toString();

        if (fullnameStr.isEmpty()) {
            fullname_edittext.setError("Please,Enter User Name!");
            fullname_edittext.requestFocus();
            return;
        } else if (emailStr.isEmpty()) {
            email_edittext.setError("Email is Required");
            email_edittext.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email_edittext.setError("Enter a valid Email id!");
            email_edittext.requestFocus();
            return;
        }

        if (contact.isEmpty()) {
            contact_edittext.setError("Phone No. is Required");
            contact_edittext.requestFocus();
            return;
        } else if (!Patterns.PHONE.matcher(contact).matches() || contact.length() != 10) {
            contact_edittext.setError("Please,Enter a vaild  Phone No. without +91");
            contact_edittext.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            password_edittext.setError("Password is Required");
            password_edittext.requestFocus();
            return;
        } else if (password.length() < 6) {
            password_edittext.setError("Min. password length is 6");
            password_edittext.requestFocus();
            return;
        }

        if (confpass.isEmpty()) {
            confpass_edittext.setError("Password not matched");
            confpass_edittext.requestFocus();
            return;
        } else if (!password.equals(confpass)) {
            confpass_edittext.setError("Password not matched");
            confpass_edittext.requestFocus();
            return;
        } else {
            //imageProgressBar.setVisibility(View.VISIBLE);
            RegisterUser(emailStr, password, fullnameStr, contact, college);
        }

    }

    public void RegisterUser(final String email, final String pswd, final String name, final String contact, final String college) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Wait!");
        builder.setMessage("Registering...");
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();

        Map<String, String> registerRequest = new HashMap<>();
        registerRequest.put("name", name);
        registerRequest.put("college", college);
        registerRequest.put("contact", contact);
        registerRequest.put("email", email);
        registerRequest.put("password", pswd);

        Single<SimpleResponse> res = apiService
                .postRegister(registerRequest);

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SimpleResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(SimpleResponse simpleResponse) {
                        Log.d("REGISTER_API_RES:", simpleResponse.getMsg());
                        if (simpleResponse.getSuccess()) {
                            tosty(getApplicationContext(), simpleResponse.getMsg() + "\nPlease, Login with your Credentials");
                            finish();
                        } else {
                            tosty(getApplicationContext(), "Register Failed: " + simpleResponse.getMsg());
                        }

                        reg_progressBar.setVisibility(View.GONE);
                        alert.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("REGISTER_API_ERROR:", "Failed", e);
                        if (e instanceof HttpException) {
                            int errorCode = ((HttpException) e).code();
                            Response res = ((HttpException) e).response();
                            ResponseBody body = res.errorBody();
                            Gson gson = new Gson();
                            TypeAdapter<SimpleResponse> adapter = gson.getAdapter(SimpleResponse.class);

                            String msg = "";
                            try {
                                SimpleResponse errorParser = adapter.fromJson(body.string());
                                msg = errorParser.getMsg();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            if (errorCode == 400) {
                                tosty(RegisterActivity.this,"Register Error: " + msg);
                            }
                        }
                        //tosty(RegisterActivity.this, "Failed, Try Again");
                        reg_progressBar.setVisibility(View.GONE);
                        alert.cancel();
                    }
                });
    }

    private void showPhotoChooser() {
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(in, "Select Profile Image"), CHOOSE_IMAGE_CODE);

    }
}
