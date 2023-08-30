package com.earthmap.satellite.map.location.map.navigationModule.helpers

import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.navigationModule.models.NavigationModel


class NavigationHelper {
    companion object{
        fun navigationListFillHelper():ArrayList<NavigationModel>{
            val list=ArrayList<NavigationModel>()
            list.add(NavigationModel("Navigate", R.drawable.ic_baseline_navigation_24,1))
            list.add(NavigationModel("Voice", R.drawable.voice_navigation,2))
            list.add(NavigationModel("Route", R.drawable.roure_finder,3))
            list.add(NavigationModel("Transit", R.drawable.transit_route,4))

            return list
        }



    }
}