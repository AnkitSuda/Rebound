/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ankitsuda.rebound.data.stopwatch

import android.content.SharedPreferences

/**
 * This class encapsulates the transfer of data between [Stopwatch] and [Lap] domain
 * objects and their permanent storage in [SharedPreferences].
 */
internal object StopwatchDAO {
    /** Key to a preference that stores the state of the stopwatch.  */
    private const val STATE = "sw_state"

    /** Key to a preference that stores the last start time of the stopwatch.  */
    private const val LAST_START_TIME = "sw_start_time"

    /** Key to a preference that stores the epoch time when the stopwatch last started.  */
    private const val LAST_WALL_CLOCK_TIME = "sw_wall_clock_time"

    /** Key to a preference that stores the accumulated elapsed time of the stopwatch.  */
    private const val ACCUMULATED_TIME = "sw_accum_time"

    /** Prefix for a key to a preference that stores the number of recorded laps.  */
    private const val LAP_COUNT = "sw_lap_num"

    /** Prefix for a key to a preference that stores accumulated time at the end of a lap.  */
    private const val LAP_ACCUMULATED_TIME = "sw_lap_time_"

    /**
     * @return the stopwatch from permanent storage or a reset stopwatch if none exists
     */
    fun getStopwatch(prefs: SharedPreferences): Stopwatch {
        val stateIndex: Int = prefs.getInt(STATE, Stopwatch.State.RESET.ordinal)
        val state = Stopwatch.State.values()[stateIndex]
        val lastStartTime: Long = prefs.getLong(LAST_START_TIME, Stopwatch.UNUSED)
        val lastWallClockTime: Long = prefs.getLong(LAST_WALL_CLOCK_TIME, Stopwatch.UNUSED)
        val accumulatedTime: Long = prefs.getLong(ACCUMULATED_TIME, 0)
        var s = Stopwatch(state, lastStartTime, lastWallClockTime, accumulatedTime)

        // If the stopwatch reports an illegal (negative) amount of time, remove the bad data.
        if (s.totalTime < 0) {
            s = s.reset()
            setStopwatch(prefs, s)
        }
        return s
    }

    /**
     * @param stopwatch the last state of the stopwatch
     */
    fun setStopwatch(prefs: SharedPreferences, stopwatch: Stopwatch) {
        val editor: SharedPreferences.Editor = prefs.edit()

        if (stopwatch.isReset) {
            editor.remove(STATE)
                    .remove(LAST_START_TIME)
                    .remove(LAST_WALL_CLOCK_TIME)
                    .remove(ACCUMULATED_TIME)
        } else {
            editor.putInt(STATE, stopwatch.state.ordinal)
                    .putLong(LAST_START_TIME, stopwatch.lastStartTime)
                    .putLong(LAST_WALL_CLOCK_TIME, stopwatch.lastWallClockTime)
                    .putLong(ACCUMULATED_TIME, stopwatch.accumulatedTime)
        }

        editor.apply()
    }

    /**
     * @return a list of recorded laps for the stopwatch
     */
    fun getLaps(prefs: SharedPreferences): MutableList<Lap> {
        // Prepare the container to be filled with laps.
        val lapCount: Int = prefs.getInt(LAP_COUNT, 0)
        val laps: MutableList<Lap> = mutableListOf()

        var prevAccumulatedTime: Long = 0

        // Lap numbers are 1-based and so the are corresponding shared preference keys.
        for (lapNumber in 1..lapCount) {
            // Look up the accumulated time for the lap.
            val lapAccumulatedTimeKey = LAP_ACCUMULATED_TIME + lapNumber
            val accumulatedTime: Long = prefs.getLong(lapAccumulatedTimeKey, 0)

            // Lap time is the delta between accumulated time of this lap and prior lap.
            val lapTime = accumulatedTime - prevAccumulatedTime

            // Create the lap instance from the data.
            laps.add(Lap(lapNumber, lapTime, accumulatedTime))

            // Update the accumulated time of the previous lap.
            prevAccumulatedTime = accumulatedTime
        }

        // Laps are stored in the order they were recorded; display order is the reverse.
        laps.reverse()

        return laps
    }

    /**
     * @param newLapCount the number of laps including the new lap
     * @param accumulatedTime the amount of time accumulate by the stopwatch at the end of the lap
     */
    fun addLap(prefs: SharedPreferences, newLapCount: Int, accumulatedTime: Long) {
        prefs.edit()
                .putInt(LAP_COUNT, newLapCount)
                .putLong(LAP_ACCUMULATED_TIME + newLapCount, accumulatedTime)
                .apply()
    }

    /**
     * Remove the recorded laps for the stopwatch
     */
    fun clearLaps(prefs: SharedPreferences) {
        val editor: SharedPreferences.Editor = prefs.edit()

        val lapCount: Int = prefs.getInt(LAP_COUNT, 0)
        for (lapNumber in 1..lapCount) {
            editor.remove(LAP_ACCUMULATED_TIME + lapNumber)
        }
        editor.remove(LAP_COUNT)

        editor.apply()
    }
}