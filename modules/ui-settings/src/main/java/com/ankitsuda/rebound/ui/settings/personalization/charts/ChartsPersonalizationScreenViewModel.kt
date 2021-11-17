package com.ankitsuda.rebound.ui.settings.personalization.charts

import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
//import com.ankitsuda.rebound.utils.LabelVisible
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartsPersonalizationScreenViewModel @Inject constructor(/*val prefStorage: PrefStorage*/) :
    ViewModel() {
//    val shaderEnabled = prefStorage.chartsShaderEnabled
//    val lineThickness = prefStorage.chartsLineThickness
//    val pointDiameter = prefStorage.chartsPointDiameter
//    val pointLineThickness = prefStorage.chartsPointLineThickness
//
//    fun setShaderEnabled(value: Boolean) {
//        viewModelScope.launch {
//            prefStorage.setChartsShaderEnabled(value)
//        }
//    }
//    fun setLineThickness(value: Int) {
//        viewModelScope.launch {
//            prefStorage.setChartsLineThickness(value)
//        }
//    }
//    fun setPointDiameter(value: Int) {
//        viewModelScope.launch {
//            prefStorage.setChartsPointDiameter(value)
//        }
//    }
//    fun setPointLineThickness(value: Int) {
//        viewModelScope.launch {
//            prefStorage.setChartsPointLineThickness(value)
//        }
//    }
}