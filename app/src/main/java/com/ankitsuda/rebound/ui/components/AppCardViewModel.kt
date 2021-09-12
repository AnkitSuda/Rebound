package com.ankitsuda.rebound.ui.components

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppCardViewModel @Inject constructor(prefStorage: PrefStorage) : ViewModel() {
    val cardColor = prefStorage.cardColor
    val elevation = prefStorage.cardElevation
}