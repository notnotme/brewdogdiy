package com.notnotme.brewdog_recipes.controller.application.api.retrofit;

import com.notnotme.brewdog_recipes.model.Beer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface PunkApi {

    String API_URL = "https://api.punkapi.com/v2/";

    @GET("beers?per_page=80")
    Call<List<Beer>> getBeers(@Query("page") int page);

}