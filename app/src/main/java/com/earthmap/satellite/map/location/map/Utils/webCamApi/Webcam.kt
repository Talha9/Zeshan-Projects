package com.earthmap.satellite.map.location.map.Utils.webCamApi

data class Webcam(
    val id: String,
    val image: Image,
    val player: Player,
    val status: String,
    val title: String
)