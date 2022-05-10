package com.lhd.apps.pagenote.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class ListStringConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }

    @TypeConverter
    fun fromListString(list: List<String>): String {
        return Gson().toJson(list)
    }
}