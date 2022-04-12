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

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.time.LocalDateTime
import java.util.*


@Entity(tableName = "body_part_measurement_logs")
data class BodyPartMeasurementLog(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "body_part_id")
    var bodyPartId: String? = null,
    @ColumnInfo(name = "note")
    var note: String? = null,
    @ColumnInfo(name = "image")
    var image: String? = null,
    @ColumnInfo(name = "measurement")
    var measurement: Double,
    @ColumnInfo(name = "measurement_unit")
    var measurementUnit: String? = null,
    @ColumnInfo(name = "measurement_type")
    var measurementType: String? = null,
    @ColumnInfo(name = "created_at")
    var createdAt: LocalDateTime? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
)