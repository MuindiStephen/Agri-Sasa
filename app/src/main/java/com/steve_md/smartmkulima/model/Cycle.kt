package com.steve_md.smartmkulima.model

/**
 * This data model class accommodates both crop cycle and service cycle
 */
data class Cycle(
    val farmId: String,
    val cropName: String,
    val startDate: String,
    val type: String, // "crop cycle" or "service cycle/livestock"
    val tasks: Tasks
) {
    // require this no arg constructor for deserialization
    constructor() : this("", "", "","", tasks = Tasks("","",""))
}

data class Tasks (
    val taskName: String,
    val startDate: String,
    val endDate: String
)
