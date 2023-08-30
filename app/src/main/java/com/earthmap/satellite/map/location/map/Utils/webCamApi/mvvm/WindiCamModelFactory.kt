package com.earthmap.satellite.map.location.map.Utils.webCamApi.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WindiCamModelFactory(var mWindiCamRepository: WindiCamRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WindiCamViewModel::class.java)) {
            WindiCamViewModel(this.mWindiCamRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}