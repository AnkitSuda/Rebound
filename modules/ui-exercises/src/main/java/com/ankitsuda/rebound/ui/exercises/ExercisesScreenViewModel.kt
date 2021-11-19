package com.ankitsuda.rebound.ui.exercises

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val allExercises = exercisesRepository.getAllExercisesWithMuscles()
    val allMuscles = musclesRepository.getMuscles()

    fun toggleSearchMode() {
        _isSearchMode.value = !(_isSearchMode.value)!!
    }

    fun setSearchTerm(term: String) {
        _searchTerm.value = term
    }


}