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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.ankitsuda.base.utils.extensions.shareWhileObserved
import com.ankitsuda.rebound.data.repositories.ExercisesRepository
import com.ankitsuda.rebound.domain.entities.ExerciseWithExtraInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
}