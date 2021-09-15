package com.ankitsuda.rebound.ui.screens.personalization.card

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardPersonalizationScreenViewModel @Inject constructor(val prefStorage: PrefStorage) :
    ViewModel() {

    val cardColor = prefStorage.cardColor
    val borderColor = prefStorage.cardBorderColor
    val borderWidth = prefStorage.cardBorderWidth
    val elevation = prefStorage.cardElevation

    fun setCardColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setCardColor(color)
        }
    }
    fun setBorderColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setCardBorderColor(color)
        }
    }

    fun setBorderWidth(value: Int) {
        viewModelScope.launch {
            prefStorage.setCardBorderWidth(value)
        }
    }

    fun setElevation(value: Int) {
        viewModelScope.launch {
            prefStorage.setCardElevation(value)
        }
    }

}