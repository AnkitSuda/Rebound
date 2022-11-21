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
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ankitsuda.base.util.toReadableString
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.base.utils.toLocalDate
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.domain.entities.CountWithDate
import com.ankitsuda.rebound.domain.entities.WorkoutWithExtraInfo
import com.ankitsuda.rebound.ui.components.TopBar2
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import com.ankitsuda.rebound.ui.history.components.HistoryHeader
import com.ankitsuda.rebound.ui.history.components.HistorySessionItemCard
import com.ankitsuda.rebound.ui.history.enums.WorkoutsDateRangeType
import com.ankitsuda.rebound.ui.history.models.CountWithLocalDate
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HistoryScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: HistoryScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val workoutsPage = viewModel.workouts.collectAsLazyPagingItems()

    val dateRangeType = viewModel.dateRangeType
    val argDay = viewModel.argDay
    val argMonth = viewModel.argMonth
    val argYear = viewModel.argYear

//    LaunchedEffect(key1 = argumentsDate?.value) {
//        if (argumentsDate?.value == null) return@LaunchedEffect;
//
//        try {
//            val mDate = argumentsDate.value?.toLocalDate()
//            var index = -1
//
////            var loopIndex = 0;
////            for (map in workoutsMap) {
////                if (map.key?.month == mDate?.month && map.key?.year == mDate?.year) {
////                    index = loopIndex;
////                    break;
////                } else {
////                    loopIndex += 1 + map.value.size
////                }
////            }
////            workoutsPage?.forEachIndexed { i, item ->
////                if (item is WorkoutWithExtraInfo && item.workout?.completedAt.toString() == mDate.toString()) {
////                    index = i
////                }
////            }
//
//            val allWorkouts = viewModel.workouts2.firstOrNull()
//
//            if (allWorkouts != null) {
//                for (i in allWorkouts.indices) {
//                    val item = allWorkouts[i]
//                    if (item is WorkoutWithExtraInfo) {
//                        val a = item.workout?.completedAt?.toLocalDate().toString()
//                        val b = mDate.toString()
//                        if (a == b) {
//                            index = i
//                            break
//                        }
//                    }
//                }
//            }
//
//            if (index > -1) {
//                try {
//                    workoutsPage[index]
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                delay(100)
//                scrollState.animateScrollToItem(
//                    index
//                )
//            }
//
//            Timber.d("items count ${workoutsPage.itemCount}")
//
//            navController.currentBackStackEntry
//                ?.savedStateHandle?.remove(DATE_KEY)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar2(
                title = when (dateRangeType) {
                    WorkoutsDateRangeType.ALL -> stringResource(
                        id = R.string.history
                    )
                    WorkoutsDateRangeType.MONTH -> YearMonth.of(argYear!!, argMonth!!).toString()
                    WorkoutsDateRangeType.YEAR -> "Year $argYear"
                    WorkoutsDateRangeType.DAY -> LocalDate.of(argYear!!, argMonth!!, argDay!!)
                        .toString()
                },
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    if (dateRangeType == WorkoutsDateRangeType.ALL) {
                        TopBarIconButton(
                            icon = Icons.Outlined.DateRange,
                            title = stringResource(id = R.string.show_calendar),
                            onClick = {
                                navigator.navigate(LeafScreen.Calendar.createRoute(selectedDate = LocalDate.now()))
                            }
                        )
                    } else {
                        TopBarIconButton(
                            icon = Icons.Outlined.ArrowBack,
                            title = stringResource(id = R.string.back),
                            onClick = {
                                navigator.goBack()
                            }
                        )
                    }
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            items(workoutsPage, key = {
                when (it) {
                    is WorkoutWithExtraInfo -> {
                        it.workout!!.id
                    }
                    is CountWithDate -> {
                        it.date.toString()
                    }
                    is Long -> {
                        it.toString()
                    }
                    else -> {
                        generateId()
                    }
                }
            }) {
                when (it) {
                    is WorkoutWithExtraInfo ->
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
                            volume = it.totalVolume,
                            prs = it.totalPRs ?: 0,
                            date = it.workout?.startAt ?: it.workout?.completedAt
                            ?: it.workout?.createdAt,
                        )
                    is Long -> HistoryHeader(
                        title = null,
                        totalWorkouts = it.toInt()
                    )
                    is CountWithDate ->
                        HistoryHeader(
                            date = it.date.toLocalDate() ?: LocalDate.now(),
                            totalWorkouts = it.count.toInt()
                        )
                }
            }
        }
    }

}