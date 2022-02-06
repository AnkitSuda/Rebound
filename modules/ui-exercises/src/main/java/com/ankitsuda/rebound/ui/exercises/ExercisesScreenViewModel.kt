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

package com.ankitsuda.rebound.ui.exercises

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import com.ankitsuda.rebound.domain.entities.ExerciseWithMuscle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private val allExercises = arrayListOf<ExerciseWithMuscle>()

    val allMuscles = musclesRepository.getMuscles()

    private var _filteredExercises: SnapshotStateList<ExerciseWithMuscle> =
        SnapshotStateList()
    val filteredExercises = _filteredExercises

    init {
        viewModelScope.launch {
            exercisesRepository.getAllExercisesWithMuscles().collect {
                allExercises.clear()
                allExercises.addAll(it)
                filterExercises()
            }
        }
    }

    fun toggleSearchMode() {
        _isSearchMode.value = !(_isSearchMode.value)!!
        filterExercises()
    }

    fun setSearchTerm(term: String) {
        _searchTerm.value = term
        filterExercises()
    }

    private fun filterExercises() {
        viewModelScope.launch {
//            if (_searchTerm.value?.isNotBlank() == true) {
//                _filteredExercises.value = allExercises.last()
//                    .filter { it.exercise.name?.contains(_searchTerm.value!!, true) == true }
//            } else {
            Timber.d("Filtering list $allExercises")
            _filteredExercises.clear()
            if (_isSearchMode.value == true) {
                _filteredExercises.addAll(allExercises.filter {
                    it.exercise.name?.contains(
                        _searchTerm.value!!,
                        true
                    ) == true
                })
            } else {
                _filteredExercises.addAll(allExercises)
            }
//            }
        }
    }

}