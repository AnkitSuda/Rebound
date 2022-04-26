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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.common.compose.LocalPanel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.workout_panel.common.components.workoutExerciseItemAlt
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
    val logEntriesWithJunction by viewModel.logEntriesWithExerciseJunction.collectAsState()

//    val mainPanel = LocalPanel.current
//    val coroutine = rememberCoroutineScope()

//    BackHandler(mainPanel.isExpanded) {
//        coroutine.launch {
//            mainPanel.collapse()
//        }
//    }

    if (currentWorkoutId != NONE_WORKOUT_ID) {

        val workoutName = workout?.name ?: ""
        val workoutNote = workout?.note ?: ""

        // Observes results when ExercisesScreen changes value of arg
        val exercisesScreenResult = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String?>("result_exercises_screen_exercise_id")?.observeAsState()

        LaunchedEffect(key1 = exercisesScreenResult?.value) {
            exercisesScreenResult?.value?.let { resultId ->
                viewModel.addExerciseToWorkout(resultId)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "result_exercises_screen_exercise_id",
                    null
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ReboundTheme.colors.background)
        ) {
            item {
                Column() {
                    WorkoutQuickInfo(currentDurationStr = currentDurationStr)
                    Divider()
                }
            }
            item {
                Text(text = "TEST: current workout id $currentWorkoutId")
            }
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    AppTextField(
                        value = workoutName,
                        onValueChange = { viewModel.updateWorkoutName(it) },
                        placeholderValue = "Workout name",
                        modifier = Modifier.fillMaxWidth()
                    )
                    AppTextField(
                        value = workoutNote,
                        onValueChange = { viewModel.updateWorkoutNote(it) },
                        placeholderValue = "Workout note",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }

            for (logEntriesWithJunctionItem in logEntriesWithJunction) {
                workoutExerciseItemAlt(
                    logEntriesWithJunction = logEntriesWithJunctionItem,
                    onValuesUpdated = { updatedEntry ->
                        viewModel.updateLogEntry(updatedEntry)
                    },
                    onSwipeDelete = { entryToDelete ->
                        viewModel.deleteLogEntry(entryToDelete)
                    },
                    onAddSet = {
                        viewModel.addEmptySetToExercise(
                            try {
                                logEntriesWithJunctionItem.logEntries[logEntriesWithJunctionItem.logEntries.size - 1].setNumber!! + 1
                            } catch (e: Exception) {
                                1
                            },
                            logEntriesWithJunctionItem.junction
                        )
                    },
                    onDeleteExercise = {
                        viewModel.deleteExerciseFromWorkout(logEntriesWithJunctionItem)
                    }
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {

                        navigator.navigate(LeafScreen.ExercisesBottomSheet().route)
//                    navController.navigate(Route.ExercisesBottomSheet.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                        tint = MaterialTheme.colors.onPrimary
                    )
                    Text(text = "Add Exercise", style = MaterialTheme.typography.button)
                }
            }

            item {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {
                        viewModel.cancelCurrentWorkout()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.Red
                    )
                    Text(
                        text = "Cancel Workout",
                        style = MaterialTheme.typography.button,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkoutQuickInfo(currentDurationStr: String) {

    FlowRow(
        mainAxisAlignment = MainAxisAlignment.SpaceEvenly,
        mainAxisSize = SizeMode.Expand,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
    ) {
//        repeat(3) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = currentDurationStr,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                text = "Duration",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                color = Color(117, 117, 117),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
//        }
    }
}