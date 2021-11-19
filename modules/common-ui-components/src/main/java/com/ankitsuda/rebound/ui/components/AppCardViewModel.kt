package com.ankitsuda.rebound.ui.components

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppCardViewModel @Inject constructor(prefStorage: PrefStorage) : ViewModel() {
    val cardColor = Color.White//prefStorage.cardColor
    val elevation = 1//prefStorage.cardElevation
}