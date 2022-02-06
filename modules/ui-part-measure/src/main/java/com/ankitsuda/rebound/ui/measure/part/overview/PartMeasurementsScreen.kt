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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.navigation.LeafScreen
import com.ankitsuda.navigation.LocalNavigator
import com.ankitsuda.navigation.Navigator
import com.ankitsuda.rebound.ui.components.*
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import me.onebone.toolbar.*

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun PartMeasurementsScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: PartMeasurementsScreenViewModel = hiltViewModel()
) {
    val partId by remember {
        mutableStateOf(
            navController.currentBackStackEntry?.arguments?.getString(
                "partId"
            )?.toLong()
        )
    }


    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val logs by viewModel.getLogsForPart(partId!!).collectAsState(initial = emptyList())

    val points = if (logs.isEmpty()) emptyList() else logs.map {
        LineChartData.Point(it.measurement, "id ${it.id}")
    }

    val showChart = points.size > 1


    ToolbarWithFabScaffold(
        state = collapsingState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            TopBar(title = partId.toString(), strictLeftIconAlignToStart = true, leftIconBtn = {
                TopBarBackIconButton {
                    navController.popBackStack()
                }
            })
        },
        fab = {
            FloatingActionButton(onClick = {
//                bottomSheet.show {
                partId?.let {
//                        AddPartMeasurementBottomSheet(it)
//                    navController.navigate(Route.AddPartMeasurement.createRoute(partId = it))
                    navigator.navigate(LeafScreen.AddPartMeasurement.createRoute(partId = it))
                }
//                }
            }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add measurement")
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
                    text = "History",
                    style = ReboundTheme.typography.h6,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(logs.size) {
                val log = logs[it]
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
//                            bottomSheet.show {
//                            navController.navigate(
//                                Route.AddPartMeasurement.createRoute(
//                                    partId = partId!!,
//                                    logId = log.id
//                                )
//                            )
                            navigator.navigate(
                                LeafScreen.AddPartMeasurement.createRoute(
                                    partId = partId!!,
                                    logId = log.id
                                )
                            )
//                            AddPartMeasurementBottomSheet(partId = partId!!, logId = log.id)
//                            }
                        }
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.measurement.toString(),
                        style = ReboundTheme.typography.body2,
                        modifier = Modifier
                    )
//                    Text(
//                        text = log.createdAt!!.format(
//                            DateTimeFormatter.ofLocalizedDateTime(
//                                FormatStyle.MEDIUM,
//                                FormatStyle.SHORT
//                            )
//                        ),
//                        style = ReboundTheme.typography.caption,
//                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
//                        modifier = Modifier
//                    )
                }
            }

        }
    }

}