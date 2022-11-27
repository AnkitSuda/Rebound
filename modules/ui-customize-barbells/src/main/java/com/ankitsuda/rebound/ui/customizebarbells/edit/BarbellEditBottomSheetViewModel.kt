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

package com.ankitsuda.rebound.ui.customizebarbells.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.navigation.BARBELL_ID_KEY
import com.ankitsuda.rebound.data.repositories.BarbellsRepository
import com.ankitsuda.rebound.domain.entities.Barbell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BarbellEditBottomSheetViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val barbellsRepository: BarbellsRepository
) : ViewModel() {
    private val barbellId = handle.get<String?>(BARBELL_ID_KEY)

    private var _barbell = MutableStateFlow<Barbell?>(null)
    val barbell = _barbell.asStateFlow()

    private var _nameFieldValue = MutableStateFlow("")
    val nameFieldValue = _nameFieldValue.asStateFlow()

    private var _weightKgFieldValue = MutableStateFlow("")
    val weightKgFieldValue = _weightKgFieldValue.asStateFlow()

    private var _weightLbsFieldValue = MutableStateFlow("")
    val weightLbsFieldValue = _weightLbsFieldValue.asStateFlow()

    val isUpdate = barbellId != null

    init {
        if (barbellId != null) {
            viewModelScope.launch {
                val mBarbell = barbellsRepository.getBarbell(barbellId).first()

                _nameFieldValue.value = mBarbell.name ?: ""
                _weightKgFieldValue.value = mBarbell.weightKg?.toReadableString() ?: ""
                _weightLbsFieldValue.value = mBarbell.weightLbs?.toReadableString() ?: ""

                _barbell.value = mBarbell
            }
        }
    }

    fun updateNameFieldValue(value: String) {
        _nameFieldValue.value = value
    }

    fun updateWeightKgFieldValue(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() != null) {
            _weightKgFieldValue.value = value
        }
    }

    fun updateWeightLbsFieldValue(value: String) {
        if (value.isEmpty() || value.toDoubleOrNull() != null) {
            _weightLbsFieldValue.value = value
        }
    }

    fun deleteBarbell(onDeleted: () -> Unit) {
        viewModelScope.launch {
            _barbell.value?.id?.let {
                barbellsRepository.deleteBarbell(it)
            }
            onDeleted()
        }
    }

    fun saveBarbell(onSaved: () -> Unit) {
        viewModelScope.launch {
            val time = LocalDateTime.now()
            val lastBarbell = (_barbell.value ?: Barbell(
                id = generateId(),
                isActive = true,
                createdAt = time,
            ))

            val mBarbell = lastBarbell.copy(
                name = _nameFieldValue.value.takeIf { it.isNotBlank() } ?: lastBarbell.name,
                weightKg = _weightKgFieldValue.value.toDoubleOrNull() ?: lastBarbell.weightKg,
                weightLbs = _weightLbsFieldValue.value.toDoubleOrNull() ?: lastBarbell.weightLbs,
                updatedAt = time
            )

            barbellsRepository.upsertBarbell(mBarbell)

            onSaved()
        }
    }
}