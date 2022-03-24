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

import androidx.annotation.StringRes
import com.ankitsuda.rebound.stopwatchData.R

/**
 * This thin layer over [Controller.sendEvent] eases the API usage.
 */
object Events {
    /** Extra describing the entity responsible for the action being performed.  */
    const val EXTRA_EVENT_LABEL = "com.android.deskclock.extra.EVENT_LABEL"

    /**
     * Tracks a stopwatch event.
     *
     * @param action resource id of event action
     * @param label resource id of event label
     */
    @JvmStatic
    fun sendStopwatchEvent(@StringRes action: Int, @StringRes label: Int) {
        sendEvent(R.string.category_stopwatch, action, label)
    }


    /**
     * Tracks an event. Events have a category, action, label and value. This
     * method can be used to track events such as button presses or other user
     * interactions with your application (value is not used in this app).
     *
     * @param category resource id of event category
     * @param action resource id of event action
     * @param label resource id of event label
     */
    @JvmStatic
    fun sendEvent(@StringRes category: Int, @StringRes action: Int, @StringRes label: Int) {
        Controller.getController().sendEvent(category, action, label)
    }
}