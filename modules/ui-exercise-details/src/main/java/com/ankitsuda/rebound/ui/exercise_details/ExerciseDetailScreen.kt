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

package com.ankitsuda.rebound.ui.exercise_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarBackIconButton
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import me.onebone.toolbar.CollapsingToolbarScaffold
import com.ankitsuda.rebound.ui.exercise_details.about.ExerciseDetailAboutTab
import com.ankitsuda.rebound.ui.exercise_details.charts.ExerciseDetailChartsTab
import com.ankitsuda.rebound.ui.exercise_details.history.ExerciseDetailHistoryTab
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExerciseDetailScreen(
    navController: NavController,
    viewModel: ExerciseDetailScreenViewModel = hiltViewModel()
) {
    val exercise by viewModel.exercise.collectAsState(initial = null)
    val logEntriesWithWorkoutList by viewModel.history.collectAsState(initial = emptyList())
    val charts by viewModel.charts.collectAsState(initial = emptyMap())

    val tabData = listOf(
        stringResource(id = R.string.statistics),
        stringResource(id = R.string.history),
        stringResource(id = R.string.about)
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    val collapsingState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar2(
                elevationEnabled = false,
                toolbarState = collapsingState.toolbarState,
                title = exercise?.name.toString(),
                navigationIcon = {
                    TopBarBackIconButton(onClick = {
                        navController.popBackStack()
                    })
                },
                actions = {
                    TopBarIconButton(
                        icon = Icons.Outlined.StarBorder,
                        title = stringResource(id = R.string.favorite),
                        onClick = {

                        })
                },
                bottomLayout = {

                    TabRow(
                        selectedTabIndex = tabIndex,
                        backgroundColor = Color.Transparent,
                        contentColor = contentColorFor(backgroundColor = ReboundTheme.colors.topBar),
                        divider = { Divider(thickness = 0.dp) },
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.pagerTabIndicatorOffset(
                                    pagerState,
                                    tabPositions
                                ),
                                color = ReboundTheme.colors.primary
                            )
                        }
                    ) {
                        tabData.forEachIndexed { index, pair ->
                            Tab(
                                selectedContentColor = ReboundTheme.colors.primary,
                                unselectedContentColor = ReboundTheme.colors.topBarTitle.copy(
                                    0.5f
                                ),
                                selected = tabIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(text = pair)

                                }
                            )
                        }
                    }
                }
            )
        },
    ) {


        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            count = 3,
        ) { index ->
            when (index) {
                0 -> {
                    ExerciseDetailChartsTab(charts)
                }
                1 -> {
                    ExerciseDetailHistoryTab(logEntriesWithWorkoutList)
                }
                2 -> {
                    exercise?.let {
                        ExerciseDetailAboutTab(it)
                    }
                }
            }

        }
    }
}