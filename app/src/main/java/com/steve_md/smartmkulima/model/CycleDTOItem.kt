package com.steve_md.smartmkulima.model

data class CycleDTOItem(
    val cropName: String,
    val farmId: String,
    val startDate: String,
    val tasks: List<Task>,
    val type: String
)