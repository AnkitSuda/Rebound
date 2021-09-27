package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "exercise_log_entries")
data class ExerciseLogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "log_id")
    var logId: Long,
    @ColumnInfo(name = "junction_id")
    var junctionId: Long,

    // Number of set
    @ColumnInfo(name = "set_number")
    var setNumber: Int,

    @ColumnInfo(name = "weight")
    var weight: Float? = null,
    @ColumnInfo(name = "reps")
    var reps: Int? = null,

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
    var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: OffsetDateTime? = null,
)
