package com.appdev.abhishek360.instruo.Services;

import com.appdev.abhishek360.instruo.ApiModels.CredModel;
import com.appdev.abhishek360.instruo.ApiModels.LoginResponse;
import com.appdev.abhishek360.instruo.ApiModels.RequestModel;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    @POST("user/login")
    Single<LoginResponse> postLoginCred(@Body CredModel cred);
    @POST("user/register")
    Single<LoginResponse> postRegister(@Body RequestModel requestModel);
    @POST("user/forgotPassword")
    Single<LoginResponse> postForgotPassword(@Body RequestModel requestModel);
    @POST("user/forgotPassword")
    Single<LoginResponse> postUpdateProfile(@Body RequestModel requestModel);

    @GET("user/profile")
    Single<UserProfileModel> getUserProfile();
    @GET("user/logout")
    Single<UserProfileModel> getLogout();

    @PUT("user/profile")
    Single<UserProfileModel> getUserProfile(@Body RequestModel requestModel);
}
