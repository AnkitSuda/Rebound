package com.ankitsuda.rebound.ui.screens.personalization.botom_bar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.utils.LabelVisible
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomBarPersonalizationViewModel @Inject constructor(val prefStorage: PrefStorage) : ViewModel() {
    val labelVisibleItems = LabelVisible.values()
    val labelVisible = prefStorage.bottomBarLabelVisible

    fun setLabelVisible(value: String) {
        viewModelScope.launch {
            prefStorage.setBottomBarLabelVisible(value)
        }
    }
}