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

/**
 * Data that must be coordinated across all notifications is accessed via this model.
 */
internal class NotificationModel {
    /**
     * @return `true` while the application is open in the foreground
     */
    /**
     * @param inForeground `true` to indicate the application is open in the foreground
     */
    var isApplicationInForeground = false

    //
    // Notification IDs
    //
    // Used elsewhere:
    // Integer.MAX_VALUE - 4
    // Integer.MAX_VALUE - 5
    // Integer.MAX_VALUE - 7
    //

    /**
     * @return a value that identifies the stopwatch notification
     */
    val stopwatchNotificationId: Int
        get() = Int.MAX_VALUE - 1

    /**
     * @return a value that identifies the notification for running/paused timers
     */
    val unexpiredTimerNotificationId: Int
        get() = Int.MAX_VALUE - 2

    /**
     * @return a value that identifies the notification for expired timers
     */
    val expiredTimerNotificationId: Int
        get() = Int.MAX_VALUE - 3

    /**
     * @return a value that identifies the notification for missed timers
     */
    val missedTimerNotificationId: Int
        get() = Int.MAX_VALUE - 6

    //
    // Notification Group keys
    //
    // Used elsewhere:
    // "1"
    // "4"

    /**
     * @return the group key for the stopwatch notification
     */
    val stopwatchNotificationGroupKey: String
        get() = "3"

    /**
     * @return the group key for the timer notification
     */
    val timerNotificationGroupKey: String
        get() = "2"

    //
    // Notification Sort keys
    //

    /**
     * @return the sort key for the timer notification
     */
    val timerNotificationSortKey: String
        get() = "0"

    /**
     * @return the sort key for the missed timer notification
     */
    val timerNotificationMissedSortKey: String
        get() = "1"
}