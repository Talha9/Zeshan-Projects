package com.earthmap.satellite.map.location.map.satelliteTrackerModule.callbacks

import com.earthmap.satellite.map.location.map.satelliteTrackerModule.model.SatelliteTrackerMainModel

interface SatelliteTrackerDialogCallback {
    fun onItemSelected(model: SatelliteTrackerMainModel)
}