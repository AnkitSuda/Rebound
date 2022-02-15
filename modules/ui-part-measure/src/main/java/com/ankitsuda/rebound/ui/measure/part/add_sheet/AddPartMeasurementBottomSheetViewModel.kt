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

package com.ankitsuda.rebound.ui.measure.part.add_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPartMeasurementBottomSheetViewModel @Inject constructor(private val measurementsRepository: MeasurementsRepository) :
    ViewModel() {
    private var _log: MutableStateFlow<BodyPartMeasurementLog?> = MutableStateFlow(null)
    val log = _log

    private var _fieldValue = MutableStateFlow("")
    val fieldValue = _fieldValue

    fun setFieldValue(value: String) {
        _fieldValue.value = value
    }

    suspend fun setLogId(logId: String)/*: BodyPartMeasurementLog? */ {
        viewModelScope.launch {
            val log = measurementsRepository.getLog(logId)
            _log.value = log
//        return log
            _fieldValue.value = _log.value!!.measurement.toString()
        }
    }

    fun addMeasurementToDb(partId: String) {
        viewModelScope.launch {
            measurementsRepository.addMeasurementToDb(fieldValue.value.toFloat(), partId)
            _fieldValue.value = ""
            _log.value = null
        }
    }

    fun updateMeasurement() {
        if (log.value != null) {
            viewModelScope.launch {
                val mLog = log.value!!
                mLog.measurement = fieldValue.value.toFloat()
                mLog.updatedAt = LocalDateTime.now()
                measurementsRepository.updateMeasurement(
                    mLog
                )
                _fieldValue.value = ""
                _log.value = null
            }
        }
    }


    fun deleteMeasurementFromDb(logId: String) {
        viewModelScope.launch {
            measurementsRepository.deleteMeasurementFromDb(logId)
            _fieldValue.value = ""
            _log.value = null
        }
    }
}