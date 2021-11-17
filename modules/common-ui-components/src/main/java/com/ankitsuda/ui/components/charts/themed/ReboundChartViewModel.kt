package com.ankitsuda.ui.components.charts.themed

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReboundChartViewModel @Inject constructor(val prefStorage: PrefStorage) : ViewModel() {
    val shaderEnabled = prefStorage.chartsShaderEnabled
    val lineThickness = prefStorage.chartsLineThickness
    val pointDiameter = prefStorage.chartsPointDiameter
    val pointLineThickness = prefStorage.chartsPointLineThickness
}