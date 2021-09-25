package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PartMeasurementsScreenViewModel @Inject constructor(private val measurementsRepository: MeasurementsRepository) :
    ViewModel() {

    fun getLogsForPart(partId: Long) = measurementsRepository.getLogsForPart(partId)

}