package com.earthmap.satellite.map.location.map.navigationModule.callbacks

import com.earthmap.satellite.map.location.map.navigationModule.models.NavigationRouteButtonsModel


interface NavigationRouteCallback {
    fun onRouteButtonClick(model: NavigationRouteButtonsModel, position: Int)
}