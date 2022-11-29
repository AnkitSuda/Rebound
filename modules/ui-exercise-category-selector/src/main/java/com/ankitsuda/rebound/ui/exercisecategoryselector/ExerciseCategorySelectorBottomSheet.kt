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

package com.ankitsuda.rebound.ui.exercisecategoryselector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.RESULT_EXERCISE_CATEGORY_SELECTOR_KEY
import com.ankitsuda.rebound.domain.ExerciseCategory
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.exercisecategoryselector.components.ExerciseCategorySelectorListItem
import com.ankitsuda.rebound.ui.exercisecategoryselector.models.ExerciseCategorySelectorResult

@Composable
fun ExerciseCategorySelectorBottomSheet(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: ExerciseCategorySelectorBottomSheetViewModel = hiltViewModel()
) {
    val initialSelectedExerciseCategoryId = viewModel.selectedExerciseCategoryId
    val exerciseCategories = viewModel.exerciseCategories

    fun handleItemClick(exerciseCategory: ExerciseCategory) {
        navController.previousBackStackEntry?.savedStateHandle?.set(
            RESULT_EXERCISE_CATEGORY_SELECTOR_KEY,
            ExerciseCategorySelectorResult(
                categoryTag = exerciseCategory.tag,
            )
        )
        navController.popBackStack()
    }

    BottomSheetSurface {
        LazyColumn(
            Modifier
                .fillMaxWidth(),
            contentPadding = WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                .asPaddingValues()
        ) {
            item {
                TopBar(
                    title = stringResource(id = R.string.select_category),
                    statusBarEnabled = false,
                    elevationEnabled = false
                )
            }

            itemsIndexed(exerciseCategories, key = { _, exerciseCategory ->
                exerciseCategory.tag
            }) { index, exerciseCategory ->
                ExerciseCategorySelectorListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index == exerciseCategories.size - 1) 8.dp else 0.dp),
                    exerciseCategory = exerciseCategory,
                    isSelected = exerciseCategory.tag == initialSelectedExerciseCategoryId,
                    onClick = {
                        handleItemClick(exerciseCategory)
                    }
                )
            }
        }
    }
}