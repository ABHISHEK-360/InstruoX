package com.appdev.abhishek360.instruo.Services;

import com.appdev.abhishek360.instruo.ApiModels.SimpleResponse;
import com.appdev.abhishek360.instruo.ApiModels.UserProfileModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiServices {
    @POST("user/login")
    Single<SimpleResponse> postLoginCred(@Body Map<String,String> requestModel);
    @POST("user/register")
    Single<SimpleResponse> postRegister(@Body Map<String,String> requestModel);
    @POST("user/forgotpassword")
    Single<SimpleResponse> postForgotPassword(@Body Map<String,String> requestModel);
    @POST("user/events")
    Single<SimpleResponse> postRegEvent(@Body Map<String,String> requestModel);

    @GET("user/profile")
    Single<UserProfileModel> getUserProfile();
    @GET("user/logout")
    Single<UserProfileModel> getLogout();
    @GET("user/events")
    Single<ArrayList<HashMap<String,String>>> getRegEvents();

    @PUT("user/profile")
    Single<SimpleResponse> putUserProfile(@Body Map<String,String> requestModel);
    @PUT("user/resetpassword")
    Single<SimpleResponse> putUpdatePass(@Body Map<String,String> requestModel);
}
