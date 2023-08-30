package com.earthmap.satellite.map.location.map.Utils.restCountriesApi.mvvm

import androidx.lifecycle.ViewModel

class RestCountriesViewModel(var mRestCountriesRepository: RestCountriesRepository):ViewModel() {
    var mRestCountriesData=mRestCountriesRepository.restCountriesData
    var loading=mRestCountriesRepository.loading
    var errorMessage=mRestCountriesRepository.errorMessage
    val TAG = "RepoLog:"

    fun callRestCountriesData(){
        mRestCountriesRepository.getCountriesData()
    }

    override fun onCleared() {
        super.onCleared()
    }
}