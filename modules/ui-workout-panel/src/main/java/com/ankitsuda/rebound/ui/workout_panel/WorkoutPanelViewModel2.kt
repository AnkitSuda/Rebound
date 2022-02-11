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

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WorkoutPanelViewModel2 @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val exercisesRepository: ExercisesRepository
) :
    ViewModel() {
    private val _workout: MutableStateFlow<Workout?> = MutableStateFlow(
        Workout(
            id = Random.nextLong(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    )
    val workout = _workout.asStateFlow()

    private var _logEntriesWithExerciseJunction: SnapshotStateList<LogEntriesWithExerciseJunction> =
        SnapshotStateList()
    val logEntriesWithExerciseJunction = _logEntriesWithExerciseJunction

    fun updateWorkoutName(name: String) {
        viewModelScope.launch {
            _workout.value = _workout.value?.copy(name = name)
        }
    }

    fun updateWorkoutNote(note: String) {
        viewModelScope.launch {
            _workout.value = _workout.value?.copy(note = note)
        }
    }

    fun cancelCurrentWorkout() {
        viewModelScope.launch {
//            workoutsRepository.setCurrentWorkoutId(-1)
//
//            mWorkout?.let {
//                workoutsRepository.deleteWorkoutWithEverything(it)
//            }
        }
    }

    fun addExerciseToWorkout(exerciseId: Long) {
        viewModelScope.launch {
            val exercise = exercisesRepository.getExercise(exerciseId).first()
            val junction = LogEntriesWithExerciseJunction(
                junction = ExerciseWorkoutJunction(
                    id = Random.nextLong(),
                    exerciseId = exercise.exerciseId
                ),
                exercise = exercise,
                logEntries = emptyList()
            )
            _logEntriesWithExerciseJunction.add(junction)
        }
    }

    fun addEmptySetToExercise(setNumber: Int, exerciseWorkoutJunction: ExerciseWorkoutJunction) {
        viewModelScope.launch {
            val lastJunctions = _logEntriesWithExerciseJunction.toMutableList()
            val junction = lastJunctions.find { it.junction.id == exerciseWorkoutJunction.id }
            updateLog(
                junction = junction,
                task = {
                    it.add(
                        ExerciseLogEntry(
                            entryId = Random.nextLong(),
                            setNumber = setNumber,
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now()
                        )
                    )
                }
            )
        }
    }

    fun updateLogEntry(entry: ExerciseLogEntry) {
        viewModelScope.launch {
            updateLog(
                entryId = entry.entryId,
                task = {
                    it[it.indexOf(it.find { l -> l.entryId == entry.entryId })] = entry.copy(
                        updatedAt = LocalDateTime.now()
                    )
                })
        }
    }

    fun deleteLogEntry(entry: ExerciseLogEntry) {
        viewModelScope.launch {

            updateLog(
                entryId = entry.entryId,
                task = {
                    it.removeAt(it.indexOf(it.find { l -> l.entryId == entry.entryId }))
                })

        }
    }

    fun deleteExerciseFromWorkout(logEntriesWithJunctionItem: LogEntriesWithExerciseJunction) {
        viewModelScope.launch {
            _logEntriesWithExerciseJunction.remove(logEntriesWithJunctionItem)
        }
    }

    private fun updateLog(
        entryId: Long? = null,
        junction: LogEntriesWithExerciseJunction? = null,
        task: (ArrayList<ExerciseLogEntry>)
        -> Unit
    ) {

        val lastJunctions = _logEntriesWithExerciseJunction.toMutableList()
        val mJunction =
            junction ?: lastJunctions.find { it.logEntries.any { e -> e.entryId == entryId } }
        val indexOfJunction = lastJunctions.indexOf(mJunction)
        val logs = mJunction?.logEntries?.toArrayList()
        if (logs != null) {
            task(logs)
            mJunction.logEntries = logs
            val lastList = _logEntriesWithExerciseJunction.toMutableList()
            lastList[indexOfJunction] = mJunction
            _logEntriesWithExerciseJunction.clear()
            _logEntriesWithExerciseJunction.addAll(lastList)
        }
    }
}