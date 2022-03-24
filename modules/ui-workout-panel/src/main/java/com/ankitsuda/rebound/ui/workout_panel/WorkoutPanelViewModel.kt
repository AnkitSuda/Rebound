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
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.domain.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.domain.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutPanelViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    val currentWorkoutId: Flow<String> = workoutsRepository.getCurrentWorkoutId()
    var mWorkoutId: Long = -1
    var mWorkout: Workout? = null

    private var _currentDurationStr = MutableStateFlow<String>("")
    val currentDurationStr = _currentDurationStr.asStateFlow()

    private var workoutFlowJob: Job? = null
    private var entriesFlowJob: Job? = null
    private var durationJob: Job? = null

    private var _workout: MutableStateFlow<Workout?> =
        MutableStateFlow(null)
    val workout = _workout.asStateFlow()

    private var _logEntriesWithExerciseJunction: MutableStateFlow<List<LogEntriesWithExerciseJunction>> =
        MutableStateFlow(emptyList())
    val logEntriesWithExerciseJunction = _logEntriesWithExerciseJunction.asStateFlow()
//    private var _logEntriesWithExerciseJunction: SnapshotStateList<LogEntriesWithExerciseJunction> =
//        SnapshotStateList()
//    val logEntriesWithExerciseJunction = _logEntriesWithExerciseJunction

    init {
        Timber.d("WorkoutPanelViewModel instance $this")
        viewModelScope.launch {
            currentWorkoutId.collectLatest {
                if (it != NONE_WORKOUT_ID) {
                    refresh(it)
                }
            }
        }
    }

    private fun refresh(newWorkoutId: String) {
        entriesFlowJob?.cancel()
        entriesFlowJob = viewModelScope.launch {
            workoutsRepository.getLogEntriesWithExerciseJunction(
                newWorkoutId
            ).collectLatest {
//                _logEntriesWithExerciseJunction.clear()
//                _logEntriesWithExerciseJunction.addAll(it)
                _logEntriesWithExerciseJunction.emit(it)
            }
        }

        workoutFlowJob?.cancel()
        workoutFlowJob = viewModelScope.launch {
            workoutsRepository.getWorkout(newWorkoutId)
                .collectLatest {
                    _workout.value = it
                    setupDurationUpdater(it?.inProgress == true, it?.startAt)
                }
        }
    }

    private fun setupDurationUpdater(inProgress: Boolean, startAt: LocalDateTime?) {
        durationJob?.cancel()
        durationJob = viewModelScope.launch {
            while (inProgress && startAt != null) {
                updateDurationStr(startAt = startAt)
                delay(25)
            }
        }
    }

    private suspend fun updateDurationStr(startAt: LocalDateTime) {
        val totalTime =
            LocalDateTime.now().toEpochMillis() - startAt.toEpochMillis()

        val totalSeconds = totalTime / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val readableStr = when {
            hours > 0 -> {
                "$hours hour $minutes min $seconds sec"
            }
            minutes > 0 -> {
                "$minutes min $seconds sec"
            }
            else -> {
                "$seconds sec"
            }
        }

        _currentDurationStr.value = readableStr
    }


    fun getExerciseWorkoutJunctions() =
        workoutsRepository.getExerciseWorkoutJunctions(mWorkout?.id ?: NONE_WORKOUT_ID)


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
            workoutsRepository.reorderEntriesGroupByDelete(
                entriesGroup = _logEntriesWithExerciseJunction.value.filter { a -> a.logEntries.any { b -> b.entryId == entry.entryId } }[0].logEntries.sortedBy { it.setNumber }
                    .toArrayList(),
                entryToDelete = entry
            )
//            val entriesGroup =
//                _logEntriesWithExerciseJunction.value.filter { a -> a.logEntries.any { b -> b.entryId == entry.entryId } }[0].logEntries.sortedBy { it.setNumber }
//                    .toArrayList()
//
//            workoutsRepository.deleteExerciseLogEntry(entry)
//
//            entriesGroup.removeAt(entriesGroup.indexOf(entry))
//
//            for (groupEntry in entriesGroup) {
//                val index = entriesGroup.indexOf(groupEntry)
//                workoutsRepository.updateExerciseLogEntry(groupEntry.copy(setNumber = index + 1))
//            }

        }
    }

    fun deleteExerciseFromWorkout(logEntriesWithJunctionItem: LogEntriesWithExerciseJunction) {
        viewModelScope.launch {
            workoutsRepository.deleteExerciseFromWorkout(logEntriesWithJunctionItem)
        }
    }

    fun finishWorkout() {
        Timber.d("Finish workout")
        viewModelScope.launch {
            val workoutId = mWorkout?.id ?: return@launch
            workoutsRepository.finishWorkout(workoutId)
            durationJob?.cancel()
            workoutsRepository.setCurrentWorkoutId(NONE_WORKOUT_ID)
        }
    }
}