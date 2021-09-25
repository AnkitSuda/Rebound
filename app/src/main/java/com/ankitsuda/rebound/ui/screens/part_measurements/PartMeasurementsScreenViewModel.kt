package com.ankitsuda.rebound.ui.screens.part_measurements

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

    fun deleteMeasurementToDb(logId: Long) {
        viewModelScope.launch {
            measurementsRepository.deleteMeasurementToDb(logId)
        }
    }
}