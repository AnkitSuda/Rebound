package com.ankitsuda.rebound.ui.screens.exercise

import androidx.lifecycle.ViewModel
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailScreenViewModel @Inject constructor(private val exercisesRepository: ExercisesRepository) :
    ViewModel() {

    fun getExercise(exerciseId: Long) = exercisesRepository.getExercise(exerciseId)
}