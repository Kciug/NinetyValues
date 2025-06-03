package com.rafalskrzypczyk.ninetyvalues.room.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Long>): String = Gson().toJson(list)

    @TypeConverter
    fun toIntList(json: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return Gson().fromJson(json, type)
    }
}