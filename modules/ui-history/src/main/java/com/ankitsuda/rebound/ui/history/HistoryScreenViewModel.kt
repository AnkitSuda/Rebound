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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.CountWithDate
import com.ankitsuda.rebound.domain.entities.WorkoutWithExtraInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
) : ViewModel() {
    val workouts = workoutsRepository.getWorkoutsWithExtraInfoPaged()
        .map {
            mapData(it)
        }
        .cachedIn(viewModelScope)
        .shareWhileObserved(viewModelScope)

    val workouts2 = workoutsRepository.getWorkoutsWithExtraInfo()
        .map {
            val newList = arrayListOf<Any>()
            it.forEachIndexed { index, after ->
                val before = it.getOrNull(index - 1)
                val afterDate = after.workout?.completedAt?.toLocalDate()
                    ?.with(TemporalAdjusters.firstDayOfMonth())

                val beforeDate = before?.workout?.completedAt?.toLocalDate()
                    ?.with(TemporalAdjusters.firstDayOfMonth())

                if (after.workout?.completedAt != null && afterDate != null && beforeDate != afterDate) {
                    val mWorkoutsCounts = workoutsRepository.getWorkoutsCountOnMonth(
                        date = after.workout!!.completedAt!!.toEpochMillis()
                    ).firstOrNull()

                    newList.add(
                        CountWithDate(
                            date = afterDate.toEpochMillis(),
                            count = mWorkoutsCounts ?: 0
                        )
                    )
                }
                newList.add(after)
            }

            newList.toList()
        }
        .shareWhileObserved(viewModelScope)

    private fun mapData(pagingData: PagingData<WorkoutWithExtraInfo>) =
        pagingData.insertSeparators { before, after ->
            if (after != null) {
                val afterDate = after.workout?.completedAt?.toLocalDate()
                    ?.with(TemporalAdjusters.firstDayOfMonth())

                val beforeDate = before?.workout?.completedAt?.toLocalDate()
                    ?.with(TemporalAdjusters.firstDayOfMonth())

                if (after.workout?.completedAt != null && afterDate != null && beforeDate != afterDate) {
                    val mWorkoutsCounts = workoutsRepository.getWorkoutsCountOnMonth(
                        date = after.workout!!.completedAt!!.toEpochMillis()
                    ).firstOrNull()

                    CountWithDate(
                        date = afterDate.toEpochMillis(),
                        count = mWorkoutsCounts ?: 0
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }

    init {
        viewModelScope.launch {
            workouts2.collect()
        }
    }
}