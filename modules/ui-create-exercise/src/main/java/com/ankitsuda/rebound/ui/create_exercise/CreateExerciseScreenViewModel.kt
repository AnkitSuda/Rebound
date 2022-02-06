/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.ui.create_exercise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.domain.entities.Exercise
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import com.ankitsuda.rebound.domain.ExerciseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerciseScreenViewModel @Inject constructor(
    private val musclesRepository: MusclesRepository,
    private val exercisesRepository: ExercisesRepository
) :
    ViewModel() {
    private var _name = MutableLiveData("")
    val name = _name

    private var _note = MutableLiveData("")
    val note = _note

    private var _isCreateBtnEnabled = MutableLiveData(false)
    val isCreateBtnEnabled = _isCreateBtnEnabled

    private var _selectedCategory = MutableLiveData(ExerciseCategory.WEIGHTS_AND_REPS)
    val selectedCategory = _selectedCategory

    private var _selectedMuscle = MutableLiveData("abductors")
    val selectedMuscle = _selectedMuscle

    // Dummy
    val allCategories = ExerciseCategory.values()
    val allPrimaryMuscles = musclesRepository.getMuscles()

    fun setName(value: String) {
        _name.value = value
    }

    fun setNote(value: String) {
        _note.value = value
    }

    fun setCategory(value: ExerciseCategory) {
        _selectedCategory.value = value
    }

    fun setPrimaryMuscle(value: String) {
        _selectedMuscle.value = value
    }

    fun createExercise() {
        viewModelScope.launch {
            val exercise = Exercise(
                name = _name.value,
                notes = _note.value,
                primaryMuscleTag = _selectedMuscle.value,
                category = _selectedCategory.value ?: ExerciseCategory.UNKNOWN
            )

            exercisesRepository.createExercise(exercise)
        }
    }
}