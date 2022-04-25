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

package com.ankitsuda.rebound.ui.resttimer

import androidx.lifecycle.*
import com.ankitsuda.rebound.resttimer.Constants
import com.ankitsuda.rebound.resttimer.Constants.TIMER_STARTING_IN_TIME
import com.ankitsuda.rebound.resttimer.RestTimerRepository
import com.ankitsuda.rebound.resttimer.TimerState
import com.ankitsuda.rebound.resttimer.getFormattedStopWatchTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RestTimerScreenViewModel @Inject constructor(
    private val restTimerRepository: RestTimerRepository
) : ViewModel() {

    val timerState = restTimerRepository.getTimerServiceTimerState()

    val timeString = restTimerRepository.getTimerServiceElapsedTimeMillisESeconds().map {
        if (timerState.value != TimerState.EXPIRED)
            getFormattedStopWatchTime(it)
        else
            null
    }

    val elapsedTime = restTimerRepository.getTimerServiceElapsedTimeMillis().map {
        //Timber.i("elapsedTime: $it")
        if (timerState.value != TimerState.EXPIRED)
            it
        else
            null
    }

    val totalTime = restTimerRepository.getTimerServiceTotalTimeMillis()

    fun startTimer(time: Long) {
        restTimerRepository.startTimer(time)
    }

    fun pauseTimer() {
        restTimerRepository.pauseTimer()
    }

    fun resumeTimer() {
        restTimerRepository.resumeTimer()
    }

    fun cancelTimer() {
        restTimerRepository.cancelTimer()
    }
}