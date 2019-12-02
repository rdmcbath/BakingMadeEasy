package com.example.android.bakingmadeeasy.Networks;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Rebecca on 5/24/2017
 * Retrofit turns HTTP API into a Java interface
 */

public interface RecipeInterface {

    @GET("59121517_baking/baking.json")
    Call<List<Recipe>> getRecipe();

}

