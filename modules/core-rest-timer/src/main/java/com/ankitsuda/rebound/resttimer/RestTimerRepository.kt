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

package com.ankitsuda.rebound.resttimer

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RestTimerRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private fun sendCommandToService(action: String, time: Long? = null) {
        Intent(context, RestTimerService::class.java).also {
            it.action = action
            if (time != null) {
                it.putExtra(Constants.EXTRA_TOTAL_TIME, time)
            }
            context.startService(it)
        }
    }

    fun startTimer(time: Long) {
        sendCommandToService(Constants.ACTION_START, time)
    }

    fun pauseTimer() {
        sendCommandToService(Constants.ACTION_PAUSE)
    }

    fun resumeTimer() {
        sendCommandToService(Constants.ACTION_RESUME)
    }

    fun cancelTimer() {
        sendCommandToService(Constants.ACTION_CANCEL)
    }

    // return immutable flow from timer service
    fun getTimerServiceTimerState() = RestTimerService.currentTimerState as LiveData<TimerState>
    fun getTimerServiceElapsedTimeMillisESeconds() =
        RestTimerService.elapsedTimeInMillisEverySecond as LiveData<Long?>

    fun getTimerServiceElapsedTimeMillis() = RestTimerService.elapsedTimeInMillis as LiveData<Long?>
    fun getTimerServiceTotalTimeMillis() = RestTimerService.totalTimeInMillis as LiveData<Long?>
}