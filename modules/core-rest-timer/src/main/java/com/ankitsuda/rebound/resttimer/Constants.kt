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

object Constants {
    // Intent Actions for communication with timer service
    const val ACTION_START              = "ACTION_START"
    const val ACTION_RESUME             = "ACTION_RESUME"
    const val ACTION_PAUSE              = "ACTION_PAUSE"
    const val ACTION_CANCEL             = "ACTION_CANCEL"
    const val ACTION_CANCEL_AND_RESET   = "ACTION_CANCEL_AND_RESET"
    const val ACTION_INITIALIZE_DATA    = "ACTION_INITIALIZE_DATA"
    const val ACTION_MUTE               = "ACTION_MUTE"
    const val ACTION_VIBRATE            = "ACTION_VIBRATE"
    const val ACTION_SOUND              = "ACTION_SOUND"
    const val ACTION_SHOW_MAIN_ACTIVITY = "ACTION_SHOW_MAIN_ACTIVITY"

    const val EXTRA_REPETITION          = "EXTRA_REPETITION"
    const val EXTRA_EXERCISETIME        = "EXTRA_EXERCISETIME"
    const val EXTRA_PAUSETIME           = "EXTRA_PAUSETIME"
    const val EXTRA_WORKOUT_ID          = "EXTRA_WORKOUT_ID"

    const val EXTRA_TOTAL_TIME          = "EXTRA_TOTAL_TIME"

    const val NOTIFICATION_CHANNEL_ID   = "timer_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Timer"
    const val NOTIFICATION_ID           = 1

    const val ONE_SECOND                = 1000L
    const val TIMER_UPDATE_INTERVAL     = 5L    //5ms
    const val TIMER_STARTING_IN_TIME    = 5000L //5s
}