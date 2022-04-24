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

import java.util.concurrent.TimeUnit

fun getFormattedStopWatchTime(ms: Long?, spaces: Boolean = true): String {
    ms?.let {
        var milliseconds = ms

        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        val seprater = if (spaces) " : " else ":"
        // Build formatted String
        return if (hours > 0) "${if (hours < 10) "0" else ""}$hours$seprater" else "" +
                "${if (minutes < 10) "0" else ""}$minutes$seprater" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }
    return ""
}

fun getFormattedCompletionTime(ms: Long?): String {
    ms?.let {
        var milliseconds = ms
        // Convert to hours
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        // Convert to minutes
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        // Convert to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return (if (hours <= 0) "" else if (hours < 10) "0$hours:" else "$hours:") +
                (if (minutes <= 0) "" else if (minutes < 10) "0$minutes:" else "$minutes:") +
                "${if (seconds < 10) "0" else ""}$seconds" +
                if (hours > 0) " h" else if (minutes > 0) " min" else "sec"
    }
    return ""
}
