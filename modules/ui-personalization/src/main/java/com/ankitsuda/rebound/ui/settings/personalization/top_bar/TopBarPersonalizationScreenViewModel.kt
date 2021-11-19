package com.ankitsuda.rebound.ui.settings.personalization.top_bar

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
//import com.ankitsuda.rebound.utils.TopBarAlignment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopBarPersonalizationScreenViewModel @Inject constructor(/*val prefStorage: PrefStorage*/): ViewModel() {
//    val titleAlignment = prefStorage.topBarTitleAlignment
//    val allTitleAlignments = TopBarAlignment.values()
//    val backgroundColor = prefStorage.topBarBackgroundColor
//    val contentColor = prefStorage.topBarContentColor
//    val elevation = prefStorage.topBarElevation
//
//    fun setTitleAlignment(value: String) {
//        viewModelScope.launch {
//            prefStorage.setTopBarTitleAlignment(value)
//        }
//    }
//
//    fun setBackgroundColor(value: Color) {
//        viewModelScope.launch {
//            prefStorage.setTopBarBackgroundColor(value)
//        }
//    }
//
//    fun setContentColor(value: Color) {
//        viewModelScope.launch {
//            prefStorage.setTopBarContentColor(value)
//        }
//    }
//
//    fun setElevation(value: Int) {
//        viewModelScope.launch {
//            prefStorage.setTopBarElevation(value)
//        }
//    }

}