package com.ankitsuda.rebound.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "body_part_measurement_log")
data class BodyPartMeasurementLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)