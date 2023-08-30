package com.earthmap.satellite.map.location.map.navigationModule.callbacks

import com.earthmap.satellite.map.location.map.navigationModule.models.TransitRoutesModel


interface onRouteTransitCallback {
     fun onItemRemovedClick(position:Int)
    fun onEditTxtClick(position: Int,model: TransitRoutesModel)
}