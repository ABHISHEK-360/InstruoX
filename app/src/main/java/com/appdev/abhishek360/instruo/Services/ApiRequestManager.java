package com.appdev.abhishek360.instruo.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.appdev.abhishek360.instruo.ApiModels.LoginResponse;
import com.appdev.abhishek360.instruo.ApiModels.RequestModel;
import com.appdev.abhishek360.instruo.LoginActivity;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.appdev.abhishek360.instruo.LoginActivity.tosty;

public class ApiRequestManager {
    private final ApiServices apiService;
    private Context  ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private CompositeDisposable compositeDisposable;

    public ApiRequestManager(Context ctx, CompositeDisposable compositeDisposable) {
        this.ctx = ctx;
        this.compositeDisposable = compositeDisposable;
        apiService = ApiClientInstance
                .getRetrofitInstance(ctx)
                .create(ApiServices.class);
    }

    public boolean updateUserData(final String eventId) {
        RequestModel updateRequest = new RequestModel();
        updateRequest.setRequestData("eventIdAdd",eventId);

        Single<LoginResponse> res = apiService
                .postUpdateProfile(updateRequest);

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        Log.d("REG_EVENT_API_RES:", loginResponse.getMsg());
                        if (loginResponse.getSuccess()) {
                            sharedPreferences = ApiRequestManager.this.ctx.getSharedPreferences(LoginActivity.spKey,MODE_PRIVATE);
                            spEditor=sharedPreferences.edit();
                            Set<String> eventNameSet = sharedPreferences.getStringSet(LoginActivity.spEventsKey, new HashSet<String>());

                            try {
                                eventNameSet.add(eventId);
                            }
                            catch (Exception e) {
                                Log.d("Event Register!",""+e);
                                eventNameSet = new HashSet<>();
                            }

                            spEditor.putStringSet(LoginActivity.spEventsKey,eventNameSet).apply();
                            tosty(ctx,"Registered Successfully ! Please Check Registered Event Page for Payment status.");
                        }
                        else {
                            tosty(ctx,"Try Again! Failed To Register! ");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            builder.setTitle("Session Expired!");
                            builder.setCancelable(false);
                            builder.setMessage("Please Login Again to Continue!");
                            builder.setPositiveButton("OK", (dialog, which) -> {
                                dialog.cancel();
                                Intent  in = new Intent(ctx,LoginActivity.class);
                                spEditor=sharedPreferences.edit();
                                spEditor.clear();
                                spEditor.apply();
                                ctx.startActivity(in);
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tosty(ctx,"Trying Again: Network Error!");
                        Log.e("REG_EVENT_API_ERROR:","Failed", e);
                    }
                });

        return true;
    }


    public boolean updateUserData(final RequestModel updateRequest) {
        Single<LoginResponse> res = apiService
                .postUpdateProfile(updateRequest);

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        Log.d("UPDATE_USER_API_RES:", loginResponse.getMsg());
                        if (loginResponse.getSuccess()) {
                            tosty(ctx,"Try Again! Failed To Update Profile! ");
                        }
                        else {
                            tosty(ctx,"Profile updated!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tosty(ctx,"Trying Again: Network Error!");
                        Log.e("UPDATE_USER_API_ERROR:","Failed", e);
                    }
                });

        return true;
    }
}
