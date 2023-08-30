package com.earthmap.satellite.map.location.map.Utils.webCamApi

data class Image(
    val current: Current,
    val daylight: Daylight,
    val sizes: Sizes,
    val update: Int
)