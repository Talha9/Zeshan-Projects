package com.earthmap.satellite.map.location.map.navigationModule.callbacks

import com.earthmap.satellite.map.location.map.navigationModule.models.PlaceResultModel


interface onPlaceTextChangeCallback {
    fun onTextChange(model: PlaceResultModel)
}