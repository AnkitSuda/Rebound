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

package com.ankitsuda.common.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ankitsuda.base.utils.TimePeriod
import com.ankitsuda.rebound.domain.ExerciseCategory

@Composable
fun ExerciseCategory.toI18NString() = toI18NString(LocalContext.current)

@Composable
fun ExerciseCategory.toI18NStringExamples() = toI18NStringExamples(LocalContext.current)

fun ExerciseCategory.toI18NString(context: Context) = context.getString(
    when (this) {
        ExerciseCategory.AssistedBodyweight -> R.string.exercise_category_assisted_bodyweight_title
        ExerciseCategory.BodyweightReps -> R.string.exercise_category_bodyweight_reps_title
        ExerciseCategory.DistanceAndDuration -> R.string.exercise_category_distance_and_duration_title
        ExerciseCategory.Duration -> R.string.exercise_category_duration_title
        ExerciseCategory.WeightAndDistance -> R.string.exercise_category_weight_and_distance_title
        ExerciseCategory.WeightAndDuration -> R.string.exercise_category_weight_and_duration_title
        ExerciseCategory.WeightAndReps -> R.string.exercise_category_weight_and_reps_title
        ExerciseCategory.WeightedBodyweight -> R.string.exercise_category_weighted_bodyweight_title
        is ExerciseCategory.Unknown -> R.string.unknown
    }
)

fun ExerciseCategory.toI18NStringExamples(context: Context) = context.getString(
    when (this) {
        ExerciseCategory.AssistedBodyweight -> R.string.exercise_category_assisted_bodyweight_examples
        ExerciseCategory.BodyweightReps -> R.string.exercise_category_bodyweight_reps_examples
        ExerciseCategory.DistanceAndDuration -> R.string.exercise_category_distance_and_duration_examples
        ExerciseCategory.Duration -> R.string.exercise_category_duration_examples
        ExerciseCategory.WeightAndDistance -> R.string.exercise_category_weight_and_distance_examples
        ExerciseCategory.WeightAndDuration -> R.string.exercise_category_weight_and_duration_examples
        ExerciseCategory.WeightAndReps -> R.string.exercise_category_weight_and_reps_examples
        ExerciseCategory.WeightedBodyweight -> R.string.exercise_category_weighted_bodyweight_examples
        is ExerciseCategory.Unknown -> R.string.unknown_examples
    }
)