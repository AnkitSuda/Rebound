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

package com.ankitsuda.rebound.ui.customizeplates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.rebound.data.repositories.PlatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomizePlatesScreenViewModel @Inject constructor(
    private val platesRepository: PlatesRepository
) : ViewModel() {
    val groupedPlates = platesRepository.getPlates()
        .map {
            it.groupBy { p -> p.forWeightUnit }
        }
        .shareWhileObserved(viewModelScope)

    fun updateIsActive(plateId: String, isActive: Boolean) {
        viewModelScope.launch {
            platesRepository.updateIsActive(plateId, isActive)
        }
    }

    fun deletePlate(plateId: String) {
        viewModelScope.launch {
            platesRepository.deletePlate(plateId)
        }
    }
}