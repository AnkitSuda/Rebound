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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workout.components.folderSection
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
    val currentWorkout by viewModel.currentWorkout.collectAsState(initial = null)
    val currentWorkoutDurationStr by viewModel.currentWorkoutDurationStr.collectAsState(initial = null)
    val groupedTemplates by viewModel.groupedTemplates.collectAsState(emptyList())
    val foldersExpandedStatus by viewModel.foldersExpandedStatus.collectAsState(emptyMap())
    val coroutine = rememberCoroutineScope()
    val mainPanel = LocalPanel.current

    fun expandPanel() {
        coroutine.launch {
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
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            if (currentWorkout != null) {
                item("current_workout_overview") {
                    AppCard(
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
                            Column() {
                                Text(
                                    text = "Ongoing Workout",
                                    style = ReboundTheme.typography.h6,
                                    color = ReboundTheme.colors.onPrimary
                                )
                                currentWorkoutDurationStr?.let {
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
                }
            }

            item(key = "templates_header") {
                Column(
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
                                bottom = if (groupedTemplates.size > 1) 4.dp else 8.dp
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
            }

            for (pair in groupedTemplates) {
                val folderId = pair.first?.id
                val folderIdSafe = folderId ?: UNORGANIZED_FOLDERS_ID
                val isNullFolder = folderIdSafe == UNORGANIZED_FOLDERS_ID
                folderSection(
                    folder = pair.first,
                    templates = pair.second,
                    isExpanded = if (pair.first?.id == null) true else foldersExpandedStatus.getOrDefault(
                        folderIdSafe,
                        true
                    ),
                    onChangeExpanded = {
                        viewModel.changeIsFolderExpanded(folderIdSafe, it)
                    },
                    onClickPlay = {
                        startWorkoutFromTemplateId(
                            templateId = it,
                            discardActive = false
                        )
                    },
                    onClickTemplate = {
                        navigator.navigate(LeafScreen.WorkoutTemplatePreview.createRoute(it))
                    },
                    onAddTemplate = {
                        createAndNavigateToTemplate(folderId = if (folderId == UNORGANIZED_FOLDERS_ID) null else folderId)
                    },
                    onDeleteFolder = {
                        if (!isNullFolder) {
                            viewModel.deleteFolder(folderId!!)
                        }
                    },
                    onRenameFolder = {
                        if (!isNullFolder) {
                            navigator.navigate(
                                LeafScreen.TemplatesFolderEdit.createRoute(folderId!!)
                            )
                        }
                    }
                )
            }
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