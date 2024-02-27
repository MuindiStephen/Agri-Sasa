package com.steve_md.smartmkulima.model

import com.steve_md.smartmkulima.R
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
    val taskPriority: String, // High/Low
    val taskStatus: String
)
//    enum class TaskStatus(val status: String, val colorStatus: Int) {
//        UPCOMING("Upcoming", R.color.violet),
//        IN_PROGRESS("In Progress", R.color.blue),
//        COMPLETED("Completed", R.color.green),
//        OVERDUE("Overdue", R.color.red)
//    }
