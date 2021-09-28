package com.ankitsuda.rebound.ui.components.workout_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPanelViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {
    val currentWorkoutId: Flow<Long> = workoutsRepository.getCurrentWorkoutId()
    var mWorkoutId: Long = -1
    var mWorkout: Workout? = null

    fun getWorkout(workoutId: Long): Flow<Workout?> =
        workoutsRepository.getWorkout(workoutId)

    fun updateWorkoutName(name: String) {
        viewModelScope.launch {
            mWorkout?.let {
                workoutsRepository.updateWorkout(it.copy(name = name))
            }
        }
    }

    fun updateWorkoutNote(note: String) {
        viewModelScope.launch {
            mWorkout?.let {
                workoutsRepository.updateWorkout(it.copy(note = note))
            }
        }
    }

    fun cancelCurrentWorkout() {
        viewModelScope.launch {
            workoutsRepository.setCurrentWorkoutId(-1)
        }
    }
}