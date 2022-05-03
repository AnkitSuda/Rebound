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

package com.ankitsuda.rebound.ui.settings.personalization.presets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.ui.ThemeState
import com.ankitsuda.base.utils.extensions.lazyAsync
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.rebound.data.repositories.ThemePresetsRepository
import com.ankitsuda.rebound.domain.entities.ThemePreset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ThemePresetsPersonalizationScreenViewModel @Inject constructor(
    private val presetsRepository: ThemePresetsRepository
) : ViewModel() {

    val presets = presetsRepository.getPresets()
        .shareWhileObserved(viewModelScope)

    fun addPreset(themeState: ThemeState) {
        viewModelScope.launch {
            presetsRepository.addPreset(
                themeState = themeState,
                title = Date().time.toString(),
                description = null
            )
        }
    }

    fun deletePreset(preset: ThemePreset) {
        viewModelScope.launch {
            presetsRepository.deletePreset(preset)
        }
    }

}