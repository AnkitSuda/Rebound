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

import android.os.Looper
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.ankitsuda.rebound.R
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.stopwatch.*
import com.ankitsuda.rebound.resttimer.Constants
import com.ankitsuda.rebound.resttimer.RestTimerRepository
import com.ankitsuda.rebound.resttimer.TimerState
import com.ankitsuda.rebound.resttimer.getFormattedStopWatchTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import java.util.logging.Handler
import javax.inject.Inject
import kotlin.math.max


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    prefStorage: PrefStorage,
    private val restTimerRepository: RestTimerRepository
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

}