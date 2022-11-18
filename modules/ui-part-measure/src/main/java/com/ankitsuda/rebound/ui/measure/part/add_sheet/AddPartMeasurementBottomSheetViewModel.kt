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
import com.ankitsuda.base.util.fromKgToLbsReadable
import com.ankitsuda.base.util.fromLbsToKg
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.navigation.LOG_ID_KEY
import com.ankitsuda.navigation.PART_ID_KEY
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import com.ankitsuda.rebound.domain.entities.BodyPartUnitType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddPartMeasurementBottomSheetViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val prefs: PrefStorage,
    private val measurementsRepository: MeasurementsRepository
) :
    ViewModel() {
    private val logId = handle.get<String?>(LOG_ID_KEY)
    private val partId = requireNotNull(handle.get<String>(PART_ID_KEY))

    private var _log: MutableStateFlow<BodyPartMeasurementLog?> = MutableStateFlow(null)

    private var _isUpdate: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUpdate = _isUpdate.asStateFlow()

    private var _fieldValue = MutableStateFlow("")
    val fieldValue = _fieldValue

    private val _eventChannel = Channel<AddPartMeasurementEvent>(Channel.BUFFERED)
    val eventsFlow = _eventChannel.receiveAsFlow()

    val bodyPart = measurementsRepository
        .getBodyPartByPartId(partId)
        .distinctUntilChanged()
        .shareWhileObserved(viewModelScope)

    init {
        if (logId != null) {
            viewModelScope.launch {
                try {
                    val log = measurementsRepository.getLog(logId)
                    _log.value = log

                    _fieldValue.value = when (bodyPart.first().unitType) {
                        BodyPartUnitType.WEIGHT -> getWeightValueStrInUserPref(log.measurement)
                        else -> log.measurement.toReadableString()
                    }
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

    private suspend fun getValueInKg(): Double {
        val weightUnit = prefs.weightUnit.firstOrNull() ?: WeightUnit.KG
        val value = _fieldValue.value

        return when (weightUnit) {
            WeightUnit.KG -> value.toDoubleOrNull()
            WeightUnit.LBS -> value.toDoubleOrNull()?.fromLbsToKg()
        } ?: 0.0
    }

    private suspend fun getWeightValueStrInUserPref(weight: Double): String {
        return when (prefs.weightUnit.firstOrNull() ?: WeightUnit.KG) {
            WeightUnit.KG -> weight.toReadableString()
            WeightUnit.LBS -> weight.fromKgToLbsReadable()
        } ?: ""
    }

    private suspend fun getMeasurementForDb(): Double = when (bodyPart.firstOrNull()?.unitType) {
        BodyPartUnitType.WEIGHT -> getValueInKg()
        else -> _fieldValue.value.toDoubleOrNull() ?: 0.0
    }


    private fun addMeasurementToDb() {
        viewModelScope.launch {
            measurementsRepository.addMeasurementToDb(getMeasurementForDb(), partId)
            _fieldValue.value = ""
            _log.value = null

            _eventChannel.send(AddPartMeasurementEvent.Added)
        }

    }

    private fun updateMeasurement() {
        if (_log.value != null) {
            viewModelScope.launch {
                val mLog = _log.value!!
                mLog.measurement = getMeasurementForDb()
                mLog.updatedAt = LocalDateTime.now()
                measurementsRepository.updateMeasurement(
                    mLog
                )
                _fieldValue.value = ""
                _log.value = null

                _eventChannel.send(AddPartMeasurementEvent.Updated)
            }
        }
    }


    fun deleteMeasurementFromDb() {
        if (logId != null) {
            viewModelScope.launch {
                measurementsRepository.deleteMeasurementFromDb(logId)
                _fieldValue.value = ""
                _log.value = null
                _eventChannel.send(AddPartMeasurementEvent.Deleted)
            }
        }
    }
}

sealed class AddPartMeasurementEvent {
    object Added : AddPartMeasurementEvent()
    object Updated : AddPartMeasurementEvent()
    object Deleted : AddPartMeasurementEvent()
}