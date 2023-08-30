package com.earthmap.satellite.map.location.map.navigationModule.callbacks

import com.earthmap.satellite.map.location.map.navigationModule.models.PlaceResultModel


interface onPlaceRouteCallback {
    fun onRoutesGenerate(model: PlaceResultModel)
}