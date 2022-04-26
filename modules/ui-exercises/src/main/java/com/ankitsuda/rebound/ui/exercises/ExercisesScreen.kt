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

package com.ankitsuda.rebound.ui.exercises

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.domain.entities.ExerciseWithExtraInfo
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import me.onebone.toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ExercisesScreen(
    navController: NavController,
    isBottomSheet: Boolean,
    navigator: Navigator = LocalNavigator.current,
    viewModel: ExercisesScreenViewModel = hiltViewModel()
) {
    val isSearchMode by viewModel.isSearchMode.observeAsState(false)
    val searchTerm by viewModel.searchTerm.observeAsState("")

    val groupedExercises by viewModel.groupedExercises.collectAsState(initial = emptyMap())

    val layout: @Composable () -> Unit = {
        ExercisesScreenContent(
            navController = navController,
            navigator = navigator,
            isBottomSheet = isBottomSheet,
            isSearchMode = isSearchMode,
            searchTerm = searchTerm,
            groupedExercises = groupedExercises,
            onToggleSearchMode = {
                viewModel.toggleSearchMode()
            }
        ) {
            viewModel.setSearchTerm(it)
        }
    }

    if (isBottomSheet) {
        BottomSheetSurface {
            layout()
        }
    } else {
        layout()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExercisesScreenContent(
    navController: NavController,
    navigator: Navigator,
    isBottomSheet: Boolean,
    isSearchMode: Boolean,
    searchTerm: String,
    groupedExercises: Map<String, List<ExerciseWithExtraInfo>>,
    onToggleSearchMode: () -> Unit,
    onChangeSearchTerm: (String) -> Unit
) {
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    Column {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = collapsingState,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                if (!isSearchMode) {
                    TopBar2(
                        toolbarState = collapsingState.toolbarState,
                        statusBarEnabled = !isBottomSheet,
                        elevationEnabled = false,
                        title = "Exercises",
                        navigationIcon = {
                            TopBarIconButton(
                                icon = Icons.Outlined.Search,
                                title = "Search",
                                onClick = {
                                    onToggleSearchMode()
                                })
                        },
                        actions = {
                            TopBarIconButton(
                                icon = Icons.Outlined.Add,
                                title = "Create Exercise",
                                onClick = {
                                    navigator.navigate(LeafScreen.CreateExercise().route)
                                })
                        })
                } else {
                    TopSearchBar(
                        modifier = Modifier
                            .fillMaxWidth(),
                        statusBarEnabled = !isBottomSheet,
                        placeholder = "Search here...",
                        value = searchTerm,
                        onBackClick = {
                            onToggleSearchMode()
                        },
                        onValueChange = {
                            onChangeSearchTerm(it)
                        },
                    )

                }
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                for (group in groupedExercises) {
                    stickyHeader(key = group.key) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LocalThemeState.current.backgroundColor)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            text = group.key,
                            style = ReboundTheme.typography.caption,
                            color = LocalThemeState.current.onBackgroundColor.copy(alpha = 0.75f)
                        )
                    }

                    items(items = group.value, key = { it.exercise.exerciseId }) { item ->
                        ExerciseItem(
                            name = item.exercise.name.toString(),
                            muscle = item.primaryMuscle?.name.toString(),
                            totalLogs = item.logsCount,
                            onClick = {
                                if (isBottomSheet) {
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "result_exercises_screen_exercise_id",
                                        item.exercise.exerciseId
                                    )
                                    navController.popBackStack()
                                } else {
                                    navigator.navigate(
                                        LeafScreen.ExerciseDetails.createRoute(
                                            exerciseId = item.exercise.exerciseId
                                        )
                                    )
                                }
                            })
                    }
                }
            }


        }
    }
}