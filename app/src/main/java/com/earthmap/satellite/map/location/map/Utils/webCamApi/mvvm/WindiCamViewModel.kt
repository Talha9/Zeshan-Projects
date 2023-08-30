package com.earthmap.satellite.map.location.map.Utils.webCamApi.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WindiCamViewModel(var mWindiCamRepository: WindiCamRepository) : ViewModel() {
    var mWindiCam = mWindiCamRepository.windiCamaData
    var loading = mWindiCamRepository.loading
    var errorMessage = mWindiCamRepository.errorMessage
    val TAG = "RepoLog:"

    fun callForData(country: String, limit: Int, Key: String, show: String) {
          viewModelScope.launch {
            Dispatchers.IO
        mWindiCamRepository.getWindiCamData(country, limit, Key, show)
          }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: View model")
        super.onCleared()
    }
}