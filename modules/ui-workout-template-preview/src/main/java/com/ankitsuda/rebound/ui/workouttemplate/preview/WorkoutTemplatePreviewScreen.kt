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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ankitsuda.base.utils.toReadableDuration
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.TabRootScreen
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workouttemplate.preview.components.TemplateExerciseComponent
import me.onebone.toolbar.*
import me.onebone.toolbar.FabPosition
import kotlin.random.Random

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun WorkoutTemplatePreviewScreen(
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutTemplatePreviewScreenViewModel = hiltViewModel()
) {
    val template by viewModel.workoutTemplate.collectAsState(initial = null)
    val workout by viewModel.workout.collectAsState(initial = null)
    val entriesJunctions by viewModel.entriesJunctions.collectAsState(initial = emptyList())

    val templateName = workout?.name ?: ""
    val lastPerformedStr = template?.lastPerformedAt?.toString()

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    ToolbarWithFabScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = templateName,
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
                                    TabRootScreen.WorkoutTab
                                )
                            )
                        }
                    }
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
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = it,
                        style = ReboundTheme.typography.caption,
                        fontSize = 14.sp,
                        color = Color(158, 158, 158)
                    )
                }
            }
            items(entriesJunctions) {
                TemplateExerciseComponent(
                    name = "${it.logEntries.size} x ${it.exercise.name}",
                    muscle = ":/",
                    onClickInfo = {
                        navigator.navigate(
                            LeafScreen.ExerciseDetails.createRoute(
                                it.exercise.exerciseId,
                                TabRootScreen.WorkoutTab
                            )
                        )
                    },
                    onClick = {

                    }
                )
            }
        }
    }
}