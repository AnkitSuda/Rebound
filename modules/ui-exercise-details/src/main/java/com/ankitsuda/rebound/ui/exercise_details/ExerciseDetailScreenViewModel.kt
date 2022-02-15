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

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailScreenViewModel @Inject constructor(private val exercisesRepository: ExercisesRepository) :
    ViewModel() {

    fun getExercise(exerciseId: String) = exercisesRepository.getExercise(exerciseId)

    fun getHistory(exerciseId: String) = exercisesRepository.getAllLogEntries(exerciseId)
}