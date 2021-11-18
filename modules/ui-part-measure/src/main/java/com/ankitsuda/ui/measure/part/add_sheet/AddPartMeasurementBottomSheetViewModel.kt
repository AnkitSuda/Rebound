package com.ankitsuda.ui.measure.part.add_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.domain.entities.BodyPartMeasurementLog
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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

    suspend fun setLogId(logId: Long)/*: BodyPartMeasurementLog? */ {
        viewModelScope.launch {
            val log = measurementsRepository.getLog(logId)
            _log.value = log
//        return log
            _fieldValue.value = _log.value!!.measurement.toString()
        }
    }

    fun addMeasurementToDb(partId: Long) {
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
                mLog.updatedAt = Date()
                measurementsRepository.updateMeasurement(
                    mLog
                )
                _fieldValue.value = ""
                _log.value = null
            }
        }
    }


    fun deleteMeasurementFromDb(logId: Long) {
        viewModelScope.launch {
            measurementsRepository.deleteMeasurementFromDb(logId)
            _fieldValue.value = ""
            _log.value = null
        }
    }
}