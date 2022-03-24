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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.navigation.LOG_ID_KEY
import com.ankitsuda.navigation.PART_ID_KEY
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPartMeasurementBottomSheetViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val measurementsRepository: MeasurementsRepository
) :
    ViewModel() {
    private val logId = handle.get<String?>(LOG_ID_KEY)
    private val partId = handle.get<String?>(PART_ID_KEY)

    private var _log: MutableStateFlow<BodyPartMeasurementLog?> = MutableStateFlow(null)

    private var _isUpdate: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUpdate = _isUpdate.asStateFlow()

    private var _fieldValue = MutableStateFlow("")
    val fieldValue = _fieldValue

    init {
        Timber.d("logId null = ${logId == null}")
        if (logId != null) {
            viewModelScope.launch {
                try {
                    val log = measurementsRepository.getLog(logId)
                    _log.value = log
                    _fieldValue.value = _log.value!!.measurement.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        _isUpdate.value = logId != null
    }

    fun setFieldValue(value: String) {
        _fieldValue.value = value
    }

    fun saveMeasurement() {
        if (_isUpdate.value) {
            updateMeasurement()
        } else {
            addMeasurementToDb()
        }
    }

    private fun addMeasurementToDb() {
        if (partId != null) {
            viewModelScope.launch {
                measurementsRepository.addMeasurementToDb(fieldValue.value.toFloat(), partId)
                _fieldValue.value = ""
                _log.value = null
            }
        }
    }

    private fun updateMeasurement() {
        if (_log.value != null) {
            viewModelScope.launch {
                val mLog = _log.value!!
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


    fun deleteMeasurementFromDb() {
        if (logId != null) {
            viewModelScope.launch {
                measurementsRepository.deleteMeasurementFromDb(logId)
                _fieldValue.value = ""
                _log.value = null
            }
        }
    }
}