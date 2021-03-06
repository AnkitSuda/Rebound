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
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.rebound.domain.PersonalRecord
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "note")
    var note: String? = null,

    @ColumnInfo(name = "in_progress")
    var inProgress: Boolean? = false,
    @ColumnInfo(name = "is_hidden")
    var isHidden: Boolean? = false,

    @ColumnInfo(name = "start_at")
    var startAt: LocalDateTime? = null,
    @ColumnInfo(name = "completed_at")
    var completedAt: LocalDateTime? = null,

    @ColumnInfo(name = "personal_records")
    var personalRecords: List<PersonalRecord>? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: LocalDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: LocalDateTime? = null,
) {
    fun getDuration(): Long? =
        startAt?.toEpochMillis()?.let { completedAt?.toEpochMillis()?.minus(it) }
}
