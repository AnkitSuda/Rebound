package com.ankitsuda.rebound.data.datastore

import com.ankitsuda.base.ui.ThemeState
import kotlinx.coroutines.flow.Flow

interface PrefStorage {

    val themeState: Flow<ThemeState>
    suspend fun setThemeState(value: ThemeState)

    val currentWorkoutId: Flow<Long>
    suspend fun setCurrentWorkoutId(value: Long)


    /***
     * clears all the stored data
     */
    suspend fun clearPreferenceStorage()
}