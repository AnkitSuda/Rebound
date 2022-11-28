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

package com.ankitsuda.rebound.ui.components.workouteditor.barbellselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.navigation.BARBELL_ID_KEY
import com.ankitsuda.navigation.JUNCTION_ID_KEY
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.data.repositories.BarbellsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class BarbellSelectorBottomSheetViewModel @Inject constructor(
    handle: SavedStateHandle,
    barbellsRepository: BarbellsRepository,
) : ViewModel() {
    val junctionId = requireNotNull(handle.get<String>(JUNCTION_ID_KEY))
    val selectedBarbellId = handle.get<String>(BARBELL_ID_KEY)

    val barbells = barbellsRepository.getActiveBarbells()
        .distinctUntilChanged()
        .shareWhileObserved(viewModelScope)
}