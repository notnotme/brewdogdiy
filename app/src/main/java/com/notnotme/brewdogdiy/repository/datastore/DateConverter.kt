package com.notnotme.brewdogdiy.repository.datastore

import androidx.room.TypeConverter
import java.util.*

object DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?) = if (dateLong == null) null else Date(dateLong)

    @TypeConverter
    fun fromDate(date: Date?) = date?.time

}