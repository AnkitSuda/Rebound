package com.ankitsuda.rebound.ui.main

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.datastore.PrefStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(val prefStorage: PrefStorage) : ViewModel() {

    val currentWorkoutId = prefStorage.currentWorkoutId
}