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


import kotlin.math.max

/**
 * A read-only domain object representing a stopwatch.
 */
class Stopwatch internal constructor(
    /** Current state of this stopwatch.  */
    val state: State,
    /** Elapsed time in ms the stopwatch was last started; [.UNUSED] if not running.  */
    val lastStartTime: Long,
    /** The time since epoch at which the stopwatch was last started.  */
    val lastWallClockTime: Long,
    /** Elapsed time in ms this stopwatch has accumulated while running.  */
    val accumulatedTime: Long
) {

    enum class State {
        RESET, RUNNING, PAUSED
    }

    val isReset: Boolean
        get() = state == State.RESET

    val isPaused: Boolean
        get() = state == State.PAUSED

    val isRunning: Boolean
        get() = state == State.RUNNING

    /**
     * @return the total amount of time accumulated up to this moment
     */
    val totalTime: Long
        get() {
            if (state != State.RUNNING) {
                return accumulatedTime
            }

            // In practice, "now" can be any value due to device reboots. When the real-time clock
            // is reset, there is no more guarantee that "now" falls after the last start time. To
            // ensure the stopwatch is monotonically increasing, normalize negative time segments to
            // 0
            val timeSinceStart = Utils.now() - lastStartTime
            return accumulatedTime + max(0, timeSinceStart)
        }

    /**
     * @return a copy of this stopwatch that is running
     */
    fun start(): Stopwatch {
        return if (state == State.RUNNING) {
            this
        } else {
            Stopwatch(State.RUNNING, Utils.now(), Utils.wallClock(), totalTime)
        }
    }


    /**
     * @return a copy of this stopwatch that is paused
     */
    fun pause(): Stopwatch {
        return if (state != State.RUNNING) {
            this
        } else {
            Stopwatch(State.PAUSED, UNUSED, UNUSED, totalTime)
        }
    }

    /**
     * @return a copy of this stopwatch that is reset
     */
    fun reset(): Stopwatch = RESET_STOPWATCH

    /**
     * @return this Stopwatch if it is not running or an updated version based on wallclock time.
     * The internals of the stopwatch are updated using the wallclock time which is durable
     * across reboots.
     */
    fun updateAfterReboot(): Stopwatch {
        if (state != State.RUNNING) {
            return this
        }
        val timeSinceBoot = Utils.now()
        val wallClockTime = Utils.wallClock()
        // Avoid negative time deltas. They can happen in practice, but they can't be used. Simply
        // update the recorded times and proceed with no change in accumulated time.
        val delta = max(0, wallClockTime - lastWallClockTime)
        return Stopwatch(state, timeSinceBoot, wallClockTime, accumulatedTime + delta)
    }

    /**
     * @return this Stopwatch if it is not running or an updated version based on the realtime.
     * The internals of the stopwatch are updated using the realtime clock which is accurate
     * across wallclock time adjustments.
     */
    fun updateAfterTimeSet(): Stopwatch {
        if (state != State.RUNNING) {
            return this
        }
        val timeSinceBoot = Utils.now()
        val wallClockTime = Utils.wallClock()
        val delta = timeSinceBoot - lastStartTime
        return if (delta < 0) {
            // Avoid negative time deltas. They typically happen following reboots when TIME_SET is
            // broadcast before BOOT_COMPLETED. Simply ignore the time update and hope
            // updateAfterReboot() can successfully correct the data at a later time.
            this
        } else {
            Stopwatch(state, timeSinceBoot, wallClockTime, accumulatedTime + delta)
        }
    }

    companion object {
        const val UNUSED = Long.MIN_VALUE

        /** The single, immutable instance of a reset stopwatch.  */
        private val RESET_STOPWATCH = Stopwatch(State.RESET, UNUSED, UNUSED, 0)
    }
}