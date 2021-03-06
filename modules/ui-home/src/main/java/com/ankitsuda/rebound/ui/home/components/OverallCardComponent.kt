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
import com.ankitsuda.base.util.toReadableString
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
            CardHeaderComponent(text = "Overall")
            RSpacer(space = 16.dp)
            InfosContainerComponent {
                with(overallInfo) {
                    InfoComponent(
                        value = (totalWorkouts ?: 0).toString(),
                        title = "Workouts",
                        constrains = it
                    )
                    InfoComponent(
                        value = (totalVolumeLifted ?: 0.0).toReadableString(),
                        title = "Volume Lifted",
                        constrains = it
                    )
                    InfoComponent(
                        value = (maxWeight ?: 0.0).toReadableString(),
                        title = "Max Weight",
                        constrains = it
                    )
                    InfoComponent(
                        value = totalWorkoutsDurationStr ?: "",
                        title = "Total Duration",
                        constrains = it
                    )
                    InfoComponent(
                        value = averageWorkoutDurationStr ?: "",
                        title = "Average Duration",
                        constrains = it
                    )
                    InfoComponent(
                        value = longestWorkoutDurationStr ?: "",
                        title = "Longest Duration",
                        constrains = it
                    )
                }
            }
        }
    }
}