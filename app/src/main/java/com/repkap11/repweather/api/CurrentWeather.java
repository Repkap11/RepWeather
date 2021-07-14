package com.repkap11.repweather.api;

import com.repkap11.repweather.api.rest.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CurrentWeather {
    @GET("weather/current/zip/{zip}")
    Call<Weather> getCurrentWeather(@Path("zip") String zip);
}
