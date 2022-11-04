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

package com.ankitsuda.rebound.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.TimePeriod
import com.ankitsuda.base.utils.extensions.toArrayList
import com.ankitsuda.base.utils.toReadableDuration
import com.ankitsuda.rebound.data.repositories.WorkoutTemplatesRepository
import com.ankitsuda.rebound.data.repositories.WorkoutsRepository
import com.ankitsuda.rebound.domain.entities.TemplateWithWorkout
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.domain.entities.WorkoutTemplate
import com.ankitsuda.rebound.domain.entities.WorkoutTemplatesFolder
import com.ankitsuda.rebound.ui.components.dragdrop.move
import com.ankitsuda.rebound.ui.workout.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

const val UNORGANIZED_FOLDERS_ID = "my_templates"

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val workoutTemplatesRepository: WorkoutTemplatesRepository
) :
    ViewModel() {
    private var _currentWorkout = MutableStateFlow<Workout?>(null)
    val currentWorkout = _currentWorkout.asStateFlow()
//        .shareWhileObserved(viewModelScope)

    private var _currentWorkoutDurationStr = MutableStateFlow<String?>(null)
    val currentWorkoutDurationStr = _currentWorkoutDurationStr.asStateFlow()
//        .shareWhileObserved(viewModelScope)

    private val _unarchivedTemplates =
        workoutTemplatesRepository.getVisibleTemplatesWithWorkouts(archived = false)
//            .shareWhileObserved(viewModelScope)

    private val _folders = MutableStateFlow<List<WorkoutTemplatesFolder?>>(emptyList());
    private val folders = _folders.asStateFlow()

//    val groupedTemplates: Flow<List<Pair<WorkoutTemplatesFolder?, List<TemplateWithWorkout>>>> =
//        _unarchivedTemplates.combine(folders) { mTempWithWorkouts, mFolders ->
//            val list = arrayListOf<Pair<WorkoutTemplatesFolder?, List<TemplateWithWorkout>>>();
//            list.addAll(mFolders.map { f ->
//                Pair(f, mTempWithWorkouts.filter { t -> t.template.folderId == f?.id })
//            })
//            list.add(
//                Pair(
//                    if (list.isEmpty()) null else WorkoutTemplatesFolder(
//                        id = UNORGANIZED_FOLDERS_ID,
//                        name = "My Templates"
//                    ),
//                    mTempWithWorkouts.filter { t -> t.template.folderId == null })
//            )
//            list
//        }.shareWhileObserved(viewModelScope)

    private var _foldersExpandedStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap());
    val foldersExpandedStatus = _foldersExpandedStatus.asStateFlow()

    private val _items = MutableStateFlow<List<WorkoutScreenListItemModel>>(emptyList());
    val items = _items.asStateFlow()

    private var durationJob: Job? = null
    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            workoutsRepository.getCurrentWorkoutId().collectLatest {
                refreshCurrentWorkout(it)
            }
        }
        viewModelScope.launch {
            workoutTemplatesRepository.getFolders().collect {
                _folders.value = it
            }
        }


        viewModelScope.launch {
            combine(
                _folders,
                _unarchivedTemplates,
                _currentWorkoutDurationStr,
                _currentWorkout,
                _foldersExpandedStatus
            ) { mFolders, mTemplates, mDurationsStr, mCurrentWorkout, mFoldersExpandedStatus ->
                refreshItems(
                    mFolders,
                    mTemplates,
                    mDurationsStr,
                    mCurrentWorkout,
                    mFoldersExpandedStatus
                )
                true
            }.collect()
        }
    }

    private fun refreshCurrentWorkout(workoutId: String) {
        viewModelScope.launch {
            val workout = if (workoutId != NONE_WORKOUT_ID) {
                workoutsRepository.getWorkout(workoutId = workoutId).firstOrNull()
            } else {
                null
            }
            setupDurationUpdater(workout?.inProgress == true, workout?.startAt)

            _currentWorkout.emit(workout)
        }
    }

    private fun setupDurationUpdater(inProgress: Boolean, startAt: LocalDateTime?) {
        durationJob?.cancel()
        durationJob = viewModelScope.launch {
            while (inProgress && startAt != null) {
                _currentWorkoutDurationStr.emit(startAt.toReadableDuration())
                delay(25)
            }
        }
    }

    fun startEmptyWorkout() {
        viewModelScope.launch {
            val newWorkoutId = workoutsRepository.createWorkout(
                Workout(
                    id = "",
                    name = "${TimePeriod.now()} Workout",
                    startAt = LocalDateTime.now(),
                    completedAt = null,
                    inProgress = true,
                    isHidden = true
                )
            )
            workoutsRepository.setCurrentWorkoutId(newWorkoutId)
        }
    }

    fun createTemplate(folderId: String?, onCreated: (WorkoutTemplate) -> Unit) {
        viewModelScope.launch {
            val template = workoutTemplatesRepository.createTemplate(folderId = folderId)
            onCreated(template)
        }
    }

    fun startWorkoutFromTemplateId(
        templateId: String,
        discardActive: Boolean,
        onWorkoutAlreadyActive: () -> Unit
    ) {
        viewModelScope.launch {
            workoutsRepository.startWorkoutFromTemplate(
                templateId = templateId,
                discardActive = discardActive,
                onWorkoutAlreadyActive = onWorkoutAlreadyActive
            )
        }
    }

    fun deleteFolder(folderId: String) {
        viewModelScope.launch {
            workoutTemplatesRepository.deleteFolder(folderId)
        }
    }

    fun changeIsFolderExpanded(folderId: String, isExpanded: Boolean) {
        val newMap = _foldersExpandedStatus.value.toMutableMap()
        newMap[folderId] = isExpanded
        _foldersExpandedStatus.value = newMap
    }

    fun moveFolder(from: Int, to: Int) {
        viewModelScope.launch {
            val folderIndex1 = _folders.value.indexOfFirst { it?.id == (_items.value[from] as WorkoutScreenListItemFolderHeaderModel).folder.id }
            val folderIndex2 = _folders.value.indexOfFirst { it?.id == (_items.value[to] as WorkoutScreenListItemFolderHeaderModel).folder.id }

            try {
                val newList = _folders.value.toArrayList()
                newList.move(folderIndex1, folderIndex2)
                _folders.value = newList
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            workoutTemplatesRepository.updateFolderListOrder(_folders.last()[from]!!.id, to)
        }
    }

    private fun refreshItems(
        mFolders: List<WorkoutTemplatesFolder?>,
        mTemplates: List<TemplateWithWorkout>,
        mDurationsStr: String?,
        mCurrentWorkout: Workout?,
        mFoldersExpandedStatus: Map<String, Boolean>
    ) {
        Timber.d("refreshItems")

        val newItems = arrayListOf<WorkoutScreenListItemModel>()

        if (mCurrentWorkout != null) {
            newItems.add(
                WorkoutScreenListItemOngoingWorkoutModel(
                    durationStr = mDurationsStr
                )
            )
        }

        newItems.add(WorkoutScreenListItemHeaderModel)

        for (folder in mFolders) {
            if (folder == null) continue

            newItems.add(
                WorkoutScreenListItemFolderHeaderModel(
                    folder = folder
                )
            )

            if (!mFoldersExpandedStatus.getOrDefault(folder.id, true)) continue

            val temps = mTemplates.filter { t -> t.template.folderId == folder.id }

            if (temps.isEmpty()) {
                newItems.add(
                    WorkoutScreenListItemAddTemplateModel(
                        folderId = folder.id
                    )
                )
            } else {
                for (template in temps) {
                    newItems.add(
                        WorkoutScreenListItemTemplateModel(
                            templateWithWorkout = template
                        )
                    )
                }
            }
        }

        if (mFolders.isNotEmpty()) {
            newItems.add(
                WorkoutScreenListItemFolderHeaderModel(
                    folder = WorkoutTemplatesFolder(
                        id = UNORGANIZED_FOLDERS_ID,
                        name = "My Templates"
                    )
                )
            )
        }

        if (mFoldersExpandedStatus.getOrDefault(
                UNORGANIZED_FOLDERS_ID,
                true
            ) || mFolders.isEmpty()
        ) {
            for (template in mTemplates.filter { t -> t.template.folderId == null }) {
                newItems.add(
                    WorkoutScreenListItemTemplateModel(
                        templateWithWorkout = template
                    )
                )
            }
        }

        Timber.d("newItems $newItems")
        _items.value = newItems

    }

}