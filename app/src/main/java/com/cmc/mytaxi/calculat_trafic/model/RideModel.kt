package com.cmc.mytaxi.calculat_trafic.model

data class RideModel(
    val distance: Double = 0.0,
    val timeElapsed: Long = 0L,
    val totalFare: Double = 0.0
)