package com.steve_md.smartmkulima.model

/**
 * This data model class accommodates both service and crop cycles
 */
data class Cycle(
    val farmId: String,
    val type: String, // "crop" or "service"
    val cycle: String,
    val startDate: String
)
