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


import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes


/**
 * Interactions with Android framework components responsible for part of the user experience are
 * handled via this singleton.
 */
class Controller private constructor() {
    private var mContext: Context? = null

    /** The controller that dispatches app events to event trackers.  */
    private lateinit var mEventController: EventController

    fun setContext(context: Context) {
        if (mContext != context) {
            mContext = context.getApplicationContext()
            mEventController = EventController()
        }
    }

    //
    // Event Tracking
    //

    /**
     * @param eventTracker to be registered for tracking application events
     */
    fun addEventTracker(eventTracker: EventTracker) {
        Utils.enforceMainLooper()
        mEventController.addEventTracker(eventTracker)
    }

    /**
     * @param eventTracker to be unregistered from tracking application events
     */
    fun removeEventTracker(eventTracker: EventTracker) {
        Utils.enforceMainLooper()
        mEventController.removeEventTracker(eventTracker)
    }

    /**
     * Tracks an event. Events have a category, action and label. This method can be used to track
     * events such as button presses or other user interactions with your application.
     *
     * @param category resource id of event category
     * @param action resource id of event action
     * @param label resource id of event label
     */
    fun sendEvent(@StringRes category: Int, @StringRes action: Int, @StringRes label: Int) {
        mEventController.sendEvent(category, action, label)
    }


    companion object {
        private val sController = Controller()

        @JvmStatic
        fun getController() = sController
    }
}