package com.earthmap.satellite.map.location.map.compassModule.Callbacks

interface CompassCallbacks {
    fun onNewAzimuth(azimuth: Float)
    fun getXYValues(x: Float, y: Float)
}