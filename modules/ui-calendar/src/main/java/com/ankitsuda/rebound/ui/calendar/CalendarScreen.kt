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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Today
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.utils.toEpochMillis
import com.ankitsuda.base.utils.toLocalDate
import com.ankitsuda.navigation.DATE_KEY
import com.ankitsuda.navigation.SELECTED_DATE_KEY
import com.ankitsuda.rebound.ui.calendar.components.CalendarMonthItem
import com.ankitsuda.rebound.ui.components.TopBar
import com.ankitsuda.rebound.ui.components.TopBarIconButton
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalDate

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarScreenViewModel = hiltViewModel()
) {
    val selectedDate =
        navController.currentBackStackEntry?.arguments?.getString(SELECTED_DATE_KEY)?.toLong()
            ?.toLocalDate()
            ?: LocalDate.now()

    val collapsingState = rememberCollapsingToolbarScaffoldState()
    val scrollState = rememberLazyListState()

    val mCalendar by viewModel.calendar.collectAsState()
    val countsWithDate by viewModel.workoutsCountOnDates.collectAsState()
    val today = LocalDate.now()

    val coroutine = rememberCoroutineScope()

    mCalendar?.let { calendar ->
        LaunchedEffect(key1 = Unit) {
            if (calendar.isNotEmpty()) {
                try {
                    scrollState.scrollToItem(calendar.indexOf((calendar.filter {
                        try {
                            it.month == selectedDate.month.value && it.year == selectedDate.year
                        } catch (e1: Exception) {
                            it.month == today.month.value && today.year == today.year
                        }
                    }[0])))
                } catch (e: Exception) {
                    e.printStackTrace()
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
                            icon = Icons.Outlined.Close,
                            title = stringResource(id = R.string.close_calendar)
                        ) {
                            navController.popBackStack()
                        }
                    },
                    rightIconBtn = {
                        TopBarIconButton(
                            icon = Icons.Outlined.Today,
                            title = stringResource(id = R.string.jump_to_today),
                            onClick = {
                                coroutine.launch {
                                    scrollState.animateScrollToItem(calendar.indexOf((calendar.filter {
                                        it.month == today.month.value && it.year == today.year
                                    }[0])))
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
                items(calendar, key = { "${it.month}_${it.year}" }) {
                    val month = it
                    CalendarMonthItem(
                        month = month,
                        selectedDate = selectedDate,
                        countsWithDate = countsWithDate ?: emptyList(),
                        onClickOnDay = { dateItem ->
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                DATE_KEY,
                                dateItem.date.toEpochMillis()

                            )
                            navController.popBackStack()
                        })
                }
            }

        }
    }
}
