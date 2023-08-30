package com.earthmap.satellite.map.location.map.Utils.fourSquareApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Api_Instance
{
    companion object{
        val urlFoursquare = "https://api.foursquare.com/"
        private var retrofitFoursquare: Retrofit? = null
        fun getRetrofitInstance(): Retrofit? {
            if (retrofitFoursquare == null) {
                try {
                    val builder = OkHttpClient.Builder()
                    builder.connectTimeout(30, TimeUnit.SECONDS)
                    builder.readTimeout(30, TimeUnit.SECONDS)
                    builder.writeTimeout(30, TimeUnit.SECONDS)
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                    builder.addInterceptor(interceptor)
                    retrofitFoursquare = Retrofit.Builder()
                        .baseUrl(urlFoursquare)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(builder.build())
                        .build()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return retrofitFoursquare
        }
    }
}