package com.ankitsuda.rebound.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.datastore.PrefStorage
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPanelViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) : ViewModel() {
    private var _currentWorkoutId = workoutsRepository.getCurrentWorkoutId()
    val currentWorkoutId = _currentWorkoutId

    fun setCurrentWorkoutId(value: Long) {
        viewModelScope.launch {
            workoutsRepository.setCurrentWorkoutId(value)
        }
    }

    fun cancelCurrentWorkout() {
        viewModelScope.launch {
            workoutsRepository.setCurrentWorkoutId(-1)
        }
    }
}