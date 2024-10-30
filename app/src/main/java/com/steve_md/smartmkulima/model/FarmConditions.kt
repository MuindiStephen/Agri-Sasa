package com.steve_md.smartmkulima.model

data class FarmConditions(
    val temperature: Double,
    val humidity: Double,
    val soilMoisture: Double,
    val windspeed: Double,
    val precipitation: Double,
    val lightDensity: Double,
    val nbkLevel: Double,
    val soilPh: Double,
    val soilTemperature: Double
)