package com.chuck.android.bakingapp.REST;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeAPI {
    //Defines the API for Udacity Baking using Retrofit
    public static Retrofit getClient() {
        //Base URL for MovieDB
        final String Base_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

        return new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
