package com.ankitsuda.rebound.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseWithMuscle(
    @Embedded
    val exercise: Exercise,
    @Relation(
        parentColumn = "primary_muscle_tag",
        entityColumn = "tag"
    )
    val primaryMuscle: Muscle?,
    @Relation(
        parentColumn = "secondary_muscle_tag",
        entityColumn = "tag"
    )
    val secondaryMuscle: Muscle?,
    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "exercise_id",
        projection = ["id"]
    )
    val junctions: List<ExerciseWorkoutJunction>
)