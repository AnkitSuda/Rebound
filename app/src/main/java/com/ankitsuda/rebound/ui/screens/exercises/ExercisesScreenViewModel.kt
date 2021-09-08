package com.ankitsuda.rebound.ui.screens.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ExercisesScreenViewModel @Inject constructor(): ViewModel() {
    private var _isSearchMode = MutableLiveData(false)
    val isSearchMode = _isSearchMode

    private var _searchTerm = MutableLiveData("")
    val searchTerm = _searchTerm

    fun toggleSearchMode() {
        _isSearchMode.value = !(_isSearchMode.value)!!
    }

    fun setSearchTerm(term: String) {
        _searchTerm.value = term
    }
}