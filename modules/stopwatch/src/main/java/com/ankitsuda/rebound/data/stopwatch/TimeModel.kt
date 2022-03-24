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

package com.ankitsuda.rebound.data.stopwatch

import android.content.Context
import android.os.SystemClock
import android.text.format.DateFormat

import java.util.Calendar

/**
 * All time data is accessed via this model. This model exists so that time can be mocked for
 * testing purposes.
 */
internal class TimeModel(private val mContext: Context) {
    /**
     * @return the current time in milliseconds
     */
    fun currentTimeMillis(): Long = System.currentTimeMillis()

    /**
     * @return milliseconds since boot, including time spent in sleep
     */
    fun elapsedRealtime(): Long = SystemClock.elapsedRealtime()

    /**
     * @return `true` if 24 hour time format is selected; `false` otherwise
     */
    fun is24HourFormat(): Boolean = DateFormat.is24HourFormat(mContext)

    /**
     * @return a new Calendar with the [.currentTimeMillis]
     */
    val calendar: Calendar
        get() {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentTimeMillis()
            return calendar
        }
}