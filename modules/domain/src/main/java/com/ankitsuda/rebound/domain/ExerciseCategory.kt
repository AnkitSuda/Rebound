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

import android.content.res.TypedArray

enum class ExerciseCategory(val tag: String) {
    WEIGHTS_AND_REPS("weights_and_reps"),
    BODYWEIGHT_REPS("bodyweight_reps"),
    WEIGHTED_BODYWEIGHT("weighted_bodyweight"),
    ASSISTED_BODYWEIGHT("assisted_bodyweight"),
    DURATION("duration"),
    DISTANCE_AND_DURATION("distance_and_duration"),
    WEIGHT_AND_DISTANCE("weight_and_distance"),
    WEIGHT_AND_DURATION("weight_and_duration"),
    UNKNOWN("unknown");
}

fun String?.parseToExerciseCategory(): ExerciseCategory {
    return ExerciseCategory.values().find { it.tag == this } ?: ExerciseCategory.UNKNOWN
}