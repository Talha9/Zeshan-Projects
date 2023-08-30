package com.earthmap.satellite.map.location.map.Utils.webCamApi

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebCamApiInstance {
    companion object{
        fun getInstance(): Retrofit {
            val client = OkHttpClient.Builder()
            client.connectTimeout(1, TimeUnit.MINUTES)
            client.readTimeout(1, TimeUnit.MINUTES)
            client.writeTimeout(1, TimeUnit.MINUTES)

            val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(interceptor)
            Log.d("mSpaceInfoViewModelTAG", "getInstance: ")

            return Retrofit.Builder()
                .baseUrl("https://api.windy.com/api/webcams/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
    }

}