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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.base.utils.toReadableDuration
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    private var _currentWorkout = MutableSharedFlow<Workout?>()
    val currentWorkout = _currentWorkout
        .distinctUntilChanged()
        .shareWhileObserved(viewModelScope)

    private var _currentWorkoutDurationStr = MutableSharedFlow<String?>()
    val currentWorkoutDurationStr = _currentWorkoutDurationStr
        .distinctUntilChanged()
        .shareWhileObserved(viewModelScope)

    private var durationJob: Job? = null

    init {
        viewModelScope.launch {
            workoutsRepository.getCurrentWorkoutId().collectLatest {
                refreshCurrentWorkout(it)
            }
        }
    }

    private fun refreshCurrentWorkout(workoutId: String) {
        viewModelScope.launch {
            var workout = if (workoutId != NONE_WORKOUT_ID) {
                workoutsRepository.getWorkout(workoutId = workoutId).firstOrNull()
            } else {
                null
            }
            setupDurationUpdater(workout?.inProgress == true, workout?.startAt)

            _currentWorkout.emit(workout)
        }
    }


    private fun setupDurationUpdater(inProgress: Boolean, startAt: LocalDateTime?) {
        durationJob?.cancel()
        durationJob = viewModelScope.launch {
            while (inProgress && startAt != null) {
                _currentWorkoutDurationStr.emit(startAt.toReadableDuration())
                delay(25)
            }
        }
    }


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