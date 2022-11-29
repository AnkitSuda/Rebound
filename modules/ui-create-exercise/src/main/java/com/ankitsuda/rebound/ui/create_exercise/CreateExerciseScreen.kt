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

package com.ankitsuda.rebound.ui.create_exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.ui.components.AppTextField
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.create_exercise.components.ValueSelectorCard
import com.ankitsuda.rebound.ui.exercisecategoryselector.models.ExerciseCategorySelectorResult
import com.ankitsuda.rebound.ui.muscleselector.models.MuscleSelectorResult

@Composable
fun CreateExerciseScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: CreateExerciseScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    // Dummy lists
    val categoriesList = viewModel.allCategories
    val musclesList by viewModel.allPrimaryMuscles.collectAsState(initial = emptyList())

    val nameValue by viewModel.name.collectAsState("")
    val noteValue by viewModel.note.collectAsState("")

    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedMuscleTag by viewModel.selectedMuscleTag.collectAsState()
    val selectedMuscle by viewModel.selectedMuscle.collectAsState(null)

    val selectedCategoryTag = selectedCategory.tag

    val isCreateBtnEnabled = nameValue.trim().isNotEmpty()

    // Observes results when Superset Selector changes value of arg
    val muscleSelectorResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<MuscleSelectorResult?>(RESULT_MUSCLE_SELECTOR_KEY, null)
        ?.collectAsState()

    // Observes results when Exercise Category Selector changes value of arg
    val exerciseCategorySelectorResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<ExerciseCategorySelectorResult?>(RESULT_EXERCISE_CATEGORY_SELECTOR_KEY, null)
        ?.collectAsState()

    LaunchedEffect(key1 = muscleSelectorResult) {
        muscleSelectorResult?.value?.muscleId?.let {
            viewModel.setPrimaryMuscle(it)
        }

        navController.currentBackStackEntry?.savedStateHandle?.set(
            RESULT_MUSCLE_SELECTOR_KEY,
            null
        )
    }

    LaunchedEffect(key1 = exerciseCategorySelectorResult) {
        exerciseCategorySelectorResult?.value?.categoryTag?.let {
            viewModel.setCategory(it)
        }

        navController.currentBackStackEntry?.savedStateHandle?.set(
            RESULT_EXERCISE_CATEGORY_SELECTOR_KEY,
            null
        )
    }

    BottomSheetSurface {
        Column {
            TopBar(
                statusBarEnabled = false,
                elevationEnabled = false,
                title = stringResource(id = R.string.new_exercise),
                strictLeftIconAlignToStart = true,
                leftIconBtn = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Close,
                        title = stringResource(id = R.string.back),
                        onClick = {
                            navigator.goBack()
                        })
                },
                rightIconBtn = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Done,
                        title = stringResource(id = R.string.create),
                        enabled = isCreateBtnEnabled,
                        customTint = MaterialTheme.colors.primary
                    ) {
                        viewModel.createExercise()
                        navigator.goBack()
                    }
                })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
//                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.name),
                        style = MaterialTheme.typography.caption,
                        color = Color(117, 117, 117)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppTextField(
                        value = nameValue,
                        placeholderValue = stringResource(id = R.string.exercise_name),
                        singleLine = true,
                        onValueChange = {
                            viewModel.setName(it)
                        })
                }
                Column(
                    Modifier
                        .fillMaxWidth()
//                        .padding(top = 8.dp, bottom = 8.dp)
                ) {

                    Text(
                        text = stringResource(id = R.string.notes),
                        style = MaterialTheme.typography.caption,
                        color = Color(117, 117, 117)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AppTextField(
                        value = noteValue,
                        placeholderValue = stringResource(id = R.string.exercise_notes),
                        onValueChange = {
                            viewModel.setNote(it)
                        })
                }

                ValueSelectorCard(
                    name = stringResource(id = R.string.category),
                    value = selectedCategoryTag,
                    onClick = {
                        navigator.navigate(
                            LeafScreen.ExerciseCategorySelector.createRoute(
                                selectedCategoryTag = selectedCategoryTag
                            )
                        )
                    }
                )

                ValueSelectorCard(
                    name = stringResource(id = R.string.primary_muscle),
                    value = selectedMuscle?.name ?: selectedMuscleTag,
                    onClick = {
                        navigator.navigate(
                            LeafScreen.MuscleSelector.createRoute(
                                selectedMuscleId = selectedMuscleTag
                            )
                        )
                    }
                )
            }
        }
    }
}