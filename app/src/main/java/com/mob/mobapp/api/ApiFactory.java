package com.mob.mobapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static ApiFactory instance;
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.0.96:8080/"; // "http://10.0.2.2:8080/"; //

    private ApiFactory() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static synchronized ApiFactory getInstance() {
        if (instance == null) {
            instance = new ApiFactory();
        }
        return instance;
    }

    public ApiService getApiService() { return retrofit.create(ApiService.class); }
}
