package com.steve_md.smartmkulima.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.steve_md.smartmkulima.model.LocalTasks

class LocalFarmCycleConverter {
    @TypeConverter
    fun fromLocalTasksList(value: List<LocalTasks>?): String {
        val gson = Gson()
        val type = object : TypeToken<List<LocalTasks>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toLocalTasksList(value: String): List<LocalTasks>? {
        val gson = Gson()
        val type = object : TypeToken<List<LocalTasks>>() {}.type
        return gson.fromJson(value, type)
    }
}