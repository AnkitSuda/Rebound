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
import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.R
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.stopwatch.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.*
import java.util.logging.Handler
import javax.inject.Inject
import kotlin.math.max


@HiltViewModel
class MainScreenViewModel @Inject constructor(val prefStorage: PrefStorage) : ViewModel() {
    val currentWorkoutId = prefStorage.currentWorkoutId
    private val mTimeUpdateRunnable: Runnable = TimeUpdateRunnable()

    private val mStopwatchWatcher: StopwatchListener = StopwatchWatcher()

    private val stopwatch: Stopwatch
        get() = DataModel.dataModel.stopwatch

    private var _currentTimeStr = MutableStateFlow<String>("")
    val currentTimeStr = _currentTimeStr.asStateFlow()


    init {
        DataModel.dataModel.addStopwatchListener(mStopwatchWatcher)
        mTimeUpdateRunnable.run()
    }

    fun pauseTime() {
        Events.sendStopwatchEvent(R.string.action_pause, R.string.label_deskclock)
        DataModel.dataModel.pauseStopwatch()
    }

    fun playTime() {
        Events.sendStopwatchEvent(R.string.action_start, R.string.label_deskclock)
        DataModel.dataModel.startStopwatch()
    }

    fun resetTime() {
        Events.sendStopwatchEvent(R.string.action_reset, R.string.label_deskclock)
        DataModel.dataModel.resetStopwatch()
    }

    /**
     * Update the user interface in response to a stopwatch change.
     */
    private inner class StopwatchWatcher : StopwatchListener {
        override fun stopwatchUpdated(before: Stopwatch, after: Stopwatch) {
            if (after.isReset) {
                return
            }
            updateUI()
        }

        override fun lapAdded(lap: Lap) {
        }
    }

    private fun updateTime() {
        // Compute the total time of the stopwatch.
        val stopwatch = stopwatch
        val totalTime = stopwatch.totalTime

        val totalSeconds = totalTime / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val readableStr = when {
            hours > 0 -> {
                "$hours hour $minutes min $seconds sec"
            }
            minutes > 0 -> {
                "$minutes min $seconds sec"
            }
            else -> {
                "$seconds sec"
            }
        }
        _currentTimeStr.value = readableStr
    }

    private fun updateUI() {
        updateTime()
        val stopwatch = stopwatch
        if (!stopwatch.isReset) {
            mTimeUpdateRunnable.run()
        }
    }


    /**
     * This runnable periodically updates times throughout the UI. It stops these updates when the
     * stopwatch is no longer running.
     */
    private inner class TimeUpdateRunnable : Runnable {
        override fun run() {
            val startTime = Utils.now()

            updateTime()

            if (!stopwatch.isReset) {
                val period = (if (stopwatch.isPaused) {
                    REDRAW_PERIOD_PAUSED
                } else {
                    REDRAW_PERIOD_RUNNING
                }).toLong()
                val endTime = Utils.now()
                val delay: Long = max(0, startTime + period - endTime).toLong()
                android.os.Handler(Looper.getMainLooper()).postDelayed(this, delay)
            }
        }
    }

    companion object {
        /** Milliseconds between redraws while running.  */
        private const val REDRAW_PERIOD_RUNNING = 25

        /** Milliseconds between redraws while paused.  */
        private const val REDRAW_PERIOD_PAUSED = 500
    }
}