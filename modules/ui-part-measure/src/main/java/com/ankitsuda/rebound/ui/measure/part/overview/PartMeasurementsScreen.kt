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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ankitsuda.base.util.fromKgToLbs
import com.ankitsuda.common.compose.LocalAppSettings
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
import com.ankitsuda.rebound.domain.WeightUnit
import com.ankitsuda.rebound.ui.measure.part.overview.components.LogListItem
import kotlin.math.log

@OptIn(ExperimentalToolbarApi::class, ExperimentalFoundationApi::class)
@Composable
fun PartMeasurementsScreen(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current,
    viewModel: PartMeasurementsScreenViewModel = hiltViewModel()
) {

    val collapsingState = rememberCollapsingToolbarScaffoldState()

    val bodyPart by viewModel.bodyPart.collectAsState(initial = null)
    val logs by viewModel.logs.collectAsState(initial = null)

    val weightUnit = LocalAppSettings.current.weightUnit

    val points by remember(logs) {
        mutableStateOf(
            if (logs?.isEmpty() == true) emptyList() else logs?.map {
                LineChartData.Point(
                    when (weightUnit) {
                        WeightUnit.KG -> it.measurement
                        WeightUnit.LBS -> it.measurement.fromKgToLbs()
                    }.toFloat(), it.createdAt.toString()
                )
            }
        )
    }

    val showChart = (points?.size ?: 0) > 1


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
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(id = R.string.add_measurement)
                )
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

            if (logs != null && points != null) {

                if (showChart) {
                    item("chart") {

                        AppCard(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .animateItemPlacement()
                        ) {
                            ReboundChart(
                                points = points!!,
                                modifier = Modifier
                                    .height(250.dp)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            )

                        }
                    }
                }

                item("title_history") {
                    Text(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 20.dp, bottom = 10.dp)
                            .animateItemPlacement(),
                        text = stringResource(id = R.string.history),
                        style = ReboundTheme.typography.h6,
                        color = ReboundTheme.colors.onBackground.copy(alpha = 0.5f),
                    )
                }

                items(logs!!, key = { it.id }) { log ->
                    LogListItem(
                        modifier = Modifier.animateItemPlacement(),
                        log = log,
                        bodyPart = bodyPart,
                        onClick = {
                            navigator.navigate(
                                LeafScreen.AddPartMeasurement.createRoute(
                                    partId = bodyPart?.id!!,
                                    logId = log.id
                                )
                            )
                        }
                    )
                }
            }
        }
    }

}