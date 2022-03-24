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
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationManagerCompat

import kotlin.math.max

/**
 * All [Stopwatch] data is accessed via this model.
 */
internal class StopwatchModel(
    private val mContext: Context,
    private val mPrefs: SharedPreferences,
    /** The model from which notification data are fetched.  */
    private val mNotificationModel: NotificationModel
) {

    /** Used to create and destroy system notifications related to the stopwatch.  */
    private val mNotificationManager = NotificationManagerCompat.from(mContext)

    /** Update stopwatch notification when locale changes.  */
    private val mLocaleChangedReceiver: BroadcastReceiver = LocaleChangedReceiver()

    /** The listeners to notify when the stopwatch or its laps change.  */
    private val mStopwatchListeners: MutableList<StopwatchListener> = mutableListOf()

    /** Delegate that builds platform-specific stopwatch notifications.  */
    private val mNotificationBuilder = StopwatchNotificationBuilder()

    /** The current state of the stopwatch.  */
    private var mStopwatch: Stopwatch? = null

    /** A mutable copy of the recorded stopwatch laps.  */
    private var mLaps: MutableList<Lap>? = null

    init {
        // Update stopwatch notification when locale changes.
        val localeBroadcastFilter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        mContext.registerReceiver(mLocaleChangedReceiver, localeBroadcastFilter)
    }

    /**
     * @param stopwatchListener to be notified when stopwatch changes or laps are added
     */
    fun addStopwatchListener(stopwatchListener: StopwatchListener) {
        mStopwatchListeners.add(stopwatchListener)
    }

    /**
     * @param stopwatchListener to no longer be notified when stopwatch changes or laps are added
     */
    fun removeStopwatchListener(stopwatchListener: StopwatchListener) {
        mStopwatchListeners.remove(stopwatchListener)
    }

    /**
     * @return the current state of the stopwatch
     */
    val stopwatch: Stopwatch
        get() {
            if (mStopwatch == null) {
                mStopwatch = StopwatchDAO.getStopwatch(mPrefs)
            }

            return mStopwatch!!
        }

    /**
     * @param stopwatch the new state of the stopwatch
     */
    fun setStopwatch(stopwatch: Stopwatch): Stopwatch {
        val before = this.stopwatch
        if (before != stopwatch) {
            StopwatchDAO.setStopwatch(mPrefs, stopwatch)
            mStopwatch = stopwatch

            // Refresh the stopwatch notification to reflect the latest stopwatch state.
            if (!mNotificationModel.isApplicationInForeground) {
                updateNotification()
            }

            // Resetting the stopwatch implicitly clears the recorded laps.
            if (stopwatch.isReset) {
                clearLaps()
            }

            // Notify listeners of the stopwatch change.
            for (stopwatchListener in mStopwatchListeners) {
                stopwatchListener.stopwatchUpdated(before, stopwatch)
            }
        }

        return stopwatch
    }

    /**
     * @return the laps recorded for this stopwatch
     */
    val laps: List<Lap>
        get() = mutableLaps

    /**
     * @return a newly recorded lap completed now; `null` if no more laps can be added
     */
    fun addLap(): Lap? {
        if (!mStopwatch!!.isRunning || !canAddMoreLaps()) {
            return null
        }

        val totalTime = stopwatch.totalTime
        val laps: MutableList<Lap> = mutableLaps

        val lapNumber = laps.size + 1
        StopwatchDAO.addLap(mPrefs, lapNumber, totalTime)

        val prevAccumulatedTime = if (laps.isEmpty()) 0 else laps[0].accumulatedTime
        val lapTime = totalTime - prevAccumulatedTime

        val lap = Lap(lapNumber, lapTime, totalTime)
        laps.add(0, lap)

        // Refresh the stopwatch notification to reflect the latest stopwatch state.
        if (!mNotificationModel.isApplicationInForeground) {
            updateNotification()
        }

        // Notify listeners of the new lap.
        for (stopwatchListener in mStopwatchListeners) {
            stopwatchListener.lapAdded(lap)
        }

        return lap
    }

    /**
     * Clears the laps recorded for this stopwatch.
     */
    @VisibleForTesting
    fun clearLaps() {
        StopwatchDAO.clearLaps(mPrefs)
        mutableLaps.clear()
    }

    /**
     * @return `true` iff more laps can be recorded
     */
    fun canAddMoreLaps(): Boolean = laps.size < 98

    /**
     * @return the longest lap time of all recorded laps and the current lap
     */
    val longestLapTime: Long
        get() {
            var maxLapTime: Long = 0

            val laps = laps
            if (laps.isNotEmpty()) {
                // Compute the maximum lap time across all recorded laps.
                for (lap in laps) {
                    maxLapTime = max(maxLapTime, lap.lapTime)
                }

                // Compare with the maximum lap time for the current lap.
                val stopwatch = stopwatch
                val currentLapTime = stopwatch.totalTime - laps[0].accumulatedTime
                maxLapTime = max(maxLapTime, currentLapTime)
            }

            return maxLapTime
        }

    /**
     * In practice, `time` can be any value due to device reboots. When the real-time clock is
     * reset, there is no more guarantee that this time falls after the last recorded lap.
     *
     * @param time a point in time expected, but not required, to be after the end of the prior lap
     * @return the elapsed time between the given `time` and the end of the prior lap;
     * negative elapsed times are normalized to `0`
     */
    fun getCurrentLapTime(time: Long): Long {
        val previousLap = laps[0]
        val currentLapTime = time - previousLap.accumulatedTime
        return max(0, currentLapTime)
    }

    /**
     * Updates the notification to reflect the latest state of the stopwatch and recorded laps.
     */
    fun updateNotification() {
        val stopwatch = stopwatch

        // Notification should be hidden if the stopwatch has no time or the app is open.
        if (stopwatch.isReset || mNotificationModel.isApplicationInForeground) {
            mNotificationManager.cancel(mNotificationModel.stopwatchNotificationId)
            return
        }

        // Otherwise build and post a notification reflecting the latest stopwatch state.
//        val notification: Notification =
//                mNotificationBuilder.build(mContext, mNotificationModel, stopwatch)
//        mNotificationBuilder.buildChannel(mContext, mNotificationManager)
//        mNotificationManager.notify(mNotificationModel.stopwatchNotificationId, notification)
    }

    private val mutableLaps: MutableList<Lap>
        get() {
            if (mLaps == null) {
                mLaps = StopwatchDAO.getLaps(mPrefs)
            }

            return mLaps!!
        }

    /**
     * Update the stopwatch notification in response to a locale change.
     */
    private inner class LocaleChangedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNotification()
        }
    }
}