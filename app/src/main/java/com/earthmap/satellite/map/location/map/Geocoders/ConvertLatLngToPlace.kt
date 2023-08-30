package com.earthmap.satellite.map.location.map.Geocoders

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.earthmap.satellite.map.location.map.Utils.constants
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ConvertLatLngToPlace(var mContext: Context, var latLng: LatLng, var callBack: GeoTaskCallbackPlace) : CoroutineScope {
    private val TAG = "CoroutineTask:"
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    fun execute() = launch { /*launch is having main thread scope*/
        Log.d(TAG, "execute: ${Thread.currentThread().name}")
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    private fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute: Result: $result ${Thread.currentThread().name}")
        Log.d(TAG, "onPostExecute: Job is C ${job.isCancelled}")
        Log.d(TAG, "onPostExecute: Job is A ${job.isActive}")

        if (result != null) {
            callBack.onSuccessLocationFetched(result)
        } else {
            Log.d(TAG, "onPostExecute: Result Failed")
            callBack.onFailedLocationFetched()
        }
        cancel()
        Log.d(TAG, "onPostExecute: Job is C ${job.isCancelled}")
        Log.d(TAG, "onPostExecute: Job is A ${job.isActive}")
    }

    private suspend fun doInBackground(): String =
        withContext(Dispatchers.IO) { // to run code in Background Thread
            Log.d(TAG, "doInBackground: ${Thread.currentThread().name}")
            // delay(1000)
            var location = "User Location"
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address>
            if (Geocoder.isPresent() && isInternetAvailable()) {
                try {
                    listAddressed = geocoderHelper.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                    ) as ArrayList<Address>
                    if (listAddressed.size > 0) {
                        location = listAddressed[0].getAddressLine(0)
                        constants.countryName = listAddressed[0].countryName
                    }
                } catch (e: Exception) {
                }
            }
            return@withContext location
        }

    fun onPreExecute() {
        Log.d(TAG, "onPreExecute: ${Thread.currentThread().name}")
        // show progress
    }

    fun isInternetAvailable(): Boolean {
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


    interface GeoTaskCallbackPlace {
      fun onSuccessLocationFetched(fetchedAddress: String?)
      public  fun onFailedLocationFetched()
    }
}