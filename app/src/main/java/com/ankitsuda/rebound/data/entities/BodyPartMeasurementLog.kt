package com.ankitsuda.rebound.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import org.threeten.bp.OffsetDateTime
import java.util.*


@Entity(tableName = "body_part_measurement_logs")
data class BodyPartMeasurementLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "body_part_id")
    var bodyPartId: Long,
    @ColumnInfo(name = "note")
    var note: String,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "measurement")
    var measurement: Float,
    @ColumnInfo(name = "measurement_unit")
    var measurementUnit: String,
    @ColumnInfo(name = "measurement_type")
    var measurementType: String,
    @ColumnInfo(name = "created_at")
    var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: OffsetDateTime? = null,
)