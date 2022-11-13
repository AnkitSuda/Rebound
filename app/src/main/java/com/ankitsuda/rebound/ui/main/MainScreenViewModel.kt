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

package com.ankitsuda.rebound.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.common.compose.AppSettings
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.resttimer.RestTimerRepository
import com.ankitsuda.rebound.resttimer.TimerState
import com.ankitsuda.rebound.resttimer.getFormattedStopWatchTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    restTimerRepository: RestTimerRepository,
    private val prefStorage: PrefStorage
) : ViewModel() {
    val currentWorkoutId = prefStorage.currentWorkoutId

    val restTimerState = restTimerRepository.getTimerServiceTimerState()

    val restTimerElapsedTime = restTimerRepository.getTimerServiceElapsedTimeMillis().map {
        //Timber.i("elapsedTime: $it")
        if (restTimerState.value != TimerState.EXPIRED)
            it
        else
            null
    }

    val restTimerTotalTime =
        restTimerRepository.getTimerServiceTotalTimeMillis()

    val restTimerTimeString = restTimerRepository.getTimerServiceElapsedTimeMillisESeconds().map {
        if (restTimerState.value != TimerState.EXPIRED)
            getFormattedStopWatchTime(ms = it, spaces = false)
        else
            null
    }

    private var _appSettings = MutableStateFlow<AppSettings>(AppSettings.defValues())
    val appSettings = _appSettings.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                prefStorage.firstDayOfWeek.shareWhileObserved(viewModelScope),
                prefStorage.weightUnit.shareWhileObserved(viewModelScope),
                prefStorage.distanceUnit.shareWhileObserved(viewModelScope),
            ) { firstDayOfWeek, weightUnit, distanceUnit ->
                _appSettings.value = AppSettings(
                    firstDayOfWeek = DayOfWeek.of(firstDayOfWeek),
                    weightUnit = weightUnit,
                    distanceUnit = distanceUnit,
                )
            }.collect()
        }
    }

}