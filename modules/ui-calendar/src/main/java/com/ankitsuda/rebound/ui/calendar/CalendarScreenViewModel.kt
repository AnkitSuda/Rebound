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

package com.ankitsuda.rebound.ui.calendar

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.CountWithDate
import com.ankitsuda.rebound.ui.calendar.models.CalendarMonth
import com.ankitsuda.rebound.ui.calendar.models.InDateStyle
import com.ankitsuda.rebound.ui.calendar.models.MonthConfig
import com.ankitsuda.rebound.ui.calendar.models.OutDateStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.Month
import java.time.Year
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val prefs: PrefStorage
) : ViewModel() {
    private var _calendar = MutableStateFlow<List<CalendarMonth>?>(null)
    val calendar = _calendar.asStateFlow()

    private var _workoutsCountOnDates = MutableStateFlow<List<CountWithDate>?>(null)
    val workoutsCountOnDates = _workoutsCountOnDates.asStateFlow()

    private var calendarJob: Job? = null
    private var countJob: Job? = null

    private var firstDayOfWeek = DayOfWeek.MONDAY

    init {
        viewModelScope.launch {
            prefs.firstDayOfWeek.collect {
                firstDayOfWeek = DayOfWeek.of(it)
                getCalendar()
            }
        }
    }

    fun getCalendar(
    ) {
        calendarJob?.cancel()
        calendarJob = viewModelScope.launch {
            val monthConfig = MonthConfig(
                outDateStyle = OutDateStyle.NONE,
                inDateStyle = InDateStyle.ALL_MONTHS,
                startMonth = YearMonth.of(Year.now().value, Month.JANUARY),
                endMonth = YearMonth.of(Year.now().value, Month.DECEMBER),
                hasBoundaries = true,
                maxRowCount = Int.MAX_VALUE,
                firstDayOfWeek = firstDayOfWeek,
                job = Job()
            )

            refreshCounts()
            _calendar.value = monthConfig.months
        }
    }

    private fun refreshCounts() {
        countJob?.cancel()
        countJob = viewModelScope.launch {
            val counts = workoutsRepository.getWorkoutsCountOnDateRange(
                dateStart = YearMonth.of(
                    Year.now().value,
                    Month.JANUARY
                ).atDay(1),
                dateEnd = YearMonth.of(Year.now().value, Month.DECEMBER).atEndOfMonth()
            ).firstOrNull()

            _workoutsCountOnDates.value = counts
        }
    }

}