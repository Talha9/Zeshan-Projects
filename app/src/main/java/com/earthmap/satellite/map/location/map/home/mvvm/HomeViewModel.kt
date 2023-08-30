package com.earthmap.satellite.map.location.map.home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.earthmap.satellite.map.location.map.home.Model.HomeItemModel
import com.earthmap.satellite.map.location.map.home.helper.HomeHelper

class HomeViewModel : ViewModel() {

    private val itemList = MutableLiveData<ArrayList<HomeItemModel>>().apply {
        value = HomeHelper.fillHomeScreenItems()
    }
    val list: LiveData<ArrayList<HomeItemModel>> = itemList
}