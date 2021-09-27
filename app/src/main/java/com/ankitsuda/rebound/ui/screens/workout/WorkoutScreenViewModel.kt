package com.ankitsuda.rebound.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) : ViewModel() {
    private var _currentWorkoutId = workoutsRepository.getCurrentWorkoutId()
    val currentWorkoutId = _currentWorkoutId

    fun startEmptyWorkout() {
        viewModelScope.launch {
            // JUST TEST
            workoutsRepository.setCurrentWorkoutId(Random.nextLong())
        }
    }

    fun cancelCurrentWorkout() {
        viewModelScope.launch {
            workoutsRepository.setCurrentWorkoutId(-1)
        }
    }
}