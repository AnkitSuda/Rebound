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
import com.ankitsuda.rebound.domain.WeightUnit
import java.time.LocalDateTime

@Entity(tableName = "plates")
data class Plate(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    // weight column should be stored according to the weight unit
    @ColumnInfo(name = "weight")
    var weight: Double? = null,

    @ColumnInfo(name = "for_weight_unit")
    var forWeightUnit: WeightUnit? = null,

    @ColumnInfo(name = "is_active")
    var isActive: Boolean? = null,

    @ColumnInfo(name = "color")
    var color: String? = null,
    @ColumnInfo(name = "color_value_type")
    var colorValueType: String? = null,

    @ColumnInfo(name = "height")
    var height: Float? = null,
    @ColumnInfo(name = "width")
    var width: Float? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: LocalDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: LocalDateTime? = null,
)