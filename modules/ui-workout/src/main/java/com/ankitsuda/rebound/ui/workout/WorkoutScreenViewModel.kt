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
import com.ankitsuda.base.utils.TimePeriod
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.base.utils.toReadableDuration
import com.ankitsuda.rebound.data.repositories.WorkoutTemplatesRepository
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val workoutTemplatesRepository: WorkoutTemplatesRepository
) :
    ViewModel() {
    private var _currentWorkout = MutableStateFlow<Workout?>(null)
    val currentWorkout = _currentWorkout
        .shareWhileObserved(viewModelScope)

    private var _currentWorkoutDurationStr = MutableStateFlow<String?>(null)
    val currentWorkoutDurationStr = _currentWorkoutDurationStr
        .shareWhileObserved(viewModelScope)

    private var _unarchivedTemplates = MutableStateFlow<List<TemplateWithWorkout>>(emptyList())
    val unarchivedTemplates = _unarchivedTemplates.asStateFlow()

    private var _archivedTemplates = MutableStateFlow<List<TemplateWithWorkout>>(emptyList())
    val archivedTemplates = _archivedTemplates.asStateFlow()

    private var durationJob: Job? = null

    init {
        viewModelScope.launch {
            workoutsRepository.getCurrentWorkoutId().collectLatest {
                refreshCurrentWorkout(it)
            }
        }
        viewModelScope.launch {
            workoutTemplatesRepository.getNonHiddenTemplatesWithWorkouts().collectLatest {
                _unarchivedTemplates.value = it.filter { t -> t.template.isArchived == false }
                _archivedTemplates.value = it.filter { t -> t.template.isArchived == true }
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
                    name = "${TimePeriod.now()} Workout",
                    startAt = LocalDateTime.now(),
                    completedAt = null,
                    inProgress = true,
                    isHidden = true
                )
            )
            workoutsRepository.setCurrentWorkoutId(newWorkoutId)
        }
    }

    fun createTemplate(onCreated: (WorkoutTemplate) -> Unit) {
        viewModelScope.launch {
            val template = workoutTemplatesRepository.createTemplate()
            onCreated(template)
        }
    }

}