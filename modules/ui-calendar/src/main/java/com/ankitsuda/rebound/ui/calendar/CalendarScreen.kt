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

package com.ankitsuda.rebound.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Today
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ankitsuda.base.utils.generateId
import com.ankitsuda.navigation.*
import com.ankitsuda.rebound.ui.calendar.components.CalendarMonthItem
import com.ankitsuda.rebound.ui.calendar.components.CalendarYearHeader
import com.ankitsuda.rebound.ui.calendar.models.CalendarMonth
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalDate
import java.time.Year

@Composable
fun CalendarScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: CalendarScreenViewModel = hiltViewModel()
) {
    var didFirstAutoScroll by rememberSaveable {
        mutableStateOf(false)
    }
    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val scrollState = rememberLazyListState()

    val calendar = viewModel.calendar.collectAsLazyPagingItems()
    val countsWithDate by viewModel.workoutsCountOnDates.collectAsState(emptyList())
    val today = LocalDate.now()

    val coroutineScope = rememberCoroutineScope()

    suspend fun scrollToToday(smooth: Boolean = true) {
        for (i in 0 until calendar.itemCount) {
            val item = calendar.peek(i)
            if (item is CalendarMonth && item.yearMonth.year == today.year && item.month == today.monthValue) {
                if (smooth) {
                    scrollState.animateScrollToItem(i)
                } else {
                    scrollState.scrollToItem(i)
                }
                break
            }
        }
    }

    LaunchedEffect(calendar.itemCount) {
        calendar.let { calendar ->
            if (calendar.itemCount > 0 && !didFirstAutoScroll) {
                delay(100)
                scrollToToday()
                didFirstAutoScroll = true
            }
        }
    }

    CollapsingToolbarScaffold(
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        state = collapsingState,
        toolbar = {
            TopBar(
                title = stringResource(id = R.string.calendar),
                strictLeftIconAlignToStart = true,
                leftIconBtn = {
                    TopBarIconButton(
                        icon = Icons.Outlined.ArrowBack,
                        title = stringResource(id = R.string.back)
                    ) {
                        navController.popBackStack()
                    }
                },
                rightIconBtn = {
                    TopBarIconButton(
                        icon = Icons.Outlined.Today,
                        title = stringResource(id = R.string.jump_to_today),
                        onClick = {
                            coroutineScope.launch {
                                scrollToToday()
                            }
                        })
                })
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        LazyColumn(
            state = scrollState, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            items(calendar, key = {
                when (it) {
                    is CalendarMonth -> "month_block_${it.month}_${it.year}"
                    is Int -> "year_header_$it"
                    else -> generateId()
                }
            }) { item ->
                when (item) {
                    is Int -> CalendarYearHeader(
                        year = Year.of(item),
                        onClick = {
                            navigator.navigate(
                                LeafScreen.History.createRoute(
                                    year = item,
                                )
                            )
                        }
                    )
                    is CalendarMonth ->
                        CalendarMonthItem(
                            month = item,
                            selectedDate = today,
                            countsWithDate = countsWithDate ?: emptyList(),
                            onClickOnMonth = { monthItem ->
                                navigator.navigate(
                                    LeafScreen.History.createRoute(
                                        month = monthItem.month,
                                        year = monthItem.year,
                                    )
                                )
                            },
                            onClickOnDay = { dateItem ->
                                navigator.navigate(
                                    LeafScreen.History.createRoute(
                                        day = dateItem.day,
                                        month = dateItem.date.monthValue,
                                        year = dateItem.date.year,
                                    )
                                )
                            }
                        )
                }

            }
        }

    }
}
