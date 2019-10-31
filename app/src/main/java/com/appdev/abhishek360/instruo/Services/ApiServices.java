package com.appdev.abhishek360.instruo.Services;

import com.appdev.abhishek360.instruo.ApiModels.LoginResponse;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiServices {
    @POST("user/login")
    Single<LoginResponse> postLoginCred(@Body Map<String,String> requestModel);
    @POST("user/register")
    Single<LoginResponse> postRegister(@Body Map<String,String> requestModel);
    @POST("user/forgotpassword")
    Single<LoginResponse> postForgotPassword(@Body Map<String,String> requestModel);
    @POST("user/events")
    Single<LoginResponse> postRegEvent(@Body Map<String,String> requestModel);

    @GET("user/profile")
    Single<UserProfileModel> getUserProfile();
    @GET("user/logout")
    Single<UserProfileModel> getLogout();
    @GET("user/events")
    Single<ArrayList> getRegEvents();

    @PUT("user/profile")
    Single<LoginResponse> putUserProfile(@Body Map<String,String> requestModel);
    @PUT("user/resetpassword")
    Single<LoginResponse> putUpdatePass(@Body Map<String,String> requestModel);
}
