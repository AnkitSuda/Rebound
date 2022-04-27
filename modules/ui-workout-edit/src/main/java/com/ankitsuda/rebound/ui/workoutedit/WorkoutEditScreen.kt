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

package com.ankitsuda.rebound.ui.workoutedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.components.workouteditor.WorkoutEditorComponent
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun WorkoutEditScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutEditScreenViewModel = hiltViewModel()
) {
    val workout by viewModel.workout.collectAsState(null)
    val logEntriesWithJunction by viewModel.logEntriesWithExerciseJunction.collectAsState()

    val workoutName = workout?.name
    val workoutNote = workout?.note

    val collapsingState = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ReboundTheme.colors.background),
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar2(
                title = "Edit workout",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navigator.goBack()
                    }
                },
                actions = {
                    TopBarIconButton(icon = Icons.Outlined.MoreVert, title = "Open Menu") {

                    }
                })

        }) {
        WorkoutEditorComponent(
            navController = navController,
            navigator = navigator,
            workoutName = workoutName,
            workoutNote = workoutNote,
            cancelWorkoutButtonVisible = false,
            logEntriesWithJunction = logEntriesWithJunction,
            onChangeWorkoutName = {
                viewModel.updateWorkoutName(it)
            },
            onChangeWorkoutNote = {
                viewModel.updateWorkoutNote(it)
            },
            onAddExerciseToWorkout = {
                viewModel.addExerciseToWorkout(it)
            },
            onDeleteExerciseFromWorkout = {
                viewModel.deleteExerciseFromWorkout(it)
            },
            onAddEmptySetToExercise = { setNumber, exerciseWorkoutJunction ->
                viewModel.addEmptySetToExercise(
                    setNumber = setNumber,
                    exerciseWorkoutJunction = exerciseWorkoutJunction
                )
            },
            onDeleteLogEntry = {
                viewModel.deleteLogEntry(it)
            },
            onUpdateLogEntry = {
                viewModel.updateLogEntry(it)
            },
            onCancelCurrentWorkout = {}
        )
    }
}