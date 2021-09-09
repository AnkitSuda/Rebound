package com.ankitsuda.rebound.ui.screens.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(): ViewModel() {
    private var _isCalendarMode = MutableLiveData(false)
    val isCalendarMode = _isCalendarMode

    fun toggleCalendarMode() {
        _isCalendarMode.value = !(_isCalendarMode.value)!!
    }

}