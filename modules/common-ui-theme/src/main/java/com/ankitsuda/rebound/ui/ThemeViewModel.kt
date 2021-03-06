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

package com.ankitsuda.rebound.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.domain.models.DEFAULT_JSON_FORMAT
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.domain.entities.ThemePreset
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

object PreferenceKeys {
    const val THEME_STATE_KEY = "theme_state"
}

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val preferences: PrefStorage,
) : ViewModel() {

    val themeState = preferences.themeState

    fun applyThemeState(themeState: ThemeState) {
        viewModelScope.launch {
            preferences.setThemeState(themeState)
        }
    }

    fun applyThemePreset(preset: ThemePreset) {
        viewModelScope.launch {
            try {
                preset.themeJson?.let {
                    val newState = DEFAULT_JSON_FORMAT.decodeFromString(ThemeState.serializer(), it)
                    preferences.setThemeState(newState)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
