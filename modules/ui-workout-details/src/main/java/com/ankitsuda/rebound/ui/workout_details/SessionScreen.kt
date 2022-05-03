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

package com.ankitsuda.rebound.ui.workout_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.utils.toDurationStr
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.Screen
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.dialogs.DiscardActiveWorkoutDialog
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workout_details.components.SessionMenuComponent
import me.onebone.toolbar.FabPosition
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold

@Composable
fun SessionScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: SessionScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val logs by viewModel.logs.collectAsState(emptyList())
    val workout by viewModel.workout.collectAsState(null)
    val totalVolume by viewModel.totalVolume.collectAsState(0.0)

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    var isDiscardActiveWorkoutDialogVisible by rememberSaveable {
        mutableStateOf(false)
    }

    fun startWorkout(discardActive: Boolean) {
        viewModel.startWorkout(
            discardActive = discardActive,
            onWorkoutAlreadyActive = {
                isDiscardActiveWorkoutDialogVisible = true
            }
        )
    }

    fun deleteWorkout() {
        viewModel.deleteWorkout(
            onDeleted = {
                navigator.goBack()
            }
        )
    }



    ToolbarWithFabScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        fab = {
            ExtendedFloatingActionButton(
                modifier = Modifier,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                text = { Text(text = "Perform Again") },
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
        toolbar = {
            TopBar2(
                title = workout?.name ?: "Workout",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(
                        icon = Icons.Outlined.MoreVert,
                        title = "Open Menu",
                        onClick = { menuExpanded = true }
                    )
                    SessionMenuComponent(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        onDelete = {
                            deleteWorkout()
                        }
                    )
                })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (!workout?.personalRecords.isNullOrEmpty()) {
                item {
                    PersonalRecordsRowComponent(
                        modifier = Modifier,
                        prs = workout?.personalRecords!!
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column() {
                        Text(
                            text = "${logs.size} Exercises",
                            color = LocalThemeState.current.onBackgroundColor
                        )
                        RSpacer(space = 4.dp)
                        SessionCompleteQuickInfo(
                            time = workout?.getDuration()?.toDurationStr() ?: "NA",
                            volume = "${totalVolume.toReadableString()} kg",
                            prs = 2
                        )
                    }
                    IconButton(onClick = {
                        workout?.id?.let {
                            navigator.navigate(LeafScreen.WorkoutEdit.createRoute(workoutId = it))
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit session",
                            tint = ReboundTheme.colors.primary
                        )
                    }
                }
            }

            for (log in logs) {
                item() {
                    SessionExerciseCardItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        onClick = { },
                        title = log.exercise.name ?: "",
                        entries = log.logEntries
                    )
                }
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