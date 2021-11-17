package com.ankitsuda.rebound.domain

enum class ExerciseCategory(val tag: String, val cName: String) {
    WEIGHTS_AND_REPS("weights_and_reps", "Weights & Reps"),
    REPS("reps", "Reps"),
    DISTANCE_AND_TIME("distance_and_time", "Distance & Time"),
    TIME("time", "Time"),
    UNKNOWN("unknown", "Unknown")
}


fun String?.parseToExerciseCategory(): ExerciseCategory {
    return when (this.toString()) {
        ExerciseCategory.WEIGHTS_AND_REPS.tag -> ExerciseCategory.WEIGHTS_AND_REPS
        ExerciseCategory.REPS.tag -> ExerciseCategory.REPS
        ExerciseCategory.DISTANCE_AND_TIME.tag -> ExerciseCategory.DISTANCE_AND_TIME
        ExerciseCategory.TIME.tag -> ExerciseCategory.TIME
        else -> ExerciseCategory.UNKNOWN
    }
}