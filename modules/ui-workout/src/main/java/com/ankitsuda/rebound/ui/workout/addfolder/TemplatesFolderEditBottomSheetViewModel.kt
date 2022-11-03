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

package com.ankitsuda.rebound.ui.workout.addfolder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.navigation.FOLDER_ID_KEY
import com.ankitsuda.rebound.data.repositories.WorkoutTemplatesRepository
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TemplatesFolderEditBottomSheetViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val repository: WorkoutTemplatesRepository
) :
    ViewModel() {
    private val folderId = handle.get<String?>(FOLDER_ID_KEY)

    private var _folder: MutableStateFlow<WorkoutTemplatesFolder?> = MutableStateFlow(null)

    private var _isUpdate: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUpdate = _isUpdate.asStateFlow()

    private var _fieldValue = MutableStateFlow("")
    val fieldValue = _fieldValue

    init {
        if (folderId != null) {
            viewModelScope.launch {
                try {
                    val folder = repository.getFolder(folderId).firstOrNull()
                    _folder.value = folder
                    _fieldValue.value = _folder.value?.name ?: ""
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        _isUpdate.value = folderId != null
    }

    fun setFieldValue(value: String) {
        _fieldValue.value = value
    }

    fun saveFolder() {
        if (_isUpdate.value) {
            updateFolder()
        } else {
            addFolderToDb()
        }
    }

    private fun addFolderToDb() {
        viewModelScope.launch {
            repository.addFolder(fieldValue.value)
            _fieldValue.value = ""
            _folder.value = null

        }
    }

    private fun updateFolder() {
        if (_folder.value != null) {
            viewModelScope.launch {
                val mFolder = _folder.value!!
                mFolder.name = fieldValue.value
                mFolder.updatedAt = LocalDateTime.now()
                repository.updateFolder(
                    mFolder
                )
                _fieldValue.value = ""
                _folder.value = null
            }
        }
    }

    fun deleteFolderFromDb() {
        if (folderId != null) {
            viewModelScope.launch {
                repository.deleteFolder(folderId)
                _fieldValue.value = ""
                _folder.value = null
            }
        }
    }
}