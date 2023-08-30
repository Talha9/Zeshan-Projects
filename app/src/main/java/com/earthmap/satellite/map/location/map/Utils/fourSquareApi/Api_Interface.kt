package com.earthmap.satellite.map.location.map.Utils.fourSquareApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Api_Interface {
    @GET("/v3/places/search")
    fun getPlaces(
        @Query("ll") ll: String?,
        @Query("client_id") client_id: String?,
        @Query("client_secret") client_secret: String?,
        @Query("v") v: String?,
        @Query("radius") radius: Int,
        @Query("categories") catogries: String?,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String?
    ): Call<ApiModel?>
}