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

package com.ankitsuda.rebound.ui.workout_panel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.util.toast
import com.ankitsuda.common.compose.LocalPanel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.workouteditor.WorkoutEditorComponent
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.components.workouteditor.workoutExerciseItemAlt
import com.ankitsuda.rebound.ui.workout_panel.components.WorkoutQuickInfo
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun WorkoutPanel(
    navController: NavHostController,
    navigator: Navigator = LocalNavigator.current,
) {
    WorkoutPanel1(navController, navigator)
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun WorkoutPanel1(
    navController: NavHostController,
    navigator: Navigator,
    viewModel: WorkoutPanelViewModel = hiltViewModel()
) {
    val currentWorkoutId by viewModel.currentWorkoutId.collectAsState(initial = NONE_WORKOUT_ID)
    val workout by viewModel.workout.collectAsState(null)
    val currentDurationStr by viewModel.currentDurationStr.collectAsState("")
    val currentVolumeStr by viewModel.currentVolumeStr.collectAsState("")
    val currentSetsStr by viewModel.currentSetsStr.collectAsState("")
    val logEntriesWithJunction by viewModel.logEntriesWithExerciseJunction.collectAsState()

//    val workoutName by viewModel.workoutName.collectAsState("")
//    val workoutNote by viewModel.workoutNote.collectAsState("")
    val workoutName = workout?.name
    val workoutNote = workout?.note

    if (currentWorkoutId != NONE_WORKOUT_ID && workout != null) {
        WorkoutEditorComponent(
            navController = navController,
            navigator = navigator,
            workoutName = workoutName,
            workoutNote = workoutNote,
            useReboundKeyboard = true,
            addNavigationBarPadding = true,
            cancelWorkoutButtonVisible = true,
            logEntriesWithJunction = logEntriesWithJunction,
            layoutAtTop = {
                Column(
                    modifier = Modifier.animateItemPlacement()
                ) {
                    WorkoutQuickInfo(
                        currentDurationStr = currentDurationStr,
                        currentVolumeStr = currentVolumeStr,
                        currentSetsStr = currentSetsStr
                    )
                    Divider()
                }
            },
            onChangeWorkoutName = {
                viewModel.updateWorkoutName(it)
            },
            onChangeWorkoutNote = {
                viewModel.updateWorkoutNote(it)
            },
            onAddExerciseToWorkout = {
                viewModel.addExerciseToWorkout(it)
            },
            onCancelCurrentWorkout = {
                viewModel.cancelCurrentWorkout()
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
            onUpdateWarmUpSets = { j, s ->
                viewModel.updateWarmUpSets(j, s)
            },
            onAddEmptyNote = viewModel::addEmptyNote,
            onChangeNote = viewModel::changeNote,
            onDeleteNote = viewModel::deleteNote,
            onAddToSuperset = viewModel::addToSuperset,
            onRemoveFromSuperset = viewModel::removeFromSuperset,
        )
    }
}
