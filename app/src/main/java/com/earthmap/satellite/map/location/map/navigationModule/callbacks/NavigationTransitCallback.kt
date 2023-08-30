package com.earthmap.satellite.map.location.map.navigationModule.callbacks

import com.earthmap.satellite.map.location.map.navigationModule.models.TransitWayPointModel


interface NavigationTransitCallback {
    fun gettingWaypointsData(model: TransitWayPointModel)
}