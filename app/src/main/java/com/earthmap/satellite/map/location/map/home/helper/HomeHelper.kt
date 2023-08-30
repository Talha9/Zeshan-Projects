package com.earthmap.satellite.map.location.map.home.helper

import android.graphics.Color
import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.home.Model.HomeItemModel

object HomeHelper {
    fun fillHomeScreenItems():ArrayList<HomeItemModel>{
        val item=ArrayList<HomeItemModel>()
        item.add(HomeItemModel(R.drawable.earth_ic,R.drawable.c1_home_item,"Earth Map"))
        item.add(HomeItemModel(R.drawable.satelite_ic,R.drawable.c2_home_item,"Satellite Map"))
        item.add(HomeItemModel(R.drawable.nearby_ic,R.drawable.c5_home_item,"Nearby"))
        item.add(HomeItemModel(R.drawable.weather_ic,R.drawable.c4_home_item,"Weather"))
        item.add(HomeItemModel(R.drawable.navigation_ic,R.drawable.c6_home_item,"Navigation"))
        item.add(HomeItemModel(R.drawable.location_ic,R.drawable.c1_home_item,"My Location"))
        item.add(HomeItemModel(R.drawable.compass_tool,R.drawable.c2_home_item,"Compass"))

        return item
    }
}