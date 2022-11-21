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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.CountWithDate
import com.ankitsuda.rebound.domain.entities.WorkoutWithExtraInfo
import com.ankitsuda.rebound.ui.history.enums.WorkoutsDateRangeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val workoutsRepository: WorkoutsRepository,
) : ViewModel() {
    val argDay = handle.get<Int>(DAY_KEY).takeIf { it != -1 }
    val argMonth = handle.get<Int>(MONTH_KEY).takeIf { it != -1 }
    val argYear = handle.get<Int>(YEAR_KEY).takeIf { it != -1 }

    val dateRangeType = if (argDay != null && argMonth != null && argYear != null) {
        WorkoutsDateRangeType.DAY
    } else if (argDay == null && argMonth != null && argYear != null) {
        WorkoutsDateRangeType.MONTH
    } else if (argDay == null && argMonth == null && argYear != null) {
        WorkoutsDateRangeType.YEAR
    } else {
        WorkoutsDateRangeType.ALL
    }

    private val dateStart = when (dateRangeType) {
        WorkoutsDateRangeType.MONTH -> LocalDate.of(argYear!!, argMonth!!, 1)
        WorkoutsDateRangeType.YEAR -> LocalDate.of(argYear!!, 1, 1)
        WorkoutsDateRangeType.DAY -> LocalDate.of(argYear!!, argMonth!!, argDay!!)
        else -> null
    }

    private val dateEnd = when (dateRangeType) {
        WorkoutsDateRangeType.MONTH -> YearMonth.of(argYear!!, argMonth!!).atEndOfMonth()
        WorkoutsDateRangeType.YEAR -> Year.of(argYear!!).atMonth(Month.DECEMBER).atEndOfMonth()
        WorkoutsDateRangeType.DAY -> LocalDate.of(argYear!!, argMonth!!, argDay!!)
        else -> null
    }

    val workouts = workoutsRepository.getWorkoutsWithExtraInfoPaged(
        dateStart, dateEnd
    )
        .map {
            mapData(it)
        }
        .cachedIn(viewModelScope)
        .shareWhileObserved(viewModelScope)

    private fun mapData(pagingData: PagingData<WorkoutWithExtraInfo>) =
        pagingData.insertSeparators { before, after ->
            if (after != null) {
                val afterDate = after.workout?.completedAt?.toLocalDate()
                    ?.with(TemporalAdjusters.firstDayOfMonth())

                val beforeDate = before?.workout?.completedAt?.toLocalDate()
                    ?.with(TemporalAdjusters.firstDayOfMonth())

                if (after.workout?.completedAt != null && afterDate != null && beforeDate != afterDate) {

                    if (
                        dateRangeType != WorkoutsDateRangeType.YEAR &&
                        dateRangeType != WorkoutsDateRangeType.ALL &&
                        dateStart != null && dateEnd != null) {
                        workoutsRepository.getWorkoutsCountOnMonthOnDateRangeAlt(
                            dateStart,
                            dateEnd
                        ).firstOrNull() ?: 0L
                    } else {
                        CountWithDate(
                            date = afterDate.toEpochMillis(),
                            count = workoutsRepository.getWorkoutsCountOnMonth(
                                date = after.workout!!.completedAt!!.toEpochMillis()
                            ).firstOrNull() ?: 0
                        )
                    }
                } else {
                    null
                }
            } else {
                null
            }
        }
}