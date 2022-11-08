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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ankitsuda.rebound.ui.components.AppCard
import com.ankitsuda.rebound.ui.components.RSpacer
import com.ankitsuda.rebound.ui.components.charts.themed.ReboundChart
import com.ankitsuda.rebound.ui.home.R
import com.ankitsuda.rebound.ui.home.models.WorkoutsInfo
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun WorkoutsCardComponent(modifier: Modifier = Modifier, workoutsInfo: WorkoutsInfo) {

    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CardHeaderComponent(text = stringResource(R.string.workouts))
            RSpacer(space = 16.dp)
            InfosContainerComponent {
                InfoComponent(
                    value = workoutsInfo.workoutsThisWeek.toString(),
                    title = stringResource(R.string.this_week),
                    constrains = it
                )
                InfoComponent(
                    value = workoutsInfo.workoutsThisMonth.toString(),
                    title = stringResource(R.string.this_month),
                    constrains = it
                )
                InfoComponent(
                    value = workoutsInfo.workoutsLastMonth.toString(),
                    title = stringResource(R.string.last_month),
                    constrains = it
                )
            }
            if (workoutsInfo.chartsData != null && workoutsInfo.chartsData!!.size > 1) {
                RSpacer(space = 4.dp)
                ReboundChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    points = workoutsInfo.chartsData!!
                )
            }
        }
    }
}