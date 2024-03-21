package com.steve_md.smartmkulima.model

/**
 * This data model class accommodates both crop cycle and service cycle
 */
data class Cycle(
    val type: String, // "crop" or "service"
    val endDate: String,
    val startDate: String
) {
    // require this no arg constructor for deserialization
    constructor() : this("", "", "")
}
