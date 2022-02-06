/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

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