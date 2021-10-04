package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import java.util.*

@Entity(tableName = "workout_template_exercises")
data class WorkoutTemplateExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "template_id")
    var templateId: Long,
    @ColumnInfo(name = "exercise_id")
    var exerciseId: Long,

    @ColumnInfo(name = "sets")
    var sets: Int? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: Date? = null,
    @ColumnInfo(name = "update_at")
    var updatedAt: Date? = null,
)