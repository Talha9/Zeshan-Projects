package com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.SatelliteTrackerApiModel
import com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.SatelliteTrackerRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SatteliteTrackerRepository(var mSatteliteTrackerInterface: SatelliteTrackerRetrofit) {
    val mainSatelliteTrackerData = MutableLiveData<SatelliteTrackerApiModel>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val TAG = "RepoLog:"

    fun getSatelliteTrackerData(id:Int) {
        Log.d(TAG, "getSatelliteData: Start ")
        loading.value = true
        val result =  mSatteliteTrackerInterface.getSatelliteInfo(33.5222455,73.1539248,0.0,70,id,"N863MH-MDY7ZM-7S3UCA-4UNH")

        result.enqueue(object :Callback<SatelliteTrackerApiModel>{
            override fun onResponse(
                call: Call<SatelliteTrackerApiModel>,
                response: Response<SatelliteTrackerApiModel>
            ) {
                Log.d(TAG, "onResponse: ${response.code()}")
                if (response.isSuccessful) {
                    Log.d(TAG, "getFmData: isSuccessful")
                    if (response.body() != null && response.body()!!.above.size > 0) {
                        Log.d(TAG, "getFmData: Body size > 0")
                        mainSatelliteTrackerData.value = response.body()
                    } else {
                        errorMessage.value = response.message()
                    }
                    loading.value = false
                } else {
                    Log.d(TAG, "getFmData: Un success")
                    loading.value = false
                    errorMessage.value = response.message()
                }
            }
            override fun onFailure(call: Call<SatelliteTrackerApiModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                loading.value = false
                errorMessage.value = t.localizedMessage
            }

        })


    }
}