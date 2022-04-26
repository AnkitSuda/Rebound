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

package com.ankitsuda.rebound.ui.components.workouteditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.domain.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.theme.ReboundTheme

@Composable
fun WorkoutEditorComponent(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    workoutName: String?,
    workoutNote: String?,
    cancelWorkoutButtonVisible: Boolean,
    logEntriesWithJunction: List<LogEntriesWithExerciseJunction>,
    onChangeWorkoutName: (String) -> Unit,
    onChangeWorkoutNote: (String) -> Unit,
    onAddExerciseToWorkout: (exerciseId: String) -> Unit,
    onCancelCurrentWorkout: () -> Unit,
    onDeleteExerciseFromWorkout: (LogEntriesWithExerciseJunction) -> Unit,
    onAddEmptySetToExercise: (setNumber: Int, exerciseWorkoutJunction: ExerciseWorkoutJunction) -> Unit,
    onDeleteLogEntry: (ExerciseLogEntry) -> Unit,
    onUpdateLogEntry: (ExerciseLogEntry) -> Unit,
    layoutAtTop: @Composable LazyItemScope.() -> Unit = {}
) {
    var mWorkoutName by rememberSaveable {
        mutableStateOf(workoutName ?: "")
    }
    var mWorkoutNote by rememberSaveable {
        mutableStateOf(workoutNote ?: "")
    }

    // Observes results when ExercisesScreen changes value of arg
    val exercisesScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String?>("result_exercises_screen_exercise_id")?.observeAsState()

    fun updateWorkoutName(newName: String) {
        mWorkoutName = newName
        onChangeWorkoutName(newName)
    }

    fun updateWorkoutNote(newNote: String) {
        mWorkoutNote = newNote
        onChangeWorkoutNote(newNote)
    }

    LaunchedEffect(key1 = exercisesScreenResult?.value) {
        exercisesScreenResult?.value?.let { resultId ->
            onAddExerciseToWorkout(resultId)

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
            layoutAtTop()
        }
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                AppTextField(
                    value = mWorkoutName,
                    onValueChange = { updateWorkoutName(it) },
                    placeholderValue = "Workout name",
                    modifier = Modifier.fillMaxWidth()
                )
                AppTextField(
                    value = mWorkoutNote,
                    onValueChange = { updateWorkoutNote(it) },
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
                    onUpdateLogEntry(updatedEntry)
                },
                onSwipeDelete = { entryToDelete ->
                    onDeleteLogEntry(entryToDelete)
                },
                onAddSet = {
                    onAddEmptySetToExercise(
                        try {
                            logEntriesWithJunctionItem.logEntries[logEntriesWithJunctionItem.logEntries.size - 1].setNumber!! + 1
                        } catch (e: Exception) {
                            1
                        },
                        logEntriesWithJunctionItem.junction
                    )
                },
                onDeleteExercise = {
                    onDeleteExerciseFromWorkout(logEntriesWithJunctionItem)
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

        if (cancelWorkoutButtonVisible) {
            item {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    onClick = {
                        onCancelCurrentWorkout()
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