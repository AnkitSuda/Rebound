package com.ankitsuda.rebound.ui.settings.personalization.botom_bar

import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
//import com.ankitsuda.rebound.utils.LabelVisible
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomBarPersonalizationViewModel @Inject constructor(/*val prefStorage: PrefStorage*/) : ViewModel() {
//    val labelVisibleItems = LabelVisible.values()
//    val labelWeightItems = listOf(
//        "normal",
//        "bold"
//    )
//
//    val labelVisible = prefStorage.bottomBarLabelVisible
//    val labelWeight = prefStorage.bottomBarLabelWeight
//    val iconSize = prefStorage.bottomBarIconSize
//
//    fun setLabelVisible(value: String) {
//        viewModelScope.launch {
//            prefStorage.setBottomBarLabelVisible(value)
//        }
//    }
//
//    fun setLabelWeight(value: String) {
//        viewModelScope.launch {
//            prefStorage.setBottomBarLabelWeight(value)
//        }
//    }
//
//    fun setIconSize(value: Int) {
//        viewModelScope.launch {
//            prefStorage.setBottomBarIconSize(value)
//        }
//    }
}