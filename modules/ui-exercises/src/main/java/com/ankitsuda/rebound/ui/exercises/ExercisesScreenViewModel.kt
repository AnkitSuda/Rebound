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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.data.repositories.MusclesRepository
import com.ankitsuda.rebound.domain.entities.ExerciseWithExtraInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExercisesScreenViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {
    private var _isSearchMode = MutableStateFlow(false)
    val isSearchMode = _isSearchMode

    private var _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm

    val exercisesPaged = _searchTerm.flatMapLatest {
        exercisesRepository.getExercisesWithExtraInfoPaged(
            searchQuery = it.trim().takeIf { q -> q.isNotBlank() }
        )
            .map { pagingData ->
                mapPage(pagingData)
            }
            .cachedIn(viewModelScope)
            .shareWhileObserved(viewModelScope)
    }


    private fun mapPage(pagingData: PagingData<ExerciseWithExtraInfo>): PagingData<Any> =
        pagingData.insertSeparators { before, after ->
            val afterFirstChar = after?.exercise?.name?.firstOrNull()?.uppercase()
            if (before?.exercise?.name?.firstOrNull()
                    ?.uppercase() != afterFirstChar
            ) {
                afterFirstChar
            } else {
                null
            }
        }

    fun toggleSearchMode() {
        _isSearchMode.value = !_isSearchMode.value
        _searchTerm.value = ""
    }

    fun setSearchTerm(term: String) {
        _searchTerm.value = term
    }

//    private fun filterExercises() {
//        viewModelScope.launch {
//            _filteredExercises.clear()
//            if (_isSearchMode.value == true) {
//                _filteredExercises.addAll(allExercises.filter {
//                    it.exercise.name?.contains(
//                        _searchTerm.value!!,
//                        true
//                    ) == true
//                })
//            } else {
//                _filteredExercises.addAll(allExercises)
//            }
//
//            _groupedExercises.emit(_filteredExercises.groupBy {
//                (it.exercise.name?.firstOrNull() ?: "#").toString().uppercase(Locale.getDefault())
//            })
////            }
//        }
//    }

}