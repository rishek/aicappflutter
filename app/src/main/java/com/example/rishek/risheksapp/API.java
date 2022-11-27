package com.example.rishek.risheksapp;


import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Query;

/**
 * Created by Belal on 10/2/2017.
 */

interface Api {

    String BASE_URL ="http://countryapi.gear.host/v1/Country/";
    @GET("getCountries")
    Call<Countries> getCountries();

    String BASE_URL2="https://api.exchangeratesapi.io/";
    @GET("latest")
    Call<String> getRates(@Query("base") String fjkldsjsdjkgdfb);
}

