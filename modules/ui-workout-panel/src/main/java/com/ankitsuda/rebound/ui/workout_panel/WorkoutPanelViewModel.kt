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

package com.ankitsuda.rebound.ui.workout_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.domain.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.domain.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPanelViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    val currentWorkoutId: Flow<String> = workoutsRepository.getCurrentWorkoutId()
    var mWorkoutId: Long = -1
    var mWorkout: Workout? = null

    fun getWorkout(workoutId: String): Flow<Workout?> =
        workoutsRepository.getWorkout(workoutId)

    fun getExerciseWorkoutJunctions() =
        workoutsRepository.getExerciseWorkoutJunctions(mWorkout?.id ?: NONE_WORKOUT_ID)

    fun getLogEntriesWithExerciseJunction() =
        workoutsRepository.getLogEntriesWithExerciseJunction(mWorkout?.id ?: NONE_WORKOUT_ID)

    fun updateWorkoutName(name: String) {
        viewModelScope.launch {
            mWorkout?.let {
                workoutsRepository.updateWorkout(it.copy(name = name))
            }
        }
    }

    fun updateWorkoutNote(note: String) {
        viewModelScope.launch {
            mWorkout?.let {
                workoutsRepository.updateWorkout(it.copy(note = note))
            }
        }
    }

    fun cancelCurrentWorkout() {
        viewModelScope.launch {
            workoutsRepository.setCurrentWorkoutId(NONE_WORKOUT_ID)

            mWorkout?.let {
                workoutsRepository.deleteWorkoutWithEverything(it)
            }
        }
    }

    fun addExerciseToWorkout(exerciseId: String) {
        viewModelScope.launch {
            if (mWorkout != null) {
                workoutsRepository.addExerciseToWorkout(
                    workoutId = mWorkout!!.id,
                    exerciseId = exerciseId
                )
            }
        }
    }

    fun addEmptySetToExercise(setNumber: Int, exerciseWorkoutJunction: ExerciseWorkoutJunction) {
        viewModelScope.launch {
            workoutsRepository.addEmptySetToExercise(setNumber, exerciseWorkoutJunction)
        }
    }

    fun updateLogEntry(entry: ExerciseLogEntry) {
        viewModelScope.launch {
            workoutsRepository.updateExerciseLogEntry(entry)
        }
    }

    fun deleteLogEntry(entry: ExerciseLogEntry) {
        viewModelScope.launch {
            workoutsRepository.deleteExerciseLogEntry(entry)
        }
    }

    fun deleteExerciseFromWorkout(logEntriesWithJunctionItem: LogEntriesWithExerciseJunction) {
        viewModelScope.launch {
            workoutsRepository.deleteExerciseFromWorkout(logEntriesWithJunctionItem)
        }
    }
}