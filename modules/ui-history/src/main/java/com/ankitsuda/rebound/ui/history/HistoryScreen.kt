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

package com.ankitsuda.rebound.ui.history

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.utils.toLocalDate
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.history.components.HistoryHeader
import com.ankitsuda.rebound.ui.history.components.HistorySessionItemCard
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import timber.log.Timber
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: HistoryScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()

    val argumentsDate = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Long>(DATE_KEY)?.observeAsState()

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val workoutsMap by viewModel.workouts.collectAsState(initial = emptyMap())

    LaunchedEffect(key1 = argumentsDate?.value) {
        if (argumentsDate?.value == null) return@LaunchedEffect;

        try {
            val mDate = argumentsDate.value?.toLocalDate();
            var index = -1;

            var loopIndex = 0;
            for (map in workoutsMap) {
                if (map.key?.month == mDate?.month && map.key?.year == mDate?.year) {
                    index = loopIndex;
                    break;
                } else {
                    loopIndex += 1 + map.value.size
                }
            }

            if (index > -1) {
                delay(100)
                scrollState.animateScrollToItem(
                    index
                )
            }

            navController.currentBackStackEntry
                ?.savedStateHandle?.remove(DATE_KEY)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = stringResource(id = R.string.history),
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarIconButton(
                        icon = Icons.Outlined.DateRange,
                        title = stringResource(id = R.string.show_calendar),
                        onClick = {
                            navigator.navigate(LeafScreen.Calendar.createRoute(selectedDate = LocalDate.now()))
                        }
                    )
                },
                actions = {
                    TopBarIconButton(
                        icon = Icons.Outlined.MoreVert,
                        title = stringResource(id = R.string.open_menu),
                        onClick = {

                        }
                    )
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(ReboundTheme.colors.topBar)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            for (map in workoutsMap) {
                if (map.key != null) {
                    item(key = "${map.key}") {
                        HistoryHeader(
                            date = map.key!!,
                            totalWorkouts = map.value.size
                        )
                    }
                }
                items(map.value, key = { it.workout!!.id }) {
                    HistorySessionItemCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            navigator.navigate(
                                LeafScreen.Session.createRoute(
                                    workoutId = it.workout?.id!!
                                )
                            )
                        },
                        title = it.workout?.name.toString(),
                        totalExercises = it.totalExercises ?: 0,
                        duration = it.workout?.getDuration(),
                        volume = "${it.totalVolume?.toReadableString()} kg", // TODO: Move to strings.xml
                        prs = it.totalPRs ?: 0,
                        date = it.workout?.startAt ?: it.workout?.completedAt
                        ?: it.workout?.createdAt,
                    )
                }
            }
        }

    }

}