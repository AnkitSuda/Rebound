package com.ankitsuda.rebound.ui.components.panel_tops

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PanelTopsViewModel @Inject constructor(private val workoutsRepository: WorkoutsRepository) :
    ViewModel() {

    val currentWorkoutId = workoutsRepository.getCurrentWorkoutId()
    fun getWorkout(workoutId: Long) = workoutsRepository.getWorkout(workoutId)
}