package com.mob.mobapp.api;

import com.google.gson.JsonObject;
import com.mob.mobapp.pojos.Center;
import com.mob.mobapp.pojos.InitData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("getInitial")
    Call<InitData> getInit(@Query("name") String name, @Query("tel") String tel);

    @GET("firstAuth")
    Call<Void> firstAuth(@Query("name") String name, @Query("tel") String tel,
                                  @Query("token") String token);
    @GET("getCentersUser")
    Call<ArrayList<Center>> getCentersUser(@Query("name") String name, @Query("tel") String tel);
}
