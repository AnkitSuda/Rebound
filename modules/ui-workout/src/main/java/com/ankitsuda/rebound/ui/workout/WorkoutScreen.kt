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

package com.ankitsuda.rebound.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.util.NONE_WORKOUT_ID
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.common.compose.LocalPanel
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlinx.coroutines.launch
import me.onebone.toolbar.*
import me.onebone.toolbar.FabPosition
import timber.log.Timber
import kotlin.math.exp

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalToolbarApi::class
)
@Composable
fun WorkoutScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: WorkoutScreenViewModel = hiltViewModel(),
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val currentWorkout by viewModel.currentWorkout.collectAsState(initial = null)
    val currentWorkoutDurationStr by viewModel.currentWorkoutDurationStr.collectAsState(initial = null)
    val templatesWithWorkouts by viewModel.templatesWithWorkouts.collectAsState(emptyList())
    val coroutine = rememberCoroutineScope()
    val mainPanel = LocalPanel.current

    fun expandPanel() {
        coroutine.launch {
            mainPanel.expand()
        }
    }

    fun createAndNavigateToTemplate() {
        viewModel.createTemplate {
            Timber.d("Routine created $it")
        }
    }

    ToolbarWithFabScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = "Workout",
                toolbarState = collapsingState.toolbarState,
            )
        },
        fab = {

            if (currentWorkout == null) {
                ExtendedFloatingActionButton(
                    modifier = Modifier,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    text = { Text(text = "Empty Workout") },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        viewModel.startEmptyWorkout()
                    })

            }
        },
        fabPosition = FabPosition.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {


        // User routines
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {

            if (currentWorkout != null) {
                item {
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
                        backgroundColor = ReboundTheme.colors.primary,
                        onClick = {
                            expandPanel()
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column() {
                                Text(
                                    text = "Ongoing Workout",
                                    style = ReboundTheme.typography.h6,
                                    color = ReboundTheme.colors.onPrimary
                                )
                                currentWorkoutDurationStr?.let {
                                    RSpacer(space = 4.dp)
                                    Text(
                                        text = it,
                                        style = ReboundTheme.typography.body2,
                                        color = ReboundTheme.colors.onPrimary.copy(alpha = 0.7f)

                                    )
                                }
                            }
                            IconButton(onClick = { expandPanel() }) {
                                Icon(
                                    imageVector = Icons.Outlined.OpenInFull,
                                    contentDescription = "Open",
                                    tint = ReboundTheme.colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Plans", style = MaterialTheme.typography.body1,
                            color = LocalThemeState.current.onBackgroundColor
                        )
                        TextButton(onClick = {
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Plan"
                            )
                            RSpacer(space = 4.dp)
                            Text(text = "NEW", style = MaterialTheme.typography.button)
                        }
                    }

                    LazyRow() {
                        items(5) {
                            RoutineItemCard(
                                name = "Push",
                                date = "2 Aug 2021",
                                totalExercises = 7,
                                modifier = Modifier
                                    .width(((LocalConfiguration.current.screenWidthDp / 2) - 24).dp)
                                    .padding(
                                        start = if (it == 0) 16.dp else 0.dp,
                                        end = 16.dp
                                    )
                            ) {

                            }
                        }
                    }
                }
            }
            item {

                Column() {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Templates", style = MaterialTheme.typography.body1,
                            color = LocalThemeState.current.onBackgroundColor
                        )
                        TextButton(onClick = {
                            createAndNavigateToTemplate()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "New Template"
                            )
                            Text(text = "NEW", style = MaterialTheme.typography.button)
                        }
                    }


                }
            }

            items(templatesWithWorkouts, key = { it.template.id }) {
                TemplateItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    name = it.workout.name ?: it.template.id,
                    totalExercises = 7,
                    onClick = {
                        navigator.navigate(LeafScreen.WorkoutEdit.createRoute(it.workout.id))
                    }
                )
            }

        }

    }
}