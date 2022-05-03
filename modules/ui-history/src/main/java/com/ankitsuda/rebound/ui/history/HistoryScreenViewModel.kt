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

package com.ankitsuda.rebound.ui.history

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.getStateFlow
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.base.utils.getCurrentWeekOfMonth
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.base.utils.toLocalDate
import com.ankitsuda.navigation.DATE_KEY
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.domain.entities.WorkoutWithExtraInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SUNDAY
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters.nextOrSame
import java.time.temporal.TemporalAdjusters.previousOrSame
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val handle: SavedStateHandle,
) : ViewModel() {
    private var _selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private var _workouts: MutableStateFlow<List<WorkoutWithExtraInfo>> = MutableStateFlow(
        emptyList()
    )
    val workouts = _workouts.asStateFlow()

    private var _week: SnapshotStateList<LocalDate> = mutableStateListOf()
    val week = _week

    private var job: Job? = null

    fun getCurrentWeek() {
        val newList = getCurrentWeekOfMonth()
        week.clear()
        week.addAll(newList)
    }

    fun setSelectedDate(newDate: Long?) {
        viewModelScope.launch {
            (newDate?.toLocalDate() ?: LocalDate.now()).let {
                _workouts.value = emptyList()
                _selectedDate.value = it
                getWorkoutsOnDate(it)
            }
        }
    }

    private fun getWorkoutsOnDate(date: LocalDate) {
        job?.cancel()
        job = viewModelScope.launch {
            workoutsRepository.getWorkoutsWithExtraInfo(date).collectLatest {
                _workouts.value = it
            }
        }
    }
}