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
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.base.utils.toLocalDateTime
import com.ankitsuda.rebound.domain.*
import com.ankitsuda.rebound.domain.entities.BodyPartUnitType
import java.text.ParseException
import java.time.LocalDateTime
import java.util.*

object Converters {

    @TypeConverter
    @JvmStatic
    fun toExerciseCategory(value: String): ExerciseCategory {
        return value.parseToExerciseCategory1()

    }

    @TypeConverter
    @JvmStatic
    fun fromExerciseCategory(category: ExerciseCategory?): String? {
        return category?.tag
    }

    @TypeConverter
    @JvmStatic
    fun toBodyPartUnitType(value: String): BodyPartUnitType {
        return BodyPartUnitType.fromString(value)

    }

    @TypeConverter
    @JvmStatic
    fun fromBodyPartUnitType(value: BodyPartUnitType): String {
        return value.value ?: BodyPartUnitType.LENGTH.value
    }

    @TypeConverter
    @JvmStatic
    fun toSetType(value: String): LogSetType {
        return LogSetType.fromString(value)

    }

    @TypeConverter
    @JvmStatic
    fun fromLogSetType(value: LogSetType?): String {
        return value?.value ?: LogSetType.NORMAL.value
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

    @TypeConverter
    fun fromListOfPersonalRecords(list: List<PersonalRecord>?): String? {
        return list?.toCommaSpString()
    }

    @TypeConverter
    fun toListOfPersonalRecords(str: String?): List<PersonalRecord>? {
        return if (str != null) PersonalRecord.fromCommaSpString(str) else null
    }

    @TypeConverter
    fun toWeightUnit(value: String): WeightUnit {
        return WeightUnit.fromValue(value)

    }

    @TypeConverter
    fun fromWeightUnit(unit: WeightUnit?): String {
        return (unit ?: WeightUnit.KG).value
    }
}