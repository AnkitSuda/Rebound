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

package com.ankitsuda.rebound.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    private var _currentWorkoutId = workoutsRepository.getCurrentWorkoutId()
    val currentWorkoutId = _currentWorkoutId

    fun startEmptyWorkout() {
        viewModelScope.launch {
            val newWorkoutId = workoutsRepository.createWorkout(
                Workout(
                    id = "",
                    startAt = LocalDateTime.now(),
                    completedAt = null,
                    inProgress = true,
                    isHidden = true
                )
            )
            workoutsRepository.setCurrentWorkoutId(newWorkoutId)
        }
    }

    fun cancelCurrentWorkout() {
        viewModelScope.launch {
            workoutsRepository.setCurrentWorkoutId(NONE_WORKOUT_ID)
        }
    }
}