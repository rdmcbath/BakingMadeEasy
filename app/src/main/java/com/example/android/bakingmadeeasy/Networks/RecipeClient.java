package com.example.android.bakingmadeeasy.Networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rebecca on 5/24/2017.
 * Retrofit is the class through which API interfaces are turned into callable objects.
 */

public class RecipeClient {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/";

    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
