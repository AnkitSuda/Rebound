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

package com.ankitsuda.rebound.ui.components.workouteditor.supersetselector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.RESULT_SUPERSET_SELECTOR_SUPERSET_ID_KEY
import com.ankitsuda.rebound.domain.entities.ExerciseWorkoutJunction
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.workouteditor.R
import com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.components.SupersetExerciseListItem
import com.ankitsuda.rebound.ui.components.workouteditor.supersetselector.models.SupersetSelectorResult

@Composable
fun SupersetSelectorBottomSheet(
    navController: NavController,
    viewModel: SupersetSelectorBottomSheetViewModel = hiltViewModel()
) {
    val junctionId = viewModel.junctionId
    val junctions by viewModel.junctions.collectAsState(initial = emptyList())

    fun handleItemClick(junction: ExerciseWorkoutJunction) {
        val mSupersetId =
            junction.supersetId ?: ((junctions.mapNotNull { it.junction.supersetId }.maxOrNull()
                ?: -1) + 1)

        navController.previousBackStackEntry?.savedStateHandle?.set(
            RESULT_SUPERSET_SELECTOR_SUPERSET_ID_KEY,
            SupersetSelectorResult(
                toJunctionId = junctionId,
                selectedFromJunctionId = junction.id,
                supersetId = mSupersetId
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
                    title = stringResource(id = R.string.superset),
                    statusBarEnabled = false,
                    elevationEnabled = false
                )
            }

            items(junctions, key = {
                it.junction.id
            }) {
                SupersetExerciseListItem(
                    supersetId = it.junction.supersetId,
                    exerciseName = it.exercise.name
                        ?: stringResource(id = R.string.unnamed_exercise),
                    isSelectedJunction = it.junction.id == junctionId,
                    onClick = {
                        handleItemClick(it.junction)
                    }
                )
            }
        }
    }
}