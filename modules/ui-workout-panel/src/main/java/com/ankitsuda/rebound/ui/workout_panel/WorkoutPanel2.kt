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
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.Workout
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.ankitsuda.rebound.ui.components.workouteditor.workoutExerciseItemAlt
import com.ankitsuda.rebound.ui.workout_panel.components.WorkoutQuickInfo
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import timber.log.Timber
import java.time.LocalDateTime

@Composable
fun WorkoutPanel2(
    navController: NavHostController,
    navigator: Navigator,
    viewModel: WorkoutPanelViewModel2 = hiltViewModel()
) {
    val workout by viewModel.workout.collectAsState(
        Workout(
            id = generateId(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    )
    val logEntriesWithJunction = viewModel.logEntriesWithExerciseJunction

    val workoutName = workout?.name ?: ""
    val workoutNote = workout?.note ?: ""

    // Observes results when ExercisesScreen changes value of arg
    val exercisesScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String?>("result_exercises_screen_exercise_id")?.observeAsState()
    exercisesScreenResult?.value?.let { resultId ->

        viewModel.addExerciseToWorkout(resultId)

        navController.currentBackStackEntry?.savedStateHandle?.set(
            "result_exercises_screen_exercise_id",
            null
        )

    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ReboundTheme.colors.background)
    ) {
        item {
            Column() {
//                WorkoutQuickInfo()
                Divider()
            }
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
                onUpdateWarmUpSets = {},
                onValuesUpdated = { updatedEntry ->
                    viewModel.updateLogEntry(updatedEntry)
                },
                onSwipeDelete = { entryToDelete ->
                    Timber.d("Swiped entry $entryToDelete")
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
                onClick = {

                    navigator.navigate(LeafScreen.ExercisesBottomSheet().route)
//                    navController.navigate(Route.ExercisesBottomSheet.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
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
                onClick = {
                    viewModel.cancelCurrentWorkout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
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