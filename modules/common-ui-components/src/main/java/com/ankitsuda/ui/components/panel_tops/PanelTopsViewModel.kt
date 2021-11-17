package com.ankitsuda.ui.components.panel_tops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ankitsuda.rebound.data.entities.Workout
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PanelTopsViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {

    val currentWorkoutId = workoutsRepository.getCurrentWorkoutId()
    fun getWorkout(workoutId: Long) = workoutsRepository.getWorkout(workoutId)
}