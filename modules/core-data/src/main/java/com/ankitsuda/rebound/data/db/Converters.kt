/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.data.db

import androidx.room.TypeConverter
import com.ankitsuda.base.util.C
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.parseToExerciseCategory
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Converters {
//    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    // Set timezone value as GMT ... to make time as reasonable
    var df: DateFormat = SimpleDateFormat(C.DB_DATE_FORMAT, Locale.getDefault())


//    @TypeConverter
//    @JvmStatic
//    fun toOffsetDateTime(value: String?): OffsetDateTime? {
//        return value?.let {
//            return formatter.parse(value, OffsetDateTime::from)
//        }
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
//        return date?.format(formatter)
//    }

    @TypeConverter
    @JvmStatic
    fun toExerciseCategory(value: String): ExerciseCategory {
        return value.parseToExerciseCategory()

    }

    @TypeConverter
    @JvmStatic
    fun fromExerciseCategory(category: ExerciseCategory?): String {
        return category?.tag ?: ExerciseCategory.UNKNOWN.tag
    }


    @TypeConverter
    fun toDate(value: String?): Date? {
//        df.timeZone = TimeZone.getTimeZone("GMT")

        return if (value != null) {
            try {
                return df.parse(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }

    @TypeConverter
    fun fromDate(value: Date?): String? {
//        df.timeZone = TimeZone.getTimeZone("GMT")

        return if (value != null) {
            df.format(value)
        } else {
            null
        }
    }
}