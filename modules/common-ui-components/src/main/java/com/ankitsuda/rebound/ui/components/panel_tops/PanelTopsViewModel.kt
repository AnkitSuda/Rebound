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

package com.ankitsuda.rebound.ui.components.panel_tops

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PanelTopsViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {

    val currentWorkoutId = workoutsRepository.getCurrentWorkoutId()
    fun getWorkout(workoutId: Long) = workoutsRepository.getWorkout(workoutId)
}