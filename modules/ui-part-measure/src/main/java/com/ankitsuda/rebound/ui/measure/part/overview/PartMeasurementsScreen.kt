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

package com.ankitsuda.rebound.ui.measure.part.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.ankitsuda.common.compose.R

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun PartMeasurementsScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: PartMeasurementsScreenViewModel = hiltViewModel()
) {

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val bodyPart by viewModel.bodyPart.collectAsState(initial = null)
    val logs by viewModel.logs.collectAsState(initial = emptyList())

    val points = if (logs.isEmpty()) emptyList() else logs.map {
        LineChartData.Point(it.measurement.toFloat(), it.createdAt.toString())
    }

    val showChart = points.size > 1


    ToolbarWithFabScaffold(
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar2(
                title = bodyPart?.name ?: "",
                toolbarState = collapsingState.toolbarState,
                navigationIcon = {
                    TopBarBackIconButton {
                        navController.popBackStack()
                    }
                },
            )
        },
        fab = {
            FloatingActionButton(onClick = {
                bodyPart?.id?.let {
                    navigator.navigate(LeafScreen.AddPartMeasurement.createRoute(partId = it))
                }
//
            }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = stringResource(id = R.string.add_measurement))
            }
        },
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(top = 16.dp, bottom = 72.dp)
        ) {


            if (showChart) {
                item {

                    AppCard(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                        ReboundChart(
                            points = points,
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .padding(16.dp),
                        )

                    }
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.history),
                    style = ReboundTheme.typography.h6,
                    color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(logs, key = { it.id }) { log ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigator.navigate(
                                LeafScreen.AddPartMeasurement.createRoute(
                                    partId = bodyPart?.id!!,
                                    logId = log.id
                                )
                            )
                        }
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.createdAt!!.format(
                            DateTimeFormatter.ofLocalizedDateTime(
                                FormatStyle.MEDIUM,
                                FormatStyle.SHORT
                            )
                        ),
                        style = ReboundTheme.typography.caption,
                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier
                    )
                    Text(
                        text = log.measurement.toString(),
                        style = ReboundTheme.typography.body2,
                        color = LocalThemeState.current.onBackgroundColor,
                        modifier = Modifier
                    )
                }
            }

        }
    }

}