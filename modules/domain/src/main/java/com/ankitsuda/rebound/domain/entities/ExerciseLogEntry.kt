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

package com.ankitsuda.rebound.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "exercise_log_entries")
data class ExerciseLogEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entry_id")
    val entryId: Long = 0,

    @ColumnInfo(name = "log_id")
    var logId: Long? = null,
    @ColumnInfo(name = "junction_id")
    var junctionId: Long? = null,

    // Number of set
    @ColumnInfo(name = "set_number")
    var setNumber: Int? = null,

    @ColumnInfo(name = "weight")
    var weight: Float? = null,
    @ColumnInfo(name = "reps")
    var reps: Int? = null,

    @ColumnInfo(name = "completed")
    var completed: Boolean = false,

    // Time in milliseconds
    @ColumnInfo(name = "time_recorded")
    var timeRecorded: Long? = null,

    @ColumnInfo(name = "distance")
    var distance: Long? = null,

    @ColumnInfo(name = "weight_unit")
    var weight_unit: String? = null,
    @ColumnInfo(name = "distance_unit")
    var distance_unit: String? = null,


    @ColumnInfo(name = "created_at")
    var createdAt: Date? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: Date? = null,
)
