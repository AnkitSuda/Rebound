package com.ankitsuda.rebound.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_workout_junctions")
data class ExerciseWorkoutJunctions(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "exercise_d")
    var exerciseId: Long,
    @ColumnInfo(name = "workout_id")
    var workoutId: Long,
)

