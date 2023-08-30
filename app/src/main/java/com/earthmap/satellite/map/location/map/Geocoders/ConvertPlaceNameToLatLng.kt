package com.earthmap.satellite.map.location.map.Geocoders

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ConvertPlaceNameToLatLng : CoroutineScope {
    private val TAG = "CoroutineTask:"
    private var job: Job = Job()
    private var mContext: Context
    private var callBack: GeoTaskCallbackLatlngs
    private var placeName: String

    constructor(
        mContext: Context,
        placeName: String,
        callBack: GeoTaskCallbackLatlngs
    ) {
        this.mContext = mContext
        this.callBack = callBack
        this.placeName = placeName
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun execute() = launch { /*launch is having main thread scope*/
        Log.d(TAG, "execute: ${Thread.currentThread().name}")
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    private suspend fun doInBackground(): LatLng? =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "doInBackground: ")
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address?>
            var location: Address? = null
            var latLng: LatLng? = null
            if (Geocoder.isPresent() && isInternetAvailable()) {
                try {
                    listAddressed = geocoderHelper.getFromLocationName(
                        placeName,
                        5
                    ) as java.util.ArrayList<Address?>
                    if (listAddressed.size > 0) {
                        location = listAddressed[0]
                            latLng = LatLng(
                                location!!.latitude,
                                location.longitude
                            )

                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            return@withContext latLng
        }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        try {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun onPostExecute(result: LatLng?) {
        if (result != null) {
            Log.d(TAG, "onPostExecute: Result Success: $result")
            callBack.onSuccessLocationFetched(result)
        } else {
            Log.d(TAG, "onPostExecute: Result Failed")
            callBack.onFailedLocationFetched()
        }
        cancel()
    }

    private fun cancel() {
        Log.d(TAG, "cancel")
        job.cancel()
    }

    private fun onPreExecute() {
        Log.d(TAG, "onPreExecute: ")
        // show progress
        }

    interface GeoTaskCallbackLatlngs {
      public  fun onSuccessLocationFetched(fetchedLatLngs: LatLng?)
      public  fun onFailedLocationFetched()
    }
}