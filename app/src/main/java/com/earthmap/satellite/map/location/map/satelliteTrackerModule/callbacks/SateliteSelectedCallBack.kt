package com.earthmap.satellite.map.location.map.satelliteTrackerModule.callbacks

import com.earthmap.satellite.map.location.map.satelliteTrackerModule.model.SatelliteTrackerMainModel


interface SateliteSelectedCallBack {
    fun onSatelliteClick(model: SatelliteTrackerMainModel)
}