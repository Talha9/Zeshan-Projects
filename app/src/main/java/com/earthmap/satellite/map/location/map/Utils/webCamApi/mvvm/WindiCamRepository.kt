package com.earthmap.satellite.map.location.map.Utils.webCamApi.mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.earthmap.satellite.map.location.map.Utils.webCamApi.WebCamApiInterface
import com.earthmap.satellite.map.location.map.Utils.webCamApi.WindiCamModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WindiCamRepository(var mWebCamApiInterface: WebCamApiInterface) {
    var windiCamaData = MutableLiveData<WindiCamModel>()
    var loading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    val TAG = "RepoLog:"

   suspend fun getWindiCamData(country: String,limit:Int,Key:String,show:String) {
        Log.d(TAG, "getWindiCamData")
        loading.value = true
        val result = mWebCamApiInterface.getWindiCams(country,limit,Key,show)
        result.enqueue(object : Callback<WindiCamModel> {

            override fun onResponse(call: Call<WindiCamModel>, response: Response<WindiCamModel>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        windiCamaData.value = response.body()
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

            override fun onFailure(call: Call<WindiCamModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
                loading.value = false
                errorMessage.value = t.localizedMessage
            }

        })
    }
}