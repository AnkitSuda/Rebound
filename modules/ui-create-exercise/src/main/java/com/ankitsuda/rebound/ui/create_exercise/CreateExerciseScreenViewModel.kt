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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.utils.extensions.getStateFlow
import com.ankitsuda.navigation.RESULT_MUSCLE_SELECTOR_KEY
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.domain.allExerciseCategories
import com.ankitsuda.rebound.ui.muscleselector.models.MuscleSelectorResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerciseScreenViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val musclesRepository: MusclesRepository,
    private val exercisesRepository: ExercisesRepository
) :
    ViewModel() {
    private var _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private var _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private var _isCreateBtnEnabled = MutableStateFlow(false)
    val isCreateBtnEnabled = _isCreateBtnEnabled.asStateFlow()

    private var _selectedCategory =
        MutableStateFlow<ExerciseCategory>(ExerciseCategory.WeightAndReps)
    val selectedCategory = _selectedCategory.asStateFlow()

    private var _selectedMuscle = MutableStateFlow<String?>("abductors")
    val selectedMuscle = _selectedMuscle.asStateFlow()

    // Dummy
//    val allCategories = ExerciseCategory.values()
    val allCategories = allExerciseCategories
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
            exercisesRepository.createExercise(
                name = _name.value,
                notes = _note.value,
                primaryMuscleTag = _selectedMuscle.value,
                category = _selectedCategory.value
            )
        }
    }
}