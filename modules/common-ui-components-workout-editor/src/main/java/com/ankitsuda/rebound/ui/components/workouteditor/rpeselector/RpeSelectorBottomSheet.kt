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

package com.ankitsuda.rebound.ui.components.workouteditor.rpeselector

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ankitsuda.navigation.ENTRY_ID_KEY
import com.ankitsuda.navigation.RESULT_RPE_SELECTOR_KEY
import com.ankitsuda.navigation.RPE_KEY
import com.ankitsuda.rebound.ui.components.BottomSheetRButton
import com.ankitsuda.rebound.ui.components.BottomSheetSecondaryRButton
import com.ankitsuda.rebound.ui.components.BottomSheetSurface
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.workouteditor.R
import com.ankitsuda.rebound.ui.components.workouteditor.rpeselector.components.RpeSlider
import com.ankitsuda.rebound.ui.components.workouteditor.rpeselector.components.SelectedRpeOverview
import com.ankitsuda.rebound.ui.components.workouteditor.rpeselector.models.RpeSelectorResult

@Composable
fun RpeSelectorBottomSheet(
    navController: NavController
) {
    val initialRpe: Float? = with(
        navController.currentBackStackEntry?.arguments
            ?.getFloat(RPE_KEY)
    ) {
        if (this == -1f) null else this
    }

    var rpe: Float? by rememberSaveable {
        mutableStateOf(initialRpe)
    }

    fun handleSelectClick() {

        navController.currentBackStackEntry?.arguments?.getString(ENTRY_ID_KEY)?.let {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                RESULT_RPE_SELECTOR_KEY,
                RpeSelectorResult(
                    entryId = it,
                    rpe = rpe,
                )
            )
        }

        navController.popBackStack()

    }

    BottomSheetSurface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            TopBar(
                title = stringResource(id = R.string.rpe),
                statusBarEnabled = false,
                elevationEnabled = false
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SelectedRpeOverview(
                    modifier = Modifier.fillMaxWidth(),
                    rpe = rpe,
                )
                RpeSlider(
                    modifier = Modifier.fillMaxWidth(),
                    value = rpe,
                    onValueChange = {
                        rpe = it
                    }
                )
            }
            Row(
                Modifier
                    .padding(end = 16.dp, start = 16.dp, bottom = 16.dp, top = 16.dp)
                    .align(Alignment.End)
            ) {
                BottomSheetSecondaryRButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        navController.popBackStack()
                    }) {
                    Text(stringResource(id = R.string.cancel))
                }

                BottomSheetRButton(
                    onClick = ::handleSelectClick,
                    modifier = Modifier.width(88.dp)
                ) {
                    Text(stringResource(id = R.string.select))
                }
            }
        }
    }
}