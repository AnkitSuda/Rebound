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

package com.ankitsuda.rebound.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.getCurrentWeekOfMonth
import com.ankitsuda.base.utils.toReadableDurationStyle2
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.calculateTotalVolume
import com.ankitsuda.rebound.ui.home.models.OverallInfo
import com.ankitsuda.rebound.ui.home.models.WorkoutsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {

    private var _workoutsInfo = MutableStateFlow(WorkoutsInfo())
    val workoutsInfo = _workoutsInfo.asStateFlow()

    private var _overallInfo = MutableStateFlow(OverallInfo())
    val overallInfo = _overallInfo.asStateFlow()

    init {
        setupObservers()
    }

    private fun setupObservers() {
        val today = LocalDate.now()
        val week = getCurrentWeekOfMonth()
        val monthStart = LocalDate.of(today.year, today.month, 1)
        val monthEnd = monthStart.plusDays(monthStart.lengthOfMonth() - 1L)
        val lastMonthStart = LocalDate.of(today.year, today.month - 1, 1)
        val lastMonthEnd = lastMonthStart.plusDays(lastMonthStart.lengthOfMonth() - 1L)

        viewModelScope.launch {
            workoutsRepository.getWorkoutsCountOnDateRange(
                dateStart = week.first(),
                dateEnd = week.last()
            ).collectLatest {
                _workoutsInfo.value =
                    _workoutsInfo.value.copy(workoutsThisWeek = it.sumOf { i -> i.count })
            }
        }
        viewModelScope.launch {
            workoutsRepository.getWorkoutsCountOnDateRange(
                dateStart = monthStart,
                dateEnd = monthEnd
            ).collectLatest {
                _workoutsInfo.value =
                    _workoutsInfo.value.copy(workoutsThisMonth = it.sumOf { i -> i.count })
            }
        }
        viewModelScope.launch {
            workoutsRepository.getWorkoutsCountOnDateRange(
                dateStart = lastMonthStart,
                dateEnd = lastMonthEnd
            ).collectLatest {
                _workoutsInfo.value =
                    _workoutsInfo.value.copy(workoutsLastMonth = it.sumOf { i -> i.count })
            }
        }
        viewModelScope.launch {
            workoutsRepository.getTotalWorkoutsCount().collectLatest {
                _overallInfo.value = _overallInfo.value.copy(totalWorkouts = it)
            }
        }
        viewModelScope.launch {
            workoutsRepository.getMaxWeightLifted().collectLatest {
                _overallInfo.value = _overallInfo.value.copy(maxWeight = it)
            }
        }
        viewModelScope.launch {
            workoutsRepository.getNonHiddenExerciseLogEntries().collectLatest {
                _overallInfo.value =
                    _overallInfo.value.copy(
                        totalVolumeLifted = try {
                            it?.calculateTotalVolume()
                        } catch (e: Exception) {
                            0.0
                        }
                    )
            }
        }
        viewModelScope.launch {
            workoutsRepository.getWorkoutsDurationsOnly().collectLatest {
                val total = it.sum()

                val average = try {
                    total / it.size
                } catch (e: Exception) {
                    null
                }

                val longest = it.maxOrNull()

                var averageWorkoutDurationStr = 0L.toReadableDurationStyle2(
                    endAt = average ?: 0L,
                    hoursAndMinutesOnly = false,
                    spaces = false
                )
                averageWorkoutDurationStr += if (averageWorkoutDurationStr.filter { s -> s == ':' }.length == 2) {
                    "h"
                } else {
                    "m"
                }

                _overallInfo.value =
                    _overallInfo.value.copy(
                        totalWorkoutsDuration = total,
                        averageWorkoutDuration = average,
                        longestWorkoutDuration = longest,
                        averageWorkoutDurationStr = averageWorkoutDurationStr,
                        totalWorkoutsDurationStr = 0L.toReadableDurationStyle2(
                            endAt = total,
                            hoursAndMinutesOnly = true,
                            spaces = false
                        ) + "h",
                        longestWorkoutDurationStr = 0L.toReadableDurationStyle2(
                            endAt = longest ?: 0L,
                            hoursAndMinutesOnly = true,
                            spaces = false
                        ) + "h",
                    )
            }
        }
    }
}