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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.ui.calendar.models.CalendarMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val prefs: PrefStorage
) : ViewModel() {
    // 💀 Hack
    private var _calendar = flow<PagingData<Any>> {}

    var calendar = _calendar
        .shareWhileObserved(viewModelScope)

    val workoutsCountOnDates =
        workoutsRepository.getWorkoutsCount().shareWhileObserved(viewModelScope)

    private var firstDayOfWeek = DayOfWeek.MONDAY

    init {
        viewModelScope.launch {
            prefs.firstDayOfWeek.collect {
                firstDayOfWeek = DayOfWeek.of(it)
                calendar = Pager(
                    config = PagingConfig(pageSize = 12, prefetchDistance = 2),
                    initialKey = Year.now().value,
                    pagingSourceFactory = {
                        CalendarPagingDataSource(
                            startYear = Year.now().value,
                            firstDayOfWeek = firstDayOfWeek
                        )
                    }
                ).flow
                    .map {
                        mapCalendar(it)
                    }
                    .cachedIn(viewModelScope)
                    .shareWhileObserved(viewModelScope)
            }
        }
    }

    private fun mapCalendar(pagingData: PagingData<CalendarMonth>): PagingData<Any> {
        return pagingData.insertSeparators { before, after ->
            if (after != null && before?.year != after.year) {
                after.year
            } else {
                null
            }
        }
    }
}