package com.steve_md.smartmkulima.model

/**
 * Model to map farm conditions data values.
 */
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