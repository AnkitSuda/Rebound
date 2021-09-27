package com.ankitsuda.rebound.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(val prefStorage: PrefStorage) : ViewModel() {
    val bottomBarLabelVisible = prefStorage.bottomBarLabelVisible
    val labelWeight = prefStorage.bottomBarLabelWeight
    val iconSize = prefStorage.bottomBarIconSize
    val currentWorkoutId = prefStorage.currentWorkoutId
}