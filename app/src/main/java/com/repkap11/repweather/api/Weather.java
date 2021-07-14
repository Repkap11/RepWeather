package com.repkap11.repweather.api;

import com.repkap11.repweather.api.rest.CurrentWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Weather {
    @GET("weather/current/zip/{zip}")
    Call<CurrentWeather> getCurrentWeather(@Path("zip") String zip);
}
