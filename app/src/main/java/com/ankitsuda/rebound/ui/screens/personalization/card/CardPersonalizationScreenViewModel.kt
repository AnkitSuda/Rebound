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
    val borderEnabled = prefStorage.cardBorderEnabled

    fun setCardColor(color: Color) {
        viewModelScope.launch {
            prefStorage.setCardColor(color)
        }
    }

    fun setBorderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            prefStorage.setCardBorderEnabled(enabled)
        }
    }

}