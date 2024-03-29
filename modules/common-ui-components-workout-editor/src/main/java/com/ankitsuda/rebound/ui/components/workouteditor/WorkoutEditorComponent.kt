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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.common.compose.SAFE_RS_KEYBOARD_HEIGHT
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.domain.entities.*
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.workouteditor.barbellselector.models.BarbellSelectorResult
import com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.models.SupersetSelectorResult
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
    barbells: List<Barbell>,
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
    onAddToSuperset: (junctionId: String, supersetId: Int) -> Unit,
    onRemoveFromSuperset: (LogEntriesWithExerciseJunction) -> Unit,
    onUpdateBarbell: (junctionId: String, barbellId: String) -> Unit,
    layoutAtTop: @Composable LazyItemScope.() -> Unit = {}
) {
    // Observes results when ExercisesScreen changes value of arg
    val exercisesScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>(RESULT_EXERCISES_SCREEN_EXERCISE_ID, null)?.collectAsState()

    // Observes results when Superset Selector changes value of arg
    val supersetSelectorResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<SupersetSelectorResult?>(RESULT_SUPERSET_SELECTOR_SUPERSET_ID_KEY, null)
        ?.collectAsState()

    // Observes results when Barbell Selector changes value of arg
    val barbellSelectorResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<BarbellSelectorResult?>(RESULT_BARBELL_SELECTOR_KEY, null)
        ?.collectAsState()

    val navigationBarHeight =
        with(LocalDensity.current) { if (addNavigationBarPadding) LocalWindowInsets.current.navigationBars.bottom.toDp() else 0.dp }

    LaunchedEffect(key1 = exercisesScreenResult?.value) {
        exercisesScreenResult?.value?.let { resultId ->
            onAddExerciseToWorkout(resultId)

            navController.currentBackStackEntry?.savedStateHandle?.set(
                RESULT_EXERCISES_SCREEN_EXERCISE_ID,
                null
            )
        }
    }

    LaunchedEffect(key1 = supersetSelectorResult?.value) {
        supersetSelectorResult?.value?.let { result ->

            onAddToSuperset(result.toJunctionId, result.supersetId)

            if (!logEntriesWithJunction.any { it.junction.id == result.selectedFromJunctionId && it.junction.supersetId == result.supersetId }) {
                onAddToSuperset(result.selectedFromJunctionId, result.supersetId)
            }

            navController.currentBackStackEntry?.savedStateHandle?.set(
                RESULT_SUPERSET_SELECTOR_SUPERSET_ID_KEY,
                null
            )
        }
    }

    LaunchedEffect(key1 = barbellSelectorResult?.value) {
        barbellSelectorResult?.value?.let { result ->
            onUpdateBarbell(result.junctionId, result.barbellId)

            navController.currentBackStackEntry?.savedStateHandle?.set(
                RESULT_BARBELL_SELECTOR_KEY,
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

    fun handleRemoveFromSuperset(item: LogEntriesWithExerciseJunction) {
        onRemoveFromSuperset(item)

        val itemsWithSupersetId = logEntriesWithJunction.filter {
            it.junction.supersetId == item.junction.supersetId && it.junction.id != item.junction.id
        }

        if (itemsWithSupersetId.size == 1) {
            onRemoveFromSuperset(itemsWithSupersetId[0])
        }
    }

    fun handleDeleteExercise(item: LogEntriesWithExerciseJunction) {
        handleRemoveFromSuperset(item)
        onDeleteExerciseFromWorkout(item)
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
                    placeholderValue = stringResource(R.string.workout_name)
                )
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    value = workoutNote ?: "",
                    onValueChange = { onChangeWorkoutNote(it) },
                    placeholderValue = stringResource(R.string.workout_note)
                )
            }
        }

        for (logEntriesWithJunctionItem in logEntriesWithJunction) {
            workoutExerciseItemAlt(
                useReboundKeyboard = useReboundKeyboard,
                logEntriesWithJunction = logEntriesWithJunctionItem,
                barbells = barbells,
                onValuesUpdated = { updatedEntry ->
                    onUpdateLogEntry(updatedEntry)
                },
                onSwipeDelete = { entryToDelete ->
                    if (logEntriesWithJunctionItem.logEntries.size == 1) {
                        onDeleteExerciseFromWorkout(logEntriesWithJunctionItem)
                    } else {
                        onDeleteLogEntry(entryToDelete)
                    }
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
                    handleDeleteExercise(logEntriesWithJunctionItem)
                },
                onUpdateWarmUpSets = {
                    onUpdateWarmUpSets(logEntriesWithJunctionItem, it.toExerciseLogEntries())
                },
                onAddEmptyNote = {
                    onAddEmptyNote(logEntriesWithJunctionItem)
                },
                onChangeNote = onChangeNote,
                onDeleteNote = onDeleteNote,
                onRemoveFromSuperset = {
                    handleRemoveFromSuperset(logEntriesWithJunctionItem)
                },
                onAddToSuperset = {
                    navigator.navigate(
                        LeafScreen.SupersetSelector.createRoute(
                            workoutId = logEntriesWithJunctionItem.junction.workoutId!!,
                            junctionId = logEntriesWithJunctionItem.junction.id,
                        )
                    )
                },
                onRequestBarbellChanger = {
                    navigator.navigate(
                        LeafScreen.BarbellSelector.createRoute(
                            junctionId = logEntriesWithJunctionItem.junction.id,
                            selectedBarbellId = logEntriesWithJunctionItem.junction.barbellId
                        )
                    )
                }
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
                Text(
                    text = stringResource(R.string.add_exercise),
                    style = MaterialTheme.typography.button
                )
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
                        text = stringResource(R.string.cancel_workout),
                        style = MaterialTheme.typography.button,
                        color = Color.Red
                    )
                }
            }
        }
    }
}