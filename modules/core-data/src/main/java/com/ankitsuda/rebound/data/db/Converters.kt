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
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.base.utils.toLocalDateTime
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.parseToExerciseCategory
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Converters {

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
    fun toDate(value: Long?): Date? {
        return if (value != null) {
            try {
                return Date(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }

    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return if (value != null) {
            try {
                return value.toLocalDateTime()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            null
        } else {
            null
        }
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.toEpochMillis()
    }
}