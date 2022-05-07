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

package com.ankitsuda.rebound.ui.customizeplates.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.util.toStringP15
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.navigation.LOG_ID_KEY
import com.ankitsuda.navigation.PLATE_ID_KEY
import com.ankitsuda.rebound.data.repositories.PlatesRepository
import com.ankitsuda.rebound.domain.entities.Plate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PlateEditBottomSheetViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val platesRepository: PlatesRepository
) : ViewModel() {
    private val plateId = handle.get<String?>(PLATE_ID_KEY)

    private var _plate = MutableStateFlow<Plate?>(null)
    val plate = _plate.asStateFlow()

    private var _weightFieldValue = MutableStateFlow("")
    val weightFieldValue = _weightFieldValue.asStateFlow()

    private var _colorFieldValue = MutableStateFlow("")
    val colorFieldValue = _colorFieldValue.asStateFlow()

    private var _heightFieldValue = MutableStateFlow("")
    val heightFieldValue = _heightFieldValue.asStateFlow()

    private var _widthFieldValue = MutableStateFlow("")
    val widthFieldValue = _widthFieldValue.asStateFlow()

    val isUpdate = plateId != null

    init {
        if (plateId != null) {
            viewModelScope.launch {
                val mPlate = platesRepository.getPlate(plateId).first()
                _weightFieldValue.value = mPlate.weight?.toReadableString() ?: ""
                _colorFieldValue.value = mPlate.color ?: ""
                _heightFieldValue.value = mPlate.height?.toStringP15() ?: ""
                _widthFieldValue.value = mPlate.width?.toStringP15() ?: ""
                _plate.value = mPlate
            }
        }
    }

    fun updateWeightFieldValue(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() != null) {
            _weightFieldValue.value = value
        }
    }

    fun updateColorFieldValue(value: String) {
        _colorFieldValue.value = value
    }

    fun updateHeightFieldValue(value: String) {
        if (value.isEmpty() || value.toFloatOrNull() != null) {
            _heightFieldValue.value = value
        }
    }

    fun updateWidthFieldValue(value: String) {
        if (value.isEmpty() || value.toFloatOrNull() != null) {
            _widthFieldValue.value = value
        }
    }

    fun deletePlate(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _plate.value?.id?.let {
                platesRepository.deletePlate(it)
            }
            onDeleted()
        }
    }

    fun savePlate(onSaved: () -> Unit) {
        viewModelScope.launch {
            val time = LocalDateTime.now()
            val lastPlate = (_plate.value ?: Plate(
                id = generateId(),
                colorValueType = "hex",
                isActive = true,
                createdAt = time,
            ))

            val mPlate = lastPlate.copy(
                weight = _weightFieldValue.value.toDoubleOrNull() ?: lastPlate.weight,
                height = _heightFieldValue.value.toFloatOrNull() ?: lastPlate.height,
                width = _widthFieldValue.value.toFloatOrNull() ?: lastPlate.width,
                color = _colorFieldValue.value,
                updatedAt = time
            )

            platesRepository.upsertPlate(mPlate)

            onSaved()
        }
    }
}