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

package com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.base.utils.generateId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WarmUpCalculatorDialogViewModel @Inject constructor() : ViewModel() {
    private var _sets = MutableStateFlow<List<WarmUpSet>>(emptyList())
    val sets = _sets.asStateFlow()

    fun setSets(newSets: List<WarmUpSet>) {
        viewModelScope.launch {
            _sets.value = newSets
        }
    }

    fun updateWorkSet(barWeight: Double, newWorkSet: Double) {
        viewModelScope.launch {
            val newSets = _sets.value.toArrayList()
            newSets.forEach {
                it.weight = if (it.weightPercentage == -1 || it.weightPercentage == null) {
                    barWeight
                } else {
                    newWorkSet * it.weightPercentage!! / 100
                }
            }
        }
    }

    fun updateSet(set: WarmUpSet) {
        viewModelScope.launch {
            val newSets = _sets.value.toArrayList()
            try {
                val index = newSets.indexOf(newSets.find { set.id == it.id })
                newSets[index] = set
                _sets.value = newSets
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addEmptySet(barWeight: Double) {
        viewModelScope.launch {
            val newSets = _sets.value.toArrayList()
            newSets.add(
                WarmUpSet(
                    id = generateId(),
                    formula = "Bar x 1",
                    reps = 1,
                    weight = barWeight,
                    weightPercentage = -1
                )
            )
            _sets.value = newSets
        }
    }

    fun deleteSet(set: WarmUpSet) {
        viewModelScope.launch {
            val newSets = _sets.value.toArrayList()
            val index = newSets.indexOf(newSets.find { set.id == it.id })
            newSets.removeAt(index)
            _sets.value = newSets
        }
    }
}