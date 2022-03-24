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

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ankitsuda.rebound.stopwatchData.R

/**
 * This service exists solely to allow the stopwatch notification to alter the state of the
 * stopwatch without disturbing the notification shade. If an activity were used instead (even one
 * that is not displayed) the notification manager implicitly closes the notification shade which
 * clashes with the use case of starting/pausing/lapping/resetting the stopwatch without
 * disturbing the notification shade.
 */
class StopwatchService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action: String? = intent.getAction()
        val label: Int = intent.getIntExtra(Events.EXTRA_EVENT_LABEL, R.string.label_intent)
        when (action) {
            ACTION_SHOW_STOPWATCH -> {
                Events.sendStopwatchEvent(R.string.action_show, label)

//                val showStopwatch: Intent = Intent(this, DeskClock::class.java)
//                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(showStopwatch)
            }
            ACTION_START_STOPWATCH -> {
                Events.sendStopwatchEvent(R.string.action_start, label)
                DataModel.dataModel.startStopwatch()
            }
            ACTION_PAUSE_STOPWATCH -> {
                Events.sendStopwatchEvent(R.string.action_pause, label)
                DataModel.dataModel.pauseStopwatch()
            }
            ACTION_RESET_STOPWATCH -> {
                Events.sendStopwatchEvent(R.string.action_reset, label)
                DataModel.dataModel.resetStopwatch()
            }
            ACTION_LAP_STOPWATCH -> {
                Events.sendStopwatchEvent(R.string.action_lap, label)
                DataModel.dataModel.addLap()
            }
        }

        return START_NOT_STICKY
    }

    companion object {
        private const val ACTION_PREFIX = "com.android.deskclock.action."

        // shows the tab with the stopwatch
        const val ACTION_SHOW_STOPWATCH = ACTION_PREFIX + "SHOW_STOPWATCH"

        // starts the current stopwatch
        const val ACTION_START_STOPWATCH = ACTION_PREFIX + "START_STOPWATCH"

        // pauses the current stopwatch that's currently running
        const val ACTION_PAUSE_STOPWATCH = ACTION_PREFIX + "PAUSE_STOPWATCH"

        // laps the stopwatch that's currently running
        const val ACTION_LAP_STOPWATCH = ACTION_PREFIX + "LAP_STOPWATCH"

        // resets the stopwatch if it's stopped
        const val ACTION_RESET_STOPWATCH = ACTION_PREFIX + "RESET_STOPWATCH"
    }
}