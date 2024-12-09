package com.cmc.mytaxi.model

data class RideModel(
    val distance: Double = 0.0,
    val timeElapsed: Long = 0L,
    val totalFare: Double = 0.0
)