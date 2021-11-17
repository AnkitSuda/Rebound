package com.ankitsuda.rebound.domain.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LogEntriesWithWorkout(
    @Embedded val junction: ExerciseWorkoutJunction,
    @Relation(
        parentColumn = "workout_id",
        entityColumn = "id"
    )
    val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "junction_id",
    )
    val logEntries: List<ExerciseLogEntry>
)
