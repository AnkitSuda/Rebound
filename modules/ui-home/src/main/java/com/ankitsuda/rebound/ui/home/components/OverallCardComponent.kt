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

package com.ankitsuda.rebound.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.home.models.OverallInfo
import com.ankitsuda.rebound.ui.home.models.WorkoutsInfo
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun OverallCardComponent(modifier: Modifier = Modifier, overallInfo: OverallInfo) {

    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CardHeaderComponent(text = "Workouts")
            RSpacer(space = 4.dp)
            InfosContainerComponent {
                with(overallInfo) {
                    InfoComponent(
                        value = totalWorkouts.toString(),
                        title = "Workouts"
                    )
                    InfoComponent(
                        value = totalVolumeLifted.toString(),
                        title = "Volume Lifted"
                    )
                    InfoComponent(
                        value = maxWeight.toString(),
                        title = "Max Weight"
                    )
                    InfoComponent(
                        value = totalWorkoutsDurationStr,
                        title = "Total Duration"
                    )
                    InfoComponent(
                        value = averageWorkoutDurationStr,
                        title = "Average Duration"
                    )
                    InfoComponent(
                        value = longestWorkoutDurationStr,
                        title = "Longest Duration"
                    )
                }
            }
        }
    }
}