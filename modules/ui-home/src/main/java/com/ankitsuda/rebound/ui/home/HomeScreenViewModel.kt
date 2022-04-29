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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ankitsuda.base.utils.getCurrentWeekOfMonth
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.ui.home.models.OverallInfo
import com.ankitsuda.rebound.ui.home.models.WorkoutsInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {

    private var _workoutsInfo = MutableStateFlow(WorkoutsInfo())
    val workoutsInfo = _workoutsInfo.asStateFlow()

    private var _overallInfo = MutableStateFlow<OverallInfo?>(null)
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
    }
}