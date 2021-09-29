package com.ankitsuda.rebound.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LogEntriesWithExerciseJunction (
    @Embedded val junction: ExerciseWorkoutJunction,
    @Relation(
        parentColumn = "id",
        entityColumn = "junction_id",
    )
    val logEntries: List<ExerciseLogEntry>

)