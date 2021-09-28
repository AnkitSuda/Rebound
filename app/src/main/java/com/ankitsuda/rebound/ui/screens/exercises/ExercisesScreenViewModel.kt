package com.ankitsuda.rebound.ui.screens.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ExercisesScreenViewModel @Inject constructor(
    private val musclesRepository: MusclesRepository,
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {
    private var _isSearchMode = MutableLiveData(false)
    val isSearchMode = _isSearchMode

    private var _searchTerm = MutableLiveData("")
    val searchTerm = _searchTerm

    val allExercises = exercisesRepository.getAllExercises()
    val allMuscles = musclesRepository.getMuslces()

    fun toggleSearchMode() {
        _isSearchMode.value = !(_isSearchMode.value)!!
    }

    fun setSearchTerm(term: String) {
        _searchTerm.value = term
    }


}