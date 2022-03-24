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

interface EventTracker {

    /**
     * Record the event in some form or fashion.
     *
     * @param category indicates what entity raised the event: Alarm, Clock, Timer or Stopwatch
     * @param action indicates how the entity was altered; e.g. create, delete, fire, etc.
     * @param label indicates where the action originated; e.g. DeskClock (UI), Intent,
     * Notification, etc.; 0 indicates no label could be established
     */
    fun sendEvent(@StringRes category: Int, @StringRes action: Int, @StringRes label: Int)
}