package com.ankitsuda.rebound.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_workout_junctions")
data class ExerciseWorkoutJunction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "exercise_id")
    var exerciseId: Long? = null,
    @ColumnInfo(name = "workout_id")
    var workoutId: Long? = null,
)

