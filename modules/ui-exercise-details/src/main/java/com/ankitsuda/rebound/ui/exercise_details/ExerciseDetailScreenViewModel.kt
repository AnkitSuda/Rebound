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

package com.ankitsuda.rebound.ui.exercise_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ankitsuda.navigation.EXERCISE_ID_KEY
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailScreenViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository,
    handle: SavedStateHandle,
) : ViewModel() {
    private val exerciseId = requireNotNull(handle.get<String>(EXERCISE_ID_KEY))

    val exercise = exercisesRepository.getExercise(exerciseId)

    val history = exercisesRepository.getVisibleLogEntries(exerciseId)
}