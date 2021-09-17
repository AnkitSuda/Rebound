package com.ankitsuda.rebound.ui.screens.personalization.main_colors

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainColorsPersonalizationScreenViewModel @Inject constructor(val prefStorage: PrefStorage) :
    ViewModel() {

    val isLightTheme = prefStorage.isLightTheme

    val primaryColor = prefStorage.primaryColor
    val backgroundColor = prefStorage.backgroundColor

    val onPrimaryColor = prefStorage.onPrimaryColor
    val onBackgroundColor = prefStorage.onBackgroundColor

    fun setPrimaryColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setPrimaryColor(color)
        }
    }

    fun setIsLightTheme(value: Boolean) {
        viewModelScope.launch {
            prefStorage.setIsLightTheme(value)
        }
    }

    fun setBackgroundColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setBackgroundColor(color)
        }
    }
    fun setOnPrimaryColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setOnPrimaryColor(color)
        }
    }
    fun setOnBackgroundColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setOnBackgroundColor(color)
        }
    }


}