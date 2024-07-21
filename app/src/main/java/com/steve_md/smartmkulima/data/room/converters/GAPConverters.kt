package com.steve_md.smartmkulima.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.GAPtask
import com.steve_md.smartmkulima.model.Tasks

class Converters {
    @TypeConverter
    fun fromTasksList(tasks: List<Tasks>?): String? {
        return Gson().toJson(tasks)
    }

    @TypeConverter
    fun toTasksList(tasksString: String?): List<Tasks>? {
        val listType = object : TypeToken<List<Tasks>>() {}.type
        return Gson().fromJson(tasksString, listType)
    }
}