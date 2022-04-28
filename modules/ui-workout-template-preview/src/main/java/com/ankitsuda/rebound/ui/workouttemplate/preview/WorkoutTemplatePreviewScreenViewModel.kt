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

package com.ankitsuda.rebound.ui.workouttemplate.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.navigation.WORKOUT_TEMPLATE_ID_KEY
import com.ankitsuda.rebound.data.repositories.WorkoutTemplatesRepository
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WorkoutTemplatePreviewScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val templatesRepository: WorkoutTemplatesRepository,
    handle: SavedStateHandle,
) : ViewModel() {
    private val templateId = requireNotNull(handle.get<String>(WORKOUT_TEMPLATE_ID_KEY))
    private var _workoutId: String? = null

    private var _workoutTemplate: MutableStateFlow<WorkoutTemplate?> = MutableStateFlow(null)
    val workoutTemplate = _workoutTemplate.asStateFlow()

    private var _workout: MutableStateFlow<Workout?> = MutableStateFlow(null)
    val workout = _workout.asStateFlow()

    private var _entriesJunctions: MutableStateFlow<List<LogEntriesWithExerciseJunction>> =
        MutableStateFlow(
            emptyList()
        )
    val entriesJunctions = _entriesJunctions.asStateFlow()

    private var junctionsJob: Job? = null
    private var workoutJob: Job? = null

    init {
        viewModelScope.launch {
            templatesRepository.getTemplate(templateId = templateId).collectLatest {
                _workoutId = it?.workoutId
                it?.workoutId?.let { workoutId ->
                    refreshWorkout(workoutId)
                    refreshJunctions(workoutId)
                } ?: run {
                    workoutJob?.cancel()
                    junctionsJob?.cancel()
                }
                _workoutTemplate.value = it
            }
        }
    }

    private fun refreshWorkout(workoutId: String) {
        workoutJob?.cancel()
        workoutJob = viewModelScope.launch {
            workoutsRepository.getWorkout(workoutId).collectLatest { updatedWorkout ->
                Timber.d("workout $updatedWorkout")
                _workout.value = updatedWorkout
            }
        }

    }

    private fun refreshJunctions(workoutId: String) {
        junctionsJob?.cancel()
        junctionsJob = viewModelScope.launch {
            workoutsRepository.getLogEntriesWithExerciseJunction(workoutId)
                .collectLatest { junctions ->
                    _entriesJunctions.value = junctions
                }
        }
    }

    fun deleteTemplate(onDeleted: () -> Unit) {
        viewModelScope.launch {
            templatesRepository.deleteTemplate(templateId)
            onDeleted()
        }
    }

    fun toggleIsArchived() {
        viewModelScope.launch {
            templatesRepository.toggleArchiveTemplate(templateId)
        }
    }


    fun startWorkout(
        discardActive: Boolean,
        onWorkoutAlreadyActive: () -> Unit
    ) {
        viewModelScope.launch {
            _workoutId?.let {
                workoutsRepository.startWorkoutFromWorkout(
                    workoutId = it,
                    discardActive = discardActive,
                    onWorkoutAlreadyActive = onWorkoutAlreadyActive
                )
            }
        }
    }


}