package com.earthmap.satellite.map.location.map.weatherModule.api_weather;

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast")
    fun getForCast(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("units") unit: String?,
        @Query("appid") appId: String?
    ): Call<WeatherMainModel?>?
}