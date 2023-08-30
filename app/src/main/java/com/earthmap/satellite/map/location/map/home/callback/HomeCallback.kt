package com.earthmap.satellite.map.location.map.home.callback

import com.earthmap.satellite.map.location.map.home.Model.HomeItemModel

interface HomeCallback {
    fun onItemClick(model: HomeItemModel, position: Int)
}