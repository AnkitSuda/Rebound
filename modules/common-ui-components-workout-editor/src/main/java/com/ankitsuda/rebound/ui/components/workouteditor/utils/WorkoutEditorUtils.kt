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

package com.ankitsuda.rebound.ui.components.workouteditor.utils

import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.SetFieldValueType
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry

object WorkoutEditorUtils {
//    fun isValidSet(logEntry: ExerciseLogEntry, exerciseCategory: ExerciseCategory): Boolean =
//        with(logEntry) {
//            when (exerciseCategory) {
//                ExerciseCategory.WEIGHTS_AND_REPS, ExerciseCategory.WEIGHTED_BODYWEIGHT, ExerciseCategory.ASSISTED_BODYWEIGHT -> {
//                    weight == null || reps == null
//                }
//                ExerciseCategory.BODYWEIGHT_REPS -> {
//                    reps == null
//                }
//                ExerciseCategory.DURATION -> {
//                    timeRecorded == null
//                }
//                ExerciseCategory.DISTANCE_AND_DURATION -> {
//                    distance == null || timeRecorded == null
//                }
//                ExerciseCategory.WEIGHT_AND_DISTANCE -> {
//                    weight == null || distance == null
//                }
//                ExerciseCategory.WEIGHT_AND_DURATION -> {
//                    weight == null || timeRecorded == null
//                }
//                ExerciseCategory.UNKNOWN -> {
//                    true
//                }
//            }
//        }

    fun isValidSet(logEntry: ExerciseLogEntry, exerciseCategory: ExerciseCategory?): Boolean {
        if (exerciseCategory == null) return true

        val fields: List<Any?> = exerciseCategory.fields.map {
            when (it) {
                SetFieldValueType.WEIGHT,
                SetFieldValueType.ADDITIONAL_WEIGHT,
                SetFieldValueType.ASSISTED_WEIGHT -> logEntry.weight
                SetFieldValueType.REPS -> logEntry.reps
                SetFieldValueType.DISTANCE -> logEntry.distance
                SetFieldValueType.DURATION -> logEntry.timeRecorded
                SetFieldValueType.RPE -> true // RPE is allowed to be null
            }
        }

        return !fields.any {
            it == null
        }
    }
}
