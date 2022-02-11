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

package com.ankitsuda.rebound.data.datastore

import com.ankitsuda.base.ui.ThemeState
import kotlinx.coroutines.flow.Flow

interface PrefStorage {

    val themeState: Flow<ThemeState>
    suspend fun setThemeState(value: ThemeState)

    val currentWorkoutId: Flow<String>
    suspend fun setCurrentWorkoutId(value: String)


    /***
     * clears all the stored data
     */
    suspend fun clearPreferenceStorage()
}