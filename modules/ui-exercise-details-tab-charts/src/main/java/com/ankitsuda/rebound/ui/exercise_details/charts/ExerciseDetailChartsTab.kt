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

package com.ankitsuda.rebound.ui.exercise_details.charts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.charts.line.LineChartData
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.theme.LocalThemeState
import com.ankitsuda.rebound.ui.theme.ReboundTheme
import kotlin.random.Random

@Composable
fun ExerciseDetailChartsTab(charts: Map<String, List<LineChartData.Point>>) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        for (chart in charts) {
            item {
                AppCard(modifier = Modifier.padding(bottom = 16.dp)) {
                    Column(
                        modifier = Modifier.padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        )
                    ) {
                        Text(
                            text = chart.key,
                            style = ReboundTheme.typography.body1,
                            color = LocalThemeState.current.onBackgroundColor
                        )

                        RSpacer(space = 16.dp)

                        if (chart.value.size > 1) {
                            ReboundChart(
                                points = chart.value,
                                modifier = Modifier
                                    .height(250.dp)
                                    .fillMaxWidth(),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.not_enough_data),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}
