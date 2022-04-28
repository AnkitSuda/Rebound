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
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.domain.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionScreenViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val workoutsRepository: WorkoutsRepository
) : ViewModel() {
    private val workoutId = requireNotNull(handle.get<String>(WORKOUT_ID_KEY))

    val logs: SharedFlow<List<LogEntriesWithExerciseJunction>> =
        workoutsRepository.getLogEntriesWithExerciseJunction(
            workoutId = workoutId
        ).distinctUntilChanged()
            .map {
                calculateTotalVolumeLifted(it)
                it
            }
            .shareWhileObserved(viewModelScope)

    val workout: SharedFlow<Workout?> = workoutsRepository.getWorkout(
        workoutId = workoutId
    ).distinctUntilChanged()
        .shareWhileObserved(viewModelScope)

    val totalVolume: SharedFlow<Float> = workoutsRepository.getTotalVolumeLiftedByWorkoutId(
        workoutId = workoutId
    ).distinctUntilChanged()
        .shareWhileObserved(viewModelScope)
//
//    private var _totalVolume: MutableStateFlow<Double> = MutableStateFlow(0.0)
//    val totalVolume = _totalVolume.asStateFlow()

    private fun calculateTotalVolumeLifted(mLogs: List<LogEntriesWithExerciseJunction>) {
//        viewModelScope.launch {
//            var volume = (0.0).toDouble()
//            for (log in mLogs) {
//                for (entry in log.logEntries) {
//                    if (entry.setType != LogSetType.WARM_UP) {
//                        volume += (entry.weight?.toDouble()
//                            ?: (0).toDouble()) * (entry.reps?.toDouble()
//                            ?: (0).toDouble())
//                    }
//                }
//            }
//
//            _totalVolume.value = volume
//        }
    }


    fun startWorkout(
        discardActive: Boolean,
        onWorkoutAlreadyActive: () -> Unit
    ) {
        viewModelScope.launch {
            workoutsRepository.startWorkoutFromWorkout(
                workoutId = workoutId,
                discardActive = discardActive,
                onWorkoutAlreadyActive = onWorkoutAlreadyActive
            )

        }
    }
}