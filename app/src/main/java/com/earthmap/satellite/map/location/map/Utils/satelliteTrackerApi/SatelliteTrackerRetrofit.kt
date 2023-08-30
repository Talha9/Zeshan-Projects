package com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi

import retrofit2.Call
import retrofit2.http.*

interface SatelliteTrackerRetrofit {
    @GET("above/{observer_lat}/{observer_lng}/{observer_alt}/{search_radius}/{category_id}")
    fun getSatelliteInfo(@Path("observer_lat") observer_lat: Double,
                         @Path("observer_lng") observer_lng: Double,
                         @Path("observer_alt") observer_alt: Double,
                         @Path("search_radius") search_radius: Int,
                         @Path("category_id") category_id: Int,
                         @Query("apiKey") apiKey: String
    ): Call<SatelliteTrackerApiModel>

}