package com.ankitsuda.rebound.ui.screens.part_measurements

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.repositories.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPartMeasurementBottomSheetViewModel @Inject constructor(private val measurementsRepository: MeasurementsRepository) :
    ViewModel() {
    private var _fieldValue = MutableStateFlow("")
    val fieldValue = _fieldValue

    fun setFieldValue(value: String) {
        _fieldValue.value = value
    }

    fun addMeasurementToDb(value: Float, partId: Long) {
        viewModelScope.launch {
            measurementsRepository.addMeasurementToDb(value, partId)
        }
    }

}