package com.ankitsuda.rebound.utils

enum class ExerciseCategory(val value: String) {
    WEIGHTS_AND_REPS("weights_and_reps"),
    REPS("reps"),
    DISTANCE_AND_TIME("distance_and_time"),
    TIME("time"),
    UNKNOWN("unknown")
}


fun String?.parseToExerciseCategory(): ExerciseCategory {
    return when (this.toString()) {
        ExerciseCategory.WEIGHTS_AND_REPS.value -> ExerciseCategory.WEIGHTS_AND_REPS
        ExerciseCategory.REPS.value -> ExerciseCategory.REPS
        ExerciseCategory.DISTANCE_AND_TIME.value -> ExerciseCategory.DISTANCE_AND_TIME
        ExerciseCategory.TIME.value -> ExerciseCategory.TIME
        else -> ExerciseCategory.UNKNOWN
    }
}