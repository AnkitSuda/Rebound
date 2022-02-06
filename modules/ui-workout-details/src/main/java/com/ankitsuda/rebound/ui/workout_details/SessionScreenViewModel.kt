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

package com.ankitsuda.rebound.ui.workout_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.ui.UIDataState
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.domain.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SessionScreenViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val workoutsRepository: WorkoutsRepository
) : ViewModel() {
    private val workoutId = requireNotNull(handle.get<Long>(WORKOUT_ID_KEY))

    val logs: SharedFlow<List<LogEntriesWithExerciseJunction>> =
        workoutsRepository.getLogEntriesWithExerciseJunction(
            workoutId = workoutId
        ).distinctUntilChanged()
            .shareWhileObserved(viewModelScope)

    val workout: SharedFlow<Workout?> = workoutsRepository.getWorkout(
        workoutId = workoutId
    ).distinctUntilChanged()
        .shareWhileObserved(viewModelScope)
}