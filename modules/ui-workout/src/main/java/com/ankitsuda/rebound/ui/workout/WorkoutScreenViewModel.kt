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
import com.ankitsuda.base.utils.extensions.shareWhileObserved
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

const val UNORGANIZED_FOLDER_ID = "my_templates"

@HiltViewModel
class WorkoutScreenViewModel @Inject constructor(
    private val workoutsRepository: WorkoutsRepository,
    private val workoutTemplatesRepository: WorkoutTemplatesRepository
) :
    ViewModel() {
    private var _currentWorkout = MutableStateFlow<Workout?>(null)
    val currentWorkout = _currentWorkout.asStateFlow()

    private var _currentWorkoutDurationStr = MutableStateFlow<String?>(null)

    private val _unarchivedTemplates = MutableStateFlow<List<TemplateWithWorkout>>(emptyList())

    private val _folders = MutableStateFlow<List<WorkoutTemplatesFolder?>>(emptyList())

    private var _foldersExpandedStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap());
    val foldersExpandedStatus = _foldersExpandedStatus.asStateFlow()

    private val _items = MutableStateFlow<List<WorkoutScreenListItemModel>>(emptyList());
    val items = _items.asStateFlow()

    private var durationJob: Job? = null
    private var refreshJob: Job? = null

    private var _currentDraggedTemplateId: String? = null

    init {
        viewModelScope.launch {
            workoutsRepository.getCurrentWorkoutId().collectLatest {
                Timber.d("Current Workout Updated In Local DB")
                refreshCurrentWorkout(it)
            }
        }
        viewModelScope.launch {
            workoutTemplatesRepository.getFolders().collect {
                Timber.d("Folders Updated In Local DB")
                val mFolders = arrayListOf<WorkoutTemplatesFolder?>()
                mFolders.addAll(it)
                if (mFolders.isNotEmpty()) {
                    mFolders.add(
                        WorkoutTemplatesFolder(
                            id = UNORGANIZED_FOLDER_ID,
                            name = "My Templates"
                        )
                    )
                }
                val newExpandedStatusMap = _foldersExpandedStatus.value.toMutableMap()
                for (folder in mFolders) {
                    newExpandedStatusMap.putIfAbsent(folder!!.id, true)
                }
                _foldersExpandedStatus.value = newExpandedStatusMap
                _folders.value = mFolders.toList()
            }
        }
        viewModelScope.launch {
            workoutTemplatesRepository.getVisibleTemplatesWithWorkouts(archived = false).collect {
                _unarchivedTemplates.value = it.sortedWith(
                    compareBy(
                        { t -> t.template.folderId },
                        { t -> t.template.listOrder }
                    )
                )
            }
        }

        viewModelScope.launch {
            combine(
                _folders.shareWhileObserved(viewModelScope).distinctUntilChanged(),
                _unarchivedTemplates.shareWhileObserved(viewModelScope).distinctUntilChanged(),
                _currentWorkout.shareWhileObserved(viewModelScope).distinctUntilChanged(),
                _foldersExpandedStatus.shareWhileObserved(viewModelScope).distinctUntilChanged()
            ) { mFolders, mTemplates, mCurrentWorkout, mFoldersExpandedStatus ->
                refreshItems(
                    mFolders,
                    mTemplates,
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
            val folderIndex1 =
                _folders.value.indexOfFirst { it?.id == (_items.value[from] as WorkoutScreenListItemFolderHeaderModel).folder.id }
            val folderIndex2 =
                _folders.value.indexOfFirst { it?.id == (_items.value[to] as WorkoutScreenListItemFolderHeaderModel).folder.id }

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

    fun moveTemplate(from: Int, to: Int) {
        viewModelScope.launch {
            val mItems = _items.value
            val template =
                (mItems[from] as WorkoutScreenListItemTemplateModel).templateWithWorkout.template
            when (val toItem = mItems[to]) {
                is WorkoutScreenListItemAddTemplateModel -> {

                }
                is WorkoutScreenListItemFolderHeaderModel -> {
                }
                is WorkoutScreenListItemTemplateModel -> {
                    if (template.folderId == toItem.templateWithWorkout.template.folderId) {
//                        val newTemplates = arrayListOf<TemplateWithWorkout>()
//
//                        newTemplates.addAll(_unarchivedTemplates.value.sortedBy { it.template.folderId })

//                        val folderTemplateStartIndex =
//                            newTemplates.indexOfFirst { it.template.folderId == template.folderId }
//                        val folderTemplateEndIndex =
//                            newTemplates.indexOfLast { it.template.folderId == template.folderId }

//                        val prevIndex = newTemplates.indexOfFirst { it.template.id == template.id }
//                        val newIndex = newTemplates.indexOfFirst { it.template.id == template.id }

                        var newList: ArrayList<TemplateWithWorkout>? = null

                        var moveFrom = from
                        var moveTo = to

                        val mFolders = _folders.value
                        val mTemplates = _unarchivedTemplates.value

                        Timber.d("Before ${mTemplates.map { it.template.id }}")

//                        if (mFolders.isEmpty()) {
                        newList = mTemplates.toArrayList()
                        newList = newList.sortedWith(
                            compareBy(
                                { it.template.folderId },
                                { it.template.listOrder }
                            )
                        ).toArrayList()
                        newList.move(
                            mTemplates.indexOfFirst { it.template.id == template.id },
                            mTemplates.indexOfFirst { it.template.id == toItem.templateWithWorkout.template.id }
                        )

                        var newListOrder = 0
                        newList.toList().forEachIndexed { index, templateWithWorkout ->
                            if (templateWithWorkout.template.folderId == template.folderId) {
                                newList!![index].template.listOrder = newListOrder
                                newListOrder++
                            }
                        }
//                        } else {
//
//                            val folderId = template.folderId ?: UNORGANIZED_FOLDER_ID
//
//                            val folder =
//                                _folders.value.indexOfFirst {
//                                    (it?.id ?: UNORGANIZED_FOLDER_ID) == folderId
//                                }
//
//                            _currentDraggedTemplateId = template.id
//
////                        var folderIndex = -1
////                        var folderTemplatesStartIndex = -1
////                        var folderTemplatesEndIndex = -1
////
////
////                        mItems.forEachIndexed { itemIndex, mItem ->
////                            val nextItem = mItems.getOrNull(itemIndex + 1)
////                            when (mItem) {
////                                is WorkoutScreenListItemFolderHeaderModel -> {
////                                    if (mItem.folder.id == folderId) {
////                                        folderIndex = itemIndex
////
////                                        if (nextItem != null && nextItem is WorkoutScreenListItemTemplateModel
////                                        ) {
////                                            folderTemplatesStartIndex = itemIndex + 1
////                                        }
////                                    } else {
////
////                                    }
////                                }
////                                is WorkoutScreenListItemTemplateModel -> {
////                                    if (nextItem == null) {
////                                        folderTemplatesEndIndex = itemIndex
////                                    }
////                                }
////                                else -> {}
////                            }
////                        }
//
////                        val templateForFolderStartingIndex =
////                            mTemplates.indexOfFirst { it.template.folderId == folderId }
////                        val templateForFolderEndingIndex =
////                            mTemplates.indexOfLast { it.template.folderId == folderId }
////                        val totalTemplatesInFolder =
////                            mTemplates.count { it.template.folderId == folderId }
//
//                            moveFrom = mTemplates.indexOfFirst { it.template.folderId == folderId }
//                            moveTo =
//                                mTemplates.indexOfFirst { toItem.templateWithWorkout.template.folderId == folderId }
//                        }

                        Timber.d("moveFrom $moveFrom moveTo $moveTo")

                        try {
                            if (newList == null) {
                                newList = mTemplates.toArrayList()
//                            val foldersSize = _folders.value.size
//                            newList.move(from - foldersSize - 1, to - foldersSize - 1)
                                newList.sortBy { it.template.listOrder }
                                newList.move(moveFrom, moveTo)
                                newList[moveFrom].template.listOrder = moveTo
                                newList[moveTo].template.listOrder = moveFrom
                            }
//                            newList.forEachIndexed { index, _ ->
//                                newList[index].template.listOrder = index
//                            }

                            Timber.d("After ${newList.map { it.template.id }}")
                            _unarchivedTemplates.value = newList
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                else -> {}
            }
        }
    }


    private fun refreshItems(
    ) {
        refreshItems(
            mFolders = _folders.value,
            mTemplates = _unarchivedTemplates.value,
            mCurrentWorkout = _currentWorkout.value,
            mFoldersExpandedStatus = _foldersExpandedStatus.value,
        )
    }

    private fun refreshItems(
        mFolders: List<WorkoutTemplatesFolder?>,
        mTemplates: List<TemplateWithWorkout>,
        mCurrentWorkout: Workout?,
        mFoldersExpandedStatus: Map<String, Boolean>
    ) {
        Timber.d("refreshItems")

        val newItems = arrayListOf<WorkoutScreenListItemModel>()

        if (mCurrentWorkout != null) {
            newItems.add(
                WorkoutScreenListItemOngoingWorkoutModel(
                    durationStrFlow = _currentWorkoutDurationStr.asStateFlow()
                        .shareWhileObserved(viewModelScope)
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

            if (folder.id == UNORGANIZED_FOLDER_ID) continue

            val temps = mTemplates.filter { t -> t.template.folderId == folder.id }.toArrayList()

            if (!mFoldersExpandedStatus.getOrDefault(folder.id, true)) {
                val templateToKeep = temps.find { it.template.id == _currentDraggedTemplateId }

                if (templateToKeep != null) {
                    temps.clear()
                    temps.add(templateToKeep)
                } else {
                    continue
                }
            }


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

        if (mFoldersExpandedStatus.getOrDefault(
                UNORGANIZED_FOLDER_ID,
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

        Timber.d(
            "newItems templateIds ${
                newItems.filterIsInstance<WorkoutScreenListItemTemplateModel>()
                    .map { (it as WorkoutScreenListItemTemplateModel).templateWithWorkout.template.id }
            }"
        )
        Timber.d("newItems $newItems")
        _items.value = newItems

    }

    fun collapseAllFolders() {
        val newMap = _foldersExpandedStatus.value.toMutableMap()
        newMap.replaceAll { _, _ ->
            false
        }
        _foldersExpandedStatus.value = newMap
    }

    fun clearDraggedItemTemplateId() {
        _currentDraggedTemplateId = null
        refreshItems()
    }

}