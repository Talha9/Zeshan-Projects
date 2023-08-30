package com.earthmap.satellite.map.location.map.nearBy.callbacks

import com.earthmap.satellite.map.location.map.nearBy.models.NearByModel


interface NearByCallbacks {
    fun onNearByCategoryClick(model: NearByModel)
}