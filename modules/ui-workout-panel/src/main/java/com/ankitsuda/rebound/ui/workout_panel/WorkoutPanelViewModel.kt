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
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.base.utils.toReadableDurationStyle2
import com.ankitsuda.rebound.data.repositories.BarbellsRepository
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.LogSetType
import com.ankitsuda.rebound.domain.entities.*
import com.ankitsuda.rebound.ui.components.workouteditor.utils.WorkoutEditorUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutPanelViewModel @Inject constructor(
    barbellsRepository: BarbellsRepository,
    private val workoutsRepository: WorkoutsRepository,
) :
    ViewModel() {
    val currentWorkoutId: Flow<String> = workoutsRepository.getCurrentWorkoutId()
    var mWorkoutId: Long = -1
    var mWorkout: Workout? = null

    private var _currentDurationStr = MutableStateFlow<String>("")
    val currentDurationStr = _currentDurationStr.asStateFlow()

    private var _currentVolumeStr = MutableStateFlow<String>("")
    val currentVolumeStr = _currentVolumeStr.asStateFlow()

    private var _currentSetsStr = MutableStateFlow<String>("")
    val currentSetsStr = _currentSetsStr.asStateFlow()

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

    val barbells = barbellsRepository.getActiveBarbells()
        .distinctUntilChanged()
        .shareWhileObserved(viewModelScope)

    init {
        viewModelScope.launch {
            currentWorkoutId.collectLatest {
                didGotFirstNameNote = false
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
                _logEntriesWithExerciseJunction.emit(it)
                refreshHeaderValues(it.flatMap { j -> j.logEntries })
            }
        }

        workoutFlowJob?.cancel()
        workoutFlowJob = viewModelScope.launch {
            workoutsRepository.getWorkout(newWorkoutId)
                .collectLatest {
//                    if (!didGotFirstNameNote) {
                    _workoutName.value = it?.name
                    _workoutNote.value = it?.note
//                        didGotFirstNameNote = true
//                    }
                    _workout.value = it
                    setupDurationUpdater(it?.inProgress == true, it?.startAt)
                }
        }
    }

    private fun setupDurationUpdater(inProgress: Boolean, startAt: LocalDateTime?) {
        durationJob?.cancel()
        durationJob = viewModelScope.launch {
            while (inProgress && startAt != null) {
                _currentDurationStr.value = startAt.toReadableDurationStyle2(spaces = false)
                delay(25)
            }
        }
    }

    private suspend fun refreshHeaderValues(entries: List<ExerciseLogEntry>) {
        _currentVolumeStr.value = "${entries.calculateTotalVolume().toReadableString()}kg"
        _currentSetsStr.value = "${entries.size}"
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

    fun finishWorkout(onSetsIncomplete: () -> Unit) {
        Timber.d("Finish workout")
        viewModelScope.launch {
            val workoutId = mWorkout?.id ?: return@launch

            if (!checkIfAllSetsAreComplete()) {
                onSetsIncomplete()
                return@launch
            }
            workoutsRepository.finishWorkout(workoutId)
            durationJob?.cancel()
            workoutsRepository.setCurrentWorkoutId(NONE_WORKOUT_ID)
        }
    }

    private suspend fun checkIfAllSetsAreComplete(): Boolean {
        val junctions = _logEntriesWithExerciseJunction.value

        if (junctions.isNullOrEmpty()) return false

        for (junction in junctions) {
            val isIncomplete = junction.logEntries.any {
                WorkoutEditorUtils.isValidSet(it, junction.exercise.category)
            }

            if (isIncomplete) {
                return false
            }
        }
        return true
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
                    exerciseWorkoutJunctionId = junction.junction.id,
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

    fun updateExerciseBarbellType(junctionId: String, barbellId: String) {
        viewModelScope.launch {
            workoutsRepository.updateExerciseWorkoutJunctionBarbellId(
                junctionId, barbellId
            )
        }
    }

}