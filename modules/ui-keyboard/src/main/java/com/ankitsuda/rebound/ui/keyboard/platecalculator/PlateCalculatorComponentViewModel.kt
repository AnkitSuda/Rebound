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

package com.ankitsuda.rebound.ui.keyboard.platecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.repositories.PlatesRepository
import com.ankitsuda.rebound.domain.entities.Plate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

@HiltViewModel
class PlateCalculatorComponentViewModel @Inject constructor(
    private val platesRepository: PlatesRepository
) : ViewModel() {

    private var _plates: MutableStateFlow<List<Plate>> = MutableStateFlow(emptyList())
    val plates = _plates.asStateFlow()

    private var _remainingWeight: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val remainingWeight = _remainingWeight.asStateFlow()

    private var platesJob: Job? = null
    private var lastWeight: Double? = null

    private val _allPlates = arrayListOf<Plate>()

    init {
        viewModelScope.launch {
            platesRepository.getActivePlates().collectLatest {
                _allPlates.clear()
                _allPlates.addAll(it)
                if (lastWeight != null) {
                    refreshPlates(lastWeight!!)
                }
            }
        }
    }

    fun refreshPlates(newWeight: Double) {
        platesJob?.cancel()
        platesJob = viewModelScope.launch {
            if (_allPlates.isEmpty()) {
                val availablePlates = platesRepository.getActivePlates().first()
                _allPlates.clear()
                _allPlates.addAll(availablePlates)
            }
            val platesNeeded = calculatePlates(newWeight, _allPlates.sortedByDescending { it.weight })
            val sumOfPlates = platesNeeded.sumOf { it.weight ?: 0.0 }
            _plates.value = platesNeeded
            _remainingWeight.value = try {
                newWeight - sumOfPlates * 2
            } catch (e: Exception) {
                e.printStackTrace()
                0.0
            }
        }
        lastWeight = newWeight
    }

    private fun calculatePlates(targetWeight: Double, allPlates: List<Plate>): List<Plate> {
        var currentWeight = 0.0
        val multiplier = 2

        val result = arrayListOf<Plate>()

        for (plate in allPlates) {

            // Null limitation == infinite
            if (currentWeight < targetWeight) {
                // The weight we're testing to see if it will fit
                var testWeight = plate.weight ?: 0.0

                // Check if we can add this weight to the bar
                if (testWeight <= targetWeight - currentWeight) {
                    // How many of this plate can we add in total?
                    var qty = floor((targetWeight - currentWeight) / testWeight)

                    if (qty % multiplier == 1.0) {
                        qty -= 1;
                    }

                    if (qty != 0.0) {

                        try {
                            repeat(qty.roundToInt() / 2) {
                                result.add(
                                    plate
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    // Add weight to the bar
                    currentWeight += testWeight * qty
                }
            }
        }
        return result
    }
}