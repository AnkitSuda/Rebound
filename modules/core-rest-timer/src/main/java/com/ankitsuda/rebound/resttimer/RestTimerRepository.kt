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

import androidx.lifecycle.LiveData

class RestTimerRepository {

    // return immutable livedata from timer service
    fun getTimerServiceTimerState() = RestTimerService.currentTimerState as LiveData<TimerState>
    fun getTimerServiceElapsedTimeMillisESeconds() =
        RestTimerService.elapsedTimeInMillisEverySecond as LiveData<Long>

    fun getTimerServiceElapsedTimeMillis() = RestTimerService.elapsedTimeInMillis as LiveData<Long>
    fun getTimerServiceTotalTimeMillis() = RestTimerService.totalTimeInMillis as LiveData<Long>
}