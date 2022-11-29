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

package com.ankitsuda.rebound.ui.muscleselector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.navigation.RESULT_BARBELL_SELECTOR_KEY
import com.ankitsuda.navigation.RESULT_MUSCLE_SELECTOR_KEY
import com.ankitsuda.rebound.domain.entities.Muscle
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.muscleselector.components.MuscleSelectorListItem
import com.ankitsuda.rebound.ui.muscleselector.models.MuscleSelectorResult

@Composable
fun MuscleSelectorBottomSheet(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: MuscleSelectorBottomSheetViewModel = hiltViewModel()
) {
    val initialSelectedMuscleId = viewModel.selectedMuscleId
    val muscles by viewModel.muscles.collectAsState(initial = emptyList())

    fun handleItemClick(muscle: Muscle) {
        navController.previousBackStackEntry?.savedStateHandle?.set(
            RESULT_MUSCLE_SELECTOR_KEY,
            MuscleSelectorResult(
                muscleId = muscle.tag,
            )
        )
        navController.popBackStack()
    }

    BottomSheetSurface {
        LazyColumn(
            Modifier
                .fillMaxWidth(),
            contentPadding =
            WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                .asPaddingValues()
        ) {
            item {
                TopBar(
                    title = stringResource(id = R.string.select_muscle),
                    statusBarEnabled = false,
                    elevationEnabled = false
                )
            }

            itemsIndexed(muscles, key = { _, muscle ->
                muscle.tag
            }) { index, muscle ->
                MuscleSelectorListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index == muscles.size - 1) 8.dp else 0.dp),
                    muscle = muscle,
                    isSelected = muscle.tag == initialSelectedMuscleId,
                    onClick = {
                        handleItemClick(muscle)
                    }
                )
            }
        }
    }
}