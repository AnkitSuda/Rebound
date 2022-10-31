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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.common.compose.SAFE_RS_KEYBOARD_HEIGHT
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.ExerciseLogEntry
import com.ankitsuda.rebound.domain.entities.ExerciseSetGroupNote
import com.ankitsuda.rebound.domain.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.domain.entities.LogEntriesWithExerciseJunction
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.workouteditor.warmupcalculator.toExerciseLogEntries
import com.ankitsuda.rebound.ui.keyboard.LocalReboundSetKeyboard
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.insets.LocalWindowInsets

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun WorkoutEditorComponent(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    useReboundKeyboard: Boolean = false,
    addNavigationBarPadding: Boolean = false,
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
    onUpdateWarmUpSets: (LogEntriesWithExerciseJunction, List<ExerciseLogEntry>) -> Unit,
    onAddEmptyNote: (LogEntriesWithExerciseJunction) -> Unit,
    onDeleteNote: (ExerciseSetGroupNote) -> Unit,
    onChangeNote: (ExerciseSetGroupNote) -> Unit,
    layoutAtTop: @Composable LazyItemScope.() -> Unit = {}
) {
    // Observes results when ExercisesScreen changes value of arg
    val exercisesScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String?>("result_exercises_screen_exercise_id")?.observeAsState()

    val navigationBarHeight =
        with(LocalDensity.current) { if (addNavigationBarPadding) LocalWindowInsets.current.navigationBars.bottom.toDp() else 0.dp }

    LaunchedEffect(key1 = exercisesScreenResult?.value) {
        exercisesScreenResult?.value?.let { resultId ->
            onAddExerciseToWorkout(resultId)

            navController.currentBackStackEntry?.savedStateHandle?.set(
                "result_exercises_screen_exercise_id",
                null
            )
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val reboundKeyboard = LocalReboundSetKeyboard.current
    fun hideKeyboard() {
        try {
            keyboardController?.hide()
            reboundKeyboard.hide()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ReboundTheme.colors.background),
        contentPadding = PaddingValues(
            bottom =
            SAFE_RS_KEYBOARD_HEIGHT + navigationBarHeight
        )
    ) {
        item(key = "workout_panel_layout_at_top") {
            layoutAtTop()
        }
        item(key = "workout_panel_basic_text_fields") {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .animateItemPlacement(),
            ) {
                AppTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = workoutName ?: "",
                    onValueChange = { onChangeWorkoutName(it) },
                    placeholderValue = "Workout name"
                )
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    value = workoutNote ?: "",
                    onValueChange = { onChangeWorkoutNote(it) },
                    placeholderValue = "Workout note"
                )
            }
        }

        for (logEntriesWithJunctionItem in logEntriesWithJunction) {
            workoutExerciseItemAlt(
                useReboundKeyboard = useReboundKeyboard,
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
                },
                onUpdateWarmUpSets = {
                    onUpdateWarmUpSets(logEntriesWithJunctionItem, it.toExerciseLogEntries())
                },
                onAddEmptyNote = {
                    onAddEmptyNote(logEntriesWithJunctionItem)
                },
                onChangeNote = onChangeNote,
                onDeleteNote = onDeleteNote
            )
        }

        item(key = "workout_panel_add_exercise_button") {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .animateItemPlacement(),
                onClick = {
                    hideKeyboard()
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
            item(key = "workout_panel_cancel_workout_button") {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .animateItemPlacement(),
                    onClick = {
                        hideKeyboard()
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