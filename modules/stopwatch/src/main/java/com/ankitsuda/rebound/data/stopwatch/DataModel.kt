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
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.AudioManager.FLAG_SHOW_UI
import android.media.AudioManager.STREAM_ALARM
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_SOUND_SETTINGS
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.StringRes

import java.util.Calendar

import kotlin.Comparator
import kotlin.math.roundToInt

/**
 * All application-wide data is accessible through this singleton.
 */
class DataModel private constructor() {

    private var mNotificationModel: NotificationModel? = null

    private var mHandler: Handler? = null
    private var mContext: Context? = null

    /** The model from which stopwatch data are fetched.  */
    private var mStopwatchModel: StopwatchModel? = null
    private var mTimeModel: TimeModel? = null

    /**
     * Initializes the data model with the context and shared preferences to be used.
     */
    fun init(context: Context, prefs: SharedPreferences) {
        if (mContext !== context) {
            mContext = context
            mTimeModel = TimeModel(mContext!!)
            mNotificationModel = NotificationModel()
            mStopwatchModel = StopwatchModel(mContext!!, prefs, mNotificationModel!!)
        }
    }
    //
    // Stopwatch
    //

    /**
     * @return milliseconds since boot, including time spent in sleep
     */
    fun elapsedRealtime(): Long {
        return mTimeModel!!.elapsedRealtime()
    }

    /**
     * @param stopwatchListener to be notified when stopwatch changes or laps are added
     */
    fun addStopwatchListener(stopwatchListener: StopwatchListener) {
        Utils.enforceMainLooper()
        mStopwatchModel?.addStopwatchListener(stopwatchListener)
    }

    /**
     * @param stopwatchListener to no longer be notified when stopwatch changes or laps are added
     */
    fun removeStopwatchListener(stopwatchListener: StopwatchListener) {
        Utils.enforceMainLooper()
        mStopwatchModel?.removeStopwatchListener(stopwatchListener)
    }

    /**
     * @return the current state of the stopwatch
     */
    val stopwatch: Stopwatch
        get() {
            Utils.enforceMainLooper()
            return mStopwatchModel!!.stopwatch
        }

    /**
     * @return the stopwatch after being started
     */
    fun startStopwatch(): Stopwatch {
        Utils.enforceMainLooper()
        return mStopwatchModel!!.setStopwatch(stopwatch.start())
    }

    /**
     * @return the stopwatch after being paused
     */
    fun pauseStopwatch(): Stopwatch {
        Utils.enforceMainLooper()
        return mStopwatchModel!!.setStopwatch(stopwatch.pause())
    }

    /**
     * @return the stopwatch after being reset
     */
    fun resetStopwatch(): Stopwatch {
        Utils.enforceMainLooper()
        return mStopwatchModel!!.setStopwatch(stopwatch.reset())
    }

    /**
     * @return the laps recorded for this stopwatch
     */
    val laps: List<Lap>
        get() {
            Utils.enforceMainLooper()
            return mStopwatchModel!!.laps
        }

    /**
     * @return a newly recorded lap completed now; `null` if no more laps can be added
     */
    fun addLap(): Lap? {
        Utils.enforceMainLooper()
        return mStopwatchModel!!.addLap()
    }

    /**
     * @return `true` iff more laps can be recorded
     */
    fun canAddMoreLaps(): Boolean {
        Utils.enforceMainLooper()
        return mStopwatchModel!!.canAddMoreLaps()
    }

    /**
     * @return the longest lap time of all recorded laps and the current lap
     */
    val longestLapTime: Long
        get() {
            Utils.enforceMainLooper()
            return mStopwatchModel!!.longestLapTime
        }

    /**
     * @param time a point in time after the end of the last lap
     * @return the elapsed time between the given `time` and the end of the previous lap
     */
    fun getCurrentLapTime(time: Long): Long {
        Utils.enforceMainLooper()
        return mStopwatchModel!!.getCurrentLapTime(time)
    }

    /**
     * @return the current time in milliseconds
     */
    fun currentTimeMillis(): Long {
        return mTimeModel!!.currentTimeMillis()
    }

    companion object {
        const val ACTION_WORLD_CITIES_CHANGED = "com.android.deskclock.WORLD_CITIES_CHANGED"

        /** The single instance of this data model that exists for the life of the application.  */
        val sDataModel = DataModel()

        @get:JvmStatic
        @get:Keep
        val dataModel
            get() = sDataModel
    }
}