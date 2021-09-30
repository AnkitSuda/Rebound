package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "exercise_logs")
data class ExerciseLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "workout_id")
    var workoutId: Long? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: OffsetDateTime? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: OffsetDateTime? = null,
)
