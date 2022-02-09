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
import com.ankitsuda.rebound.ui.calendar.models.CalendarMonth
import com.ankitsuda.rebound.ui.calendar.models.InDateStyle
import com.ankitsuda.rebound.ui.calendar.models.MonthConfig
import com.ankitsuda.rebound.ui.calendar.models.OutDateStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Month
import java.time.Year
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor() : ViewModel() {
    private var _calendar: SnapshotStateList<CalendarMonth> = mutableStateListOf()
    val calendar = _calendar

    private var calendarJob: Job? = null

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
                firstDayOfWeek = DayOfWeek.MONDAY,
                job = Job()
            )

            _calendar.addAll(monthConfig.months)
        }
    }

}