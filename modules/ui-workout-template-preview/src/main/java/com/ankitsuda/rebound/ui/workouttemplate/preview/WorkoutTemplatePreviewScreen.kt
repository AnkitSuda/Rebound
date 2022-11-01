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

package com.ankitsuda.rebound.ui.workouttemplate.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.TabRootScreen
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.dialogs.DiscardActiveWorkoutDialog
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workouttemplate.preview.components.TemplateExerciseComponent
import com.ankitsuda.rebound.ui.workouttemplate.preview.components.TemplateMenuComponent
import me.onebone.toolbar.*
import me.onebone.toolbar.FabPosition
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun WorkoutTemplatePreviewScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutTemplatePreviewScreenViewModel = hiltViewModel()
) {
    val template by viewModel.workoutTemplate.collectAsState(initial = null)
    val workout by viewModel.workout.collectAsState(initial = null)
    val entriesInfo by viewModel.entriesInfo.collectAsState(initial = emptyList())

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    var isDiscardActiveWorkoutDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val templateName = workout?.name ?: ""
    val lastPerformedStr = template?.lastPerformedAt?.format(
        DateTimeFormatter.ofLocalizedDateTime(
            FormatStyle.MEDIUM,
            FormatStyle.SHORT
        )
    )

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    fun deleteTemplate() {
        viewModel.deleteTemplate {
            navigator.goBack()
        }
    }

    fun startWorkout(discardActive: Boolean) {
        viewModel.startWorkout(
            discardActive = discardActive,
            onWorkoutAlreadyActive = {
                isDiscardActiveWorkoutDialogVisible = true
            }
        )
    }

    ToolbarWithFabScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = templateName.ifBlank { "Unnamed Template" },
                italicTitle = templateName.isBlank(),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.Edit, title = "Edit") {
                        workout?.id?.let {
                            navigator.navigate(
                                LeafScreen.WorkoutEdit.createRoute(
                                    workoutId = it,
                                    isTemplate = true,
                                    root = TabRootScreen.WorkoutTab
                                )
                            )
                        }
                    }
                    TopBarIconButton(icon = Icons.Outlined.MoreVert, title = "More") {
                        menuExpanded = true
                    }
                    TemplateMenuComponent(
                        expanded = menuExpanded,
                        isArchived = template?.isArchived ?: false,
                        onDismissRequest = { menuExpanded = false },
                        onDeleteTemplate = { deleteTemplate() },
                        onToggleArchiveTemplate = {
                            viewModel.toggleIsArchived()
                        })
                })
        },
        fab = {
            ExtendedFloatingActionButton(
                modifier = Modifier,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                text = { Text(text = "Start Workout") },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = null
                    )
                },
                onClick = {
                    startWorkout(discardActive = false)
                })
        },
        fabPosition = FabPosition.Center,
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            lastPerformedStr?.let {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Last performed at",
                            style = ReboundTheme.typography.caption.copy(
                                color = ReboundTheme.colors.onBackground.copy(
                                    0.75f
                                )
                            ),
                            fontSize = 14.sp,
                        )
                        Text(
                            text = it,
                            style = ReboundTheme.typography.caption.copy(color = ReboundTheme.colors.onBackground),
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            items(entriesInfo) {
                TemplateExerciseComponent(
                    name = "${it.logEntries.size} x ${it.exercise.name}",
                    muscle = it.primaryMuscle.name,
                    onClickInfo = {
                        navigator.navigate(
                            LeafScreen.ExerciseDetails.createRoute(
                                it.exercise.exerciseId,
                                TabRootScreen.WorkoutTab
                            )
                        )
                    }
                )
            }
        }
    }

    if (isDiscardActiveWorkoutDialogVisible) {
        DiscardActiveWorkoutDialog(
            onClickDiscard = {
                isDiscardActiveWorkoutDialogVisible = false
                startWorkout(discardActive = true)
            },
            onDismissRequest = {
                isDiscardActiveWorkoutDialogVisible = false
            }
        )
    }
}