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

package com.ankitsuda.rebound

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import com.ankitsuda.base.BaseApp
import com.ankitsuda.base.initializers.AppInitializers
import com.ankitsuda.rebound.data.stopwatch.Controller
import com.ankitsuda.rebound.data.stopwatch.DataModel
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ReboundApplication : BaseApp() {

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)

        val applicationContext = applicationContext
        val prefs = getDefaultSharedPreferences(applicationContext)

        DataModel.dataModel.init(applicationContext, prefs)
        Controller.getController().setContext(applicationContext)
    }

    companion object {
        /**
         * Returns the default [SharedPreferences] instance from the underlying storage context.
         */
        @TargetApi(Build.VERSION_CODES.N)
        private fun getDefaultSharedPreferences(context: Context): SharedPreferences {
            val storageContext: Context
//            if (Utils.isNOrLater) {
            // All N devices have split storage areas. Migrate the existing preferences
            // into the new device encrypted storage area if that has not yet occurred.
            val name = PreferenceManager.getDefaultSharedPreferencesName(context)
            storageContext = context.createDeviceProtectedStorageContext()
//            } else {
//                storageContext = context
//            }
            return PreferenceManager.getDefaultSharedPreferences(storageContext)
        }
    }
}