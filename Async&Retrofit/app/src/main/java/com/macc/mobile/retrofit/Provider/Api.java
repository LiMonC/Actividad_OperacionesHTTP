package com.macc.mobile.retrofit.Provider;

import com.macc.mobile.retrofit.Objetos.WeatherResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mrodriguez on 06/03/2015.
 */
public interface Api {
    @GET("/weather")
    void getWeather(@Query("q") String cityName,@Query("units") String tipo,
                    Callback<WeatherResponse> callback);

}
