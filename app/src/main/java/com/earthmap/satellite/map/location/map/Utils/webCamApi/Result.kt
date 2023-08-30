package com.earthmap.satellite.map.location.map.Utils.webCamApi

data class Result(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val webcams: List<Webcam>
)