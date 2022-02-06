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

package com.ankitsuda.rebound.ui.measure.part.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartMeasurementsScreenViewModel @Inject constructor(private val measurementsRepository: MeasurementsRepository) :
    ViewModel() {

    fun getLogsForPart(partId: Long) = measurementsRepository.getLogsForPart(partId)

    fun deleteMeasurementFromDb(logId: Long) {
        viewModelScope.launch {
            measurementsRepository.deleteMeasurementFromDb(logId)
        }
    }
}