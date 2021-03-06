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
import com.ankitsuda.rebound.domain.LogSetType
import java.time.LocalDateTime

@Entity(tableName = "body_parts")
data class BodyPart(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "image")
    var image: String? = null,
    @ColumnInfo(name = "group_id")
    var groupId: String? = null,
    @ColumnInfo(name = "unit_type")
    var unitType: BodyPartUnitType? = null,
    @ColumnInfo(name = "is_deletable")
    var isDeletable: Boolean? = null,
    @ColumnInfo(name = "is_hidden")
    var isHidden: Boolean? = null,
    @ColumnInfo(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
    @ColumnInfo(name = "created_at")
    var createdAt: LocalDateTime? = null,
)

enum class BodyPartUnitType(val value: String) {
    WEIGHT("weight"),
    PERCENTAGE("percentage"),
    CALORIES("calories"),
    LENGTH("length");

    companion object {
        fun fromString(value: String): BodyPartUnitType {
            return values().find { it.value == value } ?: LENGTH
        }
    }
}