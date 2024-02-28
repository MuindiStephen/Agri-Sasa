package com.steve_md.smartmkulima.model

import com.steve_md.smartmkulima.R
import java.util.Date

/**
 * A data class to create a list of crop cycles tasks properties
 */
data class CropCycleTask (
    var taskName: String? = null,
    var selectedCrop: String? = null,
    var taskStartDate: Date?,
    var taskEndDate: Date?,
    var farmInputRequired: String,
    var taskPriority: String, // High/Low
    var taskStatus: String
) {
    constructor() : this("","",Date(), Date(),"","","")
}
//    enum class TaskStatus(val status: String, val colorStatus: Int) {
//        UPCOMING("Upcoming", R.color.violet),
//        IN_PROGRESS("In Progress", R.color.blue),
//        COMPLETED("Completed", R.color.green),
//        OVERDUE("Overdue", R.color.red)
//    }
