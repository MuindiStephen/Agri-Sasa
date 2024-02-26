package com.steve_md.smartmkulima.model

import java.util.Date

/**
 * A data class to create a list of crop cycles tasks properties
 */
data class CropCycleTask(
    val taskName: String,
    val selectedCrop: String,
    val taskStartDate: Date?,
    val taskEndDate: Date?,
    val farmInputRequired: String,
    val taskPriority: String // High/Low
)