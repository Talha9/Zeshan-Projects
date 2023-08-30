package com.earthmap.satellite.map.location.map.Utils.satelliteTrackerApi.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.earthmap.satellite.map.location.map.Utils.webCamApi.mvvm.WindiCamRepository
import com.earthmap.satellite.map.location.map.Utils.webCamApi.mvvm.WindiCamViewModel

class SatteliteTrackerModelFactory(var mSatteliteTrackerRepository: SatteliteTrackerRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SatteliteTrackerViewModel::class.java)) {
            SatteliteTrackerViewModel(this.mSatteliteTrackerRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}