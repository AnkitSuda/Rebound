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
