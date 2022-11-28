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

package com.ankitsuda.rebound.ui.components.workouteditor.barbellselector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.ankitsuda.navigation.RESULT_SUPERSET_SELECTOR_SUPERSET_ID_KEY
import com.ankitsuda.rebound.domain.entities.Barbell
import com.ankitsuda.rebound.domain.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.workouteditor.R
import com.ankitsuda.rebound.ui.components.workouteditor.barbellselector.components.BarbellSelectorListItem
import com.ankitsuda.rebound.ui.components.workouteditor.barbellselector.models.BarbellSelectorResult
import com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.components.SupersetExerciseListItem
import com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.models.SupersetSelectorResult

@Composable
fun BarbellSelectorBottomSheet(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: BarbellSelectorBottomSheetViewModel = hiltViewModel()
) {
    val junctionId = viewModel.junctionId
    val initialSelectedBarbellId = viewModel.selectedBarbellId
    val barbells by viewModel.barbells.collectAsState(initial = emptyList())

    fun handleItemClick(barbell: Barbell) {
        navController.previousBackStackEntry?.savedStateHandle?.set(
            RESULT_BARBELL_SELECTOR_KEY,
            BarbellSelectorResult(
                junctionId = junctionId,
                barbellId = barbell.id,
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
                    title = stringResource(id = R.string.select_barbell),
                    statusBarEnabled = false,
                    elevationEnabled = false
                )
            }

            itemsIndexed(barbells, key = { _, barbell ->
                barbell.id
            }) { index, barbell ->
                BarbellSelectorListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index == barbells.size - 1) 8.dp else 0.dp),
                    barbell = barbell,
                    isSelected = barbell.id == initialSelectedBarbellId,
                    onClick = {
                        handleItemClick(barbell)
                    }
                )
            }
        }
    }
}