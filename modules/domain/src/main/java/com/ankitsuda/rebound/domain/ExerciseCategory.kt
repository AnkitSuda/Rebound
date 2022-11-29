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

enum class SetFieldValueType {
    WEIGHT,
    ADDITIONAL_WEIGHT,
    ASSISTED_WEIGHT,
    REPS,
    RPE,
    DISTANCE,
    DURATION;
}

sealed class ExerciseCategory(open val tag: String, open val fields: List<SetFieldValueType>) {
    object WeightAndReps : ExerciseCategory(
        tag = "weights_and_reps",
        fields = listOf(SetFieldValueType.WEIGHT, SetFieldValueType.REPS, SetFieldValueType.RPE)
    )

    object BodyweightReps : ExerciseCategory(
        tag = "bodyweight_reps",
        fields = listOf(SetFieldValueType.REPS, SetFieldValueType.RPE)
    )

    object WeightedBodyweight : ExerciseCategory(
        tag = "weighted_bodyweight",
        fields = listOf(
            SetFieldValueType.ADDITIONAL_WEIGHT,
            SetFieldValueType.REPS,
            SetFieldValueType.RPE
        )
    )

    object AssistedBodyweight : ExerciseCategory(
        tag = "assisted_bodyweight",
        fields = listOf(
            SetFieldValueType.ASSISTED_WEIGHT,
            SetFieldValueType.REPS,
            SetFieldValueType.RPE
        )
    )

    object Duration : ExerciseCategory(
        tag = "duration",
        fields = listOf(SetFieldValueType.DURATION)
    )

    object DistanceAndDuration : ExerciseCategory(
        tag = "distance_and_duration",
        fields = listOf(SetFieldValueType.DISTANCE, SetFieldValueType.DURATION)
    )

    object WeightAndDistance : ExerciseCategory(
        tag = "weight_and_distance",
        fields = listOf(SetFieldValueType.WEIGHT, SetFieldValueType.DISTANCE)
    )

    object WeightAndDuration : ExerciseCategory(
        tag = "weight_and_duration",
        fields = listOf(SetFieldValueType.WEIGHT, SetFieldValueType.DURATION)
    )

    data class Unknown(
        override val tag: String,
        override val fields: List<SetFieldValueType>
    ) : ExerciseCategory(
        tag = tag,
        fields = fields
    )
}

val allExerciseCategories = listOf(
    ExerciseCategory.WeightAndReps,
    ExerciseCategory.BodyweightReps,
    ExerciseCategory.WeightedBodyweight,
    ExerciseCategory.AssistedBodyweight,
    ExerciseCategory.Duration,
    ExerciseCategory.DistanceAndDuration,
    ExerciseCategory.WeightAndDistance,
    ExerciseCategory.WeightAndDuration,
)

fun String?.parseToExerciseCategory1(): ExerciseCategory {
    return allExerciseCategories.find { it.tag == this } ?: ExerciseCategory.Unknown(
        tag = this ?: "",
        fields = emptyList()
    )
}