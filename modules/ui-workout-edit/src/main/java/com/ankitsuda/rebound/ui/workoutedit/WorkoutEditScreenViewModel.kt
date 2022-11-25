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

package com.ankitsuda.rebound.ui.workoutedit

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.base.utils.toReadableDuration
import com.ankitsuda.navigation.IS_TEMPLATE_KEY
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutEditScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private val workoutId = requireNotNull(handle.get<String>(WORKOUT_ID_KEY))
    val isTemplate = requireNotNull(handle.get<Boolean>(IS_TEMPLATE_KEY))

    private var mWorkout: Workout? = null

    private var _currentDurationStr = MutableStateFlow<String>("")
    val currentDurationStr = _currentDurationStr.asStateFlow()

    private var workoutFlowJob: Job? = null
    private var entriesFlowJob: Job? = null
    private var durationJob: Job? = null

    private var _workout: MutableStateFlow<Workout?> =
        MutableStateFlow(null)
    val workout = _workout.asStateFlow().map {
        if (mWorkout == null || mWorkout != it) {
            Timber.d("Updating viewModel mWorkout to currentWorkout")
            mWorkout = it
        }
        it
    }

    private var _workoutName: MutableStateFlow<String?> = MutableStateFlow(null)
    val workoutName = _workoutName.asStateFlow()

    private var _workoutNote: MutableStateFlow<String?> = MutableStateFlow(null)
    val workoutNote = _workoutNote.asStateFlow()

    private var didGotFirstNameNote = false

    private var _logEntriesWithExerciseJunction: MutableStateFlow<List<LogEntriesWithExerciseJunction>> =
        MutableStateFlow(emptyList())
    val logEntriesWithExerciseJunction = _logEntriesWithExerciseJunction.asStateFlow()

    init {
        viewModelScope.launch {
            didGotFirstNameNote = false
            if (workoutId != NONE_WORKOUT_ID) {
                refresh()
            }
        }
    }

    private fun refresh() {
        entriesFlowJob?.cancel()
        entriesFlowJob = viewModelScope.launch {
            workoutsRepository.getLogEntriesWithExerciseJunction(
                workoutId
            ).collectLatest {
                _logEntriesWithExerciseJunction.emit(it)
            }
        }

        workoutFlowJob?.cancel()
        workoutFlowJob = viewModelScope.launch {
            workoutsRepository.getWorkout(workoutId)
                .collectLatest {
//                    if (!didGotFirstNameNote) {
                    _workoutName.value = it?.name
                    _workoutNote.value = it?.note
//                        didGotFirstNameNote = true
//                    }
                    _workout.value = it
                }
        }
    }

    fun updateWorkoutName(name: String) {
        viewModelScope.launch {
//            _workoutName.value = name
            mWorkout?.let {
                workoutsRepository.updateWorkout(it.copy(name = name))
            }
        }
    }

    fun updateWorkoutNote(note: String) {
        viewModelScope.launch {
//            _workoutNote.value = note
            mWorkout?.let {
                workoutsRepository.updateWorkout(it.copy(note = note))
            }
        }
    }

    fun deleteWorkout() {
        viewModelScope.launch {
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
        }
    }

    fun deleteExerciseFromWorkout(logEntriesWithJunctionItem: LogEntriesWithExerciseJunction) {
        viewModelScope.launch {
            workoutsRepository.deleteExerciseFromWorkout(logEntriesWithJunctionItem)
        }
    }

    fun updateWarmUpSets(junction: LogEntriesWithExerciseJunction, sets: List<ExerciseLogEntry>) {
        viewModelScope.launch {
            workoutsRepository.updateWarmUpSets(junction, sets)
        }
    }


    fun addEmptyNote(junction: LogEntriesWithExerciseJunction) {
        viewModelScope.launch {
            val time = LocalDateTime.now()
            workoutsRepository.addExerciseSetGroupNote(
                ExerciseSetGroupNote(
                    id = generateId(),
                    exerciseWorkoutJunctionId = junction.junction.workoutId,
                    createdAt = time,
                    updatedAt = time
                )
            )
        }
    }

    fun deleteNote(note: ExerciseSetGroupNote) {
        viewModelScope.launch {
            workoutsRepository.deleteExerciseSetGroupNote(note.id)
        }
    }

    fun changeNote(note: ExerciseSetGroupNote) {
        viewModelScope.launch {
            workoutsRepository.updateExerciseSetGroupNote(note)
        }
    }

    fun removeFromSuperset(junction: LogEntriesWithExerciseJunction) {
        viewModelScope.launch {
            workoutsRepository.updateExerciseWorkoutJunctionSupersetId(
                junction.junction.id, null
            )
        }
    }

    fun addToSuperset(junctionId: String, supersetId: Int) {
        viewModelScope.launch {
            workoutsRepository.updateExerciseWorkoutJunctionSupersetId(
                junctionId, supersetId
            )
        }
    }

    fun updateStartAndCompletedAt(startAt: LocalDateTime, completedAt: LocalDateTime) {
        viewModelScope.launch {
            mWorkout?.let {
                workoutsRepository.updateWorkout(
                    it.copy(
                        startAt = startAt,
                        completedAt = completedAt
                    )
                )
            }
        }
    }

}