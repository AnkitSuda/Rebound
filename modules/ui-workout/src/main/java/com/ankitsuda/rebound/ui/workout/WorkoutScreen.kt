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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.common.compose.LocalPanel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.TabRootScreen
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.dialogs.DiscardActiveWorkoutDialog
import com.ankitsuda.rebound.ui.components.dragdrop.rememberDragDropListState
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workout.components.FolderSection
import com.ankitsuda.rebound.ui.workout.components.TemplateItem
import com.ankitsuda.rebound.ui.workout.models.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.onebone.toolbar.*
import me.onebone.toolbar.FabPosition

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalToolbarApi::class
)
@Composable
fun WorkoutScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutScreenViewModel = hiltViewModel(),
) {
    var isDiscardActiveWorkoutDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isDiscardActiveWorkoutDialogTemplateId: String? by rememberSaveable {
        mutableStateOf(null)
    }
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val currentWorkout by viewModel.currentWorkout.collectAsState(null)
    val items by viewModel.items.collectAsState(emptyList())
    val foldersExpandedStatus by viewModel.foldersExpandedStatus.collectAsState(emptyMap())
    val coroutineScope = rememberCoroutineScope()
    val mainPanel = LocalPanel.current

    var overscrollJob by remember { mutableStateOf<Job?>(null) }

    val dragDropListState = rememberDragDropListState(
        onMove = { from, to ->
            val fromItem = items[from]

            when (val toItem = items[to]) {
                is WorkoutScreenListItemFolderHeaderModel -> {
                    when (fromItem) {
                        is WorkoutScreenListItemFolderHeaderModel -> {
                            viewModel.moveFolder(from, to)
                        }
                        is WorkoutScreenListItemTemplateModel -> {
                            if (fromItem.templateWithWorkout.template.folderId != toItem.folder.id) {
                                viewModel.moveTemplate(from, to)
                            }
                        }
                        else -> {}
                    }
                }
                is WorkoutScreenListItemTemplateModel -> {
                    if (fromItem is WorkoutScreenListItemTemplateModel) {
                        viewModel.moveTemplate(from, to)
                    }
                }
                is WorkoutScreenListItemAddTemplateModel -> {
                    if (fromItem is WorkoutScreenListItemTemplateModel) {
                        viewModel.moveTemplate(from, to)
                    }
                }
                else -> {}
            }
        },
        isIndexDraggable = { from, to ->
            val fromItem = items[from]

            when (val toItem = items[to]) {
                is WorkoutScreenListItemFolderHeaderModel -> {
                    when (fromItem) {
                        is WorkoutScreenListItemFolderHeaderModel -> {
                            viewModel.collapseAllFolders()
                            toItem.folder.id != UNORGANIZED_FOLDER_ID
                        }
                        is WorkoutScreenListItemTemplateModel -> {
                            fromItem.templateWithWorkout.template.folderId != toItem.folder.id
                        }
                        else -> {
                            false
                        }
                    }
                }
                is WorkoutScreenListItemTemplateModel -> {
                    fromItem is WorkoutScreenListItemTemplateModel
                }
                is WorkoutScreenListItemAddTemplateModel -> {
                    fromItem is WorkoutScreenListItemTemplateModel
                }
                else -> {
                    false
                }

            }
        },
    )

    LaunchedEffect(key1 = dragDropListState.currentElement) {
        if (dragDropListState.currentElement == null) {
            viewModel.clearDraggedItemTemplateId()
        }
    }

    fun expandPanel() {
        coroutineScope.launch {
            mainPanel.expand()
        }
    }

    fun createAndNavigateToTemplate(folderId: String? = null) {
        viewModel.createTemplate(folderId) {
            if (it.workoutId != null) {
                navigator.navigate(
                    LeafScreen.WorkoutEdit.createRoute(
                        workoutId = it.workoutId!!,
                        isTemplate = false,
                        root = TabRootScreen.WorkoutTab
                    )
                )
            }
        }
    }

    fun startWorkoutFromTemplateId(templateId: String, discardActive: Boolean) {
        viewModel.startWorkoutFromTemplateId(
            templateId = templateId,
            discardActive = discardActive,
            onWorkoutAlreadyActive = {
                isDiscardActiveWorkoutDialogTemplateId = templateId
                isDiscardActiveWorkoutDialogVisible = true
            }
        )
    }

    ToolbarWithFabScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Workout",
                toolbarState = collapsingState.toolbarState,
            )
        },
        fab = {

            if (currentWorkout == null) {
                ExtendedFloatingActionButton(
                    modifier = Modifier,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    text = { Text(text = "Empty Workout") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        viewModel.startEmptyWorkout()
                    })

            }
        },
        fabPosition = FabPosition.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {


        // User routines
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDrag = { change, offset ->
                            change.consumeAllChanges()
                            dragDropListState.onDrag(offset)

                            if (overscrollJob?.isActive == true)
                                return@detectDragGesturesAfterLongPress

                            dragDropListState
                                .checkForOverScroll()
                                .takeIf { it != 0f }
                                ?.let {
                                    overscrollJob = coroutineScope.launch {
                                        dragDropListState.lazyListState.scrollBy(it)
                                    }
                                }
                                ?: run { overscrollJob?.cancel() }
                        },
                        onDragStart = { offset -> dragDropListState.onDragStart(offset) },
                        onDragEnd = { dragDropListState.onDragInterrupted() },
                        onDragCancel = { dragDropListState.onDragInterrupted() }
                    )
                },
            state = dragDropListState.lazyListState,
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            itemsIndexed(items, key = { _, item ->
                when (item) {
                    is WorkoutScreenListItemOngoingWorkoutModel -> "ongoing_workout"
                    is WorkoutScreenListItemHeaderModel -> "header"
                    is WorkoutScreenListItemFolderHeaderModel -> item.folder.id
                    is WorkoutScreenListItemAddTemplateModel -> "add_template_${item.folderId}"
                    is WorkoutScreenListItemTemplateModel -> item.templateWithWorkout.template.id
                }
            }) { index, item ->
                when (item) {
                    is WorkoutScreenListItemOngoingWorkoutModel -> AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                            .animateItemPlacement(),
                        backgroundColor = ReboundTheme.colors.primary,
                        onClick = {
                            expandPanel()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val durationStr by item.durationStrFlow.collectAsState(initial = null)

                            Column() {
                                Text(
                                    text = "Ongoing Workout",
                                    style = ReboundTheme.typography.h6,
                                    color = ReboundTheme.colors.onPrimary
                                )
                                durationStr?.let {
                                    RSpacer(space = 4.dp)

                                    Text(
                                        text = it,
                                        style = ReboundTheme.typography.body2,
                                        color = ReboundTheme.colors.onPrimary.copy(alpha = 0.7f)

                                    )
                                }
                            }
                            IconButton(onClick = { expandPanel() }) {
                                Icon(
                                    imageVector = Icons.Outlined.OpenInFull,
                                    contentDescription = "Open",
                                    tint = ReboundTheme.colors.onPrimary
                                )
                            }
                        }
                    }
                    is WorkoutScreenListItemHeaderModel -> Column(
                        modifier = Modifier
                            .animateItemPlacement()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Templates", style = MaterialTheme.typography.body1,
                                color = LocalThemeState.current.onBackgroundColor
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp,
                                    bottom = 8.dp
//                                    bottom = if (groupedTemplates.size > 1) 4.dp else 8.dp
                                ),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RButtonStyle2(
                                modifier = Modifier.weight(1f),
                                text = "Folder",
                                icon = Icons.Outlined.CreateNewFolder,
                                onClick = {
                                    navigator.navigate(
                                        LeafScreen.TemplatesFolderEdit.createRoute()
                                    )
                                },
                            )
                            RButtonStyle2(
                                modifier = Modifier.weight(1f),
                                text = "Template",
                                icon = Icons.Outlined.Add,
                                onClick = {
                                    createAndNavigateToTemplate()
                                },
                            )
                        }
                    }
                    is WorkoutScreenListItemFolderHeaderModel -> {
                        val folderId = item.folder.id
                        val folderIdSafe = folderId ?: UNORGANIZED_FOLDER_ID
                        val isNullFolder = folderIdSafe == UNORGANIZED_FOLDER_ID

                        FolderSection(
                            folder = item.folder,
                            index = index,
                            dragDropListState = dragDropListState,
                            isExpanded = foldersExpandedStatus.getOrDefault(
                                folderIdSafe,
                                true
                            ),
                            onChangeExpanded = {
                                viewModel.changeIsFolderExpanded(folderIdSafe, it)
                            },
                            onAddTemplate = {
                                createAndNavigateToTemplate(folderId = if (folderId == UNORGANIZED_FOLDER_ID) null else folderId)
                            },
                            onDeleteFolder = {
                                if (!isNullFolder) {
                                    viewModel.deleteFolder(folderId!!)
                                }
                            },
                            onRenameFolder = {
                                if (!isNullFolder) {
                                    navigator.navigate(
                                        LeafScreen.TemplatesFolderEdit.createRoute(folderId)
                                    )
                                }
                            }
                        )
                    }
                    is WorkoutScreenListItemAddTemplateModel -> {
                        RDashedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .animateItemPlacement(),
                            text = "Add Template",
                            icon = Icons.Outlined.Add,
                            onClick = {
                                createAndNavigateToTemplate(folderId = item.folderId)
                            }
                        )
                    }
                    is WorkoutScreenListItemTemplateModel -> {
                        TemplateItem(
                            templateWithWorkout = item.templateWithWorkout,
                            dragDropListState = dragDropListState,
                            onClickPlay = {
                                startWorkoutFromTemplateId(
                                    templateId = item.templateWithWorkout.template.id,
                                    discardActive = false
                                )
                            },
                            onClick = {
                                navigator.navigate(
                                    LeafScreen.WorkoutTemplatePreview.createRoute(
                                        item.templateWithWorkout.template.id
                                    )
                                )
                            }
                        )
                    }
                    else -> {}
                }
            }

//            groupedTemplates.forEachIndexed { index, pair ->
//                val folderId = pair.first?.id
//                val folderIdSafe = folderId ?: UNORGANIZED_FOLDERS_ID
//                val isNullFolder = folderIdSafe == UNORGANIZED_FOLDERS_ID
//                folderSection(
//                    folder = pair.first,
//                    templates = pair.second,
//                    index = index,
//                    dragDropListState = dragDropListState,
//                    isExpanded = if (pair.first?.id == null) true else foldersExpandedStatus.getOrDefault(
//                        folderIdSafe,
//                        true
//                    ),
//                    onChangeExpanded = {
//                        viewModel.changeIsFolderExpanded(folderIdSafe, it)
//                    },
//                    onClickPlay = {
//                        startWorkoutFromTemplateId(
//                            templateId = it,
//                            discardActive = false
//                        )
//                    },
//                    onClickTemplate = {
//                    },
//                    onAddTemplate = {
//                        createAndNavigateToTemplate(folderId = if (folderId == UNORGANIZED_FOLDERS_ID) null else folderId)
//                    },
//                    onDeleteFolder = {
//                        if (!isNullFolder) {
//                            viewModel.deleteFolder(folderId!!)
//                        }
//                    },
//                    onRenameFolder = {
//                        if (!isNullFolder) {
//                            navigator.navigate(
//                                LeafScreen.TemplatesFolderEdit.createRoute(folderId!!)
//                            )
//                        }
//                    }
//                )
//            }
        }

    }

    if (isDiscardActiveWorkoutDialogVisible && isDiscardActiveWorkoutDialogTemplateId != null) {
        DiscardActiveWorkoutDialog(
            onClickDiscard = {
                isDiscardActiveWorkoutDialogVisible = false
                startWorkoutFromTemplateId(
                    discardActive = true,
                    templateId = isDiscardActiveWorkoutDialogTemplateId!!
                )
            },
            onDismissRequest = {
                isDiscardActiveWorkoutDialogTemplateId = null
                isDiscardActiveWorkoutDialogVisible = false
            }
        )
    }
}