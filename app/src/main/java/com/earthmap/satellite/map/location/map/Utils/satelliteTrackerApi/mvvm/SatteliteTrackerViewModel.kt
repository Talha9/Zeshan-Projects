package com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SatteliteTrackerViewModel(var mSatteliteTrackerRepository: SatteliteTrackerRepository) : ViewModel() {
    var satelliteTrackerData = mSatteliteTrackerRepository.mainSatelliteTrackerData
    var errorMessage = mSatteliteTrackerRepository.errorMessage
    var loading = mSatteliteTrackerRepository.loading
    val TAG = "RepoLog:"

    init {
        Log.d(TAG, "initViewModel: ")
    }

    fun callForData(id: Int) {
        Log.d(TAG, "callForData: ")
        mSatteliteTrackerRepository.getSatelliteTrackerData(id)
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: View model")
        super.onCleared()
    }
}