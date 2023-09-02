package com.gb.androidapponjava.modules;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather?")
    Call<WeatherRequest> loadWeatherWithParameter(@Query("q") String city,
                                                  @Query("appid") String apiKey,
                                                  @Query("units") String weatherParameter);
}
