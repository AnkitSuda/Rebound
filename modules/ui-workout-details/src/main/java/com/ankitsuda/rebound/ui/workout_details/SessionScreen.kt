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

package com.ankitsuda.rebound.ui.workout_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.WORKOUT_ID_KEY
import com.ankitsuda.rebound.ui.components.*
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.ScrollStrategy
import kotlin.random.Random

@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: SessionScreenViewModel = hiltViewModel()
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val logs by viewModel.logs.collectAsState(emptyList())
    val workout by viewModel.workout.collectAsState(null)

    if (workout != null) {

        CollapsingToolbarScaffold(
            scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
            state = collapsingState,
            toolbar = {
                TopBar(
                    title = workout?.name ?: "Workout",
                    strictLeftIconAlignToStart = true,
                    leftIconBtn = {
                        TopBarBackIconButton {
                            navController.popBackStack()
                        }
                    },
                    rightIconBtn = {
                        TopBarIconButton(icon = Icons.Outlined.MoreVert, title = "Open Menu") {

                        }
                    })
            },
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column() {
                            Text(text = "${logs.size} Exercises")
                            RSpacer(space = 4.dp)
                            SessionCompleteQuickInfo(
                                time = "45 m",
                                volume = "1000 kg",
                                prs = 2
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit session",
                                tint = ReboundTheme.colors.primary
                            )
                        }
                    }
                }

                for (log in logs) {
                    item() {
                        SessionExerciseCardItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            onClick = { },
                            exerciseName = log.exercise.name ?: "",
                            entries = log.logEntries
                        )
                    }
                }
            }

        }
    }
}